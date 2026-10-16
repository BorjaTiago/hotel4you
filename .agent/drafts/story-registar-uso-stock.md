---
type: story
summary: Registar uso de stock
parent: epic-stock-management
requirements: [RF-29]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [stock, final-delivery]
sprint:
---

# Título: Registar uso de stock

Como funcionário do hotel ou gestor,
Eu quero registar o uso (consumo no hotel) de um item do stock,
Para que o stock reflita o material gasto no hotel.

## Critérios de aceite

### Cenário AC-1: Uso registado
  Dado que o item "toalhas" tem stock 15
  Quando o funcionário do hotel ou o gestor regista o uso de 4 unidades na data de hoje
  Então o movimento fica guardado como "uso" com quantidade 4 e a data
  E o stock de "toalhas" passa a 11
  E o material continua no hotel, consumido

### Cenário AC-2: Uso maior que o stock
  Dado que o item "toalhas" tem stock 3
  Quando o funcionário do hotel ou o gestor regista o uso de 5 unidades
  Então o sistema recusa o registo
  E o stock continua 3

### Cenário AC-3: Quantidade inválida
  Dado que o item "toalhas" existe
  Quando o funcionário do hotel ou o gestor regista um uso com quantidade 0 ou negativa
  Então o sistema recusa o registo

## Notas
Q-21 (decisão da equipa): uso e saída são movimentos com quantidade e data. Decisão da equipa (2026-10-16): uso = consumo no hotel; saída = remoção do material (descarte, devolução ou perda), ver "Registar saída de stock". Entrega final (Q-06). Depende de "Cadastrar item de stock" e "Editar item de stock".

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Movimento de uso com quantidade e data; recusa saldo negativo. Mesmo padrão da saída de stock.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; actor note edit by doc-editor, Claude Code (claude-haiku-5-5), 2026-10-16. Reviewed by: pending.
