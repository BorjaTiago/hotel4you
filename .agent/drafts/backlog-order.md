---
type: backlog
status: approved
---

# Backlog order - H4Y (Hotel4You)

Proposal for the PO. The PO decides. No Jira write. Source: 15 approved epics, 57 approved stories (all have points), `requirements.md`, `questions.md`.

## Planning assumptions

- Sprint 1 (2026-10-12): requirements only. No story.
- Sprints 2 to 13: 12 sprints of 1 week. Sprint 2 starts 2026-10-19.
- 1st delivery 2026-12-01 23:55. Final delivery 2027-01-31 23:55. Dates are fixed.
- Weeks 2026-10-19 to 2027-01-31 = 15 weeks = 12 sprints + 3 free weeks. Which weeks are free is open (Q-04).
- Total story points: 180 (57 stories). Technical tasks (foundation, scrum, quality, packaging) have no points. They run inside each sprint.
- Velocity is a GUESS. No history. Proposed: 12 to 13 points in sprints 2 to 6 (ramp-up, 5 developers, setup tasks), 14 to 18 later. Average 15 per sprint. The team must confirm it in sprint planning.
- Nominal dates below count 12 consecutive weeks (sprint 2 = week of 10-19 ... sprint 13 = week of 2027-01-04). Each free week moves later sprints by 1 week. Sprint 13 must start no later than 2027-01-25.
- 1st delivery buffer: stories of the 1st delivery end in sprint 6 (week of 11-16, ends 11-22). Up to 2 free weeks before 12-01 still keep them before the deadline. A third free week before 12-01 breaks the plan.

## Sprint slice

| Sprint | Week (nominal) | Points | Stories | Increment (what works at the end) |
|--------|----------------|--------|---------|-----------------------------------|
| 1 | 10-12 | 0 | none | Requirements documents. |
| 2 | 10-19 | 12 | login-gestor, login-restaurante, login-rececionista, janela-principal | Staff and managers log in and see the JavaFX main window. |
| 3 | 10-26 | 13 | funcoes-por-papel, model-hotel-floors, register-rooms, set-base-price, list-rooms | The manager sets up floors, 20 rooms and base prices. Each role sees only its functions. |
| 4 | 11-02 | 13 | registar-cliente, procurar-cliente, registar-cliente-rapido, query-free-rooms | The receptionist registers and finds clients, and searches free rooms. |
| 5 | 11-09 | 13 | create-reservation, cancel-reservation, delete-reservation, refuse-delete-reservation | Full reservation life cycle. RNF-03 (30 s, registration + reservation) measured here. |
| 6 | 11-16 | 13 | check-in, check-out, block-delete-after-check-in, configure-room-items | Check-in and check-out work. Room items configured. 1st delivery scope complete. |
| 7 | 11-23 | 15 | consultar-utilizadores, create-staff-account, edit-staff-account, remove-staff-account, criar-zona | 1st delivery freeze and zip. Manager manages staff accounts, lists users, creates zones. |
| 8 | 11-30 | 14 | editar-zona, remover-zona, reservation-restaurant-pool, limit-reception-floors, limit-restaurant-staff-zones | 1st delivery due 12-01. Zones complete. Floor limits for reception and restaurant staff. |
| 9 | 12-07 | 17 | emitir-cartao-check-in, registar-passagens-cartao, abrir-quarto-com-cartao, abrir-restaurante-piscina-reservados, recusar-cartao-locais-nao-permitidos | The client card is issued, opens the room and the reserved places, and every pass is logged. |
| 10 | 12-14 | 18 | desativar-cartao-check-out, abrir-tudo-cartao-mestre, bloquear-cartao-mestre, emitir-novo-cartao-mestre, consultar-locais-cartao, consultar-passagens-quarto | Master card, block and new card, card at check-out, manager card and room logs. Card epic complete. |
| 11 | 12-21 | 18 | cadastrar-item-stock, editar-item-stock, carregar-xml-fornecedor-gestor, registar-entrada-stock, ver-relatorio-importacao | The manager keeps the stock catalogue and loads the supplier XML with an import report. |
| 12 | 12-28 | 17 | registar-uso-stock, registar-saida-stock, carregar-xml-fornecedor-rececionista, definir-precos-extra, adicionar-extras-quarto-reserva | Stock use and exit. Reception loads XML. Manager sets extra prices. Extras on the reservation. |
| 13 | 2027-01-04 | 17 | adicionar-servicos-extra-reserva, calcular-preco-total-reserva, register-room-service-request, record-suggestion-complaint, list-suggestions-complaints | Extra services, total price, room service, suggestions and complaints. Final zip. |

