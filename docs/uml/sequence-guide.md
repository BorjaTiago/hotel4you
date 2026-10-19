# H4Y-8 - Sequence Guide

Use this guide to defend the [sequence diagrams](sequence-diagrams.md). Calls come from [classes.md](classes.md) and [class-diagram.md](class-diagram.md). Cardinalities are checked against the [domain model](domain-model.md), [schema](../database/schema.md), [ER diagram](../database/erd.md) and [table guide](../database/tables.md).

## Main ideas

1. Read from top to bottom. A solid arrow calls a method of the receiver. A dashed arrow shows data, an error or completion.
2. A JavaFX controller calls `HotelFacade`. Services check permissions and business rules. Controllers never call SQL Server.
3. Notes explain internal work. They do not add API methods. G-01 to G-05 identify missing or new contracts.
4. `alt` selects one branch. An activation shows execution, not a database transaction.
5. SD-04 details deletion decisions. SD-06 gives a general view of UC-15: review supplier XML, cancel or confirm, then show the report.
6. A lifeline does not define cardinality. One import can contain many stock movements even when a diagram shows one service lifeline.

## SD-04 - Detailed: delete a reservation

**What:** UC-05 deletes a booking for a manager before check-in. It also deletes that booking's extra rows and zone links.

**Why:** Reception can cancel a booking. Only a manager can delete it. A booking with any check-in history stays protected, even after check-out.

**Why this way:** The controller calls the facade. The service checks the role before changing data. It then protects the booking and reads its current state. Missing bookings and non-null `checkedInAt` return explicit errors. The service saves all deletions together. If any deletion fails, it undoes all of them.

**Other option:** Delete and save each row separately. An error could leave a booking without its extras or zone links. Hiding the button is also insufficient: the service must check permissions.

**Example:** Booking R17 has two extra rows and one pool link. Check-in has not occurred. The manager deletes R17. Success removes those four rows: two extras, one link and the booking. Room 202, the pool and the service catalogue remain. If the final booking delete fails, both extra rows and the pool link return. R17 stays unchanged.

### What a transaction means

A transaction is a group of database changes. The database saves the whole group or undoes the whole group. In this example, the group contains only R17 and its dependent rows.

- **Commit:** every deletion succeeds. Save all four deletions together.
- **Rollback:** one deletion fails. Undo all deletions in that group.

`NO ACTION` means the database will not delete linked rows automatically. The backend removes the booking's child rows first. A remaining card link still prevents the booking delete. The backend must not delete cards or access history to bypass that protection.

### What protects against a concurrent check-in

A transaction alone does not stop two callers from reading the same old state. A temporary lock must protect the selected booking. Deletion and check-in must respect the same protection.

The required persistence contract is:

1. Check that the caller is a manager.
2. Start the deletion transaction. Acquire protection for the selected booking, then read its latest state.
3. Refuse a missing booking or a booking with non-null `checkedInAt`.
4. Keep the protection while deleting the extra rows, zone links and booking.
5. Save all changes or undo all changes. End the transaction and release the protection on every path.

If check-in gets protection first, deletion waits. Its later read sees the arrival and refuses deletion. If deletion gets protection first, check-in waits. After a successful delete, check-in finds no booking. After a rollback, check-in can use the unchanged booking.

This describes a required lock or equivalent guarded operation. It does not claim that a repository or transaction API exists. G-01 and G-02 remain open design contracts. No new method arrows are added.

**Defence point:** A transaction prevents partial deletion. A lock prevents a competing change from invalidating the state check. These are different jobs.

## SD-06 - General: review and import supplier XML

**What:** UC-15 starts with a selected supplier XML file. The system previews known items for restock and unknown items for rejection. The user confirms or cancels. Confirmation returns an import report.

