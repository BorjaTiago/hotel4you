---
type: subtask
summary: Serviço: ReservationService.create, repositório de reservas, recusa de sobreposição e testes
parent: H4Y-56
requirements: [RF-11, RF-05]
points:
points_suggested:
status: published
jira_key: H4Y-75
labels: [reservations, 1st-delivery]
sprint: 05
branch: feat/H4Y-75-reservation-service
---

# create-reservation: regras e gravação

História pai: create-reservation. Usa `ReservationService.create` e `HotelFacade.createReservation`. Reutiliza a regra `RoomService.isAvailable` do sprint 4. Não cria uma segunda regra. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 5.

## Checklist

- [ ] Antes do código: acrescentar a gravação ao repositório de reservas em `docs/uml/`.
- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-56-AC-n")`.
- [ ] AC-1: a reserva fica com o estado `RESERVED` e o quarto deixa de estar livre.
- [ ] AC-2: uma reserva que se sobrepõe é recusada. Não fica nenhuma reserva duplicada.
- [ ] AC-3: sem cliente ou sem período, a reserva é recusada com os dados em falta.
- [ ] AC-4: o preço base por noite do tipo vem do serviço.
- [ ] A reserva guarda o utilizador que a criou (`created_by_user_id`).

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
