---
type: subtask
summary: Reserva com restaurante e piscina: script SQL v13 reservation_locations, duas caixas no ecrã de reserva e uma coluna na lista
parent: H4Y-92
requirements: [RF-11, RF-18]
points:
points_suggested:
status: published
jira_key: H4Y-102
labels: [reservations, 1st-delivery]
sprint: 06
branch: feat/H4Y-102-reservation-restaurant-pool
---

# reservation-restaurant-pool: locais incluídos na reserva

História pai: reservation-restaurant-pool (H4Y-92). Acrescenta a escolha de locais ao fluxo de criar reserva (H4Y-56, sprint 4, e o fluxo em três passos do sprint 5). Subtask reduzida por decisão do utilizador (2026-11-13): só o script SQL, duas caixas e uma coluna. O acesso efetivo com cartão fica na entrega final (RF-18). Uma pessoa faz tudo.
Script SQL: **v13** (`database/v13-create-reservation-locations.sql`). O código chega à versão v10. Números reservados no sprint 6: v11 room_items, v12 stock_movements, v13 reservation_locations, v14 extra_prices. Não usar outro número.
Responsável: a definir na planning do sprint 6.

## Checklist

- [ ] Escrever primeiro os testes com @Tag("H4Y-92-AC-n") e ver que falham (commit de testes antes do código).
- [ ] Antes do código: ler `ReservationService`, `ReservationRepository` e `docs/uml/`. Acrescentar o que falta (gravar e ler os locais da reserva), seguindo `docs/database/schema.md`. Atualizar `docs/uml/` e `docs/database/` no mesmo PR (regra 11).
- [ ] Script SQL `database/v13-create-reservation-locations.sql`: tabela `reservation_locations` de `docs/database/schema.md`. Corre depois de v12.
- [ ] AC-1: a reserva fica com restaurante e piscina incluídos.
- [ ] AC-2: a reserva fica só com a piscina. O restaurante não fica incluído.
- [ ] AC-3: uma reserva sem locais continua válida e sem acesso a esses locais.
- [ ] Restaurante e piscina vêm dos dados de referência (foundation T5). Dados de teste próprios: reservas de teste (foundation T4). Testes só de lógica, sem testes de texto SQL.
- [ ] Ecrã: duas caixas de seleção "Restaurante" e "Piscina" no ecrã de reserva, desmarcadas por omissão. A lógica fica no serviço (RNF-07).
- [ ] Lista "Reservas": uma coluna "Locais" com os locais incluídos (vazia se não houver).

## Interface (UI)

- No ecrã de criar reserva: caixas "Restaurante" e "Piscina".
- Na tabela "Reservas": uma coluna "Locais" mostra os locais incluídos.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; jira-publisher, Claude Code (claude-sonnet-5-5), 2026-11-13. Reviewed by: pending.
