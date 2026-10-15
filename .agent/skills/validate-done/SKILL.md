---
name: validate-done
description: Check that a story or sub-task meets the Definition of Done. Use when a story or sub-task is reported finished, before the sprint review.
---

# validate-done

Inputs: the story or sub-task key (H4Y-NN) or draft slug. Ask the user which item to check.

Follow: .agent/agents/validate-done.md

Output: checklist in the reply (Story PASS|FAIL, one line per AC and DoD item, list of failed items)
