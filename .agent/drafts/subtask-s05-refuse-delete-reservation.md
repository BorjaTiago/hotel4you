---
type: subtask
summary: Guarda de papel na ação de apagar reserva: só o gestor apaga
parent: H4Y-58
requirements: [RF-22]
points:
points_suggested:
status: published
jira_key: H4Y-78
labels: [reservations, security, 1st-delivery]
sprint: 05
branch: feat/H4Y-78-refuse-delete-reservation
---

# refuse-delete-reservation: só o gestor apaga

História pai: refuse-delete-reservation. Puxada do sprint 6 para o sprint 5 (decisão de 2026-10-23, mínimo de 13 pontos por sprint). Acrescenta uma guarda a `ReservationService.delete`. Faz-se depois de delete-reservation, no mesmo sprint, pela mesma pessoa ou a seguir. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 5.

## Checklist

- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-58-AC-n")`.
- [ ] AC-1: o rececionista que tenta apagar recebe `AccessDeniedException` e a reserva não muda.
- [ ] AC-2: o rececionista continua a cancelar (usa cancel-reservation do sprint 4).
- [ ] AC-3: sem sessão, apagar é recusado.
- [ ] AC-4: o funcionário do restaurante é recusado.
- [ ] A mensagem de recusa não mostra dados da reserva.
- [ ] Usa a regra de papéis de H4Y-23. Não cria uma regra nova.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
