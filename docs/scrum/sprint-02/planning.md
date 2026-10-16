# Sprint 2 Planning

- Dates: 2026-10-19 to 2026-10-25 (1 week).
- Team Leader (Scrum Master and Product Owner): Tiago Borja (member 4, `team.md`). He also develops.
- Status: draft. The team reviews it in the planning meeting.

## Goal

The first manager logs in and creates staff accounts. A receptionist logs in and sees the main window with the functions of the role.

## Capacity

| Item | Value |
|------|-------|
| People | 5 developers: Lukas Sperandio, Pedro Costa, Antonio Martinho, Tiago Borja (Team Leader), José Cruz |
| Hours per person | NOT GIVEN. The Team Leader must write them here. The check below waits for them. |
| Limit | 13 points |
| Limit source | forecast. The 13 points come from the approved `backlog-order.md` (guess, no velocity history). The team confirms or changes it in the meeting. |
| Sanity check with hours | Pending. Hours are a warning only. They never set the commitment. |

Technical tasks have no points. They take team time on top of the 13 points. This is the main risk (see Risks).

## Committed points

13 of 13 points.

## Stories

| Key | Story (draft) | Points | Proposed owner | Requirements |
|-----|---------------|--------|----------------|--------------|
| H4Y-24 | janela-principal | 3 | Pedro Costa | RNF-06, RNF-07 |
| H4Y-22 | login-rececionista | 5 | Antonio Martinho (service, tests), José Cruz (screen) | RF-01, RF-02 |
| H4Y-20 | login-gestor | 2 | José Cruz | RF-01, RF-02 |
| H4Y-27 | create-staff-account | 3 | Tiago Borja | RF-26, RF-01 |

Total: 3 + 5 + 2 + 3 = 13 points. All four stories are `published` with points set.

The owners are a proposal. The team decides.

## Technical tasks (no points)

| Task | Parent | Sub-task draft | Proposed owner |
|------|--------|----------------|----------------|
| Foundation T0a skeleton, domain, interfaces | H4Y-11 | `subtask-s02-foundation-t0-skeleton-core` | Lukas Sperandio |
| Foundation T0b GUI shell, stubs, class diagram | H4Y-11 | `subtask-s02-foundation-t0-gui-shell-diagram` | Pedro Costa |
| Foundation T2 SQL Server and schema | H4Y-11 | `subtask-s02-foundation-t2-sqlserver-schema` | Lukas Sperandio |
| Foundation T4 test data, T1 layer check | H4Y-11 | `subtask-s02-foundation-t4-test-data` | José Cruz |
| Auth T1 encrypted passwords | H4Y-12 | `subtask-s02-auth-t1-password-hash` | Antonio Martinho |
| Auth T2 seed (5 roles, first manager) | H4Y-12 | `subtask-s02-auth-t2-seed-roles-manager` | Antonio Martinho |
| Quality T1, T2 Javadoc and JUnit setup | H4Y-18 | `subtask-s02-quality-junit-javadoc-setup` | Pedro Costa |
| Quality T3, T4, T5 libraries, Git rule, AI use | H4Y-18 | `subtask-s02-quality-libraries-git-ai-rules` | Tiago Borja |
| Scrum T1, T2, T5 planning, daily, refinement | H4Y-17 | `subtask-s02-scrum-planning-daily-notes` | Tiago Borja |
| Scrum T3, T4, T6, T7, T8 and quality T6 | H4Y-17 | `subtask-s02-scrum-review-retro-validation` | Tiago Borja |

Story sub-task drafts (19 files in total with the table above): `subtask-s02-janela-principal-*` (2), `subtask-s02-login-rececionista-*` (3), `subtask-s02-login-gestor` (1), `subtask-s02-create-staff-account-*` (3).

## Task order (no card blocks another)

Rule: only T0a is a gate. Every other card starts from the stubs and test data, and builds and tests alone.

