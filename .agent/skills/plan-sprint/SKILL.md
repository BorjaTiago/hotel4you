---
name: plan-sprint
description: Plan a sprint from the ready backlog. Use when the user asks for a sprint plan or sprint goal.
---

# plan-sprint

Produce drafts only. Never invent requirements outside the statement.

## When to use

- When the user asks to plan or fill a sprint.

## Steps

1. Read .agent/context/enunciado.md and .agent/context/project-context.md.
2. Read .agent/policies/definition-of-ready.md, estimation.md and git-and-jira-conventions.md.
3. The sprint lasts 1 week. Ask the human for team size (3 to 5) and capacity if unknown.
4. Take ready, estimated stories in backlog order.
5. Fill up to capacity. Respect dependencies.
6. Write a sprint goal and a risk list.

## Output

.agent/drafts/sprint-<N>-plan.md. Goal, capacity, story list with points, risks.

## Checks

- Total points do not exceed capacity.
- Only ready and estimated stories are included.
- Dependencies are respected.
- Status is draft.

Delegates to agent: .agent/agents/sprint-planner.md
