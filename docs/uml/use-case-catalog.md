# Use-case catalogue

Each use case has at least one actor. An included use case gets its actor through its base use case (column "Actor(s)" shows "via UC-NN"). The short descriptions and permissions follow the current project requirements and team decisions. Team decisions that are not confirmed by the teacher are identified by their Q number. The diagrams are in [use-cases.md](use-cases.md). The flows are in [../requirements/use-cases.md](../requirements/use-cases.md).

Phase: "1st" = 1st delivery, "final" = final delivery (Q-06, Q-27). "backlog" = planned by the prioritizer; the phase is set in the backlog order (Q-25).

## 1. Access and staff accounts

| ID | Use case | Actor(s) | Short description | Permission rules | Phase | Requirements and decisions |
|---|---|---|---|---|---|---|
| UC-01 | Log in | Receptionist, manager, restaurant staff | A staff user signs in with a user name and a password. | Only these staff roles log in. Clients and suppliers do not log in. The same error message for a wrong user name or a wrong password. | 1st | RF-01, RF-02, RNF-02; Q-13, Q-17, Q-23 |
| UC-20 | Show the functions of the role | Via UC-01 | After login, the main window shows only the functions of the role of the user. | Each role sees only its own functions. | 1st | RF-02; H4Y-23, H4Y-24 |
| UC-21 | Log out | Receptionist, manager, restaurant staff | The user closes the session and goes back to the login screen. | Any logged-in user. | 1st | RF-01; H4Y-24 |
| UC-13 | Create staff account | Manager | The manager creates an account for a receptionist, a manager or a restaurant employee. | Manager only. The user name is unique. The password is saved as a hash. | backlog | RF-26, RF-01, RNF-02; Q-18, Q-46, Q-51, Q-52 |
| UC-22 | Edit staff account | Manager | The manager changes the data of a staff account. | Manager only. | backlog | RF-26; Q-18 |
| UC-23 | Remove staff account | Manager | The manager removes a staff account. | Manager only. An account with linked records cannot be removed (Q-36). | backlog | RF-26; Q-18, Q-36 |
| UC-24 | List users | Manager | The manager sees all saved users, at any time. | Manager only. | backlog | RNF-01; Q-39 |
| UC-45 | Access services of floors 0 and 1 | Receptionist | The receptionist uses only the services of floors 0 and 1. | Refused on other floors. No guest room without a manager master card (Q-08). | backlog | RF-24; Q-08 |
| UC-17 | Access kitchen and restaurant | Restaurant staff | The restaurant employee logs in and uses the kitchen and the restaurant. | Refused in every other place. | backlog | RF-25, RF-01; Q-23 |

## 2. Hotel structure and prices

| ID | Use case | Actor(s) | Short description | Permission rules | Phase | Requirements and decisions |
|---|---|---|---|---|---|---|
| UC-25 | Consult floors and zones | Manager | The manager sees floors -1 to 3 and the zones of each floor. | Manager only. | 1st | RF-03; H4Y-28 |
| UC-26 | Create zone | Manager | The manager creates a zone on a floor and sets its two card flags. | Manager only. Flags: guest card access, and reservation needed (Q-45). | final | RF-03, RF-18; Q-32, Q-45; H4Y-28 |
| UC-27 | Edit zone | Manager | The manager changes the name or the card flags of a zone. | Manager only. | final | RF-03, RF-18; Q-32, Q-45; H4Y-28 |
| UC-28 | Remove zone | Manager | The manager removes a zone. | Manager only. A zone with reservations or card passes cannot be removed (Q-36). | final | RF-03; Q-32, Q-36; H4Y-28 |
| UC-29 | Register rooms | Manager | The manager registers the 20 rooms with their number, floor and type. | Manager only. Floors 2 and 3, 10 rooms each. 7 single, 10 double, 3 family (Q-01). | 1st | RF-04; Q-01; H4Y-25 |
| UC-30 | List rooms | Receptionist | The receptionist sees the rooms with type, floor and price per night. | Logged-in staff. | 1st | RF-04, RF-06 |
| UC-12 | Configure room items | Manager | The manager adds, changes or removes the items of a room (towels, TV, safe, and so on). | Manager only. Invalid data is not saved. | 1st | RF-08; Q-43 |
| UC-31 | Set base price per room type | Manager | The manager sets the price per night of each room type. | Manager only. | 1st | RF-06; Q-11; H4Y-26 |
| UC-32 | Set extra prices | Manager | The manager sets the fixed price per night of each extra and paid service. | Manager only. | backlog | RF-07, RF-09; Q-11 |

