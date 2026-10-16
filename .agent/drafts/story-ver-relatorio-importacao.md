---
type: story
summary: Ver o relatório de importação com itens rejeitados
parent: epic-stock-management
requirements: [RF-28]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [stock, xml, final-delivery]
sprint:
---

# Título: Ver o relatório de importação com itens rejeitados

Como gestor,
Eu quero ver o relatório de uma importação XML,
Para que eu saiba que itens entraram e que itens foram rejeitados.

## Critérios de aceite

### Cenário AC-1: Relatório com rejeitados
  Dado que uma importação teve 3 itens importados e 1 item desconhecido
  Quando o gestor abre o relatório dessa importação
  Então o relatório lista os 3 itens importados
  E lista o item rejeitado com o motivo "item desconhecido"

### Cenário AC-2: Relatório sem rejeitados
  Dado que uma importação importou todos os itens
  Quando o gestor abre o relatório
  Então o relatório mostra os itens importados
  E mostra que não há itens rejeitados

### Cenário AC-3: Importação inexistente
  Dado que não existe a importação pedida
  Quando o gestor tenta abrir o relatório
  Então o sistema indica que a importação não existe

## Critérios de aceite (checklist)

- [ ] AC-1: O relatório lista os itens importados e os itens rejeitados com o motivo.
- [ ] AC-2: Sem rejeitados, o relatório mostra os itens importados e indica que não há itens rejeitados.
- [ ] AC-3: Para uma importação inexistente, o sistema indica que a importação não existe.

## Notas
Q-10 (decisão da equipa). Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: importações de teste próprias, com e sem rejeitados (foundation T4, tabela `stock_imports` do esquema H4Y-10). Entrega final (Q-06).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Guarda o resultado de cada importação (itens e rejeitados) e mostra num ecrã. Exige tabelas novas de importação. Lógica de leitura simples.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
