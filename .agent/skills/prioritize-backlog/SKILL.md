---
name: prioritize-backlog
description: Order the backlog by value and dependency. Use when the user asks to prioritize or reorder the backlog.
---

# prioritize-backlog

Produce drafts only. Never invent requirements outside the statement.

## When to use

- When the user asks to rank or reorder stories.

## Steps

1. Read .agent/context/enunciado.md and .agent/context/project-context.md.
2. Read .agent/policies/definition-of-ready.md and .agent/policies/estimation.md.
3. List all epic and story drafts.
4. Order by business value, dependency, and risk. Give a short reason for each.
5. List dependencies between stories.
6. Do not add or remove stories.

## Output

.agent/drafts/backlog-order.md. Ordered list with rank, id, reason, dependencies.

## Checks

- Dependencies come before dependents.
- Every story appears once.
- Each reason cites the statement or a dependency.
- Status is draft.

Delegates to agent: .agent/agents/backlog-prioritizer.md
