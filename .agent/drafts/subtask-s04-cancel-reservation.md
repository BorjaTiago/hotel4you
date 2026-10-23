---
type: subtask
summary: Cancelar reserva: ReservationService.cancel, estados, ecrã e testes
parent: H4Y-55
requirements: [RF-12]
points:
points_suggested:
status: published
jira_key: H4Y-74
labels: [reservations, 1st-delivery]
sprint: 04
branch: feat/H4Y-74-cancel-reservation
---

# cancel-reservation: cancelar mantendo o histórico

História pai: cancel-reservation. Usa `ReservationService.cancel` e `ReservationController.onCancelReservationClicked`. Cancelar é mudança de estado (Q-05). Reservas de teste próprias. Não espera por create-reservation (sprint 5). O AC-2 usa a pesquisa de quartos livres do mesmo sprint. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 4.

## Checklist

- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-55-AC-n")`.
- [ ] AC-1: o estado passa de `RESERVED` para `CANCELLED` e a reserva continua na base de dados.
- [ ] AC-2: o quarto volta a aparecer na pesquisa de quartos livres.
- [ ] AC-3: cancelar uma reserva já cancelada é recusado.
- [ ] AC-4: cancelar uma reserva com estado `CHECKED_IN` é recusado.
- [ ] Ecrã: botão de cancelar na lista de reservas do cliente.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
