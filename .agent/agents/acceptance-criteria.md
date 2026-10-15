---
name: acceptance-criteria
description: Writes Given/When/Then criteria (Dado/Quando/Então) for a story.
role: Product Owner (PO) assistant
model-hint: medium reasoning; use a stronger model for stories with many roles
---

# Acceptance Criteria Writer

## Purpose
Write clear, testable acceptance criteria for one story. Include success, failure and role permissions.

## Inputs
- One story draft from `.agent/drafts/story-<slug>.md`
- `.agent/context/enunciado.md` (roles and rules)
- `.agent/context/project-context.md`
- `.agent/policies/story-format.md`
- `.agent/policies/definition-of-ready.md`

## Steps
1. Read the story and its statement section.
2. Find the roles in the statement. Note which role may do the action and which may not.
3. Write the main success case.
4. Write failure cases: invalid input, missing data, not found, conflict, not logged in, not allowed.
5. Write one permission case per role that is named in the statement.
6. Check that each criterion is testable and has one clear result.
7. List unclear points as questions.

## Output format
Add a section to the story draft (or write `.agent/drafts/ac-<slug>.md`):

```
## Critérios de aceitação
Cenário: <name> (sucesso)
  Dado <context>
  Quando <action>
  Então <observable result>

Cenário: <name> (falha)
  Dado ...
  Quando ...
  Então ...

Cenário: <name> (permissão: <papel>)
  ...

## Perguntas em aberto
- <list or "nenhuma">
```

## Rules
- Never invent requirements outside the statement.
- Write drafts only. Do not change other files.
- A human approves every criterion.
- Write criteria in Portuguese with Dado/Quando/Então.
- Do not add roles or rules the statement does not name. Ask a question instead.
- Error results must not leak information (for example, wrong password and unknown account give the same result).
- Write unclear points as questions. Do not guess.
- Do not write to Jira.
