---
name: publish-to-jira
description: Publish approved drafts to Jira. Use when the user asks to push approved epics or stories to Jira, or to sync the board for a sprint.
---

# publish-to-jira

Inputs: mode (publish or sync-board), draft slugs with status approved, and sprint NN for sync-board. Ask the user to confirm each issue. Refuse drafts without `status: approved`.

Follow: .agent/agents/jira-publisher.md

Output: Jira issues (project H4Y), jira_key and status in draft front-matter, result lines and counts in the reply
