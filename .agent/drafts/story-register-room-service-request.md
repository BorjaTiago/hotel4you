---
type: story
summary: Registar um pedido de serviço de quarto
parent: epic-guest-support
requirements: [RF-15]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [guest-support]
sprint:
---

# Título: Registar um pedido de serviço de quarto

Como rececionista,
Eu quero registar um pedido de serviço de quarto de comida, bebida ou lavandaria de um cliente com check-in efetuado,
Para que o cliente seja ajudado com os serviços do quarto.

## Critérios de aceite

### Cenário AC-1: Pedido registado
  Dado que o cliente tem uma reserva com check-in efetuado no quarto 202
  Quando o rececionista registar para o quarto 202 um pedido de comida, bebida ou lavandaria
  Então o pedido fica registado com o quarto, o tipo de serviço e a data e hora
  E o rececionista vê a confirmação do registo

### Cenário AC-2: Cliente sem check-in
  Dado que o quarto 202 não tem reserva com check-in efetuado
  Quando o rececionista tentar registar um pedido de serviço para esse quarto
  Então o sistema recusa o pedido
  E mostra uma mensagem a indicar que o quarto não está ocupado

### Cenário AC-3: Tipo de serviço não permitido
  Dado que o serviço pedido não é comida, bebida nem lavandaria
  Quando o rececionista tentar registar o pedido
  Então o sistema recusa o pedido
  E não regista nada

### Cenário AC-4: Utilizador sem permissão
  Dado que o utilizador autenticado não é rececionista
  Quando tentar registar um pedido de serviço de quarto
  Então o sistema recusa a ação

## Notas
Depende do check-in (epic-check-in-out). Enunciado, secção 2. RF-15. Decisão da equipa (2026-10-16): os serviços de quarto são só comida, bebida e lavandaria (sem lista aberta de serviços). O pessoal só entra em quartos com cartão mestre do gestor (Q-08); esta história não abre portas. Fase: definida pelo priorizador (Q-25).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Validação de quarto ocupado e de três tipos de serviço, e gravação do pedido. Formulário simples. Depende do estado de check-in.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
