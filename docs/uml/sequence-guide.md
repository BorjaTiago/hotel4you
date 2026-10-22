# H4Y-8 - Sequence Guide

Use this guide to defend the [sequence diagrams](sequence-diagrams.md). Methods come from [classes.md](classes.md) and [class-diagram.md](class-diagram.md). Database terms come from [schema.md](../database/schema.md) and [tables.md](../database/tables.md).

## Main ideas

1. Read from top to bottom. A solid arrow calls a method of its receiver. A dashed arrow shows data, an error or completion.
2. A JavaFX controller reads input and shows results. It calls `HotelFacade`. Services check permissions and business rules. Controllers never call the database.
3. Notes describe internal work. They do not add methods to the class model.
4. `alt` selects one branch. An activation shows when an operation runs. It does not prove a database transaction.
5. SD-04 is detailed: it expands deletion guards, protection, save and undo. SD-06 is general: it shows XML selection, the import call and the report.
6. G-01 and G-02 are persistence gaps. The card audit question Q-49 stays open outside these diagrams.

## SD-04 - Detailed: delete a reservation

**What:** UC-05 lets a manager delete a booking only before check-in. It removes the booking and its dependent extra and zone-link rows.

**Why:** Reception can cancel a booking. Only a manager can delete it. Any check-in history protects the booking, even after check-out.

**Why this way:** The controller calls the facade, then `ReservationService.delete()`. The service checks the role. It protects the booking and reads its latest state. A missing booking or non-null `checkedInAt` gives an error. It deletes child rows before the booking and saves all deletions together. A failed deletion undoes all changes. Every path releases protection before returning.

**Other option:** Trust the delete button or save each row separately. The service still needs a role check. Separate saves could leave the booking without extras or zone links after a failure.

**Example:** R17 has two extra rows and one pool link. Success removes those three rows and R17 together. The room, pool and service catalogue remain. If the booking delete fails, rollback restores all four rows.

**Transaction and protection:** Commit saves the whole group. Rollback undoes the whole group. A transaction prevents partial deletion. A lock or equivalent guarded operation prevents a concurrent check-in from invalidating the current-state check. Check-in and deletion must respect the same protection. If check-in wins first, deletion later reads `checkedInAt` and refuses. If deletion wins first, check-in waits until save or undo finishes. G-01 and G-02 identify the persistence contracts still needed.

**Defence point:** Cancel keeps the booking in history. Delete removes it permanently (Q-05). A protected card link must stop deletion; do not remove cards or access history to force it through.

## SD-06 - General: import supplier XML

**What:** UC-15 starts when a hotel user selects a supplier XML file. The controller calls `HotelFacade.importStock(file, user)`, which calls `StockService.importStock(file, user)`. The service imports the file and returns the report.

**Why:** The [latest PR #20 decision](https://github.com/BorjaTiago/hotel4you/pull/20#issuecomment-6322549129), 2026-10-21, confirmed by the user on 2026-10-22, accepts all items from a valid XML file. It replaces the earlier catalogue-only rule and preview proposal.

**Why this way:** The service checks permission and XML validity before writing. For each XML item, it finds the catalogue entry or creates it if missing. It adds stock and an ENTRY movement. It saves new items, the import, movements and balances together. The controller shows the returned report. Matching items, creating them and storing data stay in notes because this is a general workflow view.

**Other option:** Preview the file, ask for confirmation or reject missing catalogue items. The latest decision removes those stages and accepts new items. The existing one-step import API already fits the requested flow, so no new methods are needed.

**Example:** Soap has balance 10. Towels do not exist in the catalogue. The XML supplies soap +5 and towels +3. Success leaves soap at 15, creates towels with balance 3 and saves two ENTRY movements linked to one import. If saving fails, soap stays at 10 and the new towels item is undone too.

**Error cases:** An unauthorized user or invalid XML changes no data. A failed save rolls back the whole import. An item missing from the catalogue is not an error. "Accept all items" applies to a valid file; it does not bypass permission or XML validation.

**Report and model:** `StockImport` keeps the supplier and import time. Linked movements give items and quantities; the existing user association identifies who imported. The rejected-item field, getter and database column are removed from the documents. The import report remains useful without a rejection list.

**Defence point:** The supplier sends the file but does not log in (Q-17). An authorized hotel user performs the import. Automatic item creation is part of that import; manual catalogue management remains a manager action (Q-38).

## Cardinality review

The [sequence file](sequence-diagrams.md#cardinality-review) reviews the existing multiplicities. A lifeline shows one participant in a scenario; it does not limit the number of objects in storage. One import can link many ENTRY movements. Every movement links one stock item and at most one import. Manual movements can have no import. No association counts or database indexes change in this review.

## Questions the team may get

| Question | Short answer |
|----------|--------------|
| Why two diagrams? | The statement asks for at least one general and one detailed diagram. SD-04 and SD-06 cover two existing use cases. |
| Which one is general? | SD-06 shows the import workflow. SD-04 expands deletion decisions and outcomes. |
| Where is the database participant? | The class model includes user persistence (H4Y-32). Reservation and stock persistence contracts are missing. Storage work appears in notes (G-01). |
| What happens to a new XML item? | Create the catalogue item, add its quantity and record an ENTRY movement in the same import. |
| Does accept-all mean invalid XML is accepted? | No. Permission and XML validation still apply. |
| What happens if saving a new item succeeds but a later write fails? | Roll back all writes for the import, including that item. |
| Is the card audit question closed? | No. Q-49 remains open. The card flow is outside these two diagrams. |
| Does a successful render mean Done? | No. It checks diagram syntax. Human review and persistence contracts remain. |

## Verification (2026-10-22)

Mermaid CLI 12.0.0 rendered all five blocks in the changed diagram files to temporary SVG files: the two sequences, class diagram, domain model and ER diagram. Both sequence diagrams also rendered to PNG and were visually inspected. No generated diagram files are committed.

A read-only check verified each call's receiving class, public method name and argument count against the class model and `classes.md`. Missing method names: **none**. SD-04 has 10 static message arrows and 3 calls. SD-06 has 9 static message arrows and 3 calls. The SD-04 Mermaid block is unchanged from the previous PR revision.

Checks confirmed that association counts and database indexes are unchanged. All fifteen updated documentation footers and local links passed checks. The removed rejected-item field, getter and column have no references in the docs. The card sequence and card gaps have no references in this guide or the UML index. Q-49 remains open. `git diff --check` passed.

Only Markdown documentation changed. The app, database scripts, JUnit and Javadoc were not run. No database migration is applied. These checks do not establish full Definition of Done approval; persistence contracts and human review remain.

Use Traceability and Gaps in [sequence-diagrams.md](sequence-diagrams.md) for the method list and class model feedback.

Created by: pedrocostaisep. Updated by: pedrocostaisep. AI use: sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16; PR review corrections by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17; PR-20 import decision review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; merge-conflict resolution by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22. Reviewed by: pending.
