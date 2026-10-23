---
type: backlog
status: approved
---

# Backlog order - H4Y (Hotel4You)

Proposal for the PO. The PO decides. No Jira write. Source: 15 epics, 57 stories (all have points), `requirements.md`, `questions.md`, schema H4Y-10 of PR #4 (branch `chore/db-schema-update`).

Revision 2026-10-16: full dependency audit. The status goes back to `draft` because the order changed. The PO approves it again.

## Planning assumptions

- Sprint 1 (2026-10-12): requirements only. No story.
- Sprints 2 to 13: 12 sprints of 1 week. Sprint 2 starts 2026-10-19.
- 1st delivery 2026-12-01 23:55. Final delivery 2027-01-31 23:55. Dates are fixed.
- Weeks 2026-10-19 to 2027-01-31 = 15 weeks = 12 sprints + 3 free weeks. Free weeks: 2026-12-21, 2026-12-28, 2027-01-04 (Christmas and New Year, Q-04, team decision, user, 2026-10-16).
- Total story points: 180 (57 stories). Technical tasks have no points. They run inside each sprint.
- Velocity is a GUESS. No history. Proposed: 13 to 15 points in sprints 2 to 6, 12 to 18 later. Average 15. The team confirms it in sprint planning.
- Nominal dates: sprint 2 = week of 10-19, sprint 10 = week of 12-14, sprint 11 = week of 2027-01-11, sprint 13 = week of 2027-01-25 (ends 2027-01-31). The 3 free weeks are fixed (Q-04), so no sprint moves.
- 1st delivery scope ends in sprint 6 (week of 11-16, ends 11-22). The zip is made in sprint 7 (week of 11-23), before 12-01.
- No-blocking rule (user, 2026-10-16): one card must not block another. Every story builds and tests alone with its own test data (foundation T4), stubs from the skeleton (foundation T0) and reference data (foundation T5, auth T2). A hard dependency stays only when it cannot be removed. The reason is written below.

## Revision 2026-10-23: sprint 3 planning and forecast for sprints 4 and 5

The order of the items does not change. Only the sprint slice changes. The status goes back to `draft`. The PO (Lukas Sperandio, Team Leader of sprint 3) approves it again.

- Team decision (Team Leader, 2026-10-23): the team works with at least 13 points per sprint. Sprint 2 velocity was 13 (H4Y-20, 22, 24, 27).
- Sprint 3: 15 committed points (list-rooms pulled from sprint 4) plus 2 stretch points (cadastrar-item-stock, pulled from sprint 4). No test-only sub-tasks: H4Y-21 tests go inside H4Y-23 (Team Leader decision). Plan: `docs/scrum/sprint-03/planning.md`.
- Sprint 4 goes back to 13 with cancel-reservation (from sprint 5). Sprint 5 goes back to 13 with refuse-delete-reservation (from sprint 6). Sprint 6 goes down from 15 to 13. This also removes the old sprint 6 risk (configure-room-items to sprint 7).
- If a sprint 3 stretch item does not finish, it goes first in sprint 4 (sprint 4 then has 15 points).
- Sub-task drafts for sprints 4 and 5 are a forecast: `subtask-s04-*.md`, `subtask-s05-*.md`. The refinement confirms or changes them.

### Forecast

| Sprint | Team Leader | Points | Stories | Tasks |
|--------|-------------|--------|---------|-------|
| 3 | Lukas Sperandio | 15 + 2 stretch | login-restaurante (H4Y-21, tests inside H4Y-23), funcoes-por-papel (H4Y-23), model-hotel-floors (H4Y-28), register-rooms (H4Y-25), set-base-price (H4Y-26), list-rooms; stretch: cadastrar-item-stock | none (SQL scripts inside the story sub-tasks) |
| 4 | Pedro Costa | 13 | registar-cliente (3), procurar-cliente (2), query-free-rooms (5), cancel-reservation (3) | none |
| 5 | Antonio Martinho | 13 | create-reservation (5), delete-reservation (3), refuse-delete-reservation (2), registar-cliente-rapido (3) | none |

### Refinement agenda (before the sprint 4 planning)

1. Diagram gaps. `docs/uml/class-diagram.md` has only `UserRepository`. Agree the names of the repositories for floors, rooms, room types, clients, reservations and stock items, and the missing methods (list floors, list rooms, find a reservation). AGENTS.md rule 11.
2. Client data and the duplicate rule (registar-cliente). The diagram has `name`, `document`, `birthday`. Confirm `document` as the unique key and as the search field (procurar-cliente).
3. How to measure the 30 s of RNF-03 (registar-cliente-rapido): stopwatch, how many runs, who records.
4. Publish the sprint 4 and 5 stories in Jira first. The tests need the Jira key for `@Tag("H4Y-N-AC-n")`.
5. Cards deleted from Jira on 2026-10-23: H4Y-33, H4Y-35, H4Y-36, H4Y-37, H4Y-38, H4Y-48. No task cards from now on (Team Leader decision): the SQL scripts go inside the story sub-tasks, and the scrum records stay as files. Decide if RNF-09 and RNF-10 (JUnit, Javadoc, old H4Y-35) need a card, and if the scrum records (RNF-15, RNF-16) need cards or only files in `docs/scrum/`.
6. Teacher questions still open as team decisions: Q-01 (20 rooms, 7/10/3), Q-05 (cancel vs delete), Q-11 (manager sets the base price).
7. Jira text is in Portuguese (Portugal). The title "Cadastrar item de stock" uses a Brazilian word. Propose "Registar item de stock".
8. Check the story size again after sprint 3: if velocity is above 13, pull block-delete-after-check-in (2) into sprint 5.

