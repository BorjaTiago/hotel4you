---
name: track-impediments
description: Record and follow impediments. Use when the user reports a blocker or asks for impediment status.
---

# track-impediments

Produce drafts only. Never invent requirements outside the statement.

## When to use

- When a blocker appears or the user asks for blocker status.

## Steps

1. Read .agent/context/project-context.md.
2. Read .agent/policies/definition-of-done.md and git-and-jira-conventions.md.
3. Read the current impediments file if it exists.
4. Add new impediments: description, affected stories, impact, owner, date.
5. Update status of old ones. Propose next actions.
6. Do not decide for the team.

## Output

.agent/drafts/sprint-<N>-impediments.md. Table with id, description, impact, owner, status, next action.

## Checks

- Each item has an owner and a date.
- Affected stories are linked.
- Resolved items keep their history.
- Facts only. No guesses.

Delegates to agent: .agent/agents/impediment-tracker.md
