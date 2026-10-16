# Git and Jira Conventions

## Git (from the course statement)

- Use Git flow.
- Branch name: `(feat|fix|chore)/H4Y-N-description`. N is the Jira key number, not zero-padded. Team decision (user, 2026-10-16).
- One branch per story or sub-task. The branch name carries the ticket number.
- Never commit to `main`. No exception. Always create a branch for the ticket, then open a pull request.
- Commits have clear and short descriptions.
  - Not allowed: "fix task X", "general fix".
- Wrong or careless use of the repository gets a penalty.
- Reference every AI use in docs and diagrams only. Never in source code.

### Branch cases

| Case | Jira issue | Branch |
|------|------------|--------|
| Story | H4Y-4 | `feat/H4Y-4-create-database` |
| Sub-task | H4Y-27 | `feat/H4Y-27-room-table-script` |
| Bug fix | H4Y-130 | `fix/H4Y-130-card-already-in-use` |
| Docs or chore work | H4Y-31 | `chore/H4Y-31-update-class-diagram` |

Docs and chore work use `chore/H4Y-N-...`. The statement (`enunciado.md`) allows only feat or fix. The team rule differs from the statement. This is a teacher question (see `enunciado.md`).

### Commit message rule

- Start with the Jira key: `H4Y-4 create room table`.
- Use short, imperative text. One change per commit.

### Commit and PR rule

Every agent and tool that writes a commit message, a PR title or a PR body follows this rule.

- Write in Simplified Technical English (STE): short sentences, simple words, active voice. Example: `H4Y-8 add check-in sequence diagram`.
- Never add an AI as co-author or author. Do not add a `Co-Authored-By` line. Do not add a "Generated with ..." line. Tool defaults that add them must be turned off or removed before the commit or PR is created.
- The AI-use reference stays inside the changed documentation files, never in source code (AGENTS.md rule 8). It does not go in the commit trailer.
- Check before you commit: `git log -1 --format=%B` has no `Co-Authored-By`. Check before you open the PR: the body has no "Generated with" line.

### Traceability

Requirement -> story -> test.

- A story lists its requirement IDs (RF-NN, RNF-NN) in `requirements`.
- Each acceptance scenario has an ID: AC-1, AC-2, and so on.
- Each test cites the story and scenario, for example in a JUnit `@DisplayName("H4Y-12 AC-1 check-in with valid reservation")`.
- `validate-done` checks that every AC has a test.

## Jira

- Project key: H4Y.
- Issue types: Epic > Story > Subtask (Jira name; docs say sub-task).
- Stories are in Portuguese (see `story-format.md`). All Jira text is in Portuguese (Portugal). Only code-specific terms stay in English: class, interface, method, package, table and column names, file names, tools and libraries, branch names, IDs and Jira keys (user decision 2026-10-16). Code and docs in the repository stay in English.
- Story points on stories (see `estimation.md`).
- Sprint length: 1 week.
- Suggested labels: `requirements`, `backend`, `gui`, `database`, `docs`, `tests`, `diagram`, `stock`, `access-control`, `reservation`, `blocked`.
- Acceptance criteria always go in the Jira description.

## Draft files

Agents write drafts first. A human approves. Only then `jira-publisher` publishes.

- Path: `.agent/drafts/<type>-<slug>.md`
  - Examples: `epic-room-management.md`, `story-check-in-magnetic-card.md`
- `<type>`: epic, story, subtask.
- `<slug>`: lowercase words joined by `-`.
- One canonical front-matter for all types:

```
---
type: epic | story | subtask
summary: <short title>
parent: <epic or story slug, or Jira key; empty for epics>
requirements: [RF-03]
points:            # EMPTY until the team decides
points_suggested:  # written by the estimator
status: draft | ready | approved | published
jira_key:          # empty until published, for example H4Y-12
labels: []
sprint:            # for example 3; empty until planned (written by the sprint planner)
branch:            # sub-task drafts only: (feat|fix|chore)/H4Y-N-description
---
```

- Status flow: `draft` -> `ready` (refiner, DoR met) -> `approved` (human only) -> `published` (after Jira create succeeds).
- After publish, write the Jira key in `jira_key`.

## Draft field to Jira field

| Draft field | Jira field |
|-------------|------------|
| type | Issue type: Epic, Story, Subtask (the Jira name has no hyphen) |
| summary | Summary |
| body | Description (story layout and all AC scenarios) |
| parent | `parent` system field, `{key: "H4Y-n"}` (epic for a story, story for a sub-task; no Epic Link field) |
| requirements | Description line "Requisitos: RF-03" |
| points | Story point estimate, `customfield_10016` (Story create screen) |
| labels | Labels, plus `draft-<slug>` |
| sprint | Sprint, `customfield_10020` (set in sync-board mode) |
| status | Board status (see below) |
| jira_key | Issue key (written back after create) |

## Board status mapping

| Draft status or event | Jira board status |
|--------------|-------------------|
| draft, ready, approved | not in Jira |
| published | To Do (backlog) |
| sprint started | In Progress |
| PR open | In Review |
| validated by Team Leader | Done |

Check real names with getTransitionsForJiraIssue before a transition.

## Idempotency

- On publish, add the label `draft-<slug>` to the issue.
- Before create, search by that label (JQL: `project = H4Y AND labels = "draft-<slug>"`).
- If an issue exists, do not create a second one. Write its key in `jira_key`.
- Never edit or delete an existing issue without explicit OK.
- Sync-board mode (points, sprint, transition, sub-tasks): show a dry-run table, ask confirmation per issue.

## Jira project facts (read-only lookup, 2026-10-15)

- Site: https://isep-team-p95gzvlc.atlassian.net, cloudId `39370aab-c7dc-4646-8b3c-de5de4443cdd`.
- Project H4Y, team-managed. Issue types: Epic, Story, Task, Subtask. Hierarchy: Epic > Story > Subtask.
- Required to create a Story: project, issue type, summary. The reporter has a default.
- Other fields: Rank `customfield_10019`, Flagged `customfield_10021` (option "Impediment"), Labels, Priority, Assignee, Due date.
- Statuses seen: To Do, In Progress. Confirm "In Review" and "Done" with getTransitionsForJiraIssue before the first transition.
- Existing issues H4Y-5 to H4Y-10 are Tasks for diagram work. Do not duplicate or change them.
