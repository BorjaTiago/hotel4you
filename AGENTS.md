# AGENTS.md - H4Y (Hotel4You) Agile Agents

This file is for any AI tool. It is plain Markdown.

## How to start

1. Read all files in `.agent/context/`.
   - `enunciado.md`: the course statement (source of truth).
   - `project-context.md`: facts sheet and team decisions.
2. Read all files in `.agent/policies/`.
   - `definition-of-ready.md`, `definition-of-done.md`
   - `story-format.md`, `estimation.md`
   - `git-and-jira-conventions.md`
3. Pick an agent from `.agent/agents/` or a skill from `.agent/skills/`.

## Agents (`.agent/agents/`)

- `epic-writer`
- `story-writer`
- `acceptance-criteria`
- `backlog-prioritizer`
- `refiner`
- `estimator`
- `sprint-planner`
- `impediment-tracker`
- `jira-publisher`

## Skills (`.agent/skills/`)

- `write-epic`
- `write-story`
- `write-acceptance-criteria`
- `prioritize-backlog`
- `refine-story`
- `estimate-story`
- `plan-sprint`
- `track-impediments`
- `publish-to-jira`

## Global rules

1. Drafts first. Write local files in `.agent/drafts/`. Do not write to Jira directly.
2. Human approves. A person must set `status: approved` before any Jira publish.
3. Never invent requirements that are not in the statement. If unsure, list the question for the teacher.

Also: if AI output goes into code or diagrams, tell the team to reference the AI use. Unreferenced AI use is forbidden by the course code of conduct.
