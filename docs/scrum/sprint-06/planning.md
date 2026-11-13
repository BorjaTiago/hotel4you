# Sprint 06 Planning

- Dates: 2026-11-14 to 2026-11-20
- Team Leader: Antonio Martinho (member 3, see `.agent/context/team.md`)
- Limit: 21 points (forecast, team decision of 2026-11-13, see `.agent/drafts/backlog-order.md`, revision 2026-11-13)
- Committed: 21 points. No task without points in this sprint (T3 usability test moved to sprint 7).
- Revision 2026-11-13: the user approved this final set, decided task by task. It replaces the 23-point set of 2026-11-06.

## Goal

Garantir que cada funcionário só faz o que o seu papel permite.

Value for the hotel: the hotel applies one access rule for reception (floors 0 and 1) and restaurant staff (kitchen and restaurant). The manager removes staff accounts, looks up any user, sets the extra prices per night, sets the items of each room and records stock entries. The reservation includes restaurant and pool. Sprint 7 is the freeze and the zip (due 2026-12-01).

## Capacity

| Person | Hours this week | Note |
|--------|-----------------|------|
| Lukas Sperandio | not given | |
| Pedro Costa | not given | |
| Antonio Martinho | not given | Team Leader. Also develops. Dev hours not reduced (no request). |
| Tiago Borja | not given | |
| José Cruz | not given | Also owns the carry-over H4Y-55 (see Risks). |

- Hours per person were not given. The hours sanity check is skipped.
- Limit source: forecast. The user decided the sprint 6 set on 2026-11-13 (21 points, 7 stories, all parallel). There are no `retrospective.md` files, so no velocity average. Last facts: sprint 4 finished 20 points (Jira). The sprint 5 result is not known at planning time.
- 21 points is above the sprint 4 result (20). The team accepted it. Cut order if the sprint is full: definir-precos-extra (3), then remove-staff-account (3), then reservation-restaurant-pool (2).

## Committed points

21.

## Stories

All 7 stories run in parallel. No story waits for another story of this sprint (same-sprint rule). Every dependency points to an earlier sprint. The 4 SQL migrations have fixed numbers, so no two stories use the same number.

| Key | Story (draft) | Points | Requirements | Sub-tasks (drafts) |
|-----|---------------|--------|--------------|--------------------|
| (no key yet) | Configurar os itens de um quarto (`story-configure-room-items`) | 5 | RF-08 | `subtask-s06-configure-room-items-service` (with SQL v11), `subtask-s06-configure-room-items-screen` |
| (no key yet) | Registar entrada de stock (`story-registar-entrada-stock`) | 3 | RF-29 | `subtask-s06-registar-entrada-stock-service` (with SQL v12), `subtask-s06-registar-entrada-stock-screen` |
| (no key yet) | Incluir restaurante e piscina na reserva (`story-reservation-restaurant-pool`) | 2 | RF-11, RF-18 | `subtask-s06-reservation-restaurant-pool` (SQL v13, two checkboxes, one list column) |
| (no key yet) | Remover uma conta de funcionário (`story-remove-staff-account`) | 3 | RF-26 | `subtask-s06-remove-staff-account-service`, `subtask-s06-remove-staff-account-screen` |
| H4Y-87 | Regra de acesso por local (`story-regra-acesso-por-local`) | 3 | RF-24, RF-25, RF-01 | `subtask-s06-regra-acesso-por-local-service` (rule and table tests, no screen) |
| (no key yet) | Guardar e consultar todos os utilizadores (`story-consultar-utilizadores`) | 2 | RNF-01 | `subtask-s06-consultar-utilizadores` |
| (no key yet) | Definir os preços extra por noite (`story-definir-precos-extra`) | 3 | RF-07, RF-09 | `subtask-s06-definir-precos-extra-service` (with SQL v14), `subtask-s06-definir-precos-extra-screen` |

