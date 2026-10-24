# Sprint 03 Planning

- Dates: 2026-10-24 to 2026-10-30 (same as the sprint planning PDF on the `docs` branch)
- Team Leader: Lukas Sperandio (member 1, see `.agent/context/team.md`)
- Limit: at least 13 points per sprint (velocity of sprint 2 = 13; team decision 2026-10-23)
- Committed: 16 points (13 new + 3 carry-over)

## Goal

Value for the hotel: at the end of the sprint, the hotel has its 20 rooms and their nightly prices in the system. The reception can tell a client which rooms exist and what they cost, and each employee can use only the functions of their role. This prepares the client registration and reservations of sprints 4 and 5.

Team rule (Team Leader, 2026-10-23): every sprint goal states the value for the stakeholder (the hotel), not only the work.

## Capacity

| Person | Hours this week | Note |
|--------|-----------------|------|
| Lukas Sperandio | 8 | Team Leader. Also develops. Dev hours not reduced (no request). |
| Pedro Costa | 8 | |
| Antonio Martinho | 8 | |
| Tiago Borja | 8 | |
| José Cruz | 8 | |
| Total | 40 | |

- Limit source: velocity. Sprint 2 finished H4Y-20 (2), H4Y-22 (5), H4Y-24 (3) and H4Y-27 (3) = 13 points. There is no `retrospective.md` for sprint 2 (H4Y-38 was deleted from Jira on 2026-10-23). The value comes from the Jira board on 2026-10-23.
- Hours check (warning only): 13 points use most of the 40 hours. The SQL scripts are inside the story sub-tasks. There is little slack.

## Committed points

16. The minimum is 13.

## Stories

| Key | Story | Points | Proposed owner | Requirements | Sub-tasks |
|-----|-------|--------|----------------|--------------|-----------|
| H4Y-23 | Mostrar apenas as funções permitidas ao papel | 3 | Lukas Sperandio | RF-02 | H4Y-60, H4Y-61 |
| H4Y-50 | Consultar a lista de quartos | 2 | Antonio Martinho | RF-04, RF-06 | H4Y-67 |
| H4Y-8 | Diagramas de sequência (carry-over, teacher feedback) | 3 | Pedro Costa | RNF-24 | - |
| H4Y-28 | Consultar os pisos e zonas do hotel | 2 | Tiago Borja | RF-03 | H4Y-62 |
| H4Y-25 | Registar os 20 quartos por tipo e piso | 3 | Pedro Costa | RF-04 | H4Y-63, H4Y-64 |
| H4Y-26 | Definir o preço base por noite de cada tipo de quarto | 3 | José Cruz | RF-06 | H4Y-65, H4Y-66 |

Total: 16 points (13 new + 3 carry-over). The team confirms the owners.

