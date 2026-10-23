---
type: subtask
summary: Ecrã: criar reserva a partir do cliente e do quarto livre escolhidos
parent: H4Y-56
requirements: [RF-11, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-76
labels: [reservations, gui, 1st-delivery]
sprint: 05
branch: feat/H4Y-76-reservation-screen
---

# create-reservation: ecrã

História pai: create-reservation. Depende do serviço. Liga a pesquisa de cliente e a pesquisa de quartos livres (sprint 4). Usa `ReservationController.onCreateReservationClicked`. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 5.

## Checklist

- [ ] O rececionista escolhe o cliente, o quarto livre e as datas, e confirma.
- [ ] O ecrã mostra o preço base por noite antes de confirmar (AC-4).
- [ ] Os erros vêm do serviço (RNF-07).
- [ ] Verificação manual: reservar e ver que o quarto sai da pesquisa de livres.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
