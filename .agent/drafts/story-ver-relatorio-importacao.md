---
type: story
summary: Ver o relatório de importação com itens criados, atualizados e inválidos
parent: epic-stock-management
requirements: [RF-28]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [stock, xml, final-delivery]
sprint:
---

# Título: Ver o relatório de importação com itens criados, atualizados e inválidos

Como gestor,
Eu quero ver o relatório de uma importação XML,
Para que eu saiba que itens foram criados, que itens foram atualizados e que itens eram inválidos.

## Critérios de aceite

### Cenário AC-1: Relatório com criados, atualizados e inválidos
  Dado que uma importação criou 2 itens, atualizou 3 itens e teve 1 item inválido
  Quando o gestor abre o relatório dessa importação
  Então o relatório lista os 2 itens criados
  E lista os 3 itens atualizados
  E lista o item inválido com o motivo
  E mostra as contagens 2 criados, 3 atualizados e 1 inválido

### Cenário AC-2: Relatório sem inválidos
  Dado que uma importação só criou e atualizou itens
  Quando o gestor abre o relatório
  Então o relatório mostra os itens criados e atualizados
  E mostra que há 0 itens inválidos

### Cenário AC-3: Importação inexistente
  Dado que não existe a importação pedida
  Quando o gestor tenta abrir o relatório
  Então o sistema indica que a importação não existe

## Critérios de aceite (checklist)

- [ ] AC-1: O relatório lista os itens criados, atualizados e inválidos (com o motivo) e mostra as contagens.
- [ ] AC-2: Sem inválidos, o relatório mostra os criados e atualizados e indica 0 inválidos.
- [ ] AC-3: Para uma importação inexistente, o sistema indica que a importação não existe.

## Notas
Decisão da equipa (utilizador, 2026-10-16), Q-48, substitui Q-10 e Q-38: já não há itens "desconhecidos rejeitados". O relatório tem três grupos (criados, atualizados, inválidos) com contagens; o inválido tem o motivo (formato, unidade ou preço em falta ou inválido, unidade diferente, quantidade negativa). Um ficheiro recusado por inteiro (formato inválido ou repetido) não tem relatório nem linha em `stock_imports`. Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: importações de teste próprias, com e sem inválidos (foundation T4, tabela `stock_imports` do esquema H4Y-10). Entrega final (Q-06).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Guarda o resultado de cada importação (criados, atualizados, inválidos) e mostra num ecrã com contagens. Um grupo a mais do que antes, mas a lógica de leitura continua simples.
split_needed: no

ready: true
dor_failed: []
questions: [Q-48]

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Q-48 rewrite by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Unit/price rule by docs-updater, Claude Code (claude-haiku-5-5), 2026-10-16. Reviewed by: pending.
