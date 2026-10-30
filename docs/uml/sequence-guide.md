# H4Y-8 - Sequence Guide

Use this guide to explain [SD-04 and SD-06](sequence-diagrams.md). The sources are the latest fetched `origin/main` at `ddd8757`, the [class model](class-diagram.md), [method list](classes.md), [use cases](../requirements/use-cases.md) and [database schema](../database/schema.md). The professor feedback dated 2026-10-30 was supplied by the user.

## Main ideas

1. Read from top to bottom. Every arrow has a hierarchical number. A child interaction adds a level. The next interaction at that level uses the next number. Returns also have numbers. Alternatives skip the other branch; a loop repeats the same labeled interaction.
2. A controller calls `HotelFacade`. The facade calls a service. Services check permissions and business rules. Proposed repositories own database connections and transactions. Controllers never call the database.
3. `Database.connect()` is implemented. The reservation and XML runtime flows are not implemented at this baseline. A modeled method is not proof of runtime behavior.
4. `[proposed G-xx]` marks a missing class-model method. `[SQL G-01]` marks an abbreviated database operation, not a Java method. `SQLServer` is an external database participant. All proposed contracts are listed under Gaps in the diagram file.
5. SD-04 expands reservation deletion. SD-06 presents the three import phases: prepare, review, save. Its SQL messages summarize each item batch. SD-05 stays removed.
6. No connection or transaction stays open while a person reviews the XML. Confirm saves the exact reviewed snapshot. Cancel discards it without calling persistence.

## SD-04 - Detailed: delete a reservation

**What:** UC-05 lets a Manager delete a reservation only before check-in. RF-22 and Q-05 require permanent deletion. The schema has no reservation soft-delete flag.

**Why:** Permission and current reservation state must be checked before deletion. A failure must not leave only part of the reservation deleted.

**Why this way:** The existing modeled calls are `ReservationController.onDeleteReservationClicked()`, `HotelFacade.deleteReservation(reservation, user)` and `ReservationService.delete(reservation, user)`. The proposed service first checks the role. An unauthorized user reaches no repository or SQL call. The repository then opens one connection and starts a proposed transaction. A protected SELECT retrieves the current row. The service checks that the row exists and `checked_in_at` is null. These are separate numbered messages, so the reader can see the check before the deletion.

If the read fails, the reservation is missing or check-in has happened, the flow stops before DELETE. An active transaction is rolled back. On the valid path, the repository removes this reservation's `reservation_services` and `reservation_locations` rows, then the reservation. These child tables have foreign keys that prevent deleting the parent first. The database commits only after the whole batch succeeds. A write failure follows the rollback branch. The result reaches the Manager through the service, facade and controller.

**Other option:** Read the state and delete later on another connection, or save each deletion separately. A concurrent check-in could invalidate the first read. A later write failure could leave partial deletion. The proposed design therefore keeps the protected read and deletion in the same operation-scoped transaction. Check-in must honor the same row protection before this design can be called safe.

**Example:** R17 has two extra rows and one pool link. On success, all three child rows and R17 are removed together. The room, pool and service catalogue remain. If deletion fails, the required result is that no part of this deletion remains. A protected card link must stop deletion; the flow must not delete cards or access history to force success.

**What was incorrect:** The sentence "check state and delete in one transaction" hid the read, rule check and writes. The previous revision kept those steps in notes, so the database work was still invisible.

**What changed:** The diagram now shows the repository, connection factory, external database, SELECT, DELETE batch, BEGIN, COMMIT, ROLLBACK and their results. Each arrow has a hierarchical number. The transaction contract is clearly proposed, not implemented.

**How to explain it:** "First, the service checks the Manager role. Then it reads the current reservation under the proposed transaction. It refuses a missing or checked-in reservation. It deletes the child rows and booking together. Commit completes success; failure takes the rollback path. This is a design proposal. The persistence contract and check-in protection still need implementation."

## SD-06 - General: import supplier XML

**What:** UC-15 imports a supplier delivery. The corrected sequence adds a required preview with Confirm and Cancel before any import write. The supplier supplies the file; an authorized hotel user performs the import (Q-17).

**Why:** The professor feedback of 2026-10-30 requires the user to see and confirm the data before saving. This replaces the earlier no-preview decision for the proposed flow. It retains RF-28's rule that a new item enters the catalogue automatically.

**Why this way:** Phase A checks permission, parses the complete XML and validates the imported data. The proposed API returns an immutable `StockImportPreview` containing the supplier, item names, units and quantities. Invalid XML, invalid data or missing permission returns an error before preview or persistence.

Phase B calls the proposed `StockImportPreviewView.showPreview(preview)`. The user reviews the data and selects Confirm or Cancel. Cancel closes the preview and never calls `confirmImport`, the repository or SQL Server. There is no open database transaction during this review.

Phase C starts only after Confirm. The service checks permission and the snapshot again. It does not re-read the source file, because that file could have changed after preview. After final validation, the repository opens a connection and begins the proposed transaction. It creates `stock_imports` first to obtain the ID required by movement foreign keys. For each confirmed item, it finds or creates the catalogue item, adds its quantity and records an ENTRY movement. SQL value `in` represents ENTRY. Every write shares the transaction. Success commits; a failed write stops remaining writes and takes the rollback path. The preview view shows the final report or error.

