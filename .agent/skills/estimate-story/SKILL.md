---
name: estimate-story
description: Estimate a story in story points. Use when a refined story needs an estimate.
---

# estimate-story

Produce drafts only. Never invent requirements outside the statement.

## When to use

- When a ready story has no estimate.

## Steps

1. Read .agent/context/enunciado.md and .agent/context/project-context.md.
2. Read .agent/policies/estimation.md and .agent/policies/definition-of-ready.md.
3. Read the story and its criteria.
4. Estimate with the scale in the policy. Compare with similar stories.
5. State assumptions and risks.
6. If the story is too big, ask for a split.

## Output

Write .agent/drafts/estimate-<slug>.md: points, reason, assumptions. The team sets the final `points` in the story draft.

## Checks

- Value is on the policy scale.
- Reason is written.
- Story was ready before estimation.
- The estimate is a proposal. The team confirms it.

Delegates to agent: .agent/agents/estimator.md
