---
type: story
summary: Registar cliente e reserva em 30 segundos
parent: epic-client-registration
requirements: [RNF-03, RF-10]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [clients, 1st-delivery, usability]
sprint:
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

## Notas
Âmbito dos 30 s (decisão da equipa, 2026-10-16): desde a abertura do registo de cliente até à reserva criada (Q-12). Esta história é a dona do RNF-03 (Q-29: o tempo de escolha do quarto não conta). Depende de "Registar um novo cliente" e da história de criar reserva (epic-reservations). Como medir o tempo (cronómetro em teste de utilização) é decisão da equipa. AC-1 só é testável quando a reserva existir.

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: Mostly GUI flow and usability: short form, keep data on error, 30 s check by manual timing. Little new logic. Depends on two other stories.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