Carry-over (sprint 2 retrospective, 2026-10-23): H4Y-7, H4Y-8 and H4Y-9 had teacher feedback. H4Y-7 and H4Y-9 are fixed on `main` (PRs #23 and #21). H4Y-8 is reopened in Jira (2026-10-24, Team Leader OK). Feedback to apply: show the database transactions, put the cardinality in the diagram (not in a table), add a data preview screen before the insert.

H4Y-21 (restaurant login, 2 points) was deleted from Jira by the Team Leader on 2026-10-23. The restaurant login already works since sprint 2 (H4Y-22, H4Y-27). Counting it again would inflate the velocity. No test-only sub-tasks (Team Leader decision, 2026-10-23).

## Tasks

None. Team Leader decision (2026-10-23): the sprint has only stories and their sub-tasks. No task cards in Jira.

Today the database has only `roles` and `users`. Each story sub-task creates the tables it uses: H4Y-28 `floors` and `locations` (with the floors and places of the statement), H4Y-26 `room_types`, H4Y-25 `rooms`, cadastrar-item-stock `items`. Scrum records (planning, daily notes, review, retrospective) stay as files in `docs/scrum/sprint-03/`, without cards.

Sub-task drafts: `.agent/drafts/subtask-s03-*.md` (8 committed, 1 stretch).

## Stretch (extra work)

Team decision (2026-10-23, Team Leader): the team works with at least 13 points per sprint. The 13 points above are the commitment. The items below come in only when the committed work is in progress, in this order. An item that does not finish goes first in sprint 4.

| Order | Key | Item | Points | Proposed owner | Why it fits now |
|-------|-----|------|--------|----------------|-----------------|
| 1 | H4Y-51 | Cadastrar item de stock (sub-task H4Y-68) | 2 | Tiago Borja | No link to the other stories. Tiago has 2 committed points. Needed before configure-room-items (sprint 6). |

With the stretch item: 18 points. Hours check (warning only): this is above the 40 hours. Do not start a stretch item before the committed sub-tasks of the same person are in review.

H4Y-50 and H4Y-51 were created in Jira on 2026-10-23, under the new epic H4Y-49 (stock) and H4Y-14 (rooms).

## Order inside the sprint

1. In parallel: list-rooms (Antonio), H4Y-28 (Tiago), 23.1 then 23.2 (Lukas), 25.1 then 25.2 (Pedro), 26.1 then 26.2 (José).
2. SQL order: `floors` (H4Y-28) and `room_types` (H4Y-26) before `rooms` (H4Y-25), because of the foreign keys. Merge H4Y-28 and 26.1 early. Until then, the tests use in-memory repositories.
3. The new menu buttons of H4Y-25, H4Y-26 and H4Y-28 use the role rule of H4Y-23. If 23.1 is not on `main` yet, they use `AccessDeniedException` as in H4Y-27.

## Not selected

- H4Y-35, H4Y-36, H4Y-37, H4Y-38 and H4Y-48 (sprint 2 tasks): deleted from Jira by the Team Leader on 2026-10-23. H4Y-48 is done on `main`. The work of H4Y-35 (JUnit, Javadoc: RNF-09, RNF-10) and H4Y-38 (sprint 2 retrospective) has no card now. See the refinement agenda in `.agent/drafts/backlog-order.md`.
- registar-cliente (3) and the other sprint 4 stories: over the limit. See the forecast in `.agent/drafts/backlog-order.md`.

## Risks

- The class diagram has no repository for floors, rooms or room types, and no facade method to list floors. Each story adds them to `docs/uml/` before the code (AGENTS.md rule 11). Two developers can add similar classes. Agree on the names on day 1.
- Pedro Costa has H4Y-25 and the H4Y-8 carry-over. The H4Y-8 fixes come first. If they slip, Tiago helps and H4Y-51 does not start.
- The SQL scripts of H4Y-28 and H4Y-26 are on the critical path of H4Y-25. If they slip, H4Y-25 finishes with in-memory tests only and the manual check waits.
- H4Y-23 changes `MainController`, and H4Y-25, H4Y-26 and H4Y-28 add buttons to it. Expect merge conflicts. Merge 23.2 early.
- Jira: sprint "H4Y - Sprint 2" is still active (end date 2026-10-22). Close it in Jira before sprint 3 starts. Sub-tasks H4Y-39, 40, 42, 44, 45 and 47 are still open, but their stories are Done.
- Q-01 (20 rooms, 7/10/3 split) and Q-11 (manager sets the base price) are team decisions, not confirmed by the teacher.

## Next steps

1. Done (2026-10-23, Team Leader approval): epic H4Y-49, stories H4Y-50 to H4Y-59 and sub-tasks H4Y-60 to H4Y-79 are in Jira. Points are set only on the sprint 3 stories. H4Y-67 got a screen and a menu button, and H4Y-60 lost the H4Y-21 part (Team Leader OK, 2026-10-23). Backlog stories (H4Y-52 to H4Y-59) have no points until the refinement.
2. Create the sprint "H4Y - Sprint 3" on the Jira board. Then add H4Y-23, 25, 26, 28, 50, 51, H4Y-8 and the sub-tasks to it. Sprint goal in Jira: the same text as the Goal above.
3. Close "H4Y - Sprint 2" after the sprint review.

```
committed_points: 16
limit: 16
limit_source: forecast (team decision: at least 13, sprint 2 velocity 13)
over_limit: false
```

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; value goal, H4Y-21 removal and UI review by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-8 carry-over and dates by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-24. Reviewed by: pending.
