---
name: write-story
description: Draft a user story. Use when the user asks to write a story from an epic or statement section.
---

# write-story

Produce drafts only. Never invent requirements outside the statement.

## When to use

- When the user asks for a story under an epic.

## Steps

1. Read .agent/context/enunciado.md and .agent/context/project-context.md.
2. Read .agent/policies/story-format.md and .agent/policies/definition-of-ready.md.
3. Pick the parent epic draft. Stop if none exists and ask the user.
4. Write the story in the format of the policy. Add the source reference.
5. Mark open questions. Do not guess answers.
6. Set `status: draft`.

## Output

.agent/drafts/story-<slug>.md. Markdown with the front-matter in git-and-jira-conventions.md (parent = epic draft, status: draft).

## Checks

- Format matches story-format.md.
- Story is small and independent.
- No requirement outside the statement.
- Status is draft.

Delegates to agent: .agent/agents/story-writer.md
