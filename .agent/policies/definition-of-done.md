# Definition of Done (DoD)

A story is done only when all items are true. Each item needs evidence. `validate-done` lists the evidence. The Team Leader validates the story against the acceptance tests.

| Item | Evidence |
|------|----------|
| All acceptance criteria are verified (Given/When/Then). | Tests with `@Tag("H4Y-NN-AC-n")`, at least one per AC. |
| Code follows `docs/uml/` and `docs/database/` exactly: same classes, abstractions, signatures, tables and columns (R1). | Diagram files in the PR match the code. |
| Tests are written first and fail. Then the code makes them pass (R2). | In the PR, the test commit comes before the code commit. |
| Test names follow `on<Action>_<result>_<condition>` (R3). | Test method names, for example `onCreateUser_throw_whenUsernameExists`. |
| The functional requirement from the statement is met. | Requirement IDs in the story, linked to the tests. |
| Non-functional rules are met (encrypted passwords, business logic separate from GUI, and so on). | PR link and file or test that shows the rule. |
| Unit tests (JUnit) exist for the main behavior and all pass. | Test names and the test run result. |
| The code has Javadoc. The Javadoc builds without errors. | Javadoc build command and result. |
| Diagrams are updated (domain model, class, use case, sequence) when the change affects them. | Diagram file paths in the PR. |
| The pull request is reviewed and approved by at least one other member. | PR link and reviewer name. |
| The branch name is `(feat|fix|chore)/H4Y-N-description`. | Branch name. |
| Commits are clear and short, written in STE, and start with the Jira key. No AI co-author line in commits or PR. | Commit list. No "fix task X" or "general fix". No `Co-Authored-By`, no "Generated with". |
| No unreferenced AI docs or AI diagrams. Every AI use has a reference in the docs. | Footer lines or "no AI use". |
| No AI-use reference in source code (comment, header, annotation, file, name). Every doc footer has Created by and Updated by. | Grep result on `src` and the footer lines. |
| Every new external library is justified in writing. | Justification text or "no new library". |
| The Team Leader tested and validated the story against the acceptance tests. | Name of the Team Leader and date. |
| The Jira issue is up to date. It is "In Review" during validation and moves to Done after PASS. | Jira status. |

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-17. Reviewed by: pending.