Check: 12+13+13+13+13+15+14+17+18+18+17+17 = 180.

## Technical tasks (no points) by sprint

| Epic | Tasks | Sprint |
|------|-------|--------|
| foundation-architecture | T1 layers business/GUI (RNF-05, 07, 08): start of sprint 2, before any story. T2 SQL Server (RNF-12): sprint 2, same week. T3 usability test (RNF-04): sprint 6 first run, repeat in sprint 10. | 2, 6, 10 |
| authentication-users | T1 encrypted passwords (RNF-02): sprint 2, with the login stories. | 2 |
| engineering-quality | Setup JUnit, Javadoc, `docs/libraries.md`, branch and commit rule, AI-use reference (RNF-09, 10, 11, 20, 21, 25): sprint 2. Code-of-conduct check (RNF-26): in each sprint review. Final audit: sprint 12 to 13. | 2 to 13 |
| scrum-process | Planning, daily notes, review, retrospective, refinement, leader rotation, validation, diagram update (RNF-13 to 19, 27): every sprint 2 to 13. Sprint 1 has no leader (Q-24). | 2 to 13 |
| delivery-packaging | Domain model and use case diagram: sprint 3. Class diagram: sprint 5 and 6. Sequence diagrams (general + detailed): sprint 6. Executables and Javadoc output: sprint 6. Zip and validation of 1st delivery: sprint 7, submit before 12-01. Update diagrams for the final scope: sprints 10 to 12. Final zip dry run: sprint 12. Final zip and submit: sprint 13. | 3 to 13 |

## Ordered backlog

Value: H = high (must, in a delivery), M = medium, L = low (should). Risk: H/M/L. Target sprint is the proposal.