Total: 5 + 3 + 2 + 3 + 3 + 2 + 3 = 21 points. `sprint: 06` and `status: approved` are set in the 7 story drafts (user decision, 2026-11-13).

Each story has one owner from service to screen. Proposed owners are not set. The team decides in the planning meeting.

Decisions of the user (2026-11-13) that shape the stories:

- configure-room-items: 5 points. Reuses `Permission.MANAGE_ROOMS`. The old tests-docs sub-task is `removed` ("merged into service and screen").
- registar-entrada-stock: manager only (`Permission.MANAGE_STOCK`, no new permission). Only movement type `in`. It creates `stock_movements`.
- remove-staff-account: new AC-5 (the manager cannot remove his own account). Only reservations (`reservations.created_by_user_id`) count as associated records.
- regra-acesso-por-local: new story. It merges limit-reception-floors and limit-restaurant-staff-zones (both `status: merged`). One rule `canAccess(location, actor)` and table-driven tests. No screen. The restaurant AC-3 (no create or delete of reservations) is already built: `ReservationService.create` needs `Permission.MANAGE_RESERVATIONS` and `ReservationService.delete` needs `Permission.DELETE_RESERVATIONS`. Tests: `onCreate_throw_whenActorIsNotReceptionistOrManager`, `onDelete_throwAccessDenied_whenUserIsNotManager`.
- consultar-utilizadores: pulled from sprint 7. Listing exists (`StaffService.listUsers`). Remaining: `findUser(username, actor)`, facade method, small lookup in the staff screen, actor check on `listUsers`, tests.
- definir-precos-extra: pulled from sprint 12. Nothing exists in code: table, domain class, repositories, service, permission `MANAGE_EXTRA_PRICES`, controller and FXML, tests.

## Tasks (no points)

None in this sprint. The T3 usability test (`subtask-s07-t3-usability-test`) moved to sprint 7. The old file `subtask-s06-t3-usability-test.md` has `status: removed` and must be deleted.

## Needs from earlier sprints (not intra-sprint)

- reservation-restaurant-pool needs create-reservation H4Y-56 (sprint 4) and the 3-step flow of sprint 5.
- remove-staff-account, consultar-utilizadores and regra-acesso-por-local need H4Y-27 (create-staff-account, sprint 2) and `Permission` (H4Y-23, sprint 3).
- The other stories need nothing: they use their own test data (foundation T4) and reference data (foundation T5).

## Not selected

- Cut from the sprint (user, 2026-11-13): `story-refuse-delete-reservation` (H4Y-58, sub-task H4Y-78) and `story-block-delete-after-check-in`. Both have `status: removed`, `sprint:` empty. Reason: already delivered by H4Y-57 (`ReservationService.delete`: `Permission.DELETE_RESERVATIONS` and status RESERVED only). H4Y-58 and H4Y-78 stay open in Jira until the user OKs closing them. Jira is not edited here.
- Merged: `story-limit-reception-floors` and `story-limit-restaurant-staff-zones` (into `story-regra-acesso-por-local`).
- Moved to sprint 7: registar-saida-stock and registar-uso-stock (one block, one owner, 5 points; they need `stock_movements` from registar-entrada-stock). T3 usability test (task).
- Sprint 8 or later: the card stories need T6 first (card reader simulator, card numbers, first master card: 5 points, nothing exists: no card tables or classes). They are not startable before T6. The extras chain (adicionar-extras-quarto-reserva 5, adicionar-servicos-extra-reserva 3 to 5, calcular-preco-total-reserva 3) follows definir-precos-extra in sprint 7 or later. carregar-xml-fornecedor-gestor stays in sprint 11 (blockers in Risks).
- edit-staff-account: planned for sprint 5. If it leaves sprint 5, it is a candidate here. It is not committed now.
- cancel-reservation (H4Y-55): not in sprint 6. See Risks (carry-over).
- Stories `merged` or `removed` (editar-item-stock, criar-zona, editar-zona, remover-zona, registar-cliente-rapido): not candidates.

## Risks

