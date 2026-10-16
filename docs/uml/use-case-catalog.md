# Use-case catalogue

Each use case has at least one actor. The short descriptions and permissions follow the current project requirements and team decisions. Team decisions that are not confirmed by the teacher are identified by their Q number.

| ID | Use case | Actor(s) | Short description | Permission rules | Requirements and decisions |
|---|---|---|---|---|---|
| UC-01 | Log in | Receptionist, manager, restaurant staff | A staff user signs in with a user name and password. | Only these staff roles log in. Clients and suppliers do not log in. | RF-01, RF-02, RNF-02; Q-13, Q-17, Q-23 |
| UC-02 | Register client | Receptionist | The receptionist records a client's details before making a reservation. | Receptionist only. The requirements do not define the client data fields. | RF-10; Q-12, Q-29 |
| UC-03 | Make reservation | Receptionist | The receptionist selects a registered client, room type, nights, available room, extras, and eligible zones, then saves the reservation and price. | Receptionist only. Extras and linked zones can change until check-in. Dates and room cannot change; cancel and create a new reservation. A reservation can include sea or mountain view, not both. | RF-05, RF-06, RF-07, RF-09, RF-11, RF-18; Q-20, Q-25, Q-37, Q-40, Q-42 |
| UC-04 | Cancel reservation | Receptionist | The receptionist cancels a reservation and the system keeps it in history. | Receptionist only. Cancellation after check-in is refused. Deletion is a separate manager-only use case. | RF-12; Q-05, Q-40 |
| UC-05 | Delete reservation | Manager | The manager permanently deletes a reservation before check-in. | Manager only. The system refuses deletion after check-in and for other roles. | RF-22; Q-05 |
| UC-06 | Check in | Receptionist | The receptionist checks in a guest; the system issues one card for the reservation and links it to the room and reserved places. | Receptionist only. Card issue and linking are final-delivery steps. | RF-13, RF-16, RF-17, RF-18; Q-06, Q-09 |
| UC-07 | Check out | Receptionist | The receptionist checks out a guest and the system saves the check-out. | Receptionist only. At check-out, the guest card stops working; access logs remain. | RF-14; Q-30 |
| UC-08 | Help with room services | Client, receptionist | The client asks reception for food, drink, or laundry service; the receptionist records it and the system adds its cost when applicable. | Client may request service. Receptionist records it. Staff may enter guest rooms only with a manager master card. | RF-07, RF-09, RF-15; Q-08, Q-11, Q-25, Q-33 |
| UC-09 | Use magnetic card at a place | Client, manager | A client uses a guest card or a manager uses a master card to request access; the system records allowed passes. | A guest card opens its own room and eligible places in its reservation. The manager master card opens every room and zone. A blocked or inactive card opens nothing. Client cards do not open places excluded by Q-20. | RF-17, RF-18, RF-19, RF-23, RF-24; Q-09, Q-19, Q-20, Q-26, Q-32 |
| UC-10 | List places of one card | Manager | The manager views the places and entry times recorded for a card. | Manager only. | RF-19, RF-20 |
| UC-11 | List card passes of a room | Manager | The manager views card numbers and entry times recorded for a room, including master-card passes. | Manager only. | RF-19, RF-21, RF-23 |
| UC-12 | Configure room items | Manager | The manager adds, changes, or removes items assigned to a room. | Manager only. Invalid data is not saved. | RF-08 |
| UC-13 | Manage staff | Manager | The manager creates, edits, or removes staff accounts and can list users. | Manager only. This use case manages accounts, not schedules. Receptionists are limited to floors 0 and 1; restaurant staff are limited to the kitchen and restaurant. | RF-01, RF-24, RF-25, RF-26; Q-08, Q-18, Q-23, Q-39 |
| UC-14 | Receive suggestion or complaint | Client, receptionist, manager | The client gives feedback to reception, the receptionist records it, and the manager receives it. | Client gives feedback; receptionist records it; manager views or receives it. | RF-27; Q-18, Q-25 |
| UC-15 | Enter supplier delivery in stock | Supplier, receptionist, manager | The supplier sends a delivery XML file; hotel staff or the manager loads it and the system records valid stock entries. | Supplier does not log in or load the file. Receptionist or manager loads it. Invalid XML is rejected; unknown items are reported and not imported. | RF-28, RF-29; Q-10, Q-17, Q-38 |
| UC-16 | Record stock use and exit | Receptionist, manager | Staff record stock use or exit with its quantity and date; the system updates the stock balance. | Receptionist or manager only among the named project actors. Use and exit reduce the balance. The requirements do not define what happens when the quantity exceeds stock. | RF-29; Q-21, Q-34 |
| UC-17 | Restrict restaurant staff access | Restaurant staff | A restaurant employee signs in and enters the kitchen or restaurant. | Restaurant staff only access the kitchen and restaurant. Access to other places is refused. | RF-01, RF-25; Q-23 |
| UC-18 | Block lost master card and issue a new one | Manager | The manager blocks a lost master card, then issues a replacement with a new number. | Manager only. Block the old card before issuing the replacement; keep the block and card number in the log. | RF-23, RF-30; Q-19, Q-26, Q-31 |
| UC-19 | Manage stock catalogue | Manager | The manager creates or edits stock items in the catalogue. | Manager only. Catalogue item removal is not defined by the current team decision. | RF-28, RF-29; Q-38 |

## Actor permission summary

| Actor | Application access |
|---|---|
| Client | No login. Chooses an available room type and nights through the reservation process. Uses the room and eligible reserved places with a guest card. May request services and give feedback. |
| Receptionist | Logs in; registers clients; creates and cancels reservations; checks guests in and out; records room-service requests, feedback, and stock movements; loads supplier XML. Access is limited to floors 0 and 1. Cannot delete reservations or enter guest rooms without a manager master card. |
| Manager | Logs in; manages staff accounts, room items, and stock; receives feedback and views card reports; deletes reservations only before check-in; uses a master card for all rooms and zones. |
| Restaurant staff | Logs in. Access is limited to the kitchen and restaurant. Cannot delete reservations. |
| Supplier | No login. Sends delivery data as XML for hotel staff or the manager to load. |

## Source references

- `docs/requirements/actors.md`
- `docs/requirements/requirements.md`
- `docs/requirements/use-cases.md`
- `docs/requirements/questions.md`

AI use: OpenAI Codex, GPT-6, 2026-10-16. Reviewed by: pending.
