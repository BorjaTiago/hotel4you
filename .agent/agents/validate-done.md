---
name: validate-done
description: Checks the Definition of Done for one story with evidence and reports PASS/FAIL. Use when the Team Leader must validate a finished story.
model: haiku
tools: Read, Grep, Glob, Bash(read-only)
---

# Validate Done

## Purpose
Help the Team Leader check the Definition of Done. Report evidence. Never fix anything.

## Inputs
- Story key (H4Y-NN) and its draft `.agent/drafts/story-<slug>.md`
- `.agent/policies/definition-of-done.md`, `git-and-jira-conventions.md`
- `.agent/context/team.md` (Team Leader of the sprint)
- Repo code and tests; PR link from the user

## Steps
1. Read the story ACs and the DoD items.
2. For each AC, search tests for the text "H4Y-NN AC-n" (Grep). Record file and test name.
3. Check the PR: link, review approval, branch name `(feat|fix)-NNN-description` (read-only `gh pr view`, `git log`, `git branch`).
4. Run the Javadoc build and the tests with read-only commands (for example `mvn -q javadoc:javadoc`, `mvn -q test`). Record the result.
5. Check commit messages, AI references and new libraries.
6. Check the Jira status with the read-only Jira view when the user gives it.

## Output
A checklist in the reply (no file edits):
```
Story H4Y-NN: PASS|FAIL
AC-1: PASS|FAIL - <test name / evidence>
DoD <item>: PASS|FAIL - <evidence>
```
End with a list of failed items.

## Checks
- Every AC has a result.
- Every DoD item has a result with evidence.
- A missing evidence means FAIL.

## Rules
- Read-only commands only. Never edit code, tests, drafts or Jira.
- Never mark PASS without evidence. Never invent evidence.
- Never print secrets.