## Dependency audit result

- Before: 51 story-to-story links in the old Dependencies list, all treated as blocking. Plus 11 hidden blockers (missing prerequisites or items placed before their prerequisite):
  1. No item created the 5 roles (RF-02) or a first manager account. The login stories in sprint 2 could not run.
  2. `create-staff-account` was in sprint 7. No one could create receptionist and restaurant accounts for the sprint 2 and 3 logins.
  3. `janela-principal` (sprint 2) was placed before `login-rececionista` and depended on `funcoes-por-papel` (sprint 3).
  4. `registar-cliente-rapido` (sprint 4) needs `create-reservation` (sprint 5) for its 30 s test.
  5. `limit-reception-floors` (sprint 8) cited the master card (sprint 10).
  6. `configure-room-items` (sprint 6) needs the stock item catalogue (`room_items` -> `items` in H4Y-10). Only `cadastrar-item-stock` (sprint 11) created items.
  7. No item gives a manager the first master card. `abrir-tudo-cartao-mestre` and `bloquear-cartao-mestre` need an active one.
  8. No item created reference data: floors and the places of RF-03 (`model-hotel-floors` only shows them), room types (`register-rooms`, `set-base-price`), the list of extras and paid services (`definir-precos-extra` AC-3, room service types Q-33).
  9. No item owned the database schema version. `main` has the old H4Y-10. PR #4 changes it, and the stories follow PR #4 (no `users.active`, view as a service, `reservation_locations`, `stock_imports`).
  10. Six card stories needed one shared access core, a card number generator and a simulated card reader screen. No item owned them.
  11. No test data. Every story needed earlier stories to make its data.
- After: 11 hard story-to-story dependencies (list below). Every other link is removed with test data, a stub or reference data. Every hard dependency points to an earlier sprint, or to an earlier item in the same sprint.

## Sprint slice

| Sprint | Week (nominal) | Points | Stories | Increment (what works at the end) |
|--------|----------------|--------|---------|-----------------------------------|
| 1 | 10-12 | 0 | none | Requirements documents. |
| 2 | 10-19 | 13 | janela-principal, login-rececionista, login-gestor, create-staff-account | Skeleton, database and seed ready. The first manager logs in and creates staff accounts. Receptionists log in and see the main window. |
| 3 | 10-26 | 15 + 2 stretch | login-restaurante, funcoes-por-papel, model-hotel-floors, register-rooms, set-base-price, list-rooms; stretch: cadastrar-item-stock | Restaurant staff log in. Each role sees only its functions. The manager sees floors, registers the 20 rooms and sets base prices. Reception sees the room list. Stretch: item catalogue. |
| 4 | 11-02 | 13 | registar-cliente, procurar-cliente, query-free-rooms, cancel-reservation | The receptionist registers and finds clients, searches free rooms and cancels reservations. |
| 5 | 11-09 | 13 | create-reservation, delete-reservation, refuse-delete-reservation, registar-cliente-rapido | Full reservation life cycle. Only the manager deletes. RNF-03 (30 s, registration + reservation) measured. |
| 6 | 11-16 | 13 | check-in, check-out, block-delete-after-check-in, configure-room-items | Check-in and check-out. Delete rules complete. Room items configured. 1st delivery scope complete. |
| 7 | 11-23 | 12 | consultar-utilizadores, edit-staff-account, remove-staff-account, criar-zona | 1st delivery freeze and zip. Manager edits and removes accounts, lists users, creates zones. |
| 8 | 11-30 | 14 | editar-zona, remover-zona, reservation-restaurant-pool, limit-reception-floors, limit-restaurant-staff-zones | 1st delivery was due 12-01 (sent at the end of sprint 7). Zones complete. Restaurant and pool in the reservation. Role limits. |
| 9 | 12-07 | 17 | emitir-cartao-check-in, registar-passagens-cartao, abrir-quarto-com-cartao, abrir-restaurante-piscina-reservados, recusar-cartao-locais-nao-permitidos | The client card is issued, opens the room and the reserved places, and every pass is logged. |
| 10 | 12-14 | 18 | desativar-cartao-check-out, abrir-tudo-cartao-mestre, bloquear-cartao-mestre, emitir-novo-cartao-mestre, consultar-locais-cartao, consultar-passagens-quarto | Card off at check-out, master card, block and new card, card and room logs. |
| 11 | 2027-01-11 | 16 | editar-item-stock, carregar-xml-fornecedor-gestor, registar-entrada-stock, ver-relatorio-importacao | The manager loads the supplier XML with an import report. Stock entry. |
| 12 | 2027-01-18 | 17 | registar-uso-stock, registar-saida-stock, carregar-xml-fornecedor-rececionista, definir-precos-extra, adicionar-extras-quarto-reserva | Stock use and exit. Reception loads XML. Extra prices. Extras on the reservation. |
| 13 | 2027-01-25 | 17 | adicionar-servicos-extra-reserva, calcular-preco-total-reserva, register-room-service-request, record-suggestion-complaint, list-suggestions-complaints | Paid services, total price, room service, suggestions and complaints. Final zip. |