- Goal wording: "Garantir que cada funcionário só faz o que o seu papel permite" now covers only the access rule, remove-staff-account and consultar-utilizadores. The other stories (room items, stock entry, restaurant and pool, extra prices) do not fit it. The team should reword the goal to customer value in the planning meeting.
- Carry-over from sprint 5, owner José: cancel-reservation H4Y-55 (PR #62, merge conflicts). It is not in sprint 6 points. José handles it.
- Sprint 4 closed 20 points. 21 points is above it. See Capacity for the cut order.
- Shared SQL numbering. The code stops at v10. Reserved numbers: v11 `room_items` (configure-room-items), v12 `stock_movements` (registar-entrada-stock), v13 `reservation_locations` (reservation-restaurant-pool), v14 `extra_prices` (definir-precos-extra). The scripts run in this order. Do not use another number. If a story slips, its number stays reserved.
- Stock role decision: only the manager records a stock entry (`Permission.MANAGE_STOCK`). The story text said "funcionário do hotel ou gestor". The story actor and the ACs are changed to "gestor". The team confirms.
- No screen for the access rule. `canAccess(location, actor)` has no user. Reception and restaurant staff see no change in sprint 6. The screen is left for the card stories (sprints 9 and 10). `Location` has no floor number: the team agrees the way to read the floor, and updates `docs/uml/`.
- Draft state (checks of `sprint-planner`). `status: approved` and `points` were set in the 7 story drafts by the user decision of 2026-11-13. Reviewer: pending. The 7 stories and the 11 sub-tasks were published on 2026-11-13 (H4Y-86 to H4Y-104). The sprint "H4Y - Sprint 6" does not exist in Jira yet, so run `jira-publisher` in mode `sync-board` once the team creates it. Tests need the key for `@Tag("H4Y-N-AC-n")`. In the sub-task drafts, `N` stands for the story key and the branch names use `H4Y-N`. Replace `N` after publish.
- Label: `story-registar-entrada-stock` now has `1st-delivery` (was `final-delivery`).
- registar-entrada-stock has no AC for the manager-only rule. A test with `@Tag("H4Y-N-AC-1")` covers it. The team may add an AC.
- remove-staff-account AC-3 and the configure-room-items AC-4: the foreign key error (SQL 547) becomes an own exception. Only reservations count for remove-staff-account.
- The class diagram has gaps: remove item and change quantity (configure-room-items), `canAccess`, `findUser`, `listUsers(actor)`, `removeStaff` without `actor`, the extra price classes and `MANAGE_EXTRA_PRICES`. Each sub-task updates `docs/uml/` and `docs/database/` before the code (rule 11).
- `listUsers` gets an `actor` parameter (consultar-utilizadores). All callers and tests change in the same PR.
- Merge conflict risk: several stories edit `Permission`, `HotelFacade` and the main menu. The owners rebase often.
- carregar-xml-fornecedor-gestor (sprint 11) blockers, noted here only: Q-59 (JSON field names, still open), the Gson dependency is missing, and AC-2 contradicts `docs/uml/traceability.md` (reject unknown items versus create missing items). No new Q-NN is added here.
- 21 points is above the last known result (20). See Capacity.

## Next steps

1. Done (2026-11-13): the sub-task drafts are `approved`. The 7 stories and 11 sub-tasks are in Jira (H4Y-86 to H4Y-104) with points, priority and owners.
2. The team creates the sprint "H4Y - Sprint 6" in Jira.
3. Run `jira-publisher` in mode `sync-board`: it adds the 7 stories to the sprint. `N` in branch names and tags is already replaced.
4. The user decides when to close H4Y-58 and H4Y-78 in Jira.

```
committed_points: 21
limit: 21
limit_source: forecast (team decision, user 2026-11-13, .agent/drafts/backlog-order.md, revision 2026-11-13)
over_limit: false
```

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; doc-updater, Claude Code (claude-haiku-5-5), 2026-11-13. Reviewed by: pending.
