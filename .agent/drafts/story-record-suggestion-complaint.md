---
type: story
summary: Registar uma sugestão ou reclamação de um cliente
parent: epic-guest-support
requirements: [RF-27]
points: 2
points_suggested: 2
status: approved
jira_key:
labels: [guest-support]
sprint:
---

# Título: Registar uma sugestão ou reclamação de um cliente

Como rececionista,
Eu quero registar na aplicação uma sugestão ou reclamação de um cliente,
Para que o gestor a receba.

## Critérios de aceite

### Cenário AC-1: Sugestão registada
  Dado que o rececionista tem sessão iniciada
  Quando registar uma sugestão com texto e o nome do cliente
  Então a sugestão fica guardada com a data e hora e o tipo "sugestão"
  E fica disponível para o gestor

### Cenário AC-2: Reclamação registada
  Dado que o rececionista tem sessão iniciada
  Quando registar uma reclamação com texto
  Então a reclamação fica guardada com a data e hora e o tipo "reclamação"
  E fica disponível para o gestor

### Cenário AC-3: Texto vazio
  Dado que o rececionista tem sessão iniciada
  Quando tentar registar uma sugestão ou reclamação sem texto
  Então o sistema recusa o registo
  E mostra uma mensagem a pedir o texto

### Cenário AC-4: Funcionário do restaurante
  Dado que o utilizador autenticado é funcionário do restaurante
  Quando tentar registar uma sugestão ou reclamação
  Então o sistema recusa a ação

## Critérios de aceite (checklist)

- [ ] AC-1: A sugestão com texto e nome do cliente fica guardada com data e hora e tipo "sugestão", e o gestor consegue vê-la.
- [ ] AC-2: A reclamação com texto fica guardada com data e hora e tipo "reclamação", e o gestor consegue vê-la.
- [ ] AC-3: Um registo sem texto é recusado com uma mensagem a pedir o texto.
- [ ] AC-4: O funcionário do restaurante não consegue registar sugestões nem reclamações.

## Notas
Enunciado, secção 2. RF-27. Q-18: a receção regista na aplicação. O cliente não tem login (Q-13). Fase: Q-25.
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: sessões simuladas de rececionista e restaurante (foundation T0), base de teste (T4).

points_suggested: 2
reference: Registar um cliente (3)
compared: smaller
reasoning: Formulário de texto e tipo, grava com data e hora. Poucas validações (texto vazio, perfil). Menor que registar um cliente, que tem mais campos.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