**Other option:** Save directly from the file with `importStock(file, user)`. That is the current one-step class-model contract, but it cannot express the new preview requirement. Re-reading the file after confirmation could also save different data from the preview. G-03 proposes a two-phase API and an immutable snapshot instead.

**Example:** Soap has stock 10. Towels do not exist. The preview shows soap +5 and towels +3. Cancel keeps stock and catalogue unchanged. Confirm requests soap 15, a new towels item with stock 3, one import and two ENTRY movements. A save failure must undo all import writes, including a newly created item. This guarantee is not implemented or runtime-tested here.

**What was incorrect:** The previous sequence imported immediately and omitted preview and confirmation. It also hid stock persistence in notes.

**What changed:** The diagram shows the preview view, the user decision, separate Confirm/Cancel paths and explicit SQL transaction messages. All database writes are inside Confirm. A missing catalogue item is created rather than rejected. No card-access flow is restored.

**How to explain it:** "First, parse and validate the file. Show the exact data in a preview. Cancel saves nothing. Confirm checks again and saves the reviewed snapshot. The import, new items, stock changes and entry movements belong to one proposed transaction. Commit reports success; a failed write rolls back. The screen and two-phase API still need implementation."

## Corrected numbering

The professor's requested numbers identify messages, not UML association multiplicities. Numbers such as `1.1.1` are on the rendered arrows in both sequences. There is no separate numbering table or flat Mermaid autonumber.

In SD-04, `1` starts the deletion event. `1.1` calls the facade; `1.1.1` calls the service. Repository and SQL work add levels under the service interaction. Returns use the next number at their caller's level. `2` displays the result.

In SD-06, `1` starts preparation and its child messages reach the preview. `2` is the next user action: Confirm or Cancel. Each branch has distinct identifiers. The drawing includes both alternatives; one execution takes only one branch. Loop identifiers describe the repeated interaction, not a new sequence counter for each item.

## Remaining gaps

- **G-01:** Both repositories, transaction context, SQL contracts, resource cleanup and connection/commit/rollback failure policies are missing. `Database.connect()` exists; there are no invented `Database.commit()` or `Database.rollback()` calls.
- **G-02:** The reservation runtime flow and the proposed authorization/state helpers are missing. Check-in must share the reservation protection contract.
- **G-03:** The two-phase XML methods, parser contract and immutable preview class are missing. The existing one-step `importStock(file, user)` contract needs an explicit class-model revision before coding.
- **G-04:** The preview view and decision handler are proposed. No JavaFX screen was implemented by this task.
- **G-05:** Current UC-15 still says no preview or confirmation. The proposed sequence follows the latest professor feedback supplied by the user. The use-case and class-model owners must align their documents. The unknown-item creation rule stays valid.

The complete proposed method names and receivers appear under Gaps in [sequence-diagrams.md](sequence-diagrams.md). This task changes only these two sequence documents. No requirements, class model, Java code, SQL script or application behavior changes.

## Verification scope

Validate both Mermaid blocks, their visible numbering, database boundaries and preview order. Check existing calls against their modeled receivers and signatures, and list every proposed call in Gaps. Rendering checks syntax and layout. Static checks cannot establish runtime rollback, concurrency safety or full Definition of Done approval. Record only checks actually executed in the PR.

## Verification performed (2026-10-30)

- Mermaid CLI rendered both final blocks to temporary SVG and normal-scale PNG files with the installed Chrome. Both PNGs were visually inspected. Database participants, transaction commands, preview, Confirm/Cancel and arrow numbers are visible. An initial render exposed Mermaid's semicolon separator rule; message separators were corrected before the successful final renders.
- A read-only Node check verified 28 numbered arrows in SD-04 and 33 in SD-06. IDs are unique, parents appear before children and sibling IDs are contiguous in the drawing. Every arrow, including responses, has a number.
- The check matched six existing call occurrences to their class-model receiver, method name and argument count. Sixteen proposed calls match the explicit Gaps contracts. Eleven external SQL messages are marked separately from Java methods. The stock movement summary includes its required item and time fields.
- The check verified preview before writes, no persistence calls on Cancel, BEGIN and writes inside Confirm, and the import row before its linked movements. Source review checked the manager/state rules, permanent deletion, schema names and unknown-item creation. Local links and documentation footers passed checks.
- `git diff --check` passed. Only `sequence-diagrams.md` and this guide changed. No application, full build, JUnit, Javadoc, SQL script or migration was run. Rendered files remain outside the repository. These checks validate the documents; they do not test runtime transactions or close G-01 to G-05.
Created by: pedrocostaisep. Updated by: pedrocostaisep. AI use: sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16; PR review corrections by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; PR-20 import decision review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; merge-conflict resolution by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; carryover review by sequence-diagram-editor, OpenAI Codex (gpt-6.1-sol), 2026-10-30; clarified professor feedback by sequence-diagram-editor, OpenAI Codex (gpt-6.1-sol), 2026-10-30. Reviewed by: pending.
