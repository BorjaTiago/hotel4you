# Definition of Done (DoD)

A story is done only when all items are true.

- [ ] All acceptance criteria are verified (Given/When/Then).
- [ ] The functional requirement from the statement is met.
- [ ] Non-functional rules are met (encrypted passwords, business logic separate from GUI, and so on).
- [ ] Unit tests (JUnit) exist for the main behavior and all pass.
- [ ] The code has Javadoc. The Javadoc builds without errors.
- [ ] Diagrams are updated (domain model, class, use case, sequence) when the change affects them.
- [ ] The pull request is reviewed and approved by at least one other member.
- [ ] The branch name is `(feat|fix)-NNN-description`.
- [ ] Commits are clear and short. No "fix task X" or "general fix".
- [ ] No unreferenced AI code or AI diagrams. Every AI use has a reference.
- [ ] Every new external library is justified in writing.
- [ ] The Team Leader tested and validated the story against the acceptance tests.
- [ ] The Jira issue is updated and moved to Done.
