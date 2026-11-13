---
type: subtask
summary: Serviço: script SQL v12 de stock_movements, StockService.registerMovement só para entradas (gestor), stock atualizado e testes
parent: H4Y-90
requirements: [RF-29]
points:
points_suggested:
status: published
jira_key: H4Y-98
labels: [stock, 1st-delivery]
sprint: 06
branch: feat/H4Y-98-stock-entry-service
---

# registar-entrada-stock: regras e gravação

História pai: registar-entrada-stock (H4Y-90). Usa `StockService.registerMovement` e `HotelFacade.registerMovement` do diagrama, com `MovementType` de entrada (`in`). A história regista só entradas. Uso e saída ficam em registar-saida-stock e registar-uso-stock (sprint 7): não construir esses tipos agora.
Script SQL: esta subtask cria o **v12** (`database/v12-create-stock-movements.sql`). O código chega à versão v10. Números reservados no sprint 6: v11 room_items, v12 stock_movements, v13 reservation_locations, v14 extra_prices. Não usar outro número.
Só o gestor regista (decisão do utilizador, 2026-11-13): `Permission.MANAGE_STOCK`, sem permissão nova.
Responsável: a definir na planning do sprint 6.

## Checklist

- [ ] Escrever primeiro os testes com @Tag("H4Y-90-AC-n") e ver que falham (commit de testes antes do código).
- [ ] Antes do código: ler `StockService` e `docs/uml/`. Ajustar o diagrama se a assinatura de `registerMovement` ou `applyMovement` mudar.
- [ ] Script SQL `database/v12-create-stock-movements.sql`: tabela `stock_movements` de `docs/database/schema.md` (`type`, `quantity > 0`, `moved_at`, `item_id`, `user_id`). Corre depois de v11.
- [ ] AC-1: a entrada (tipo `in`) fica guardada com quantidade e data de hoje, e `items.stock_quantity` sobe (10 + 5 = 15). Movimento e stock na mesma transação (`docs/database/schema.md`).
- [ ] AC-2: quantidade 0 ou negativa é recusada e o stock não muda.
- [ ] AC-3: item inexistente é recusado (`StockItemNotFoundException`).
- [ ] Só o gestor: um rececionista e um funcionário do restaurante são recusados (`AccessDeniedException`, `Permission.MANAGE_STOCK`). Teste com `@Tag("H4Y-90-AC-1")`.
- [ ] Outros tipos de movimento (saída, uso) não são construídos aqui.
- [ ] Dados de teste próprios: item "toalhas" com stock 10 (foundation T4). Testes só de lógica, com repositórios em memória. Sem testes de texto SQL.

## Interface (UI)

Sem ecrã nesta subtask. O ecrã está em registar-entrada-stock (ecrã).

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; jira-publisher, Claude Code (claude-sonnet-5-5), 2026-11-13. Reviewed by: pending.