**Why:** The [PR #20 comment](https://github.com/BorjaTiago/hotel4you/pull/20#issuecomment-6265012673) asks for a more useful flow than the card check. The import has a user decision, validation, stock changes and a report. It still covers one existing use case and supplies the general diagram required by the statement.

**Why this way:** The controller calls the facade for `previewStock()`. The service reads the file and catalogue without saving stock. `StockImportPreview` carries the two review groups. Cancel sends no confirmation call. Confirm uses `confirmStockImport(preview.id, user)`. The service checks permission, preview ownership and current catalogue again. It saves the import, ENTRY movements and balances together. The facade reads `StockImport.getRejectedItems()` for the report. The controller handles no business rule.

**Other option:** Call the old one-step `importStock()` to get a preview, or read the same file path again after confirmation. The first option writes before the user agrees. The second could import different file data from the preview. The new contract keeps the exact file snapshot under a preview ID held by the service for that user. It accepts one successful confirmation only.

**Example:** Soap is in the catalogue with balance 10. Towels are in the catalogue with balance 0. The file supplies soap +5, towels +3 and an unknown item, "Blue gel" +2. The preview lists soap and towels for stock entry. It lists Blue gel as unknown and rejected. Cancel leaves balances 10 and 0, with no saved import or movement. Confirm changes them to 15 and 3 and saves two ENTRY movements linked to one `StockImport`. The report lists Blue gel as rejected. A second confirmation of the same preview adds nothing. If any save fails, both balances stay 10 and 0 and no partial import remains.

### How the review comment maps to the flow

| Review request | Result in SD-06 |
|----------------|-----------------|
| Attach the supplier XML | The user selects the file before `onImportXmlClicked()`. The supplier does not log in (Q-17). |
| Decide to import | The first click requests a preview only. No stock is saved. |
| Show new items and restock items | Known catalogue items, including zero-balance items, appear for stock entry. Unknown catalogue names appear in a separate rejected group. |
| Confirm, import and show a report | A separate confirmation calls the facade and service. Success returns `StockImport` and its rejected items. |
| Cancel and do nothing | No confirmation call, import row, movement or stock change. |

**Catalogue rule:** RF-28, Q-10 and Q-38 reject items outside the catalogue. A catalogue item with zero stock is still known. The import does not create new catalogue items. UC-19 lets the manager create them separately. Q-51 asks whether the review intended a different rule. Until that is answered, the existing rule stays in force.

**Error rule:** Invalid XML or an unauthorized caller gives a preview error with no stock changes. Unknown items are rejected by line; known items can still be imported after confirmation. A lost permission, missing, foreign, changed or used preview gives an import error. A changed catalogue requires a fresh preview. Saving is atomic: no partial import remains on failure. If every line is unknown, the report rejects every line and no stock is added.

**Model change:** The baseline has only `importStock(file, user)`. It has no preview or confirmation contract. This change explicitly adds `StockImportPreview`, `StockController.onConfirmImportClicked()`, and `previewStock` / `confirmStockImport` on both `HotelFacade` and `StockService`. They appear in [class-diagram.md](class-diagram.md), [classes.md](classes.md), [guide.md](guide.md) and [traceability.md](traceability.md). G-05 records the implementation and H4Y-5 review still needed. No missing method is hidden in a sequence arrow.

**Defence point:** This is a general workflow view. Parsing, matching catalogue items, adding stock and storing movements remain service notes. It does not add parser, SQL or repository classes. SD-05 is replaced, not renumbered: exactly two diagrams remain, SD-04 and SD-06. Card reader and denied-audit gaps remain open outside them.

## Cardinality review

Multiplicity tells how many objects may be linked. `*` means zero or more (`0..*`). The class and domain diagrams already have the counts below. Their numeric multiplicities do not need to change.

| Relationship | Multiplicity and rule | Source and review result |
|--------------|-----------------------|--------------------------|
| Room and Reservation | Each booking has exactly one room. One room has `0..*` bookings across dates. | `reservations.room_id` is required. Non-overlap is a separate rule; it does not make this one-to-one. |
| Reservation and MagneticCard | Each booking has `0..1` card. Each card has `0..1` reservation. A guest card must have its reservation link. | Q-09 and the UML agree. The ER previously allowed `0..*` cards per booking. Corrected to `0..1`; the schema now documents a filtered unique index. |
| User and MagneticCard | Each card has `0..1` staff user. One user has `0..*` cards across history. | Q-19, Q-26 and Q-31 allow a blocked old master card and its replacement to remain linked to the same manager. Do not make `user_id` unique. |
| Card owner XOR | Exactly one of Reservation and User is set. Each individual link remains `0..1`. | The existing `access_cards` CHECK rejects both owners and rejects no owner. Domain prose now states this clearly. |
| MagneticCard and CardAccessRecord | Each record has exactly one card. A card has `0..*` records. | `access_logs.access_card_id` is required. A new card can have no entries. Guest and master entries share this relation. |
| CardAccessRecord and Room or Location | Each target link is `0..1`. Exactly one is set. One target has `0..*` records. | The existing target XOR CHECK rejects both targets and rejects no target. Use `register(card, room, null)` or `register(card, null, location)`. |
| Reservation and ReservationExtra | Each extra row belongs to one booking. A booking has `0..*` extra rows. | `reservation_services.reservation_id` is required. Deletion removes these owned rows, not the shared ExtraService catalogue. |
| Reservation and Location | Each side has `0..*` links. Each link row has one booking and one location. | `reservation_locations` is a join table. Its composite key prevents duplicate links. Delete its rows for the booking, not the locations. |
| StockItem and StockMovement | Each movement has exactly one item. An item has `0..*` movements. | `stock_movements.item_id` is required. Each accepted XML line creates an ENTRY movement for a catalogue item. |
| StockImport and StockMovement | A movement has `0..1` import. An import has `0..*` movements. | Manual use or exit has no import link. Accepted XML movements link to one import. A file with only rejected items can have no movements. |
| User and StockImport | Each saved import has one user. A user can have `0..*` imports. | The existing `StockImport` to `User` association and `stock_imports.user_id` agree. `StockImportPreview` is review data, not another saved import. |

### Why one booking card does not mean one staff card

Before check-in, a booking can have no card. After card issue, it has one. That is `0..1`. The current model does not define guest-card replacement; this task adds no such rule.

A manager can retain blocked card 90001 and later use replacement 90002. Both card rows remain for history. Only the current usable master card may open a door. Q-19, Q-26 and Q-31 control that state; the association still allows `0..*` staff cards.

### Database documentation correction

The ER allowed many cards for one reservation. The schema had only a non-unique index on that link. This did not enforce Q-09.

The ER now shows `0..1` on both sides of the reservation-card relation. The schema specifies `UNIQUE (reservation_id)` only where `reservation_id IS NOT NULL`. Staff cards have null reservation IDs, so many staff cards remain possible. The existing owner XOR still applies.

This corrects the documentation of an existing team decision. No SQL script was changed or run. A future database implementation must create the documented index. No database migration is claimed here.

## Teacher feedback addressed

| Feedback | Change |
|----------|--------|
| Explain the state check and transaction. | SD-04 shows protection, the current-state decision, deletion, save and undo. This guide separates transaction atomicity from the concurrent check-in guard. |
| Review cardinality. | The table above checks the original eight rules and three stock rules. The ER and schema match Q-09. Domain prose makes owner and entry-target XOR rules explicit. |
| CardAccessService is too simple. | Following PR #20 review, replace SD-05 with SD-06 for supplier XML import. The general workflow has preview, cancel, confirmation, validation and report. |

## Validation results (2026-10-19)

Mermaid CLI 12.0.0 rendered all five blocks in the PR diagram files to temporary SVG files: two sequences, the class diagram, domain model and ER diagram. Both sequences also rendered to PNG and were visually inspected. The XML sequence was rendered and inspected again after shortening clipped notes. No rendered file is committed.

A read-only check verified participants, receiving classes, public method names and argument counts against `class-diagram.md` and the receiving class rows in `classes.md`. Missing method names: **none**. The preview and confirmation methods are explicit additions to the documented class design, not implemented Java methods. The SD-04 Mermaid block is unchanged from the previous PR revision.

| Diagram | Static message arrows, including returns | Method call arrows |
|---------|------------------------------------------|--------------------|
| SD-04 | 10 | 3 |
| SD-06 | 18 | 7 |

The read-only check verified eleven associations in both UML models: the original eight relations and three stock relations. It also checked the changed document links, requirement IDs, use case IDs, question IDs, gap IDs and all nine updated documentation footers. Q-10 and Q-38 remain unchanged. `git diff --check` passed. The previous review also checked the reservation-card ER correction, filtered unique index and owner and target XOR constraints; those files do not change in this revision.

Exactly two sequence diagrams remain. This revision changes nine Markdown documents. No Java, test, SQL script, dependency, CI or policy file changed. The application, database scripts, JUnit tests and Javadoc were not run.

These document checks do not validate the application or establish full Definition of Done approval. Human review and the contracts in G-01 to G-05 remain. Jira was not queried for this revision. Scope comes from the user's supplied ticket text and the PR #20 comment. Q-51 leaves catalogue creation undecided; Q-10 and Q-38 still apply.

Created by: pedrocostaisep. Updated by: pedrocostaisep. AI use: sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16; PR review corrections by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16. teacher feedback corrections by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-19; PR-20 stock import review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-19. Reviewed by: pending.
