# AGENTS.md - H4Y (Hotel4You) Agile Agents

This file is for any AI tool. It is plain Markdown.

## How to start

1. Read all files in `.agent/context/`.
   - `enunciado.md`: the course statement (source of truth).
   - `project-context.md`: facts sheet and team decisions.
   - `team.md`: roster and Team Leader rotation.
2. Read all files in `.agent/policies/`.
   - `definition-of-ready.md`, `definition-of-done.md`
   - `story-format.md`, `estimation.md`
   - `git-and-jira-conventions.md`
3. Pick an agent from `.agent/agents/` or a skill from `.agent/skills/`.
4. See `.agent/examples/` for one sample of each document. Samples are EXAMPLES, not real backlog.

## Pipeline

Run the steps in this order. Each step reads the file of the step before.

1. `requirements-analyst` (skill `requirements-analysis`): statement -> `docs/requirements/*.md`.
2. `epic-writer` (skill `write-epic`): requirements -> epic drafts.
3. `story-writer` (skill `write-story`): epic -> story drafts. `acceptance-criteria` (skill `write-acceptance-criteria`) writes the criteria inside the story body.
4. `refiner` (skill `refine-story`): checks the Definition of Ready, splits big stories.
5. `estimator` (skill `estimate-story`): writes `points_suggested`. The team sets `points`.
6. A human sets `status: approved`.
7. `jira-publisher` (skill `publish-to-jira`): drafts -> Jira. Sync-board mode after planning.
8. `sprint-planner` (skill `plan-sprint`): writes `docs/scrum/sprint-NN/planning.md`. `backlog-prioritizer` (skill `prioritize-backlog`) orders the backlog before it.
9. Sprint runs. `impediment-tracker` (skill `track-impediments`) keeps `impediments.md`.
10. `validate-done` (skill `validate-done`): checks the Definition of Done.
11. `impediment-tracker` in review and retro modes: skills `sprint-review` and `run-retro` write `review.md` and `retrospective.md`.

## Agents (`.agent/agents/`)

- `requirements-analyst`
- `epic-writer`
- `story-writer`
- `acceptance-criteria`
- `backlog-prioritizer`
- `refiner`
- `estimator`
- `sprint-planner`
- `impediment-tracker`
- `jira-publisher`
- `validate-done`

## Skills (`.agent/skills/`)

- `requirements-analysis`
- `write-epic`
- `write-story`
- `write-acceptance-criteria`
- `prioritize-backlog`
- `refine-story`
- `estimate-story`
- `plan-sprint`
- `track-impediments`
- `run-retro`
- `sprint-review`
- `validate-done`
- `publish-to-jira`

Skills are thin. They name the inputs and point to the agent file. Rules live in the policies.

## Global rules

1. Drafts first. Write local files in `.agent/drafts/`. Do not write to Jira directly.
2. Human approves. A person must set `status: approved` before any Jira publish.
3. Never invent requirements that are not in the statement. If unsure, list the question for the teacher in `enunciado.md` ("Ambiguities to ask the teacher") or `docs/requirements/questions.md`.
4. Points stay empty until the team decides. Agents only write `points_suggested`.
5. Criteria live only in the story body. No separate criteria or estimate files.
6. Every requirement has an ID (RF-NN or RNF-NN). Every story cites it. Every test cites "H4Y-NN AC-n".
7. Never edit or delete an existing Jira issue without explicit OK. No REST or curl fallback. No tokens in files.
8. Reference every AI use in code, docs and diagrams. Unreferenced AI use is forbidden by the course code of conduct. Tell the team.

## Output paths

| Output | Path |
|--------|------|
| Drafts (epic, story, subtask) | `.agent/drafts/<type>-<slug>.md` |
| Requirements | `docs/requirements/{requirements,actors,use-cases,glossary,questions}.md` |
| Sprint planning | `docs/scrum/sprint-NN/planning.md` |
| Sprint review | `docs/scrum/sprint-NN/review.md` |
| Retrospective | `docs/scrum/sprint-NN/retrospective.md` |
| Impediments and daily notes | `docs/scrum/sprint-NN/impediments.md` |
| Team roster and rotation | `.agent/context/team.md` |
| Examples | `.agent/examples/` |

## Portability

- Claude Code: `.claude/agents` and `.claude/skills` are symlinks to `.agent/agents` and `.agent/skills`. `CLAUDE.md` imports this file with `@AGENTS.md`.
  - On Windows, Git needs symlink support (`git config core.symlinks true`, Developer Mode or admin rights). If symlinks fail, copy `.agent/agents` and `.agent/skills` into `.claude/` and copy again after each change.
- Codex: reads `AGENTS.md`. It may look for skills in `.agents/skills` (unconfirmed). Fallback: tell Codex to read the file named in the pipeline above, for example "Read `.agent/agents/refiner.md` and refine `.agent/drafts/story-x.md`".
- Other tools: read this file, then the agent file for the step.
