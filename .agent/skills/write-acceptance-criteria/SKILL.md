---
name: write-acceptance-criteria
description: Write acceptance criteria for a story. Use when a story needs testable criteria.
---

# write-acceptance-criteria

Produce drafts only. Never invent requirements outside the statement.

## When to use

- When a story has no criteria or weak criteria.

## Steps

1. Read .agent/context/enunciado.md and .agent/context/project-context.md.
2. Read .agent/policies/story-format.md and .agent/policies/definition-of-ready.md.
3. Read the story draft.
4. Write criteria in Given/When/Then (Dado/Quando/Então, in Portuguese). One behavior per criterion.
5. Add error and edge cases only if the statement supports them.
6. Flag gaps as open questions.

## Output

Add a `Critérios de aceitação` section to .agent/drafts/story-<slug>.md, or write .agent/drafts/ac-<slug>.md.

## Checks

- Each criterion is testable and unambiguous.
- Each criterion traces to the statement.
- Failure and role-permission cases are covered when the statement supports them.
- No invented behavior.

Delegates to agent: .agent/agents/acceptance-criteria.md