| # | Item | Type | Requirements | Points suggested | Reason (value / risk / dependency / date) | Target sprint |
|---|------|------|--------------|------------------|-------------------------------------------|---------------|
| 1 | Foundation T1+T2 layers and SQL Server | Task | RNF-05, 07, 08, 12 | - | Value H: every story needs it. Risk H: DB and layers decide all code. Dependency: none, first. Date: 1st delivery. | 2 |
| 2 | Auth T1 encrypted passwords | Task | RNF-02 | - | Value H (security). Risk M. Dependency: foundation T2. Date: before login. | 2 |
| 3 | Engineering-quality setup | Task | RNF-09, 10, 11, 20, 21, 25 | - | Value H: graded, every PR uses it. Risk L. Dependency: none. Date: sprint 2, then each sprint. | 2 |
| 4 | janela-principal | Story | RNF-06, 07 | 3 | Value H: GUI base for all screens. Risk M: first JavaFX. Dependency: foundation T1. Date: 1st delivery. | 2 |
| 5 | login-rececionista | Story | RF-01, 02 | 5 | Value H: gate of the app. Risk M: first full path GUI, logic, DB. Dependency: items 1, 2, 4. Date: 1st delivery. | 2 |
| 6 | login-gestor | Story | RF-01, 02 | 2 | Value H. Risk L: reuses item 5. Dependency: item 5. Date: 1st delivery. | 2 |
| 7 | login-restaurante | Story | RF-01, 02 | 2 | Value M: needed for RF-25 later. Risk L. Dependency: item 5. Date: login is 1st delivery (Q-06). | 2 |
| 8 | funcoes-por-papel | Story | RF-02 | 3 | Value H: role access base. Risk M. Dependency: items 5 to 7. Date: 1st delivery. | 3 |
| 9 | model-hotel-floors | Story | RF-03 | 2 | Value H: floors needed by rooms and zones. Risk L. Dependency: item 1. Date: 1st delivery. | 3 |
| 10 | register-rooms | Story | RF-04 | 3 | Value H: no reservation without rooms. Risk L. Dependency: item 9. Date: 1st delivery. | 3 |
| 11 | set-base-price | Story | RF-06 | 3 | Value H. Risk L. Dependency: item 10. Date: 1st delivery. | 3 |
| 12 | list-rooms | Story | RF-04, 06 | 2 | Value M. Risk L. Dependency: items 10, 11. Date: 1st delivery. | 3 |
| 13 | Delivery T: domain model + use case diagram | Task | RNF-24 | - | Value H: graded document. Risk L. Dependency: requirements (sprint 1). Date: early, 1st delivery. | 3 |
| 14 | registar-cliente | Story | RF-10 | 3 | Value H: reservation needs the client (Q-27). Risk L. Dependency: item 5. Date: 1st delivery. | 4 |
| 15 | procurar-cliente | Story | RF-10 | 2 | Value M: reservation picks an existing client. Risk L. Dependency: item 14. Date: 1st delivery. | 4 |
| 16 | registar-cliente-rapido | Story | RNF-03, RF-10 | 3 | Value H: 30 s limit is measurable. Risk M: time target. Dependency: item 14. Date: 1st delivery. | 4 |
| 17 | query-free-rooms | Story | RF-05 | 5 | Value H: core of booking. Risk M: date overlap rule. Dependency: items 10, 11. Date: 1st delivery. | 4 |
| 18 | create-reservation | Story | RF-11, 05 | 5 | Value H: central flow. Risk H: links client, room, dates, price. Dependency: items 14, 17. Date: 1st delivery. | 5 |
| 19 | cancel-reservation | Story | RF-12 | 3 | Value H. Risk L. Dependency: item 18. Date: 1st delivery. | 5 |
| 20 | delete-reservation | Story | RF-22 | 3 | Value H. Risk M: role rule. Dependency: items 18, 6. Date: 1st delivery. | 5 |
| 21 | refuse-delete-reservation | Story | RF-22 | 2 | Value H: refusal rule. Risk L. Dependency: item 20. Date: 1st delivery. | 5 |
| 22 | check-in | Story | RF-13 | 3 | Value H. Risk M: state change on reservation. Dependency: item 18. Date: 1st delivery. | 6 |
| 23 | check-out | Story | RF-14 | 3 | Value H. Risk L. Dependency: item 22. Date: 1st delivery. | 6 |
| 24 | block-delete-after-check-in | Story | RF-13, 22 | 2 | Value H: closes RF-22. Risk L. Dependency: items 20, 22. Date: 1st delivery. | 6 |
| 25 | configure-room-items | Story | RF-08 | 5 | Value M (should), but in the 1st delivery (rooms, Q-06). Risk L. Dependency: item 10. Date: 1st delivery; first to move to sprint 7 if sprint 6 is full. | 6 |
| 26 | Delivery T: class, sequence, executables, Javadoc | Task | RNF-24 | - | Value H: 20% documentation grade. Risk M: diagrams late. Dependency: code of items 1 to 25. Date: sprints 5 and 6. | 5 to 6 |
| 27 | Foundation T3 usability test | Task | RNF-04 | - | Value M. Risk L. Dependency: reception screens (items 14 to 18). Date: 1st delivery. | 6 |
| 28 | Delivery T: 1st zip and validation | Task | RNF-22, 23, 24 | - | Value H: 35% of grade. Risk H: deadline. Dependency: items 1 to 27. Date: 12-01 23:55. | 7 |
| 29 | consultar-utilizadores | Story | RNF-01 | 3 | Value M. Risk L. Dependency: item 5. Date: final delivery (not named in Q-06, Q-25). | 7 |
| 30 | create-staff-account | Story | RF-26, 01 | 3 | Value H (must). Risk L. Dependency: item 6. Date: final delivery. | 7 |
| 31 | edit-staff-account | Story | RF-26 | 3 | Value H. Risk L. Dependency: item 30. Date: final delivery. | 7 |
| 32 | remove-staff-account | Story | RF-26 | 3 | Value H. Risk M: Q-36 refuse with links. Dependency: item 30. Date: final delivery. | 7 |
| 33 | criar-zona | Story | RF-03 | 3 | Value H: zones gate the card rules. Risk M. Dependency: item 9. Date: final delivery; starts the card chain early. | 7 |
| 34 | editar-zona | Story | RF-03 | 3 | Value H. Risk L. Dependency: item 33. Date: final delivery. | 8 |
| 35 | remover-zona | Story | RF-03 | 3 | Value M. Risk M: Q-36 refuse with links. Dependency: item 33. Date: final delivery. | 8 |
| 36 | reservation-restaurant-pool | Story | RF-11, 18 | 2 | Value H: the card rule needs it. Risk M. Dependency: items 18, 33. Date: RF-11 is 1st delivery but the zone flag comes in sprint 7 (see Q-42). | 8 |
| 37 | limit-reception-floors | Story | RF-24 | 3 | Value H (must). Risk M. Dependency: items 8, 9, 33. Date: final delivery. | 8 |
| 38 | limit-restaurant-staff-zones | Story | RF-25, 01 | 3 | Value H (must). Risk M. Dependency: items 7, 33. Date: final delivery. | 8 |
| 39 | emitir-cartao-check-in | Story | RF-13, 16 | 3 | Value H: start of the card chain. Risk H: card number rule. Dependency: item 22. Date: final delivery (Q-06). | 9 |
| 40 | registar-passagens-cartao | Story | RF-19 | 3 | Value H: base of all logs. Risk H: every access writes. Dependency: item 39. Date: final delivery. | 9 |
| 41 | abrir-quarto-com-cartao | Story | RF-17 | 3 | Value H. Risk M. Dependency: items 39, 40. Date: final delivery. | 9 |
| 42 | abrir-restaurante-piscina-reservados | Story | RF-18, 03 | 5 | Value H. Risk H: reservation + zone rule. Dependency: items 33, 36, 39. Date: final delivery. | 9 |
| 43 | recusar-cartao-locais-nao-permitidos | Story | RF-18, 03 | 3 | Value H. Risk M. Dependency: item 42. Date: final delivery. | 9 |
| 44 | desativar-cartao-check-out | Story | RF-14, 16 | 3 | Value H (Q-30). Risk M. Dependency: items 23, 39. Date: final delivery. | 10 |
| 45 | abrir-tudo-cartao-mestre | Story | RF-23, 19 | 3 | Value H. Risk M. Dependency: items 33, 40. Date: final delivery. | 10 |
| 46 | bloquear-cartao-mestre | Story | RF-30 | 3 | Value H. Risk M: block is immediate (Q-26). Dependency: item 45. Date: final delivery. | 10 |
| 47 | emitir-novo-cartao-mestre | Story | RF-30, 23 | 3 | Value H. Risk M. Dependency: item 46 (Q-31). Date: final delivery. | 10 |
| 48 | consultar-locais-cartao | Story | RF-20, 19 | 3 | Value H. Risk L. Dependency: item 40. Date: final delivery. | 10 |
| 49 | consultar-passagens-quarto | Story | RF-21, 19 | 3 | Value H. Risk L. Dependency: items 40, 45. Date: final delivery. | 10 |
| 50 | cadastrar-item-stock | Story | RF-28, 29 | 2 | Value H: catalogue base (Q-38). Risk L. Dependency: item 6. Date: final delivery. | 11 |
| 51 | editar-item-stock | Story | RF-28, 29 | 2 | Value M. Risk L. Dependency: item 50. Date: final delivery. | 11 |
| 52 | carregar-xml-fornecedor-gestor | Story | RF-28 | 8 | Value H. Risk H: biggest story, new XML format, unknown-item rule. Dependency: item 50. Date: final delivery; early in the stock block. | 11 |
| 53 | registar-entrada-stock | Story | RF-29 | 3 | Value H. Risk L. Dependency: item 50. Date: final delivery. | 11 |
| 54 | ver-relatorio-importacao | Story | RF-28 | 3 | Value H: report of rejected items. Risk M. Dependency: item 52 (same sprint, last). Date: final delivery. | 11 |
| 55 | registar-uso-stock | Story | RF-29 | 3 | Value H. Risk L. Dependency: items 50, 53. Date: final delivery. | 12 |
| 56 | registar-saida-stock | Story | RF-29 | 3 | Value H. Risk L. Dependency: items 50, 53. Date: final delivery. | 12 |
| 57 | carregar-xml-fornecedor-rececionista | Story | RF-28 | 3 | Value M: same function, other role. Risk L. Dependency: items 52, 54. Date: final delivery. | 12 |
| 58 | definir-precos-extra | Story | RF-07, 09 | 3 | Value H (RF-07 must). Risk L. Dependency: item 11. Date: no phase in Q-06 (Q-25); planned late. | 12 |
| 59 | adicionar-extras-quarto-reserva | Story | RF-07 | 5 | Value H. Risk M: one view per reservation (Q-37), change until check-in (Q-40). Dependency: items 18, 58. Date: final delivery. | 12 |
| 60 | adicionar-servicos-extra-reserva | Story | RF-09 | 5 | Value M (should). Risk M. Dependency: items 18, 58. Date: final delivery. | 13 |
| 61 | calcular-preco-total-reserva | Story | RF-06, 07, 09 | 5 | Value H: total price. Risk H: sums base and extras. Dependency: items 11, 59, 60. Date: final delivery. | 13 |
| 62 | register-room-service-request | Story | RF-15 | 3 | Value M (should). Risk L. Dependency: items 18, 22. Date: final delivery; cut-first candidate. | 13 |
| 63 | record-suggestion-complaint | Story | RF-27 | 2 | Value L (should). Risk L. Dependency: item 5. Date: final delivery; cut-first candidate. | 13 |
| 64 | list-suggestions-complaints | Story | RF-27 | 2 | Value L (should). Risk L. Dependency: item 63. Date: final delivery; cut-first candidate. | 13 |
| 65 | Delivery T: final diagrams, Javadoc, zip, submit | Task | RNF-22, 23, 24 | - | Value H: 65% of grade. Risk H: deadline 2027-01-31 23:55. Dependency: all items. Date: final. | 12 to 13 |
| 66 | Scrum-process and engineering-quality tasks | Task | RNF-13 to 19, 26, 27 | - | Value H: 20% grade per delivery. Risk L if done each sprint. Dependency: none. Date: each sprint 2 to 13. | 2 to 13 |

