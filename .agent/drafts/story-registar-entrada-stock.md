---
type: story
summary: Registar entrada de stock
parent: epic-stock-management
requirements: [RF-29]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [stock, final-delivery]
sprint:
---

# Título: Registar entrada de stock

Como rececionista,
Eu quero registar a entrada de um item no stock,
Para que o hotel saiba o material que tem.

## Critérios de aceite

### Cenário AC-1: Entrada registada
  Dado que o item "toalhas" existe com stock 10
  Quando o rececionista regista uma entrada de 5 unidades na data de hoje
  Então o movimento fica guardado com quantidade 5 e a data
  E o stock de "toalhas" passa a 15

### Cenário AC-2: Quantidade inválida
  Dado que o item "toalhas" existe
  Quando o rececionista regista uma entrada com quantidade 0 ou negativa
  Então o sistema recusa o registo
  E o stock não muda

### Cenário AC-3: Item inexistente
  Dado que o item "foguetes" não existe
  Quando o rececionista regista uma entrada desse item
  Então o sistema recusa o registo

## Notas
Cada movimento tem quantidade e data (Q-21, decisão da equipa). O stock cobre todos os itens de fornecedor. Entrega final (Q-06). Depende de "Cadastrar item de stock" e "Editar item de stock".

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Movimento de entrada com quantidade e data, atualiza o stock. Cria a tabela de movimentos usada pelas outras histórias de stock.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