1. Days 1 to 2: Lukas does T0a. He merges the core interfaces, domain classes and in-memory `UserRepository` first, by end of day 2. The others read the docs, set up their tools, and write tests against the interface names. Pedro starts the build file for the quality setup.
2. After T0a (days 2 to 4), in parallel with no shared files:
   - Lukas: T2 database.
   - Antonio: auth T1 hash, then auth T2 seed. T2 seed needs T2 database and T1 hash.
   - Pedro: T0b GUI shell and stubs, then janela-principal, then JUnit and Javadoc setup.
   - José: T4 test data, then the login screen.
   - Tiago: create-staff-account, plus the docs cards.
3. Login chain inside the sprint (the only hard story link): login-rececionista service (Antonio) then login-gestor (José). login-gestor reuses the same code. Start it when the service is merged.
4. The logins test with T4 test users. They do not wait for the seed. The end-to-end check with the seed manager runs at the end of the sprint.
5. Days 5 to 7: tests, Javadoc, diagrams, merge of PRs, manual end-to-end check, review, retrospective.

If T0a or T2 slips: move create-staff-account to the start of sprint 3 (`backlog-order.md` risk). The manager can still log in.

## Risks

- Sprint 2 has 13 points plus 10 technical tasks. The velocity is a guess. If the team cannot finish, the Team Leader tells the PO. The cut order is: create-staff-account to sprint 3, then the diagram and docs parts.
- T0a is a single gate. If it is late, every card waits. Mitigation: merge the interfaces first, then the rest.
- First JavaFX use (janela-principal, login screen). Keep the screens simple.
- Database setup: SQL Server access for 5 machines. Check the merged PR #4 data (5 roles, Q-46). Report gaps early.
- The first manager password must never be in Git, docs or logs. Use local config or environment values only.
- Hours are not given. The sanity check is not done.
- Jira: the technical sub-tasks have an epic as parent (H4Y-11, 12, 17, 18). In Jira, a Subtask cannot have an epic as parent. The team decides before `sync-board`: create them as `Task` issues under the epic, or put them under one story. This planning does not decide.
- Branch names: the sub-task Jira keys do not exist yet. Technical sub-tasks use `feat-NNN-...`. Write the real number after publish. Story sub-tasks use the story key number (for example `feat-022-...`). The team may change them after publish.
- Story drafts are not edited here (instruction of the Team Leader). `sprint: 02` is not set in the four story drafts. Set it, or let `jira-publisher` sync it.
- Q-15 (AI reference format) is still open. Use the "AI use:" line until the answer.

## Definition of Done reminder

A story is done only with all items in `.agent/policies/definition-of-done.md`:

- Every AC has a test that cites "H4Y-NN AC-n".
- Requirements and non-functional rules are met (passwords encrypted, logic separate from GUI).
- JUnit tests pass. Javadoc builds.
- Diagrams are updated when the change affects them.
- PR reviewed by at least one other member.
- Branch name `(feat|fix)-NNN-description`. Commits are short, in STE, start with the Jira key. No AI co-author line.
- Every AI use has a reference. Every new library is justified in `docs/libraries.md`.
- The Team Leader tests and validates against the acceptance tests. The Jira issue moves to Done.

## Not selected

Sprint 3 and later stories are not selected. They are in `backlog-order.md` for their sprint. `login-restaurante` (H4Y-21) moves to sprint 3 on purpose, to keep sprint 2 at 13 points.

## Next step

1. The team reviews this plan and the 19 sub-task drafts. Change owners and text if needed.
2. A human approves the sub-task drafts (`status: approved`). The agent does not set it.
3. Then run `jira-publisher` in mode `sync-board` (points, sprint, sub-tasks). Decide the parent for the technical tasks first.

```
committed_points: 13
limit: 13
limit_source: forecast
over_limit: false
```

Note: the Jira text (epics, stories, sub-tasks) is in Portuguese (user decision 2026-10-16).

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Note line by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
