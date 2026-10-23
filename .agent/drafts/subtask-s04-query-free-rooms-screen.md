---
type: subtask
summary: Ecrã: pesquisa de quartos livres por tipo e datas
parent: H4Y-54
requirements: [RF-05, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-73
labels: [reservations, gui, 1st-delivery]
sprint: 04
branch: feat/H4Y-73-free-rooms-screen
---

# query-free-rooms: ecrã

História pai: query-free-rooms. Depende do serviço. Usa `RoomController.onSearchClicked`. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 4.

## Checklist

- [ ] O rececionista escolhe o tipo, a data de entrada e a data de saída.
- [ ] O ecrã mostra os quartos livres ou a mensagem do serviço.
- [ ] Nenhuma regra de datas no controlador (RNF-07).
- [ ] O ecrã deixa escolher um quarto. A reserva (sprint 5) usa essa escolha.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
