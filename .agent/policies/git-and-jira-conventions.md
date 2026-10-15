# Git and Jira Conventions

## Git (from the course statement)

- Use Git flow.
- Branch name: `(feat|fix)-NNN-description`. NNN is the task number.
  - Example: `feat-004-create-database`
- Commits have clear and short descriptions.
  - Not allowed: "fix task X", "general fix".
- Wrong or careless use of the repository gets a penalty.
- Reference every AI use in code, docs and diagrams.

## Jira

- Project key: H4Y.
- Issue types: Epic > Story > Sub-task.
- Stories are in Portuguese (see `story-format.md`). Epics and sub-tasks follow the team language rule: code and docs in English.
- Estimates: Fibonacci story points on stories (see `estimation.md`).
- Sprint length: 1 week.
- Suggested labels: `requirements`, `backend`, `gui`, `database`, `docs`, `tests`, `diagram`, `stock`, `access-control`, `reservation`, `blocked`.
- Link the task number in the branch name to the Jira issue number (for example `H4Y-4` and `feat-004-...`).

## Draft files

Agents write drafts first. A human approves. Only then the `jira-publisher` agent publishes.

- Path: `.agent/drafts/<type>-<slug>.md`
  - Examples: `epic-room-management.md`, `story-check-in-magnetic-card.md`
- `<type>`: epic, story, subtask.
- `<slug>`: lowercase words joined by `-`.
- Front-matter:

```
---
type: epic | story | subtask
summary: <short title>
parent: <draft file name or Jira key, empty for epics>
points: <1|2|3|5|8, empty for epics>
status: draft | approved | published
jira_key: <empty until published, for example H4Y-12>
---
```

- Status flow: `draft` -> `approved` (human only) -> `published` (after the Jira create succeeds).
- After publish, write the Jira key in `jira_key`.
