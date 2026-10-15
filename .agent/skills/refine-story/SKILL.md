---
name: refine-story
description: Refine a story until it is ready. Use when a story fails the definition of ready or is too big.
---

# refine-story

Produce drafts only. Never invent requirements outside the statement.

## When to use

- When a story is vague, big, or not ready.

## Steps

1. Read .agent/context/enunciado.md and .agent/context/project-context.md.
2. Read .agent/policies/definition-of-ready.md and .agent/policies/story-format.md.
3. Read the story draft.
4. Check each ready rule. List the failures.
5. Split big stories. Clarify vague text. Keep the original intent.
6. List open questions for the human.

## Output

Write .agent/drafts/refinement-<slug>.md. Do not rewrite the story without saying what changed.

## Checks

- Story passes definition-of-ready.md or lists what blocks it.
- No new requirement outside the statement.
- Split stories keep a link to the parent.
- Status stays draft.

Delegates to agent: .agent/agents/refiner.md