## 3. Clients and reservations

| ID | Use case | Actor(s) | Short description | Permission rules | Phase | Requirements and decisions |
|---|---|---|---|---|---|---|
| UC-02 | Register client | Receptionist | The receptionist records the data of a new client. Extends UC-03 when the client is not registered. | Receptionist only. Registration plus reservation: 30 seconds or less (RNF-03). | 1st | RF-10, RNF-03; Q-12, Q-27, Q-29 |
| UC-33 | Search client | Via UC-03 | The receptionist finds a registered client. Included in UC-03. | Receptionist only. | 1st | RF-10 |
| UC-03 | Make reservation | Receptionist | The receptionist selects a client, room type, nights and a free room, then saves the reservation and its price. | Receptionist only. Dates and room cannot change; cancel and create a new reservation (Q-40). | 1st | RF-05, RF-11; Q-40 |
| UC-34 | Query free rooms | Via UC-03 | The system shows the free rooms of a type for the selected nights. Included in UC-03. | A room with an overlapping reservation is not free. | 1st | RF-05 |
| UC-35 | Calculate reservation price | Via UC-03 | The system calculates the total: base price per night plus extras and paid services. Included in UC-03. | Prices come from UC-31 and UC-32. | 1st | RF-06, RF-07, RF-09; Q-11 |
| UC-36 | Add room extras to reservation | Receptionist | The receptionist adds or removes a special view, drinks or laundry. Extends UC-03. | Until check-in only (Q-40). Sea or mountain view, not both (Q-37). | backlog | RF-07; Q-11, Q-37, Q-40 |
| UC-37 | Add paid services to reservation | Receptionist | The receptionist adds or removes food, cultural events, private pools, spa or conference rooms. Extends UC-03. | Until check-in only (Q-40). These services give no card access (Q-20). | backlog | RF-09; Q-20, Q-40 |
| UC-38 | Include restaurant and pool in reservation | Receptionist | The receptionist adds the restaurant or the pool to the reservation, so that the card opens them. Extends UC-03. | Until check-in only (Q-40). Only zones with both card flags (Q-45). | final | RF-11, RF-18; Q-42, Q-45 |
| UC-04 | Cancel reservation | Receptionist | The receptionist cancels a reservation and the system keeps it in history. | Receptionist only. Refused after check-in. | 1st | RF-12; Q-05, Q-40 |
| UC-05 | Delete reservation | Manager | The manager deletes a reservation for good, before check-in. | Manager only. Refused after check-in and for other roles. Includes UC-51. | 1st | RF-22; Q-05 |
| UC-51 | Check reservation has check-in | Via UC-05 | The system checks if the reservation has a check-in. Included in UC-05. | Automatic. With a check-in, the system refuses the delete. | 1st | RF-22; Q-05 |

## 4. Stay and guest services

