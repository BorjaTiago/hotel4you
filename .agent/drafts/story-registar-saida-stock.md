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

Como funcionário do hotel ou gestor,
Eu quero registar a saída (descarte, devolução ou perda) de um item do stock,
Para que o stock reflita o material removido.

## Critérios de aceite

### Cenário AC-1: Saída registada
  Dado que o item "lençóis" tem stock 20
  Quando o funcionário do hotel ou o gestor regista uma saída de 6 unidades por descarte na data de hoje
  Então o movimento fica guardado como "saída" com quantidade 6, motivo "descarte" e a data
  E o stock de "lençóis" passa a 14

### Cenário AC-2: Saída maior que o stock
  Dado que o item "lençóis" tem stock 2
  Quando o funcionário do hotel ou o gestor regista uma saída de 6 unidades
  Então o sistema recusa o registo
  E o stock continua 2

### Cenário AC-3: Item inexistente
  Dado que o item "foguetes" não existe
  Quando o funcionário do hotel ou o gestor regista uma saída desse item
  Então o sistema recusa o registo

### Cenário AC-4: Motivo da saída
  Dado que o item "lençóis" existe com stock suficiente
  Quando o funcionário do hotel ou o gestor regista uma saída sem motivo, ou com motivo diferente de descarte, devolução ou perda
  Então o sistema recusa o registo

## Critérios de aceite (checklist)

- [ ] AC-1: A saída fica guardada como "saída" com quantidade, motivo e data, e o stock do item baixa em conformidade.
- [ ] AC-2: Uma saída maior que o stock é recusada e o stock não muda.
- [ ] AC-3: Uma saída de um item inexistente é recusada.
- [ ] AC-4: Uma saída sem motivo, ou com motivo fora de descarte, devolução ou perda, é recusada.

## Notas
Q-21 (decisão da equipa). Decisão da equipa (2026-10-16): saída = remoção do material (descarte, devolução ou perda); uso = consumo no hotel, ver "Registar uso de stock". Entrega final (Q-06). Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: itens de teste com stock conhecido (foundation T4), sem depender de story-registar-entrada-stock.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Movimento com quantidade, data e motivo; atualiza o stock e recusa saldo negativo. Validação e gravação simples.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; actor note edit by doc-editor, Claude Code (claude-haiku-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