Sprint 1 holds no code story.

## Requirement coverage

Every RF and RNF of `requirements.md`. No `MISSING`.

| Requirement ID | Epic | Stories |
|----------------|------|---------|
| RF-01 | authentication-users, staff-management-access | login-rececionista, login-gestor, login-restaurante, create-staff-account, limit-restaurant-staff-zones |
| RF-02 | authentication-users | login-rececionista, login-gestor, login-restaurante, funcoes-por-papel |
| RF-03 | rooms-pricing, magnetic-card-access | model-hotel-floors, criar-zona, editar-zona, remover-zona, abrir-restaurante-piscina-reservados, recusar-cartao-locais-nao-permitidos |
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
| RF-16 | magnetic-card-access | emitir-cartao-check-in, desativar-cartao-check-out |
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
| RNF-02 | authentication-users | task T1 (no story, Q-35) |
| RNF-03 | client-registration, reservations | registar-cliente-rapido |
| RNF-04 | foundation-architecture | task T3 (no story, Q-41) |
| RNF-05 | foundation-architecture | task T1 (no story) |
| RNF-06 | foundation-architecture | janela-principal |
| RNF-07 | foundation-architecture | janela-principal, task T1 |
| RNF-08 | foundation-architecture | task T1 (no story) |
| RNF-09 | engineering-quality | task (no story) |
| RNF-10 | engineering-quality | task (no story) |
| RNF-11 | engineering-quality | task (no story) |
| RNF-12 | foundation-architecture | task T2 (no story) |
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

