# Definition of Done (DoD)

A story is done only when all items are true. Each item needs evidence. `validate-done` lists the evidence. The Team Leader validates the story against the acceptance tests.

| Item | Evidence |
|------|----------|
| All acceptance criteria are verified (Given/When/Then). | Test names that cite "H4Y-NN AC-n", one per AC. |
| The functional requirement from the statement is met. | Requirement IDs in the story, linked to the tests. |
| Non-functional rules are met (encrypted passwords, business logic separate from GUI, and so on). | PR link and file or test that shows the rule. |
| Unit tests (JUnit) exist for the main behavior and all pass. | Test names and the test run result. |
| The code has Javadoc. The Javadoc builds without errors. | Javadoc build command and result. |
| Diagrams are updated (domain model, class, use case, sequence) when the change affects them. | Diagram file paths in the PR. |
| The pull request is reviewed and approved by at least one other member. | PR link and reviewer name. |
| The branch name is `(feat|fix)-NNN-description`. | Branch name. |
| Commits are clear and short, and start with the Jira key. | Commit list. No "fix task X" or "general fix". |
| No unreferenced AI code or AI diagrams. Every AI use has a reference. | Reference list or "no AI use". |
| Every new external library is justified in writing. | Justification text or "no new library". |
| The Team Leader tested and validated the story against the acceptance tests. | Name of the Team Leader and date. |
| The Jira issue is up to date. It is "In Review" during validation and moves to Done after PASS. | Jira status. |
