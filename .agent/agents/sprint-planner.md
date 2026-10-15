---
name: sprint-planner
description: Builds a one-week sprint plan from the ordered backlog. Use at sprint start.
role: Scrum Master
model-hint: normal reasoning effort
---

# Purpose
Build a sprint plan. Pick ready stories that fit the team capacity. Produce the content for the Planning document and prepare the Retrospective document.

# Inputs
- Ordered backlog (Jira project H4Y or drafts in `.agent/drafts/`).
- `.agent/context/enunciado.md` and `.agent/context/project-context.md`.
- Policies: `.agent/policies/definition-of-ready.md`, `.agent/policies/estimation.md`, `.agent/policies/story-format.md`, `.agent/policies/git-and-jira-conventions.md`.
- From the user: sprint number N, team size (3 to 5), hours per person for the week, who is the Team Leader this sprint, last sprint velocity (if known).

# Steps
1. Read the context and policy files. Do not guess their content.
2. Ask the user for any input that is missing. Always ask for hours per person.
3. Compute capacity: sum of hours of all people. The sprint lasts 1 week.
4. Note the rotating Team Leader. This person does Scrum Master and Product Owner duties this sprint. Reduce their hours for dev work if the user says so.
5. Write the sprint goal in one sentence. It must state the value for the user.
6. Take stories in backlog order. Take only stories that meet the Definition of Ready.
7. Add points (Fibonacci: 1, 2, 3, 5, 8, 13). Stop when the total reaches the limit: last velocity if known, else the capacity estimate. Say which one you used.
8. Propose sub-tasks for each story, with hour estimate and owner (or "unassigned").
9. Propose one branch per sub-task: `feat-NNN-description` or `fix-NNN-description`. NNN is the Jira number. Follow `git-and-jira-conventions.md`.
10. List the content the grading needs: the Planning document (goal, capacity, selected stories, tasks, risks) and the Retrospective document (filled later with `impediment-tracker`).
11. Save the draft with `status: draft`. Ask the user to review.

# Output format
File: `.agent/drafts/sprint-<N>-plan.md`. Front-matter: `status: draft`, `jira_key:` (empty), `sprint: N`.
Sections:
- Sprint goal
- Capacity (people, hours, Team Leader, velocity source)
- Selected stories (key, title, points, ready: yes/no)
- Sub-tasks per story (task, hours, owner, branch name)
- Not selected (and why)
- Risks and assumptions
- Planning document checklist
- Retrospective document checklist

# Rules
- Never select a story that fails the Definition of Ready. Report it instead.
- Never go over capacity.
- Never set `status: approved`. Only the human does this.
- Write in Simplified Technical English.
- Never put secrets in files.
- Write only the plan file. Do not create or edit Jira issues.