- foundation T1, T2 -> every story. auth T1 -> login stories.
- janela-principal -> login-rececionista -> login-gestor, login-restaurante, funcoes-por-papel.
- model-hotel-floors -> register-rooms -> set-base-price, list-rooms, query-free-rooms, configure-room-items.
- registar-cliente -> procurar-cliente, registar-cliente-rapido -> create-reservation (RNF-03 test needs both).
- query-free-rooms -> create-reservation -> cancel-reservation, delete-reservation -> refuse-delete-reservation, block-delete-after-check-in.
- create-reservation -> check-in -> check-out; check-in -> emitir-cartao-check-in.
- criar-zona -> editar-zona, remover-zona, reservation-restaurant-pool, limit-reception-floors, limit-restaurant-staff-zones, abrir-tudo-cartao-mestre.
- emitir-cartao-check-in -> registar-passagens-cartao -> abrir-quarto-com-cartao, abrir-restaurante-piscina-reservados -> recusar-cartao-locais-nao-permitidos.
- check-out, emitir-cartao-check-in -> desativar-cartao-check-out.
- registar-passagens-cartao -> consultar-locais-cartao, consultar-passagens-quarto, abrir-tudo-cartao-mestre -> bloquear-cartao-mestre -> emitir-novo-cartao-mestre.
- cadastrar-item-stock -> editar-item-stock, registar-entrada-stock -> registar-uso-stock, registar-saida-stock.
- cadastrar-item-stock -> carregar-xml-fornecedor-gestor -> ver-relatorio-importacao, carregar-xml-fornecedor-rececionista.
- set-base-price -> definir-precos-extra -> adicionar-extras-quarto-reserva, adicionar-servicos-extra-reserva -> calcular-preco-total-reserva.
- record-suggestion-complaint -> list-suggestions-complaints.
- All dependencies point to an earlier or same-sprint item (order inside a sprint follows the list above).

