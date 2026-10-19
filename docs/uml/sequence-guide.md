# H4Y-8 - Sequence Guide

Use this guide to defend the [sequence diagrams](sequence-diagrams.md). Calls come from [classes.md](classes.md) and [class-diagram.md](class-diagram.md). Cardinalities are checked against the [domain model](domain-model.md), [schema](../database/schema.md), [ER diagram](../database/erd.md) and [table guide](../database/tables.md).

## Main ideas

1. Read from top to bottom. A solid arrow calls a method of the receiver. A dashed arrow shows data, an error or completion.
2. A JavaFX controller calls `HotelFacade`. Services check permissions and business rules. Controllers never call SQL Server.
3. Notes explain internal work. They do not add API methods. G-01 to G-04 identify missing contracts.
4. `alt` selects one branch. An activation shows execution, not a database transaction.
5. SD-04 details deletion decisions. SD-05 shows the service boundary for two access scenarios within UC-09.
6. A lifeline does not define cardinality. One manager lifeline can use a card with many earlier entry records.

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

## SD-05 - General: use a magnetic card and log entry

**What:** UC-09 has a client scenario and a manager master-card scenario. Both show the access decision and a real entry record.

**Why:** A master card is not tied to the guest's reservation. The second scenario makes that permission difference visible. It also proves that a block or inactive flag overrides master access.

**Why this way:** The client calls `canOpenRoom()` for a room or `canOpen()` for a zone. The manager calls `canOpenRoom(masterCard, room202)` for another guest's room. Both receive a boolean. `register()` runs only after access is allowed and entry occurs. It records exactly one target and the entry time.

**Other option:** Describe the master card only in a table. That does not show its messages. Adding card issue or replacement would cover a different use case. They are not added here.

**Example:** Ana's card enters room 202 at 09:30. The manager's active, unblocked master card enters the same room at 10:00. The manager has no reservation for room 202. Both entries use the same log contract. If the master card is blocked or inactive, `canOpenRoom()` returns `false` and no entry is recorded.

**Refusal rule:** Every refused check returns the same `false` value. The reader receives no reason. The internal error labels do not change the response. This applies to guest, staff and master cards.

**Role variants:** An active master card opens every room and zone. Reception gets zones on floors 0 and 1. Restaurant staff get only kitchen and restaurant. Blocked or inactive cards open nothing.

**Boundary point:** The actors stand for the card reader boundary. G-03 identifies the missing reader, door and entry-event contracts. The diagram does not let JavaFX controllers bypass the facade.

**Defence point:** This remains a general view. It shows two variants of UC-09 at the existing service API. It does not expand internal checks into invented methods.

**Audit gap:** Q-49 and G-04 remain open. `register()` stores an entry and has no outcome argument. Calling it for a refusal would create a false entry record.

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
| Review cardinality. | The table above checks eight relationships and rules. The ER and schema now match Q-09. Domain prose makes the owner and entry-target XOR rules explicit. |
| CardAccessService is too simple. | SD-05 adds manager-to-service calls for master-card entry and refusal. It remains UC-09 and stays general. |

## Validation results (2026-10-19)

Mermaid CLI 12.0.0 rendered four blocks to temporary SVG files: the two sequences, the domain model and the ER diagram. Both sequence diagrams also rendered to PNG. Their previews were inspected for clipped text and readability. No rendered file is committed.

A read-only checker verified participants, receiving classes, public method visibility and argument counts against `classes.md` and `class-diagram.md`. Missing method names: **none**.

| Diagram | Static message arrows, including returns | Method call arrows |
|---------|------------------------------------------|--------------------|
| SD-04 | 10 | 3 |
| SD-05 | 9 | 5 |

Eight association rules were checked in both UML models. The review also checked the reservation-card ER correction, filtered unique index and existing owner and target XOR constraints. Links, requirement IDs, use case IDs, question IDs, gap IDs and documentation footers passed the checks. `git diff --check` passed.

Exactly two sequence diagrams remain. Only six Markdown files changed against `origin/main`. No Java, test, SQL script, dependency, CI or policy file changed. The application, database scripts, JUnit tests and Javadoc were not run.

These document checks do not validate the application or establish full Definition of Done approval. Human review and the contracts in G-01 to G-04 remain. The Jira connection was unavailable. Scope and teacher feedback came from the user's supplied ticket text.

Created by: pedrocostaisep. Updated by: pedrocostaisep. AI use: sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16; PR review corrections by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16. teacher feedback corrections by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-19. Reviewed by: pending.
