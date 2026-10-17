# H4Y-8 - Sequence Guide

Use this guide to defend the [sequence diagrams](sequence-diagrams.md). Methods come from [classes.md](classes.md) and [class-diagram.md](class-diagram.md). Database terms come from [schema.md](../database/schema.md) and [tables.md](../database/tables.md).

## Main ideas

1. Read from top to bottom. A call names a method of its receiver. A dashed arrow shows data, an error or completion.
2. A JavaFX controller reads input and shows results. It calls `HotelFacade`. Services check permissions and business rules.
3. Notes describe internal work. They do not add methods to the class model.
4. `alt` selects one branch. An activation shows when an operation runs. It does not prove a database transaction.
5. There are two diagrams. SD-04 expands deletion guards. SD-05 shows the card service boundary without internal calls.
6. G-01 to G-04 are model gaps. Q-49 remains open. They are not approved requirements.

## SD-04 - Detailed: delete a reservation

**What:** UC-05 deletes a booking for a manager, only before arrival.

**Why:** Reception may cancel a booking, but only the manager may delete it. Check-in history must remain.

**Why this way:** The controller calls the facade. The service checks the caller and the current arrival state. Each error branch returns an explicit result. The state check and delete share a transaction. This protects against a concurrent check-in. The database uses `NO ACTION` foreign keys. Delete child rows before the reservation.

**Other option:** Hide the button from reception and trust the screen. A caller could still reach the action. Another option is a database cascade. The current schema uses explicit child deletion so the service controls the transaction.

**Example:** The manager deletes Ana's booking before check-in. The service deletes its extra and zone links first. Reception receives a permission error. A checked-out booking remains protected because `checkedInAt` is not null. A protected link or storage failure rolls back the whole delete.

**Defence point:** Cancel, delete and deactivate mean different things. `cancel()` changes status and keeps history. This use case deletes a reservation. Q-36 does not permit a staff deactivation feature.

## SD-05 - General: use a magnetic card and log entry

**What:** A general view of UC-09. It shows the access decision and the record of a real entry. The client is the main actor. Staff and master cards use the role variants in the diagram document.

**Why:** The hotel must control access and keep entry records. The overview shows this order without expanding each internal permission check.

**Why this way:** `canOpenRoom()` checks a room. `canOpen()` checks a zone. Both return only a boolean. Every refusal gives the same `false` response. The reader receives no reason. `register()` runs only after access is allowed and entry occurs. It saves one target and the entry time. G-03 identifies the missing reader and door contracts.

**Other option:** Send a separate reason for an inactive card, a blocked card or a forbidden target. The review requires the same response for all refusals. Another option is to record every check as an entry. The current model cannot distinguish a refused attempt from a real entry.

**Example:** Ana's card opens room 202. At 15:05, `register(card, room202, null)` records her entry. Her card opens the pool only if the pool is included. An inactive card, a blocked card and a forbidden target all return `false`. The reader cannot tell which check failed.

**Role variants:** An active master card opens every room and zone. Reception gets zones on floors 0 and 1. Restaurant staff get only kitchen and restaurant. A blocked or inactive card opens nothing. The same boolean response and entry flow apply to every role.

**Defence point:** SD-05 is general because it stops at the service boundary. It has no domain or persistence lifelines. SD-04 is detailed because it expands service guards and deletion outcomes. Both cover existing use cases. No assumed hotel-stay use case is needed.

**Boundary point:** The actor arrows stand for the reader boundary. They do not allow JavaFX controllers to bypass the facade. Exactly one target is set: room or location. Giving a card to a guest does not record a room entry.

**Audit gap:** Denied audit remains open in Q-49 and G-04. Do not call `register()` for a refusal. Its current contract would create a false entry record.

## Questions the team may get

| Question | Short answer |
|----------|--------------|
| Why two diagrams? | The PR review reduced the scope. One general view and one detailed view meet the statement's minimum. |
| Which one is general? | SD-05 shows only the card service boundary. SD-04 expands deletion decisions and outcomes. |
| Where is the database participant? | The class model has no persistence API, except `UserRepository` (H4Y-32). SQL Server work appears in notes (G-01). |
| Does the reader get a refusal reason? | No. Every refusal returns the same `false` value. The reason stays internal. |
| Is denied access logged? | The current model cannot store an outcome. Q-49 and G-04 keep this gap explicit. |
| Does a successful render mean Done? | No. It checks diagram syntax. Human review and model decisions remain. |

## Validation results (2026-10-16)

Mermaid CLI 12.0.0 rendered both blocks to temporary SVG and PNG files. The previews were inspected. No rendered file or dependency was added to the repository.

A read-only checker verified each call against its receiving class and argument count. It also checked `classes.md`, participant names, controller-to-facade calls, links, requirement IDs and AI-use footers. Missing method names: **none**.

| Diagram | Static message arrows, including returns | Method call arrows |
|---------|------------------------------------------|--------------------|
| SD-04 | 10 | 3 |
| SD-05 | 5 | 3 |

The counts describe the source, not runtime calls. `git diff --check` passed. The application, JUnit tests and Javadoc were not run. These checks do not establish full Definition of Done approval.

Use Traceability and Gaps in [sequence-diagrams.md](sequence-diagrams.md) for the method list and class model feedback.

Created by: pedrocostaisep. Updated by: BorjaTiago. AI use: sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16; PR review corrections by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17. Reviewed by: pending.
