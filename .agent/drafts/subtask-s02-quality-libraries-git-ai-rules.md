---
type: subtask
summary: Create docs/libraries.md and write the branch, commit and AI-use rules
parent: H4Y-18
requirements: [RNF-11, RNF-20, RNF-21, RNF-25]
points:
points_suggested:
status: published
jira_key: H4Y-36
labels: [quality, docs, 1st-delivery]
sprint: 02
branch: feat-036-libraries-git-ai-rules
---

# Engineering quality T3, T4 and T5: libraries, Git rule, AI-use reference

Source: `epic-engineering-quality.md`, tasks T3, T4 and T5. Docs only. No dependency on other cards. Q-15 (AI reference format) is still open.
Proposed owner: Tiago Borja, Team Leader (the team decides).

## Checklist

- [ ] `docs/libraries.md` exists with columns: name, version, reason, rejected alternative (RNF-11).
- [ ] A short team guide states the branch rule `(feat|fix)-NNN-description` and the commit rule (start with the Jira key, STE, no AI co-author line). Link `git-and-jira-conventions.md`.
- [ ] A PR check list item asks for a valid branch name and clear commits.
- [ ] The AI-use rule is written for the team: the line "AI use: ..." ends each file made with AI (RNF-25). Diagrams have a reference too.
- [ ] Ask the teacher or the team to close Q-15, or note the team format used until then.
- [ ] Every team member reads the rules. The Team Leader notes it in the daily note.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