| ID | Use case | Actor(s) | Short description | Permission rules | Phase | Requirements and decisions |
|---|---|---|---|---|---|---|
| UC-06 | Check-in | Receptionist | The receptionist checks in the client of a reservation. | Receptionist only. A reservation must exist. Includes UC-52. | 1st | RF-13 |
| UC-52 | Check client has reservation | Via UC-06 | The system checks if the client has a reservation. Included in UC-06. | Automatic. Without a reservation, the system refuses the check-in. | 1st | RF-13 |
| UC-39 | Issue guest card | Via UC-06 | The system gives one numbered card per reservation and links it to the room and the reserved zones. Included in UC-06. | One card per reservation (Q-09). | final | RF-13, RF-16, RF-17, RF-18; Q-09 |
| UC-07 | Check-out | Receptionist | The receptionist checks out the client and the system saves the check-out. | Receptionist only. | 1st | RF-14; Q-30 |
| UC-40 | Deactivate guest card | Via UC-07 | The card stops working at check-out. No door opens. The log is kept. Included in UC-07. | Automatic. | final | RF-14, RF-16; Q-30 |
| UC-08 | Record room service request | Receptionist (client: secondary) | The client asks reception for food, drink or laundry. The receptionist records it and the system adds the cost to the stay. | Receptionist records. Staff enter guest rooms only with a manager master card (Q-08). | backlog | RF-15, RF-07, RF-09; Q-08, Q-11, Q-33 |
| UC-14 | Record suggestion or complaint | Receptionist (client: secondary) | The client gives a suggestion or a complaint to reception. The receptionist records it. | Receptionist records. | backlog | RF-27; Q-18 |
| UC-41 | Consult suggestions and complaints | Manager | The manager receives and reads the suggestions and complaints. | Manager only. | backlog | RF-27; Q-18 |

## 5. Magnetic cards

| ID | Use case | Actor(s) | Short description | Permission rules | Phase | Requirements and decisions |
|---|---|---|---|---|---|---|
| UC-09 | Open room with guest card | Client | The client opens the reserved room with the card. | A guest card opens only its own room, between check-in and check-out. | final | RF-17; Q-09, Q-30 |
| UC-42 | Open restaurant or pool with guest card | Client | The client opens the restaurant or the pool with the card. | Only when the zone is in the reservation. The card never opens the bar, conference rooms, garage, storeroom, private pools or spa (Q-20). | final | RF-18; Q-09, Q-20, Q-32, Q-45 |
| UC-43 | Open any room or zone with master card | Manager | The manager opens any room or zone with the master card. | One master card per manager. A blocked card opens nothing (Q-26). | final | RF-23; Q-19, Q-26 |
| UC-44 | Record card pass | Via UC-09, UC-42, UC-43 | The system saves each card pass with the place and the entry time. Included in the card use cases. | Automatic. Master card passes are also saved (Q-19). | final | RF-19; Q-09, Q-19 |
| UC-10 | List places of one card | Manager | The manager enters a card number and sees each place and entry time. | Manager only. | final | RF-19, RF-20 |
| UC-11 | List card passes of a room | Manager | The manager enters a room number and sees each card pass, master card included. | Manager only. | final | RF-19, RF-21, RF-23 |
| UC-18 | Block lost master card | Manager | The manager blocks a lost master card. The block is immediate and logged. | Manager only. | final | RF-30, RF-23; Q-19, Q-26 |
| UC-46 | Issue new master card | Manager | The manager issues a new master card with a new number. | Manager only. The old card is blocked first (Q-31). A manager created later also gets a card this way (Q-44). | final | RF-30, RF-23; Q-26, Q-31, Q-44 |

## 6. Stock

