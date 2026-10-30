# H4Y-8 - Sequence Guide

Use this guide to defend the [sequence diagrams](sequence-diagrams.md). Methods come from [classes.md](classes.md) and [class-diagram.md](class-diagram.md). Database terms come from [schema.md](../database/schema.md) and [tables.md](../database/tables.md).

## Main ideas

1. Read from top to bottom. A solid arrow calls a method of its receiver. A dashed arrow shows data, an error or completion.
2. A JavaFX controller reads input and shows results. It calls `HotelFacade`. Services check permissions and business rules. Controllers never call the database.
3. Notes describe required internal work and model multiplicities. They do not add methods to the class model.
4. `alt` selects one branch. An activation shows when an operation runs. It does not prove a database transaction.
5. SD-04 is detailed: it expands deletion checks and required outcomes. SD-06 is general: it shows one import API with notes for parsing, validation and item processing.
6. G-01 and G-02 are implementation gaps. G-03 records a schema enforcement gap. The card audit question Q-49 stays open outside these diagrams.

Both diagrams are design flows at the `origin/main` baseline `44ccc4d`. Their call names exist in the class model. The Java controllers, facade operations, services and persistence for these two flows do not exist yet. The diagrams do not prove that a transaction, lock or rollback runs.

## SD-04 - Detailed: delete a reservation

**What:** UC-05 lets a manager delete a booking only before check-in. It removes the booking and its dependent extra and zone-link rows.

**Why:** Reception can cancel a booking. Only a manager can delete it. Any check-in history protects the booking, even after check-out.

**Why this way:** The modeled controller calls the facade, then `ReservationService.delete()`. The required order is clear: check the role, find the current booking, check `checkedInAt`, delete this booking's extra rows and zone links, then delete the booking. A missing booking or any check-in history must give an error. There is no lookup or persistence contract to call yet, so those steps stay in notes and G-01/G-02.

**Other option:** Trust the delete button or save each row separately. The service still needs a role check. Separate saves could leave the booking without extras or zone links after a failure.

**Example:** R17 has two extra rows and one pool link. Required success removes those three rows and R17 together. The room, pool and service catalogue must remain. A failed deletion must keep all four rows. This outcome has not been tested against SQL Server.

**Original wording:** Commit `ac61949` used the SD-04 note "Check state and delete in one transaction". That note combined a state lookup, a business check and several writes. It also hid the concurrency contract. The current main revision had already replaced the sentence with protection, save and undo notes, but these could still look implemented.

**Transaction and protection:** A transaction would save all deletions together or undo all of them. It would not by itself prevent a concurrent check-in from making an earlier state check stale. Check-in and deletion would need a shared protection contract. No such Java flow or persistence contract exists at this baseline. The revised diagram therefore shows the required checks and outcomes, with no transaction, lock or rollback call (G-01, G-02).

**Defence point:** Cancel keeps the booking in history. Delete removes it permanently (Q-05). A protected card link must stop deletion; do not remove cards or access history to force it through.

## SD-06 - General: import supplier XML

**What:** UC-15 starts when a hotel user receives and selects a supplier XML file. The class model calls `HotelFacade.importStock(file, user)`, then `StockService.importStock(file, user)`. The required result is a `StockImport` report. These operations are not implemented in Java yet.

