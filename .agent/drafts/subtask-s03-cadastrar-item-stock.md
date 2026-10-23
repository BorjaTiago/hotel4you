---
type: subtask
summary: Cadastrar item de stock: StockService.createItem, ecrã e testes
parent: H4Y-51
requirements: [RF-28, RF-29]
points:
points_suggested:
status: published
jira_key: H4Y-68
labels: [stock, 1st-delivery]
sprint: 03
branch: feat/H4Y-68-stock-item
---

# cadastrar-item-stock: catálogo de itens

História pai: cadastrar-item-stock (2 pontos). Usa `StockService.createItem(name, unit)` e `StockController.onSaveItemClicked`. Esta subtask cria a tabela de itens.
Extra do sprint 3: entra quando o trabalho comprometido estiver em curso. Se não acabar, passa para o início do sprint 4.
Responsável proposto: Tiago Borja (a equipa decide).

## Checklist

- [ ] Antes do código: acrescentar o repositório de itens em `docs/uml/`.
- [ ] Script SQL em `database/`: tabela `items` de `docs/database/schema.md`.
- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-51-AC-n")`.
- [ ] AC-1: o item "toalhas" com a unidade "unidade" fica no catálogo com stock 0.
- [ ] AC-2: sem nome ou sem unidade, o registo é recusado com o campo em falta.
- [ ] AC-3: um item com o mesmo nome é recusado.
- [ ] AC-4: um utilizador que não é gestor é recusado.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
