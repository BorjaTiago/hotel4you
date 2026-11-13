---
type: subtask
summary: Serviço: script SQL v14 extra_prices com os 9 itens, classe de domínio, repositórios, ExtraPriceService, permissão MANAGE_EXTRA_PRICES e testes
parent: H4Y-93
requirements: [RF-07, RF-09]
points:
points_suggested:
status: published
jira_key: H4Y-103
labels: [pricing, services]
sprint: 06
branch: feat/H4Y-103-extra-prices-service
---

# definir-precos-extra: tabela, domínio, repositório e serviço

História pai: definir-precos-extra (H4Y-93). Nada existe ainda no código. Script SQL: **v14** (`database/v14-create-extra-prices.sql`). O código chega à versão v10. Números reservados no sprint 6: v11 room_items, v12 stock_movements, v13 reservation_locations, v14 extra_prices. Não usar outro número.
Os 9 itens do seed: vista mar, vista montanha, bebidas, lavandaria, comida, eventos culturais, piscinas privadas, spa e salas de conferências. Preço extra por noite, valor fixo (Q-11).
Responsável: a definir na planning do sprint 6.

## Checklist

- [ ] Escrever primeiro os testes com @Tag("H4Y-93-AC-n") e ver que falham (commit de testes antes do código).
- [ ] Antes do código: acrescentar a `docs/uml/` e `docs/database/` a classe de domínio, o repositório, o serviço, o método da fachada e a permissão. Seguir o padrão de `RoomType` e do preço base (H4Y-26). Regra 11 de `AGENTS.md`.
- [ ] Script SQL `database/v14-create-extra-prices.sql`: tabela `extra_prices` (nome do extra único, preço por noite `DECIMAL`, preço pode ser vazio até o gestor o definir) e o seed dos 9 itens, repetível (`IF NOT EXISTS`). Corre depois de v13.
- [ ] Classe de domínio do extra com preço (nome, tipo de extra ou serviço pago, preço por noite).
- [ ] Repositório: interface, versão JDBC e versão em memória (para os testes).
- [ ] `Permission.MANAGE_EXTRA_PRICES` (só `Role.MANAGER`) em `Permission` e em `PermissionTest`.
- [ ] Serviço (`ExtraPriceService`, nome a confirmar no diagrama) e método na fachada: listar os preços extra e definir um preço, ambos com verificação de `Permission.MANAGE_EXTRA_PRICES`.
- [ ] AC-1: definir a vista mar a 15,00 por noite e guardar.
- [ ] AC-2: definir o spa a 40,00 por noite e guardar.
- [ ] AC-3: a lista contém os 9 itens (vista mar, vista montanha, bebidas, lavandaria, comida, eventos culturais, piscinas privadas, spa, salas de conferências).
- [ ] AC-4: preço negativo ou que não é número é recusado e o preço anterior mantém-se.
- [ ] AC-5: o rececionista é recusado (`AccessDeniedException`).
- [ ] Dados de teste próprios: lista dos 9 itens sem preço, um gestor e um rececionista (foundation T4). Testes só de lógica, com repositório em memória. Sem testes de texto SQL.
- [ ] Javadoc nas classes e métodos públicos novos. O Javadoc compila.

## Interface (UI)

Sem ecrã nesta subtask. O ecrã está em definir-precos-extra (ecrã).

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; jira-publisher, Claude Code (claude-sonnet-5-5), 2026-11-13. Reviewed by: pending.