## Risks

- Velocity is a guess. Plan needs 15 points per sprint on average. If the real velocity is 13, about 24 points do not fit. Cut-first order (all "should" or duplicate paths): list-suggestions-complaints, record-suggestion-complaint, register-room-service-request (7 points, RF-15 and RF-27), adicionar-servicos-extra-reserva (5, RF-09), carregar-xml-fornecedor-rececionista (3, same function as the manager loader). Sum 18. The PO decides.
- Sprint 2 has the highest setup risk: foundation T1, T2 and 4 stories in one week. If T2 slips, move login-restaurante to sprint 3.
- The 1st delivery is 35% of the grade. Sprints 2 to 6 hold only 1st delivery scope (64 points). Sprint 7 holds the zip, so no sprint 7 story may touch the frozen delivery branch before the zip is tagged.
- Card chain (sprints 9 and 10, 35 points) is high risk and high value for the 65% final delivery. It starts as early as the dependencies allow.
- XML loader (8 points) is the biggest story and has no real example file (Q-10). Do it first in the stock block (sprint 11). Ask the team to split it if refinement shows more than 8.
- Sprint 13 is the last week. The final zip has no buffer. If a free week is unused, use it before 01-31 as a buffer week.
- Free weeks (Q-04, open): up to 2 free weeks before 12-01 are safe. A free week in a late sprint shifts only final-delivery items.
- The extras (RF-07, RF-09) are late because Q-25 gives no phase. Value is "must" for RF-07. The PO may swap sprint 12 and 13 stock and extras if RF-07 should come earlier.

## Not-ready items

None. All 57 stories and 15 epics have `status: approved`. All stories have `points`. Notes for the PO (no edit made):

- `epic-check-in-out.md` Open questions says the card state at check-out is not answered. Q-30 answers it. Text is stale.
- Stock movement stories name the receptionist as actor. RF-29 says "staff or managers". The manager cannot record movements in the current stories.
- Stories do not have Jira keys (H4Y-NN) yet. The table uses draft slugs.

## Open questions

- Q-04 (open): which 3 weeks have no sprint. Dates of sprints 8 to 13 depend on it.
- Q-15 (open): AI reference format. Affects the engineering-quality task.
- Q-28 (open): developer as story actor. The technical work stays as tasks (Q-35), so this plan is not blocked.
- Q-42 (answered, team decision, user, 2026-10-16: keep in sprint 8): `reservation-restaurant-pool` (RF-11, RF-18) is in sprint 8, after the 1st delivery, because it needs zones. RF-11 is 1st delivery scope. Is this accepted, or must the reservation have a simple restaurant and pool flag in sprint 5?

AI use: backlog-prioritizer, Claude Code (claude-sonnet-5-5), 2026-10-16; status set to approved by doc-editor, Claude Code (claude-haiku-5-5), 2026-10-16. Reviewed by: pending.
