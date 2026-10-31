# Sprint 04 Planning

- Dates: 2026-10-31 to 2026-11-06
- Team Leader: José Cruz (member 5, see `.agent/context/team.md`)
- Limit: 21 points (average velocity of sprints 2 and 3)
- Committed: 20 points
- Team version in Portuguese: `docs/sprint-4/sprint-planning.html` on the `docs` branch

## Goal

Value for the hotel: at the end of the sprint, reception serves clients at the desk. It registers and finds clients, tells them which rooms are free for their dates, and creates the reservation.

## Capacity

| Person | Hours this week | Note |
|--------|-----------------|------|
| José Cruz | 8 | Team Leader. Also develops. |
| Tiago Borja | 8 | |
| Lukas Sperandio | 8 | |
| Pedro Costa | 8 | |
| António Martinho | 8 | |
| Total | 40 | 80% of 50 hours (5 people, 2 hours per day, 5 days) |

- Limit source: velocity. Sprint 2 = 18 points. Sprint 3 = 25 points (Jira, 2026-10-31). The average is 21.5, rounded down.
- Sprint 3 had 30 points in Jira at the end. The planning had 24. H4Y-28 grew from 2 to 8 points when the zone stories were merged into it.
- Hours check (warning only): sprint 3 finished 25 points with the same hours. No actual hours are logged in Jira, so this is an estimate.

## Committed points

20: 15 points of new stories and 5 points of carry-over.

## Stories

| Order | Key | Story | Points | Owner | Requirements | Sub-tasks |
|-------|-----|-------|--------|-------|--------------|-----------|
| 1 | H4Y-9 | Diagrama UML de Componentes (carry-over) | 3 | José Cruz | RNF-24 | - |
| 2 | H4Y-51 | Cadastrar item de stock (carry-over) | 2 | Tiago Borja | RF-28, RF-29 | H4Y-68 |
| 3 | H4Y-52 | Registar um novo cliente | 3 | Lukas Sperandio, José Cruz | RF-10 | H4Y-69, H4Y-70 |
| 4 | H4Y-54 | Consultar quartos livres por tipo e noites | 5 | Pedro Costa, Tiago Borja | RF-05 | H4Y-72, H4Y-73 |
| 5 | H4Y-53 | Procurar um cliente já registado | 2 | Lukas Sperandio | RF-10 | H4Y-71 |
| 6 | H4Y-56 | Criar uma reserva para um cliente | 5 | António Martinho | RF-11, RF-05 | H4Y-75, H4Y-76 |

Total: 20 points. The Team Leader set the points of H4Y-52, H4Y-53, H4Y-54 and H4Y-56 in Jira on 2026-10-31.

Scope change (Team Leader, 2026-10-31): H4Y-56 replaces H4Y-55. Reception cannot cancel a reservation before it can create one. H4Y-55 moves to sprint 5.

## Work split

The Team Leader split the work evenly (2026-10-31). Jira has points only on stories. The split of H4Y-52 (2 + 1) and H4Y-54 (3 + 2) is an estimate. Pedro has fewer points because H4Y-72 owns the date overlap rule, the highest risk of the sprint.

| Person | Work | Points |
|--------|------|--------|
| José Cruz | H4Y-9 (3), H4Y-70 (1) | 4 |
| Tiago Borja | H4Y-68 (2), H4Y-73 (2) | 4 |
| Lukas Sperandio | H4Y-69 (2), H4Y-71 (2) | 4 |
| Pedro Costa | H4Y-72 (3) | 3 |
| António Martinho | H4Y-75 (3), H4Y-76 (2) | 5 |

## Order inside the sprint

The sprint 3 retrospective asks for work with no dependency first.

1. Level 1: H4Y-9, H4Y-68, H4Y-69, H4Y-72.
2. Level 2: H4Y-70 and H4Y-71 (after H4Y-69), H4Y-73 and H4Y-75 (after H4Y-72).
3. Level 3: H4Y-76 (uses the client of H4Y-71, the room of H4Y-73 and the service of H4Y-75).
4. Checkpoint on 2026-11-03: H4Y-69, H4Y-72 and the shared SQL script are on `main`.

## Not selected

- H4Y-55, H4Y-57, H4Y-58, H4Y-59 (sprint 5): no points yet. H4Y-55 needs reservations from H4Y-56.
- Sprint 5 forecast: 11 points after the swap. Proposal for the sprint 5 planning: pull check-in (3) from sprint 6, for 14 points. Block-delete-after-check-in (2) stays in sprint 6, because it needs check-in first.
- H4Y-4 "Subtask 2.1": no parent, no sprint. It looks like Jira sample data. Deleting it needs an explicit OK (AGENTS.md rule 7).

## Risks

- `reservations` has a foreign key to `customers`, so H4Y-72 needs a table from H4Y-69. Lukas and Pedro merge one SQL script (`v7`) early. Until then, the tests use in-memory repositories.
- The class diagram has no repository for clients, reservations or items. Agree on the names on day 1 (AGENTS.md rule 11).
- H4Y-70 and H4Y-71 both change `client.fxml`. José merges the screen first. H4Y-68, H4Y-70, H4Y-73 and H4Y-76 add menu buttons. Keep these PRs small.
- H4Y-28 grew by 6 points during sprint 3 and the documents did not follow. Record each scope change when it happens.
- H4Y-9: the diagram looks wrong because Mermaid does not support some of the changes, and it has no output ports. PR #42 did not close this. The Jira card has the remaining work.
- Q-05 (cancel vs delete) is a team decision, not confirmed by the teacher. It affects H4Y-55 in sprint 5.
- H4Y-76 depends on three other sub-tasks and comes last. If the checkpoint fails, H4Y-51 leaves the sprint first, because it has no link to the goal. H4Y-53 cannot leave first, because H4Y-76 uses the client search.

## Next steps

1. Done (2026-10-31): sprint 3 is closed, sprint 4 started, H4Y-65 and H4Y-66 are Done, and the points and assignees are set.
2. In Jira: move H4Y-55 out of sprint 4, move H4Y-56 in, and update the sprint goal.

```
committed_points: 20
limit: 21
limit_source: velocity
over_limit: false
```

Created by: jmbcruz. Updated by: jmbcruz. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-31. Reviewed by: pending.
