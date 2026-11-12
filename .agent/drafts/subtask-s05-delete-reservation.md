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

História pai H4Y-57. Usa `ReservationService.delete(reservation, user)`. Esta história é dona da ação de apagar. refuse-delete-reservation (sprint 5, a seguir) e block-delete-after-check-in (sprint 6) acrescentam guardas à mesma ação. Deixar o método fácil de estender. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 5.

## Checklist

- [x] Antes do código: o diagrama não tem um método para procurar uma reserva. Acrescentar o método em `docs/uml/` se o AC-3 precisar. (`ReservationRepository.findById` já existe, H4Y-82.)
- [x] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-57-AC-n")`.
- [x] AC-1: a reserva sem check-in é removida da base de dados e não aparece no histórico.
- [x] AC-2: o quarto volta a aparecer na pesquisa de quartos livres.
- [x] AC-3: apagar uma reserva que não existe mostra "A reserva não existe".
- [x] Ecrã: botão de apagar só para o gestor (regra de H4Y-23), com `Permission.DELETE_RESERVATIONS`.
- [x] Ecrã: o botão "Reservas" também aparece no menu do gestor (`MANAGE_RESERVATIONS` já inclui o gestor).
- [x] AC-4: o botão "Apagar" (`action-button, danger-button`, estilo comum de H4Y-83) só fica ativo numa linha RESERVED.

## Interface (UI)

- No ecrã `reservations-list.fxml` ("Reservas", H4Y-81): botão "Apagar" na barra de ações, para a linha selecionada, visível só para o gestor (regra de H4Y-23).
- Pergunta de confirmação: "Apagar de vez a reserva do quarto 201? Esta ação não pode ser desfeita."
- Depois de apagar: a reserva sai da tabela (AC-1). Reserva inexistente: "A reserva não existe." (AC-3).

Created by: lukassperandio. Updated by: antonioprmartinho. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; checklist and screen update for H4Y-57 by Claude Code (claude-opus-5-5), 2026-11-12. Reviewed by: pending.