Check: 13+17+13+13+13+12+14+17+18+16+17+17 = 180. 1st delivery scope (sprints 2 to 6) = 69 points.

Changes from the approved order: `create-staff-account` 7 -> 2; `login-restaurante` 2 -> 3; `list-rooms` 3 -> 4; `cadastrar-item-stock` 11 -> 4 (accepted, Q-43); `registar-cliente-rapido` 4 -> 5; `refuse-delete-reservation` 5 -> 6. Inside sprint 2, `janela-principal` no longer waits for login (stub session).

## Parallel work per sprint

Stories in one lane group have no link between them and run in parallel. "Then" marks a hard dependency inside the sprint.

| Sprint | Parallel (no link) | Serial inside the sprint |
|--------|--------------------|--------------------------|
| 2 | janela-principal, login-rececionista, create-staff-account | login-rececionista then login-gestor |
| 3 | login-restaurante, funcoes-por-papel, model-hotel-floors, register-rooms, set-base-price, list-rooms, cadastrar-item-stock | none |
| 4 | registar-cliente, procurar-cliente, query-free-rooms, cancel-reservation | none |
| 5 | create-reservation, delete-reservation | create-reservation then registar-cliente-rapido; delete-reservation then refuse-delete-reservation |
| 6 | check-in, check-out, configure-room-items, block-delete-after-check-in | none |
| 7 | consultar-utilizadores, edit-staff-account, remove-staff-account, criar-zona | none |
| 8 | editar-zona, remover-zona, reservation-restaurant-pool, limit-reception-floors, limit-restaurant-staff-zones | none |
| 9 | emitir-cartao-check-in, registar-passagens-cartao, abrir-quarto-com-cartao, abrir-restaurante-piscina-reservados, recusar-cartao-locais-nao-permitidos | none (each access rule is its own class, T0) |
| 10 | all 6 stories | none |
| 11 | editar-item-stock, carregar-xml-fornecedor-gestor, registar-entrada-stock, ver-relatorio-importacao | none |
| 12 | registar-uso-stock, registar-saida-stock, carregar-xml-fornecedor-rececionista, definir-precos-extra, adicionar-extras-quarto-reserva | none |
| 13 | all 5 stories | none |

## Technical tasks (no points) by sprint

| Epic | Tasks | Sprint |
|------|-------|--------|
| foundation-architecture | T0 skeleton and core classes: first task of sprint 2, before every story and before the seed. Then T2 SQL Server with the PR #4 schema, T4 test data mechanism, T1 layer check. T5 reference data: start of sprint 3. T3 usability test: sprint 6, repeat in sprint 10. T6 card reader simulator, card number generator, master card for the first manager: sprint 8. | 2, 3, 6, 8, 10 |
| authentication-users | T1 encrypted passwords, then T2 seed (5 roles + first manager from config): sprint 2, after foundation T0 and T2, before the login stories. | 2 |
| engineering-quality | Setup JUnit, Javadoc, `docs/libraries.md`, branch and commit rule, AI-use reference (RNF-09, 10, 11, 20, 21, 25): sprint 2. Code-of-conduct check (RNF-26): each sprint review. Final audit: sprints 12 to 13. | 2 to 13 |
| scrum-process | Planning, daily notes, review, retrospective, refinement, leader rotation, validation, diagram update (RNF-13 to 19, 27): every sprint 2 to 13. | 2 to 13 |
| delivery-packaging | Domain model and use case diagram: sprint 3 (start from the T0 class diagram). Class diagram: sprints 5 and 6. Sequence diagrams: sprint 6. Executables and Javadoc output: sprint 6. 1st zip: sprint 7. Final diagrams: sprints 10 to 12. Final zip dry run: sprint 12. Final zip: sprint 13. | 3 to 13 |

