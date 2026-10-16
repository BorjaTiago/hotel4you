# H4Y-8 - Sequence Guide

Use this guide to explain and defend the [sequence diagrams](sequence-diagrams.md). Class names and methods come from [classes.md](classes.md) and [class-diagram.md](class-diagram.md). Database terms come from [schema.md](../database/schema.md) and [tables.md](../database/tables.md).

## Main ideas

1. Read from top to bottom. A call arrow names a method of the receiver. A dashed arrow shows data, an error or completion.
2. A JavaFX controller reads input and shows results. It calls `HotelFacade`. Services check permissions and business rules.
3. Notes expose checks and data changes when the class model has no smaller method. They do not pretend that a new API exists.
4. `alt` means one branch runs. `opt` means the block runs only if its condition is true. `loop` repeats the work for each matching row or file line.
5. Booking, check-in, card issue, real entry and check-out are separate events. Their order matters.
6. `Gaps` are design feedback. Q-48 to Q-50 are open questions, not approved requirements.

## SD-01 - General: complete a hotel stay (ASSUMED)

**What:** One overview of Ana Silva's stay: book room 202 for three nights, add spa, check in and check out.

**Why:** The course asks for a general diagram. This one shows how the existing reservation, arrival and departure use cases fit together.

**Why this way:** It uses the same `Reservation` throughout. There is no separate stay object in the class model. The controller calls the facade and the facade calls services. SQL Server appears in notes because no persistence API exists. The overview is marked **ASSUMED** because the use case list has no single end-to-end stay use case.

**Other option:** One large diagram with all errors, database calls, card entries and payment. It would be hard to read and would require methods that do not exist. SD-02 to SD-06 expand the key parts.

**Example:** Ana books 2026-10-20 to 2026-10-23. Reception adds one spa extra. `Reservation.calculateTotal()` calculates the amount inside the service. Check-out records departure and stops the card. The diagram does not record a payment: payment has no use case or API (G-05, Q-48).

**Defence point:** The total is calculated from the current room type price and copied extra prices, as the present schema says. A room price snapshot needs a decision (G-06, Q-50). Avoid quoting a fixed total while that rule is open.

## SD-02 - Detailed: check-in with a magnetic card

**What:** UC-06 marks arrival, then issues the reservation's card.

**Why:** Arrival changes the booking state. Card issue gives the guest access to the room and included zones.

**Why this way:** `ReservationService.checkIn()` checks the current reservation before it calls `Reservation.checkIn()`. A later `CardAccessService.issueCard()` call creates the card. This follows the existing class guide. The diagram shows no invented reservation lookup or room occupancy method (G-02).

**Other option:** Treat handing over the card as an entry to room 202. That creates a false place and time. SD-05 records the first real room entry. Another unsafe option is to use the booking overlap check without excluding Ana's own reservation.

**Example:** Reception selects Ana's booking. A missing booking, an existing check-in or another active occupant stops the arrival operation. When arrival succeeds, reception issues card 12346. If a card is already linked, the service refuses a second card. A card issue failure does not undo an already saved arrival; reception resolves that failure and retries only card issue.

**Defence point:** One reservation has at most one card. Its owner is the reservation, not the receptionist who issued it. The number is unique. The database now keeps card numbers unique for all time; the earlier number reuse rule is not used.

## SD-03 - Detailed: create a reservation

**What:** UC-03 checks permission, dates and room overlap, then creates the booking and adds a selected extra.

**Why:** A room shown as free can become occupied before the user saves. A second check prevents an overlapping reservation.

**Why this way:** `ReservationService.create()` owns the save transaction. It uses `RoomService.isAvailable()` and `Reservation.overlaps()`. The room lock, overlap read and insert belong to the same transaction. The lock matters: a transaction alone does not prevent two callers from reading the same free room.

**Other option:** Check only in the controller, or trust the earlier room search. The saved room could then overlap another booking. Another option is to read the current extra price whenever the total is shown; that would lose the extra price at the time it was added.

**Example:** A booking from 20 October to 23 October overlaps one from 22 October to 24 October. It does not overlap one that starts on 23 October. Adding spa creates a `ReservationExtra` with a copy of `ExtraService.price`. A later catalogue change does not change that extra's `unitPrice`.

**Defence point:** The room price and extra price have different rules in the current sources. There is no reservation room price column. Do not claim that the whole booking price is frozen. `addExtra` can fail after a booking succeeds; the UI must show that result without claiming the booking was rolled back.

**Zone choice:** Spa is a paid extra, with no card access. To include restaurant or pool, use the existing `addLocation(reservation, location)` operation. The class model allows it only before check-in and only for zones with both access flags set.

## SD-04 - Detailed: delete a reservation

**What:** UC-05 deletes a booking for a manager, only before arrival.

**Why:** Reception may cancel a booking, but only the manager may delete it. Check-in history must remain.

**Why this way:** The service checks the caller and the current arrival state. It must recheck in the same transaction as the delete, so a concurrent check-in cannot invalidate the decision. The database has `NO ACTION` foreign keys. The backend deletes reservation extras and zone links before the reservation.

**Other option:** Hide the button from reception and trust the screen. A caller could still reach the action. Another option is a database cascade. The current schema uses explicit child deletion so the service controls the transaction.

