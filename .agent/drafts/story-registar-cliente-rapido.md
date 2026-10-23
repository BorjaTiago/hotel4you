---
type: story
summary: Registar cliente e reserva em 30 segundos
parent: epic-client-registration
requirements: [RNF-03, RF-10]
points:
points_suggested: 3
status: published
jira_key: H4Y-59
labels: [clients, 1st-delivery, usability]
sprint: 05
---

# Título: Registar cliente e reserva em 30 segundos

Como rececionista,
Eu quero registar um cliente e fazer a sua reserva em formulário curto,
Para que o atendimento demore no máximo 30 segundos.

## Critérios de aceite

### Cenário AC-1: Registo e reserva dentro do limite
  Dado que o rececionista tem sessão iniciada
  E existe um quarto livre
  Quando o rececionista abre o registo de cliente, regista um novo cliente e conclui a reserva
  Então o tempo desde a abertura do registo de cliente até à reserva criada é 30 segundos ou menos
  E o tempo de escolha do quarto não conta

### Cenário AC-2: Formulário só com o necessário
  Dado que o rececionista inicia o registo
  Quando o sistema mostra o formulário
  Então o formulário pede apenas os dados obrigatórios do cliente
  E não pede dados opcionais antes de concluir

### Cenário AC-3: Erro de preenchimento corrigido sem repetir
  Dado que o rececionista preencheu o formulário com um dado inválido
  Quando o rececionista confirma
  Então o sistema indica o dado inválido
  E mantém os restantes dados já preenchidos

## Critérios de aceite (checklist)

- [ ] AC-1: O cliente fica registado e a reserva criada dentro do limite de interações definido.
- [ ] AC-2: O formulário pede apenas os dados necessários.
- [ ] AC-3: Um erro de preenchimento é corrigido sem repetir os campos já preenchidos.

## Notas
Âmbito dos 30 s (decisão da equipa, 2026-10-16): desde a abertura do registo de cliente até à reserva criada (Q-12). Esta história é a dona do RNF-03 (Q-29: o tempo de escolha do quarto não conta). Dependências (auditoria 2026-10-16): duas fortes, story-registar-cliente e story-create-reservation. O AC-1 mede o fluxo real dos dois ecrãs; um stub não mede os 30 s. Por isso fica no sprint 5, depois das duas (antes estava no sprint 4, antes da reserva). AC-2 e AC-3 testam-se sozinhos com dados de teste (foundation T4). Como medir o tempo (cronómetro em teste de utilização) é decisão da equipa. AC-1 só é testável quando a reserva existir.

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: Mostly GUI flow and usability: short form, keep data on error, 30 s check by manual timing. Little new logic. Depends on two other stories.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: lukassperandio. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; sprint field by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