Sprint 2 task order: foundation T0 (days 1 to 2) -> foundation T2 (database, PR #4 schema) and auth T1 (hash) in parallel -> auth T2 (seed) -> login stories. `janela-principal` and `create-staff-account` start after T0 with stubs. Foundation T4 grows with each story.

## Ordered backlog

Value: H/M/L. Risk: H/M/L. Dependency: "none (hard)" means it builds and tests alone; the data or stub source is in the story note.

| # | Item | Type | Requirements | Points suggested | Reason (value / risk / dependency / date) | Target sprint |
|---|------|------|--------------|------------------|-------------------------------------------|---------------|
| 1 | Foundation T0 skeleton and core classes | Task | RNF-05, 07, 08, 12 | - | Value H: domain, service and repository interfaces let 5 developers work in parallel. Risk H: decides all code. Dependency: none, first. Date: day 1 of sprint 2. | 2 |
| 2 | Foundation T2 SQL Server + PR #4 schema scripts | Task | RNF-12 | - | Value H. Risk H: PR #4 is merged (merge commit 678291b). Dependency: item 1. Date: sprint 2. | 2 |
| 3 | Auth T1 encrypted passwords | Task | RNF-02 | - | Value H (security). Risk M. Dependency: item 1. Date: sprint 2, before the seed. | 2 |
| 4 | Auth T2 seed: 5 roles + first manager | Task | RF-02, RF-01, RNF-02 | - | Value H: removes hidden blocker 1. Risk M: secret must stay out of Git. Dependency: items 2, 3. Date: before the logins. | 2 |
| 5 | Foundation T4 test data mechanism, T1 layer check | Task | RNF-07, 08, 10 | - | Value H: removes most links. Risk L. Dependency: item 1. Date: sprint 2, then each story adds its data. | 2 |
| 6 | Engineering-quality setup | Task | RNF-09, 10, 11, 20, 21, 25 | - | Value H: graded. Risk L. Dependency: none. Date: sprint 2. | 2 |
| 7 | janela-principal | Story | RNF-06, 07 | 3 | Value H: GUI base. Risk M: first JavaFX. Dependency: none hard (stub session and permission policy from T0). Date: 1st delivery. | 2 |
| 8 | login-rececionista | Story | RF-01, 02 | 5 | Value H: gate of the app. Risk M: first full path. Dependency: none hard (seed, test account). Date: 1st delivery. | 2 |
| 9 | login-gestor | Story | RF-01, 02 | 2 | Value H. Risk L. Dependency: hard on item 8 (same login code; 2 points assume reuse). Date: 1st delivery. | 2 |
| 10 | create-staff-account | Story | RF-26, 01 | 3 | Value H: the first manager creates the other accounts (removes hidden blocker 2). Risk L. Dependency: none hard (stub manager session). Date: moved from sprint 7. | 2 |
| 11 | Foundation T5 reference data | Task | RF-03, 04, 07, 09 | - | Value H: floors, places, room types, extras list (removes hidden blocker 8). Risk L. Dependency: item 2. Date: start of sprint 3. | 3 |
| 12 | login-restaurante | Story | RF-01, 02 | 2 | Value M: needed for RF-25. Risk L. Dependency: hard on item 8 (same login code). Date: login is 1st delivery (Q-06); moved to sprint 3 to lower sprint 2 load. | 3 |
| 13 | funcoes-por-papel | Story | RF-02 | 3 | Value H: role access base. Risk M. Dependency: none hard (implements the T0 permission policy). Date: 1st delivery. | 3 |
| 14 | model-hotel-floors | Story | RF-03 | 2 | Value H. Risk L. Dependency: none hard (T5 data). Date: 1st delivery. | 3 |
| 15 | register-rooms | Story | RF-04 | 3 | Value H: no reservation without rooms. Risk L. Dependency: none hard (floors and types from T5). Date: 1st delivery. | 3 |
| 16 | set-base-price | Story | RF-06 | 3 | Value H. Risk L. Dependency: none hard (types from T5). Date: 1st delivery. | 3 |
| 17 | Delivery T: domain model + use case diagram | Task | RNF-24 | - | Value H: graded. Risk L. Dependency: item 1 class diagram. Date: early. | 3 |
| 18 | registar-cliente | Story | RF-10 | 3 | Value H: reservation needs the client (Q-27). Risk L. Dependency: none hard. Date: 1st delivery. | 4 |
| 19 | procurar-cliente | Story | RF-10 | 2 | Value M. Risk L. Dependency: none hard (test clients). Date: 1st delivery. | 4 |
| 20 | query-free-rooms | Story | RF-05 | 5 | Value H: core of booking; owns the overlap rule. Risk M: date edges. Dependency: none hard (test rooms and reservations). Date: 1st delivery. | 4 |
| 21 | list-rooms | Story | RF-04, 06 | 2 | Value M. Risk L. Dependency: none hard (test rooms and prices). Date: 1st delivery; moved from sprint 3 for balance; back to sprint 3, committed (2026-10-23). | 3 |
| 22 | cadastrar-item-stock | Story | RF-28, 29 | 2 | Value H: room items need the catalogue (removes hidden blocker 6). Risk L. Dependency: none hard. Date: moved from sprint 11 into the 1st delivery (accepted, Q-43); sprint 3 stretch (2026-10-23). | 3 |
| 23 | create-reservation | Story | RF-11, 05 | 5 | Value H: central flow. Risk H: client, room, dates, price. Dependency: hard on item 20 (one overlap rule). Date: 1st delivery. | 5 |
| 24 | cancel-reservation | Story | RF-12 | 3 | Value H. Risk L. Dependency: none hard (test reservations). Date: 1st delivery; moved to sprint 4 (2026-10-23). | 4 |
| 25 | delete-reservation | Story | RF-22 | 3 | Value H. Risk M: owns the delete action. Dependency: none hard (test reservations). Date: 1st delivery. | 5 |
| 26 | registar-cliente-rapido | Story | RNF-03, RF-10 | 3 | Value H: 30 s limit. Risk M: time target. Dependency: hard on items 18 and 23 (timed real flow, no stub; removes hidden blocker 4). Date: 1st delivery; last in sprint 5. | 5 |
| 27 | check-in | Story | RF-13 | 3 | Value H. Risk M. Dependency: none hard (test reservations). Date: 1st delivery. | 6 |
| 28 | check-out | Story | RF-14 | 3 | Value H. Risk L. Dependency: none hard (test reservations with check-in). Date: 1st delivery. | 6 |
| 29 | block-delete-after-check-in | Story | RF-13, 22 | 2 | Value H. Risk L. Dependency: hard on item 25 (guard on the same delete action). Date: 1st delivery. | 6 |
| 30 | refuse-delete-reservation | Story | RF-22 | 2 | Value H. Risk L. Dependency: hard on item 25 (role guard on the same delete action). Date: 1st delivery; moved from sprint 5 for balance; back to sprint 5 (2026-10-23). | 5 |
| 31 | configure-room-items | Story | RF-08 | 5 | Value M (should), in the 1st delivery (Q-06). Risk L. Dependency: none hard (test rooms and items). Date: first to move to sprint 7 if sprint 6 is full. | 6 |
| 32 | Delivery T: class, sequence, executables, Javadoc | Task | RNF-24 | - | Value H. Risk M. Dependency: code of sprints 2 to 6. Date: sprints 5 and 6. | 5 to 6 |
| 33 | Foundation T3 usability test | Task | RNF-04 | - | Value M. Risk L. Dependency: reception screens. Date: 1st delivery. | 6 |
| 34 | Delivery T: 1st zip and validation | Task | RNF-22, 23, 24 | - | Value H: 35% of grade. Risk H: deadline. Dependency: items 1 to 33. Date: 12-01 23:55. | 7 |
| 35 | consultar-utilizadores | Story | RNF-01 | 3 | Value M. Risk L. Dependency: none hard (test users). Date: final delivery. | 7 |
| 36 | edit-staff-account | Story | RF-26 | 3 | Value H. Risk L. Dependency: none hard (test accounts). Date: final delivery. | 7 |
| 37 | remove-staff-account | Story | RF-26 | 3 | Value H. Risk M: Q-36 refusal. Dependency: none hard (test accounts with links). Date: final delivery. | 7 |
| 38 | criar-zona | Story | RF-03 | 3 | Value H. Risk M. Dependency: none hard (floors from T5). Date: final delivery. | 7 |
| 39 | editar-zona | Story | RF-03 | 3 | Value H. Risk L. Dependency: none hard (test zones). Date: final delivery. | 8 |
| 40 | remover-zona | Story | RF-03 | 3 | Value M. Risk M: Q-36. Dependency: none hard (test zones). Date: final delivery. | 8 |
| 41 | reservation-restaurant-pool | Story | RF-11, 18 | 2 | Value H: the card rule needs it. Risk M. Dependency: hard on item 23 (same create form); places from T5. Date: sprint 8 (Q-42). | 8 |
| 42 | limit-reception-floors | Story | RF-24 | 3 | Value H (must). Risk M. Dependency: none hard (T5 places; AC-3 tests refusal only; removes hidden blocker 5). Date: final delivery. | 8 |
| 43 | limit-restaurant-staff-zones | Story | RF-25, 01 | 3 | Value H (must). Risk M. Dependency: none hard (test account and zones). Date: final delivery. | 8 |
| 44 | Foundation T6 card reader simulator, card numbers, first master card | Task | RF-16, 17, 23 | - | Value H: shared core for 11 card stories (removes hidden blockers 7 and 10). Risk M. Dependency: item 1. Date: before sprint 9. | 8 |
| 45 | emitir-cartao-check-in | Story | RF-13, 16 | 3 | Value H. Risk H: number rule. Dependency: hard on item 27 (inside the check-in flow); generator from item 44. Date: final delivery. | 9 |
| 46 | registar-passagens-cartao | Story | RF-19 | 3 | Value H: base of all logs. Risk H. Dependency: none hard (implements the T0 logger). Date: final delivery. | 9 |
| 47 | abrir-quarto-com-cartao | Story | RF-17 | 3 | Value H. Risk M. Dependency: none hard (test cards). Date: final delivery. | 9 |
| 48 | abrir-restaurante-piscina-reservados | Story | RF-18, 03 | 5 | Value H. Risk H: reservation + zone rule. Dependency: none hard (test reservations with places). Date: final delivery. | 9 |
| 49 | recusar-cartao-locais-nao-permitidos | Story | RF-18, 03 | 3 | Value H. Risk M: zone flags (answered, Q-45). Dependency: none hard. Date: final delivery. | 9 |
| 50 | desativar-cartao-check-out | Story | RF-14, 16 | 3 | Value H (Q-30). Risk M. Dependency: hard on item 28 (inside the check-out flow). Date: final delivery. | 10 |
| 51 | abrir-tudo-cartao-mestre | Story | RF-23, 19 | 3 | Value H. Risk M. Dependency: none hard (test master cards). Date: final delivery. | 10 |
| 52 | bloquear-cartao-mestre | Story | RF-30 | 3 | Value H. Risk M: block is immediate (Q-26). Dependency: none hard. Date: final delivery. | 10 |
| 53 | emitir-novo-cartao-mestre | Story | RF-30, 23 | 3 | Value H. Risk M. Dependency: none hard (blocked test card). Date: final delivery. | 10 |
| 54 | consultar-locais-cartao | Story | RF-20, 19 | 3 | Value H. Risk L. Dependency: none hard (test passes). Date: final delivery. | 10 |
| 55 | consultar-passagens-quarto | Story | RF-21, 19 | 3 | Value H. Risk L. Dependency: none hard (test passes). Date: final delivery. | 10 |
| 56 | editar-item-stock | Story | RF-28, 29 | 2 | Value M. Risk L. Dependency: none hard (test items). Date: final delivery. | 11 |
| 57 | carregar-xml-fornecedor-gestor | Story | RF-28 | 8 | Value H. Risk H: biggest story, team XML format. Dependency: none hard (test items and files). Date: early in the stock block. | 11 |
| 58 | registar-entrada-stock | Story | RF-29 | 3 | Value H. Risk L. Dependency: none hard. Date: final delivery. | 11 |
| 59 | ver-relatorio-importacao | Story | RF-28 | 3 | Value H. Risk M. Dependency: none hard (test imports). Date: final delivery. | 11 |
| 60 | registar-uso-stock | Story | RF-29 | 3 | Value H. Risk L. Dependency: none hard. Date: final delivery. | 12 |
| 61 | registar-saida-stock | Story | RF-29 | 3 | Value H. Risk L. Dependency: none hard. Date: final delivery. | 12 |
| 62 | carregar-xml-fornecedor-rececionista | Story | RF-28 | 3 | Value M. Risk L. Dependency: hard on item 57 (reuses the XML reader; 3 points assume reuse). Date: final delivery. | 12 |
| 63 | definir-precos-extra | Story | RF-07, 09 | 3 | Value H (RF-07 must). Risk L. Dependency: none hard (extras list from T5). Date: Q-25. | 12 |
| 64 | adicionar-extras-quarto-reserva | Story | RF-07 | 5 | Value H. Risk M: one view (Q-37), until check-in (Q-40). Dependency: none hard (test reservations and prices). Date: final delivery. | 12 |
| 65 | adicionar-servicos-extra-reserva | Story | RF-09 | 5 | Value M (should). Risk M. Dependency: none hard. Date: final delivery. | 13 |
| 66 | calcular-preco-total-reserva | Story | RF-06, 07, 09 | 5 | Value H. Risk H: sums. Dependency: none hard (test reservations with extras). Date: final delivery. | 13 |
| 67 | register-room-service-request | Story | RF-15 | 3 | Value M (should). Risk L. Dependency: none hard. Date: cut-first candidate. | 13 |
| 68 | record-suggestion-complaint | Story | RF-27 | 2 | Value L (should). Risk L. Dependency: none hard. Date: cut-first candidate. | 13 |
| 69 | list-suggestions-complaints | Story | RF-27 | 2 | Value L (should). Risk L. Dependency: none hard (test records). Date: cut-first candidate. | 13 |
| 70 | Delivery T: final diagrams, Javadoc, zip, submit | Task | RNF-22, 23, 24 | - | Value H: 65% of grade. Risk H: deadline. Dependency: all items. Date: 2027-01-31. | 12 to 13 |
| 71 | Scrum-process and engineering-quality tasks | Task | RNF-13 to 19, 26, 27 | - | Value H. Risk L. Dependency: none. Date: each sprint. | 2 to 13 |

Sprint 1 holds no code story.

## Requirement coverage

Every RF and RNF of `requirements.md`. No `MISSING`.

| Requirement ID | Epic | Stories |
|----------------|------|---------|
| RF-01 | authentication-users, staff-management-access | login-rececionista, login-gestor, login-restaurante, create-staff-account, limit-restaurant-staff-zones; task auth T2 |
| RF-02 | authentication-users | login-rececionista, login-gestor, login-restaurante, funcoes-por-papel; task auth T2 (5 roles) |
| RF-03 | rooms-pricing, magnetic-card-access | model-hotel-floors, criar-zona, editar-zona, remover-zona, abrir-restaurante-piscina-reservados, recusar-cartao-locais-nao-permitidos; task foundation T5 |
| RF-04 | rooms-pricing | register-rooms, list-rooms |
| RF-05 | reservations | query-free-rooms, create-reservation |
| RF-06 | rooms-pricing, extra-services-pricing | set-base-price, list-rooms, calcular-preco-total-reserva |
| RF-07 | extra-services-pricing | definir-precos-extra, adicionar-extras-quarto-reserva, calcular-preco-total-reserva |
| RF-08 | rooms-pricing | configure-room-items |
| RF-09 | extra-services-pricing | definir-precos-extra, adicionar-servicos-extra-reserva, calcular-preco-total-reserva |
| RF-10 | client-registration | registar-cliente, procurar-cliente, registar-cliente-rapido |
| RF-11 | reservations | create-reservation, reservation-restaurant-pool |
| RF-12 | reservations | cancel-reservation |
| RF-13 | check-in-out, magnetic-card-access | check-in, block-delete-after-check-in, emitir-cartao-check-in |
| RF-14 | check-in-out | check-out, desativar-cartao-check-out |
| RF-15 | guest-support | register-room-service-request |
| RF-16 | magnetic-card-access | emitir-cartao-check-in, desativar-cartao-check-out; task foundation T6 |
| RF-17 | magnetic-card-access | abrir-quarto-com-cartao |
| RF-18 | magnetic-card-access | abrir-restaurante-piscina-reservados, recusar-cartao-locais-nao-permitidos, reservation-restaurant-pool |
| RF-19 | magnetic-card-access, master-card-audit | registar-passagens-cartao, abrir-tudo-cartao-mestre, consultar-locais-cartao, consultar-passagens-quarto |
| RF-20 | master-card-audit | consultar-locais-cartao |
| RF-21 | master-card-audit | consultar-passagens-quarto |
| RF-22 | reservations, check-in-out | delete-reservation, refuse-delete-reservation, block-delete-after-check-in |
| RF-23 | master-card-audit | abrir-tudo-cartao-mestre, emitir-novo-cartao-mestre |
| RF-24 | staff-management-access | limit-reception-floors |
| RF-25 | staff-management-access | limit-restaurant-staff-zones |
| RF-26 | staff-management-access | create-staff-account, edit-staff-account, remove-staff-account |
| RF-27 | guest-support | record-suggestion-complaint, list-suggestions-complaints |
| RF-28 | stock-management | cadastrar-item-stock, editar-item-stock, carregar-xml-fornecedor-gestor, carregar-xml-fornecedor-rececionista, ver-relatorio-importacao |
| RF-29 | stock-management | cadastrar-item-stock, editar-item-stock, registar-entrada-stock, registar-uso-stock, registar-saida-stock |
| RF-30 | master-card-audit | bloquear-cartao-mestre, emitir-novo-cartao-mestre |
| RNF-01 | authentication-users | consultar-utilizadores |
| RNF-02 | authentication-users | task T1 (Q-35) |
| RNF-03 | client-registration, reservations | registar-cliente-rapido |
| RNF-04 | foundation-architecture | task T3 (Q-41) |
| RNF-05 | foundation-architecture | task T0, T1 |
| RNF-06 | foundation-architecture | janela-principal |
| RNF-07 | foundation-architecture | janela-principal, task T0, T1 |
| RNF-08 | foundation-architecture | task T0, T1 |
| RNF-09 | engineering-quality | task (no story) |
| RNF-10 | engineering-quality | task (no story) |
| RNF-11 | engineering-quality | task (no story) |
| RNF-12 | foundation-architecture | task T0, T2 |
| RNF-13 | scrum-process | task (no story) |
| RNF-14 | scrum-process | task (no story) |
| RNF-15 | scrum-process | task (no story) |
| RNF-16 | scrum-process | task (no story) |
| RNF-17 | scrum-process | task (no story) |
| RNF-18 | scrum-process | task (no story) |
| RNF-19 | scrum-process | task (no story) |
| RNF-20 | engineering-quality | task (no story) |
| RNF-21 | engineering-quality | task (no story) |
| RNF-22 | delivery-packaging | task (no story) |
| RNF-23 | delivery-packaging | task (no story) |
| RNF-24 | delivery-packaging | task (no story) |
| RNF-25 | engineering-quality | task (no story) |
| RNF-26 | engineering-quality | task (no story) |
| RNF-27 | scrum-process | task (no story) |

## Dependencies (A -> B)

### Hard story dependencies (11, cannot be removed)

| # | A -> B | Sprints | Why it stays |
|---|--------|---------|--------------|
| 1 | login-rececionista -> login-gestor | 2 -> 2 | Same login code. The 2 points assume reuse. Building both at once doubles the login screen. |
| 2 | login-rececionista -> login-restaurante | 2 -> 3 | Same reason. |
| 3 | registar-cliente -> registar-cliente-rapido | 4 -> 5 | AC-1 times the real registration screen. A stub cannot measure 30 s. |
| 4 | create-reservation -> registar-cliente-rapido | 5 -> 5 | AC-1 ends at the real reservation created. |
| 5 | query-free-rooms -> create-reservation | 4 -> 5 | One overlap rule (`RoomAvailability`). Two versions would differ. |
| 6 | delete-reservation -> refuse-delete-reservation | 5 -> 5 | Adds a role guard to the same delete action. |
| 7 | delete-reservation -> block-delete-after-check-in | 5 -> 6 | Adds a state guard to the same delete action. |
| 8 | create-reservation -> reservation-restaurant-pool | 5 -> 8 | Adds the place choice to the same create form. |
| 9 | check-in -> emitir-cartao-check-in | 6 -> 9 | The card is issued inside the check-in flow. |
| 10 | check-out -> desativar-cartao-check-out | 6 -> 10 | The card is turned off inside the check-out flow. |
| 11 | carregar-xml-fornecedor-gestor -> carregar-xml-fornecedor-rececionista | 11 -> 12 | Reuses the XML reader. The 3 points assume reuse; alone it is 8. |

### Task prerequisites (all at the start of their sprint)

- foundation T0 -> every story and every other task. foundation T2 and auth T1 -> auth T2 seed -> login stories (sprint 2).
- foundation T5 -> stories from sprint 3 (floors, places, room types, extras list). In sprint 2 no story needs T5.
- foundation T6 -> card stories (sprints 9 and 10).
- foundation T4 -> every story test. Each story adds its own test data file.

### Removed links (now soft: test data, stub or reference data)

All other links of the old list. Examples: register-rooms -> room stories (test rooms); create-reservation -> cancel, delete, check-in (test reservations); criar-zona -> zone and card stories (T5 places, test zones); emitir-cartao -> access stories (test cards); registar-passagens -> log queries (test passes); bloquear -> emitir-novo (blocked test card); cadastrar-item -> stock stories (test items); definir-precos-extra -> extras -> total (test prices and extras); record -> list suggestions (test records); janela-principal -> login and funcoes-por-papel (stub session and policy). Some acceptance checks still use a screen of an earlier sprint (free-room query in cancel, delete, check-out). They do not block, because the earlier sprint is done first.

## Risks

- Sprint 2 holds 6 tasks and 13 points. If T0 or the PR #4 merge slips, move create-staff-account to the start of sprint 3; the first manager can still log in.
- Velocity is a guess. Sprints 2 to 6 need 69 points (13.8 per sprint). If sprint 6 is full, configure-room-items (5) moves to sprint 7, before the zip.
- Final scope needs 111 points in 7 sprints (15.9 per sprint). Cut-first order (should or duplicate path): list-suggestions-complaints, record-suggestion-complaint, register-room-service-request, adicionar-servicos-extra-reserva, carregar-xml-fornecedor-rececionista. Sum 15. The PO decides.
- PR #4 is merged (merge commit 678291b). Check the merged schema data before T2 and T5: the roles data must give 5 roles (RF-02, Q-46); floors and place floors must match RF-03; room_types must match RF-04 (20 rooms). The zone flag rule is answered by Q-45 (`guest_card_access`, `requires_reservation`).
- Test data must never run in production. The first manager password must never be in Git or docs.
- The 1st delivery is 35% of the grade. Sprint 7 holds the zip; no sprint 7 story may touch the frozen delivery branch before the zip is tagged.
- Card chain (sprints 9 and 10, 35 points) has high risk. T6 in sprint 8 makes all 11 card stories parallel.
- Sprint 13 (week of 2027-01-25) ends on 2027-01-31, the final delivery day. No slack. The final zip (LP3-GY.zip) must be ready before the last day.

## Not-ready items

None by the Definition of Ready. Notes for the PO:

- 7 stories are published in Jira (H4Y-20 to H4Y-26). Their drafts are not edited. Needed Jira changes:
  - login-rececionista (H4Y-22): note "Contas criadas aqui por dados iniciais" -> the first manager comes from auth T2; receptionist accounts come from create-staff-account (sprint 2) or test data. Depends on foundation T0, T2, auth T1, T2.
  - login-gestor (H4Y-20): "existe um gestor" = the seed manager (auth T2). Hard dependency on login-rececionista, same sprint.
  - login-restaurante (H4Y-21): target sprint 3. The restaurant account comes from create-staff-account or test data. Hard dependency on login-rececionista.
  - funcoes-por-papel (H4Y-23): sprint 3. Implements the T0 permission policy that janela-principal uses as a stub.
  - janela-principal (H4Y-24): remove "Depende de login-rececionista e funcoes-por-papel". Build with the T0 stub session and policy. Re-check AC-3 after funcoes-por-papel.
  - register-rooms (H4Y-25): depends on floors and room types from foundation T5, not on story-model-hotel-floors. Rooms are not seeded. Tests use test data.
  - set-base-price (H4Y-26): depends on room types from T5 (base price empty), not on story-register-rooms.
- cadastrar-item-stock: sprint 4 accepted (Q-43). The label must change from `final-delivery` to `1st-delivery`. No Jira write in this draft.
- `epic-check-in-out.md` Open questions text is stale (Q-30 answers it).

## Open questions

- Q-15 (open): AI reference format.
- Answered, removed from the open list:
  - Free weeks: Q-04 (team decision, user, 2026-10-16). The 3 free weeks are Christmas and New Year (2026-12-21, 2026-12-28, 2027-01-04).
  - Developer as story actor: Q-28, closed by Q-35. Technical work is a task, not a story.
  - Master card for a manager created later: Q-44. The story "emitir novo cartão mestre" issues it. The seed gives the first card to the initial manager (Q-46).
  - Zone flags: Q-45. `guest_card_access` (0 = client card never opens the place, Q-20) and `requires_reservation` (1 = card opens only if the reservation lists the place). See docs/database.
- Accepted (user, 2026-10-16, Q-43): cadastrar-item-stock in sprint 4 (1st delivery). Seeding room items is not needed.

Created by: BorjaTiago. Updated by: lukassperandio. AI use: backlog-prioritizer, Claude Code (claude-sonnet-5-5), 2026-10-16; status set to approved by doc-editor, Claude Code (claude-haiku-5-5), 2026-10-16; dependency audit and reorder by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; Q-43 to Q-45 answers, PR #4 notes and cadastrar-item-stock acceptance by doc-editor, Claude Code (claude-haiku-5-5), 2026-10-16. Week column, planning assumptions, sprint 13 risk and Q-04 and Q-28 closure by doc-editor, Claude Code (claude-haiku-5-5), 2026-10-16; sprint 3 planning, forecast for sprints 4 and 5 and refinement agenda by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
