---
type: story
summary: Registar saída de stock
parent: epic-stock-management
requirements: [RF-29]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [stock, final-delivery]
sprint:
---

# Título: Registar saída de stock

Como rececionista,
Eu quero registar a saída (descarte, devolução ou perda) de um item do stock,
Para que o stock reflita o material removido.

## Critérios de aceite

### Cenário AC-1: Saída registada
  Dado que o item "lençóis" tem stock 20
  Quando o rececionista regista uma saída de 6 unidades por descarte na data de hoje
  Então o movimento fica guardado como "saída" com quantidade 6, motivo "descarte" e a data
  E o stock de "lençóis" passa a 14

### Cenário AC-2: Saída maior que o stock
  Dado que o item "lençóis" tem stock 2
  Quando o rececionista regista uma saída de 6 unidades
  Então o sistema recusa o registo
  E o stock continua 2

### Cenário AC-3: Item inexistente
  Dado que o item "foguetes" não existe
  Quando o rececionista regista uma saída desse item
  Então o sistema recusa o registo

### Cenário AC-4: Motivo da saída
  Dado que o item "lençóis" existe com stock suficiente
  Quando o rececionista regista uma saída sem motivo, ou com motivo diferente de descarte, devolução ou perda
  Então o sistema recusa o registo

## Notas
Q-21 (decisão da equipa). Decisão da equipa (2026-10-16): saída = remoção do material (descarte, devolução ou perda); uso = consumo no hotel, ver "Registar uso de stock". Entrega final (Q-06). Depende de "Cadastrar item de stock" e "Editar item de stock".

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Movimento com quantidade, data e motivo; atualiza o stock e recusa saldo negativo. Validação e gravação simples.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
