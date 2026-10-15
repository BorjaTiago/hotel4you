---
name: estimate-story
description: Estimate a story in story points. Use when a refined story needs an estimate.
---

# estimate-story

Inputs: the ready story draft slug. Ask the user to refine the story first if it is not ready.

Follow: .agent/agents/estimator.md

Output: .agent/drafts/story-<slug>.md (sets points_suggested only; points stay empty for the team)