**Example:** The manager deletes Ana's booking before check-in. The service removes its service and location rows, then the reservation. Reception gets a permission error. A checked-out booking is still protected because `checkedInAt` is not null. If a card or another protected link prevents deletion, the whole operation rolls back.

**Defence point:** Cancel, delete and deactivate mean different things. `cancel()` changes status and keeps history. This use case deletes a reservation. Q-36 does not permit a staff deactivation feature.

## SD-05 - Detailed: use a magnetic card and log entry

**What:** UC-09 checks a room or zone and records a real entry. Staff variants also apply the restriction in UC-17.

**Why:** A card number is not permission to enter every place. The hotel also needs a timeline of real card entries, including the master card.

**Why this way:** `CardAccessService.canOpenRoom()` checks rooms. `canOpen()` checks zones. Check `active` and `blockedAt` first, then the role and reservation. `register()` saves one target and `entryDateTime` after access is granted and the entry occurs. A card reader adapter must coordinate these calls; it is not in the class model (G-03).

**Other option:** Call `register()` after every access test. The present log cannot distinguish a denied attempt, so this would make a refused entry look successful. Denied audit needs its own contract or an outcome field (G-04, Q-49).

**Example:** Ana's card opens room 202. At 2026-10-20 15:05, `register(card, room202, null)` records that entry. The same card opens the pool only if the pool is included. A manager's active master card opens all rooms and zones. Reception gets zones on floors 0 and 1. Restaurant staff get only kitchen and restaurant. A blocked card gets nothing, including a master card.

**Defence point:** Exactly one target is set: room or location. The service supplies the time; issuing a card at reception does not create a room entry. SQL Server stores the record so the existing card and room report methods can read it later.

**Boundary point:** Actors call the service API in the diagram to expose the missing reader boundary. This is not JavaFX code. It does not permit a controller to bypass `HotelFacade` or call the database.

## SD-06 - Detailed: enter supplier delivery in stock

**What:** UC-15 reads supplier XML, imports known items and reports unknown names.

**Why:** The supplier sends the delivery file. An authorised staff user loads it. The balance and movement history must agree.

**Why this way:** Parse the whole file before stock writes. Invalid XML changes no stock. For valid XML, reject unknown items and retain their names in `StockImport.rejectedItems`. Valid lines call `StockService.applyMovement()` with `ENTRY`, a positive quantity, a null exit reason and the user. Stock, movements and report share a transaction.

**Other option:** Stop the whole import when one name is unknown. Q-10 says valid items must still be imported. Another option is to change only the stock balance; that loses the record of who changed it and why.

**Example:** TextilPT sends 50 towels and an unknown item named `bath kit`. The report lists `bath kit`. The towel balance rises by 50 and an entry movement links to this import. A malformed file saves no entry movements. The movement carries the time and the signed-in user.

**Defence point:** Unknown names are strings because they have no catalogue `StockItem`. The parser, lookup, invalid quantity policy and import-to-movement context still need contracts (G-07). The diagram describes these effects in notes rather than inventing methods.

## Questions the team may get

| Question | Short answer |
|----------|--------------|
| Why six diagrams? | One general overview and five detailed diagrams cover the main booking, access and stock flows. |
| Why is the overview ASSUMED? | The source lists booking, arrival and departure as separate use cases. It has no single hotel stay journey. |
| Where is the database participant? | No database class or persistence methods exist in the class diagram. SQL Server work is shown in notes (G-01). |
| Why is there no payment arrow? | There is no payment use case, class or method. Q-48 asks whether it belongs in scope. |
| Is denied access logged? | It is requested, but the current audit cannot record an outcome. G-04 and Q-49 make that gap explicit. |
| Which prices are copied? | Extra prices are copied to `unitPrice`. The current room base price is derived. Q-50 asks about the requested room snapshot. |
| Does the green syntax check mean Done? | No. It proves the diagrams render. Human review must still accept the assumptions and resolve the model gaps. |
| Did this work change Java or the class model? | No. It adds sequence documents, links and open questions. |

## Validation results (2026-10-16)

Mermaid CLI 12.0.0 rendered all six blocks. Temporary SVG and PNG files were used for syntax and visual checks. No rendered image is added to the repository. A temporary Puppeteer config uses the installed Microsoft Edge in headless mode.

A read-only checker compared every call with its receiving class, its argument count and its entry in `classes.md`. It also checked participant names, controller-to-facade calls, document links, requirement IDs, question IDs and AI-use footers. Missing method names: **none**.

| Diagram | Static message arrows, including returns | Method call arrows |
|---------|------------------------------------------|--------------------|
| SD-01 | 21 | 14 |
| SD-02 | 15 | 7 |
| SD-03 | 19 | 8 |
| SD-04 | 6 | 3 |
| SD-05 | 10 | 6 |
| SD-06 | 10 | 5 |

A loop can run more than once; the table counts arrows in the source, not runtime calls. `git diff --check` also passed. The application, JUnit tests and Javadoc were not run. This is a documentation check, not a full Definition of Done approval.

Use the Traceability and Gaps sections in [sequence-diagrams.md](sequence-diagrams.md) for the full method list and H4Y-5 feedback.

AI use: sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16. Reviewed by: pending.
