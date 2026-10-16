---
type: story
summary: Definir os preços extra por noite
parent: epic-extra-services-pricing
requirements: [RF-07, RF-09]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [pricing, services]
sprint:
---

# Título: Definir os preços extra por noite

Como gestor,
Eu quero definir o preço extra por noite de cada extra e serviço pago,
Para que o preço da reserva use valores corretos.

## Critérios de aceite

### Cenário AC-1: Preço de um extra de quarto
  Dado que o gestor está autenticado
  Quando o gestor define o preço extra da vista mar em 15,00 por noite
  Então o sistema guarda o preço 15,00 para a vista mar

### Cenário AC-2: Preço de um serviço pago
  Dado que o gestor está autenticado
  Quando o gestor define o preço extra do spa em 40,00 por noite
  Então o sistema guarda o preço 40,00 para o spa

### Cenário AC-3: Todos os extras previstos
  Dado que o gestor está autenticado
  Quando o gestor consulta os preços extra
  Então pode definir preço para vista (mar ou montanha), bebidas, lavandaria, comida, eventos culturais, piscinas privadas, spa e salas de conferências

### Cenário AC-4: Preço inválido
  Dado que o gestor está autenticado
  Quando o gestor define um preço negativo ou não numérico
  Então o sistema recusa o valor
  E o preço anterior mantém-se

### Cenário AC-5: Perfil sem permissão
  Dado que o utilizador é rececionista
  Quando tenta alterar um preço extra
  Então o sistema recusa a operação

## Critérios de aceite (checklist)

- [ ] AC-1: O gestor consegue definir e guardar o preço extra por noite de um extra de quarto (vista mar a 15,00).
- [ ] AC-2: O gestor consegue definir e guardar o preço extra por noite de um serviço pago (spa a 40,00).
- [ ] AC-3: A lista de preços extra permite definir vista (mar ou montanha), bebidas, lavandaria, comida, eventos culturais, piscinas privadas, spa e salas de conferências.
- [ ] AC-4: Um preço negativo ou não numérico é recusado e o preço anterior mantém-se.
- [ ] AC-5: O rececionista não consegue alterar preços extra.

## Notas
Preços fixos por noite definidos pelo gestor (Q-11, decisão da equipa). Piscinas privadas, spa e salas são extras pagos sem acesso por cartão (Q-20). Fase pelo priorizador (Q-25).
Dependências (auditoria 2026-10-16): nenhuma forte (antes ligada a story-set-base-price sem razão de código). A lista de extras e serviços pagos, sem preço, vem dos dados de referência (foundation T5). Construir e testar sozinha: essa lista e sessões simuladas (T0, T4).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Tabela de preços por extra (cerca de 9 itens), validação do valor e perfil gestor. Formulário simples, sem cálculo.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
