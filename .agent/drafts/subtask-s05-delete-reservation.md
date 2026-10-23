---
type: subtask
summary: Apagar reserva: ReservationService.delete, ecrã do gestor e testes
parent: H4Y-57
requirements: [RF-22]
points:
points_suggested:
status: published
jira_key: H4Y-77
labels: [reservations, 1st-delivery]
sprint: 05
branch: feat/H4Y-77-delete-reservation
---

# delete-reservation: apagar antes do check-in

História pai: delete-reservation. Usa `ReservationService.delete(reservation, user)`. Esta história é dona da ação de apagar. refuse-delete-reservation (sprint 5, a seguir) e block-delete-after-check-in (sprint 6) acrescentam guardas à mesma ação. Deixar o método fácil de estender. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 5.

## Checklist

- [ ] Antes do código: o diagrama não tem um método para procurar uma reserva. Acrescentar o método em `docs/uml/` se o AC-3 precisar.
- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-57-AC-n")`.
- [ ] AC-1: a reserva sem check-in é removida da base de dados e não aparece no histórico.
- [ ] AC-2: o quarto volta a aparecer na pesquisa de quartos livres.
- [ ] AC-3: apagar uma reserva que não existe mostra "A reserva não existe".
- [ ] Ecrã: botão de apagar só no menu do gestor (regra de H4Y-23).

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