| ID | Use case | Actor(s) | Short description | Permission rules | Phase | Requirements and decisions |
|---|---|---|---|---|---|---|
| UC-15 | Enter supplier delivery in stock | Receptionist, manager (supplier: secondary) | The supplier sends an XML delivery file. Hotel staff or the manager loads it. The system imports every item from a valid file, creates missing catalogue items and records the entries. | Includes UC-47 and UC-53. Supplier does not log in or load the file. No permission or invalid XML changes no data. A failed save undoes the whole import, including new catalogue items. No preview or confirmation (PR #20 decision, 2026-10-21). | final | RF-28, RF-29; Q-10, Q-17, Q-38 |
| UC-47 | Show import report | Via UC-15 | The system shows the result of the XML import. Included in UC-15. | Automatic. | final | RF-28; Q-10 |
| UC-19 | Create stock item | Manager | The manager creates a catalogue item with its name and unit. | Manager only. Invalid data is not saved. | final | RF-28, RF-29; Q-38, Q-43 |
| UC-48 | Edit stock item | Manager | The manager changes the name or unit of a catalogue item. | Manager only. Removal is not defined (Q-38). | final | RF-28, RF-29; Q-38 |
| UC-50 | Record stock entry | Receptionist, manager | Staff load the supplier XML stock file. The system records an entry for each item with quantity and date. The balance goes up. | Hotel staff or manager. Includes UC-53. No manual entry (retro A15). | final | RF-28, RF-29; Q-17, Q-21 |
| UC-53 | Read supplier XML stock file | Via UC-50, UC-15 | The system reads the XML stock file of the supplier. The file gives the stock entry. Included in UC-50 and UC-15. | Automatic. Invalid XML changes no data. | final | RF-28, RF-29; Q-10, Q-17 |
| UC-16 | Record stock use | Receptionist, manager | Staff record a consumption in the hotel with quantity and date. The balance goes down. | Hotel staff or manager. Quantity above the stock is not defined. | final | RF-29; Q-21, Q-34 |
| UC-49 | Record stock exit | Receptionist, manager | Staff record a removal (discard, return, loss) with quantity and date. The balance goes down. | Hotel staff or manager. Quantity above the stock is not defined. | final | RF-29; Q-21, Q-34 |

## Actor permission summary

| Actor | Application access |
|---|---|
| Client | No login. Uses the guest card for the room and the reserved restaurant and pool (UC-09, UC-42). Asks reception for room services and gives feedback (secondary actor in UC-08, UC-14). |
| Receptionist | Logs in (UC-01, UC-21). Registers clients and searches them in a reservation (UC-02, UC-33). Makes, changes and cancels reservations (UC-03, UC-34, UC-36 to UC-38, UC-04). Checks guests in and out (UC-06, UC-07). Records room-service requests and feedback (UC-08, UC-14). Lists rooms (UC-30). Loads supplier XML and records stock movements (UC-15, UC-16, UC-49, UC-50). Only services of floors 0 and 1 (UC-45). Cannot delete reservations. |
| Manager | Logs in (UC-01, UC-21). Manages staff accounts and lists users (UC-13, UC-22 to UC-24). Manages floors, zones, rooms, room items and prices (UC-12, UC-25 to UC-32). Deletes reservations before check-in (UC-05). Reads feedback (UC-41). Uses, blocks and issues master cards and sees card reports (UC-10, UC-11, UC-18, UC-43, UC-46). Manages the stock catalogue and stock (UC-15, UC-16, UC-19, UC-48 to UC-50). |
| Restaurant staff | Logs in (UC-01, UC-21). Only the kitchen and the restaurant (UC-17). |
| Supplier | No login. Sends the XML delivery file for hotel staff or the manager to load (secondary actor in UC-15). |

## Source references

- `docs/requirements/actors.md`
- `docs/requirements/requirements.md`
- `docs/requirements/use-cases.md`
- `docs/requirements/questions.md`
- `.agent/drafts/story-*.md` (story actors and scope)

Created by: antonioprmartinho. Updated by: antonioprmartinho. AI use: OpenAI Codex, GPT-6, 2026-10-16; edited by OpenAI Codex, GPT-6, 2026-10-16; PR-20 import decision review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; H4Y-7 review (50 use cases by area, phase column, include and extend notes) by use-case-diagram-editor, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-7 retrospective fixes (UC-51, UC-52, UC-53) by use-case-diagram-editor, Claude Code (claude-opus-5-5), 2026-10-29; UC-50 XML-only entry and UC-15 include by use-case-diagram-editor, Claude Code (claude-opus-5-5), 2026-10-30. Reviewed by: pending.
