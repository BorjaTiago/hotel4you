> EXAMPLE only. Not real sprint data.

# Sprint 3 Planning (EXAMPLE)

- Dates: 2026-10-26 to 2026-11-01
- Team Leader: Member 2 (see `.agent/context/team.md`)
- Limit: 12 points (velocity of sprint 2, limit_source: velocity)
- Committed: 11 points (hours only as a sanity check: 3 people x about 8 h)

## Sprint goal
The receptionist can register a client and create a reservation.

## Committed stories

| Key | Story | Points | Owner | Requirements |
|-----|-------|--------|-------|--------------|
| H4Y-12 | Registar cliente | 3 | Member 1 | RF-03 |
| H4Y-13 | Criar reserva | 5 | Member 2 | RF-05 |
| H4Y-14 | Listar quartos por tipo | 2 | Member 3 | RF-04 |
| H4Y-15 | Mostrar utilizador autenticado | 1 | Member 3 | RF-01 |

Total: 11 points.

## Not selected
- H4Y-16 Cancelar reserva (3 points): over the limit.

## Dependencies and risks
- H4Y-13 needs H4Y-12.
- Risk: SQL Server access for all members.

## Definition of Done reminder
See `.agent/policies/definition-of-done.md`.

```
committed_points: 11
limit: 12
limit_source: velocity
over_limit: false
```

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, <AI tool and model>, 2026-10-26. Reviewed by: pending.
