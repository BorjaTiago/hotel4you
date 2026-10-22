# Story Format

This is the single canonical story layout. Stories are written in Portuguese. Policies and notes are in English. Epics and sub-tasks use the same front-matter (see `git-and-jira-conventions.md`).

## Template

```
---
type: story
summary: <verbo + objeto, curto>
parent: <epic slug or key>
requirements: [RF-NN]
points:            # EMPTY until the team decides
points_suggested:  # written by the estimator
status: draft
jira_key:
labels: []
sprint:
---

# Título: <verbo + objeto, curto>

Como <papel>,
Eu quero <ação>,
Para que <benefício>.

## Critérios de aceite

### Cenário AC-1: <nome do cenário>
  Dado <contexto inicial>
  E <contexto adicional, opcional>
  Quando <ação do ator>
  Então <resultado observável>
  E <resultado adicional, opcional>

### Cenário AC-2: <nome>
  ...

## Critérios de aceite (checklist)

- [ ] AC-1: <resultado verificável, curto, em português>
- [ ] AC-2: <...>

## Notas
<dependências, perguntas (Q-NN), referência ao enunciado>

Created by: <GitHub name>. Updated by: <GitHub name>. AI use: <agent name>, <AI tool and model>, <YYYY-MM-DD>. Reviewed by: <name or pending>.
```

## Checklist rule (user decision, 2026-10-16)

- Every story keeps the Dado/Quando/Então scenarios (AC-n) as detail. It also has the section `## Critérios de aceite (checklist)`.
- The checklist has one line `- [ ] AC-n: ...` per AC, in Portuguese. Each line is short and can be ticked.
- The checklist goes to the Jira description as a checklist. The doer ticks each line when done.
- AC-n IDs must match in three places: the scenario heading (`### Cenário AC-n`), the checklist line (`- [ ] AC-n`), and the test tag (`@Tag("H4Y-NN-AC-n")`). Same number, same meaning.
- Technical tasks (epic section "Tarefas técnicas", sub-tasks) use the same format: one `- [ ]` line per criterion.

## Rules

- One actor per story. Allowed actors: cliente, rececionista, gestor, funcionário do restaurante, fornecedor.
- Size 8 points or less. If larger, split the story (see `estimation.md`).
- Criteria live only in the story body. No separate criteria or estimate file.
- Number scenarios AC-1, AC-2, and so on. Each test has a repeatable JUnit tag `@Tag("H4Y-NN-AC-n")`, for example `@Tag("H4Y-32-AC-8")`. A later card that changes the test adds one more tag. JUnit tags cannot contain spaces. To find the tests of an AC: `grep -rn '@Tag("H4Y-32-AC-8")' src/test`. The AC is not in the method name.
- Write at least one success scenario and one failure scenario.
- Use only rules from the statement. Do not invent requirements. Link the requirement IDs.
- Criteria use Gherkin keywords in Portuguese: Dado, Quando, Então (E, Mas for extra steps). Add the checklist section (see "Checklist rule"). Gherkin alone is not enough.
- Do not describe the design (tables, classes, screens) in the story.
- Leave `points` empty. The team writes it after estimation.

## Full example

```
---
type: story
summary: Realizar check-in com cartão magnético
parent: epic-card-access
requirements: [RF-12, RF-13]
points:
points_suggested: 5
status: ready
jira_key:
labels: [reservation, access-control]
sprint:
---

# Título: Realizar check-in com cartão magnético

Como rececionista,
Eu quero efetuar o check-in de um cliente com reserva e atribuir-lhe um cartão magnético,
Para que o cliente aceda ao seu quarto e aos locais incluídos na reserva.

## Critérios de aceite

### Cenário AC-1: Check-in com reserva válida
  Dado que existe uma reserva ativa para o quarto 202 sem check-in efetuado
  E existe um cartão magnético livre com o número 12345
  Quando o rececionista efetua o check-in e associa o cartão 12345 à reserva
  Então o estado da reserva passa a "check-in efetuado"
  E o cartão 12345 fica associado ao quarto 202
  E o cartão 12345 dá acesso ao restaurante e à piscina apenas se estes constarem da reserva

### Cenário AC-2: Check-in sem reserva
  Dado que não existe reserva ativa para o cliente
  Quando o rececionista tenta efetuar o check-in
  Então o sistema recusa o check-in
  E mostra uma mensagem a indicar que não existe reserva

### Cenário AC-3: Cartão já em uso
  Dado que o cartão 12345 está associado a outra reserva
  Quando o rececionista tenta associar o cartão 12345
  Então o sistema recusa a associação
  E o estado da reserva não muda

## Critérios de aceite (checklist)

- [ ] AC-1: Check-in com reserva válida associa o cartão e muda o estado
- [ ] AC-2: Check-in sem reserva é recusado com mensagem
- [ ] AC-3: Cartão já em uso é recusado e a reserva não muda

## Notas
Depende das histórias "Registar cliente" e "Criar reserva". Enunciado, secção 2 (cartão magnético).
AC-3 depende da resposta à pergunta Q-09 (regras do cartão). Se a resposta mudar a regra, atualizar AC-3.

Created by: <GitHub name>. Updated by: <GitHub name>. AI use: story-writer, acceptance-criteria, <AI tool and model>, <YYYY-MM-DD>. Reviewed by: pending.
```

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-17. Reviewed by: pending.
