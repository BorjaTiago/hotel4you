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

Como funcionário do hotel ou gestor,
Eu quero registar a entrada de um item no stock,
Para que o hotel saiba o material que tem.

## Critérios de aceite

### Cenário AC-1: Entrada registada
  Dado que o item "toalhas" existe com stock 10
  Quando o funcionário do hotel ou o gestor regista uma entrada de 5 unidades na data de hoje
  Então o movimento fica guardado com quantidade 5 e a data
  E o stock de "toalhas" passa a 15

### Cenário AC-2: Quantidade inválida
  Dado que o item "toalhas" existe
  Quando o funcionário do hotel ou o gestor regista uma entrada com quantidade 0 ou negativa
  Então o sistema recusa o registo
  E o stock não muda

### Cenário AC-3: Item inexistente
  Dado que o item "foguetes" não existe
  Quando o funcionário do hotel ou o gestor regista uma entrada desse item
  Então o sistema recusa o registo

## Critérios de aceite (checklist)

- [ ] AC-1: A entrada fica guardada com quantidade e data, e o stock do item sobe em conformidade.
- [ ] AC-2: Uma entrada com quantidade 0 ou negativa é recusada e o stock não muda.
- [ ] AC-3: Uma entrada de um item inexistente é recusada.

## Notas
Cada movimento tem quantidade e data (Q-21, decisão da equipa). O stock cobre todos os itens de fornecedor. Entrega final (Q-06). Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: itens de teste com stock conhecido (foundation T4).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Movimento de entrada com quantidade e data, atualiza o stock. Cria a tabela de movimentos usada pelas outras histórias de stock.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; actor note edit by doc-editor, Claude Code (claude-haiku-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