**Why:** The [latest PR #20 decision](https://github.com/BorjaTiago/hotel4you/pull/20#issuecomment-6322549129), 2026-10-21, confirmed by the user on 2026-10-22, accepts all items from a valid XML file. It replaces the earlier catalogue-only rule and preview proposal.

**Why this way:** The design checks permission first, then parses and validates the whole XML before any write. A `loop` describes each valid item. An `opt` shows creation of a missing catalogue item. The required next steps add stock and record an ENTRY movement linked to the import and hotel user. The controller shows the report. These steps stay in notes because the parser and persistence contracts are missing. No helper method or rollback implementation is invented.

**Other option:** Preview the file, ask for confirmation or reject missing catalogue items. The latest decision removes those stages and accepts new items. The existing one-step import API already fits the requested flow, so no new methods are needed.

**Example:** Soap has balance 10. Towels do not exist in the catalogue. The XML supplies soap +5 and towels +3. Required success leaves soap at 15, creates towels with balance 3 and saves two ENTRY movements linked to one import. Required failure leaves soap at 10 without a new towels item. G-01 still needs an implementation that guarantees this outcome.

**Error cases:** An unauthorized user, invalid XML or a failed save must change no stock. A failed save must leave no partial writes or new catalogue items. The diagram marks these as required outcomes, not an implemented rollback. An item missing from the catalogue is not an error. "Accept all items" applies to a valid file; it does not bypass permission or XML validation.

**Report and model:** `StockImport` keeps the supplier and import time. Linked movements give items and quantities; the existing user association identifies who imported. The rejected-item field, getter and database column are removed from the documents. The import report remains useful without a rejection list.

**Defence point:** The supplier sends the file but does not log in (Q-17). An authorized hotel user performs the import. Automatic item creation is part of that import; manual catalogue management remains a manager action (Q-38).

## Cardinality review

The eight relevant multiplicities appear in `Note over` annotations inside the existing SD-04 and SD-06 blocks. They match the canonical class and domain models. UML `*` means `0..*`. A lifeline shows one participant in a scenario; it does not limit the number of stored objects. In this XML flow each ENTRY links the import. The model allows an optional import link because manual movements have none.

RF-16 and Q-09 require one card per reservation. The UML shows `Reservation 0..1 - 0..1 MagneticCard`. The schema text has a nullable card FK and a non-unique filtered index, so it does not enforce the upper bound. G-03 records that difference. The visual note gives the model rule and flags the schema gap. No association, database constraint or index changes here.

## Carry-over corrections (2026-10-30)

| Feedback | What was unclear | Change and evidence | How to explain it in class |
|----------|------------------|---------------------|----------------------------|
| SD-04 transaction wording | Several checks and writes looked like one implemented transaction. | Keep the existing three class-model calls. Expand the required check and deletion order in notes. No reservation Java flow exists; G-01/G-02 remain. | "Only a manager may delete. Find the current booking and reject any check-in before deleting its child rows and the booking. The atomic persistence contract still needs code." |
| Cardinality | The counts were only in the Markdown table. | Add four model associations inside each sequence. Class and domain associations agree; the card rule has the schema gap G-03. | "The numbers describe links between stored objects. They do not count calls or lifelines. One booking can have many extra rows, and one import can have many movements." |
| SD-06 review | Parsing was implicit and undo notes could look implemented. | Show parse, whole-file validation, item lookup, optional creation, stock update and ENTRY recording as required internal work. UC-15 and RF-28 accept new items. | "Validate first. Import every valid item and create a missing catalogue item. Permission, XML and storage errors must leave the original stock. There is no implemented rollback yet." |

## Questions the team may get

| Question | Short answer |
|----------|--------------|
| Why two diagrams? | The statement asks for at least one general and one detailed diagram. SD-04 and SD-06 cover two existing use cases. |
| Which one is general? | SD-06 shows the import workflow. SD-04 expands deletion decisions and outcomes. |
| Where is the database participant? | User and floor persistence exist, but reservation and stock persistence contracts are missing. The required storage work stays in notes (G-01). |
| Where are the cardinalities? | In model-multiplicity notes inside each rendered sequence. No separate diagram is needed. |
| What happens to a new XML item? | Create the catalogue item, add its quantity and record an ENTRY movement in the same import. |
| Does accept-all mean invalid XML is accepted? | No. Permission and XML validation still apply. |
| What happens if saving a new item succeeds but a later write fails? | UC-15 requires no partial writes, including that item. The atomic implementation is still G-01. |
| Is the card audit question closed? | No. Q-49 remains open. The card flow is outside these two diagrams. |
| Does a successful render mean Done? | No. It checks diagram syntax. Human review and persistence contracts remain. |

## Historical verification (2026-10-22)

This record describes the earlier revision only. It is not the validation result for the carry-over changes above.

Mermaid CLI 12.0.0 rendered all five blocks in the changed diagram files to temporary SVG files: the two sequences, class diagram, domain model and ER diagram. Both sequence diagrams also rendered to PNG and were visually inspected. No generated diagram files are committed.

A read-only check verified each call's receiving class, public method name and argument count against the class model and `classes.md`. Missing method names: **none**. SD-04 has 10 static message arrows and 3 calls. SD-06 has 9 static message arrows and 3 calls. The SD-04 Mermaid block is unchanged from the previous PR revision.

Checks confirmed that association counts and database indexes are unchanged. All fifteen updated documentation footers and local links passed checks. The removed rejected-item field, getter and column have no references in the docs. The card sequence and card gaps have no references in this guide or the UML index. Q-49 remains open. `git diff --check` passed.

Only Markdown documentation changed. The app, database scripts, JUnit and Javadoc were not run. No database migration is applied. These checks do not establish full Definition of Done approval; persistence contracts and human review remain.

## Carry-over verification (2026-10-30)

- Mermaid CLI rendered both modified blocks to temporary SVG and 2x PNG files. Both final PNGs were visually inspected. The eight multiplicities are fully visible inside the sequences. Disabling automatic wrapping fixed clipped note text; explicit line breaks keep the notes readable.
- A read-only static check verified all six call arrows against the public method names, receiving classes and argument counts in the class diagram, and against the method lists in `classes.md`. Missing class-model methods: none. This does not mean the Java methods are implemented.
- The same check matched all eight visual multiplicities against the class and domain associations. The schema enforcement difference remains G-03. No separate cardinality diagram was created.
- UC-05 and RF-22 were checked for manager access and refusal after check-in. UC-15 and RF-28 were checked for permission, XML validation, missing-item creation, stock entry and required failure outcomes. The current-state and transaction implementation gaps remain explicit.
- Checks confirmed exactly two `sequenceDiagram` blocks, with IDs SD-04 and SD-06. No SD-05 section or CardAccessService participant was restored. Only this guide and `sequence-diagrams.md` changed. `git diff --check` passed.
- No application, full build, JUnit, Javadoc, SQL script or database migration was run. Rendered images are temporary and are not committed. The applicable professor feedback is addressed in the design documents; this is not runtime or full Definition of Done approval.

Use Traceability and Gaps in [sequence-diagrams.md](sequence-diagrams.md) for the method list and class model feedback.

Created by: pedrocostaisep. Updated by: pedrocostaisep. AI use: sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16; PR review corrections by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; PR-20 import decision review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; merge-conflict resolution by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; carryover review by sequence-diagram-editor, OpenAI Codex (gpt-6.1-sol), 2026-10-30. Reviewed by: pending.
