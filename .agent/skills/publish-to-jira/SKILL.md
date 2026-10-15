---
name: publish-to-jira
description: Publish approved drafts to Jira. Use when the user asks to push approved epics or stories to Jira.
---

# publish-to-jira

Needs `status: approved` and human confirmation. Never invent requirements outside the statement.

## When to use

- When approved drafts must go to Jira. This is the only skill that writes outside drafts.

## Steps

1. Read .agent/policies/git-and-jira-conventions.md, story-format.md and definition-of-ready.md.
2. Read .agent/context/project-context.md for the Jira project key.
3. Select drafts. Refuse any draft without `status: approved`.
4. Show the human the exact issues to create or update.
5. Wait for explicit human confirmation. Do not publish without it.
6. Create or update the issues. Write the Jira key back to the draft.
7. Report the keys created.

## Output

Jira issues. Updated draft front-matter with `jira_key`. Short publish report in .agent/drafts/publish-report.md.

## Checks

- Every published draft has status: approved.
- The human confirmed in this session.
- Content matches the draft. Nothing is added.
- Names follow the Jira conventions policy.

Delegates to agent: .agent/agents/jira-publisher.md
