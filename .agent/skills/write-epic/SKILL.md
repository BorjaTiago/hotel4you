---
name: write-epic
description: Draft an epic from the statement. Use when the user asks to create or draft an epic.
---

# write-epic

Produce drafts only. Never invent requirements outside the statement.

## When to use

- When the user asks for a new epic or an epic split.

## Steps

1. Read .agent/context/enunciado.md and .agent/context/project-context.md.
2. Read .agent/policies/story-format.md and .agent/policies/definition-of-ready.md.
3. Find the statement scope the epic covers. Cite the source section.
4. Write title, goal, business value, scope, out of scope, and list of candidate stories (titles only).
5. Set `status: draft` in the front-matter.

## Output

.agent/drafts/epic-<slug>.md. Markdown with the front-matter in git-and-jira-conventions.md (status: draft).

## Checks

- Every item traces to the statement.
- No invented requirement.
- Out of scope is explicit.
- Status is draft.

Delegates to agent: .agent/agents/epic-writer.md
