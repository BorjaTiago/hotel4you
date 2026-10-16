---
type: story
summary: Adicionar serviços extra pagos à reserva
parent: epic-extra-services-pricing
requirements: [RF-09]
points: 5
points_suggested: 5
status: approved
jira_key:
labels: [pricing, services]
sprint:
---

# Título: Adicionar serviços extra pagos à reserva

Como rececionista,
Eu quero adicionar à reserva serviços como comida, eventos culturais, piscinas privadas, spa e salas de conferências,
Para que estes serviços apareçam na reserva e na fatura.

## Critérios de aceite

### Cenário AC-1: Adicionar um serviço
  Dado que existe uma reserva ativa e o preço extra do spa está definido
  Quando o rececionista adiciona o spa à reserva
  Então a reserva mostra o spa
  E o custo do spa entra no valor da reserva

### Cenário AC-2: Vários serviços
  Dado que existe uma reserva ativa com preços definidos para comida e eventos culturais
  Quando o rececionista adiciona os dois serviços
  Então a reserva mostra os dois serviços
  E o valor soma os dois custos

### Cenário AC-3: Serviço sem acesso por cartão
  Dado que a reserva inclui o spa
  Quando o cliente passa o cartão na porta do spa
  Então o sistema recusa a entrada

### Cenário AC-4: Serviço sem preço definido
  Dado que o preço extra das salas de conferências não está definido
  Quando o rececionista tenta adicionar esse serviço
  Então o sistema recusa a operação

### Cenário AC-5: Serviços depois do check-in
  Dado que a reserva tem check-in efetuado
  Quando o rececionista tenta adicionar ou remover um serviço extra
  Então o sistema recusa a operação
  E a reserva mantém os serviços anteriores

## Critérios de aceite (checklist)

- [ ] AC-1: O serviço adicionado (spa) aparece na reserva e o seu custo entra no valor da reserva.
- [ ] AC-2: Vários serviços adicionados aparecem na reserva e o valor soma os custos de todos.
- [ ] AC-3: A passagem do cartão na porta do spa é recusada, mesmo com o spa na reserva.
- [ ] AC-4: Um serviço sem preço extra definido não pode ser adicionado e a operação é recusada.
- [ ] AC-5: Depois do check-in, adicionar ou remover serviços é recusado e a reserva mantém os serviços anteriores.

## Notas
Decisão da equipa (2026-10-16): extras editáveis até ao check-in; datas e quarto não são editáveis. Q-11 e Q-20 (decisão da equipa). Fatura: o enunciado não descreve fatura além de "aparecer na fatura" no epic; aqui só o valor da reserva. Fase pelo priorizador (Q-25).
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: reservas e preços de teste (foundation T4). O AC-3 (porta do spa) usa o leitor simulado (T6) e a regra de recusa; o spa não é zona com cartão (Q-20).

points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: Liga serviços à reserva, soma ao valor, bloqueia depois do check-in, exige preço definido. Toca dados, regras e GUI da reserva. Depende do modelo da reserva.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
