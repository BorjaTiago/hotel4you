# Team

The planner and `validate-done` read this file. Fill the placeholders. Team size is 3 to 5.

## Roster

| # | Name | GitHub | Role skills |
|---|------|--------|-------------|
| 1 | <name> | <github-user> | <for example: backend, SQL Server> |
| 2 | <name> | <github-user> | <for example: JavaFX GUI> |
| 3 | <name> | <github-user> | <for example: docs, UML> |
| 4 | <name or empty> | <github-user> | <skills> |
| 5 | <name or empty> | <github-user> | <skills> |

## Team Leader rotation

Team Leader = Scrum Master + Product Owner. The role changes every sprint. Sprint 1 has no roles.

Rule: nobody leads twice before everyone has led once. After a full round, start a new round in the same order. Use members 1 to N, with N = team size.

| Sprint | Team Leader (member #) | Name |
|--------|------------------------|------|
| 1 | none (no roles) | - |
| 2 | 1 | <name> |
| 3 | 2 | <name> |
| 4 | 3 | <name> |
| 5 | ((5-2) mod N)+1 | <name> |
| 6 | ((6-2) mod N)+1 | <name> |
| 7 | next in the order | <name> |
| 8 | next in the order | <name> |
| 9 | next in the order | <name> |
| 10 | next in the order | <name> |
| 11 | next in the order | <name> |
| 12 | next in the order | <name> |
| 13 | next in the order | <name> |

General formula for sprint S (2 to 13): leader = member ((S - 2) mod N) + 1. The team may swap two sprints only if the rule still holds. Write each change here.
