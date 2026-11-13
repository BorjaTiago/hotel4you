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
sprint: 05
branch: feat/H4Y-74-cancel-reservation
---

# cancel-reservation: cancelar mantendo o histórico

História pai H4Y-55. Usa `ReservationService.cancel` e `ReservationController.onCancelReservationClicked`. Cancelar é mudança de estado (Q-05). Reservas de teste próprias. Não espera por create-reservation (sprint 5). O AC-2 usa a pesquisa de quartos livres do mesmo sprint. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 4.

## Checklist

- [x] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-55-AC-n")`.
- [x] AC-1: o estado passa de `RESERVED` para `CANCELLED` e a reserva continua na base de dados.
- [x] AC-2: o quarto volta a aparecer na pesquisa de quartos livres.
- [x] AC-3: cancelar uma reserva já cancelada é recusado.
- [x] AC-4: cancelar uma reserva com estado `CHECKED_IN` é recusado.
- [x] Ecrã: botão "Cancelar" na barra de ações do ecrã principal "Reservas" (AC-5 da história), ativo só numa linha RESERVED, com pergunta de confirmação.

## Interface (UI)

O ecrã de reservas já existe. H4Y-56 criou `reservations.fxml`, o botão "Reservas" no menu do rececionista e do gestor, a pesquisa do cliente pelo documento e a tabela com as reservas do cliente (quarto, entrada, saída, estado), incluindo as canceladas (histórico). Esta subtask só acrescenta o botão "Cancelar reserva" a esse ecrã.
- Botão "Cancelar reserva" na linha selecionada, com pergunta de confirmação ("Cancelar a reserva do quarto 201 de 10 a 12?").
- O botão fica desativado em reservas canceladas ou com check-in (AC-3, AC-4). Depois de cancelar, o estado na tabela muda para "Cancelada".

Created by: lukassperandio. Updated by: jmbcruz. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; PR #51 review round 2 by Claude Code (claude-opus-5-5), 2026-11-06; H4Y-55 cancel reservation by Claude Code (claude-opus-5-5), 2026-11-13. Reviewed by: pending.
