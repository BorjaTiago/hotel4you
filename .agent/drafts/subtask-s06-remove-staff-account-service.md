---
type: subtask
summary: Serviço: StaffService.removeStaff, recusa com registos associados, login bloqueado e testes
parent: H4Y-88
requirements: [RF-26]
points:
points_suggested:
status: published
jira_key: H4Y-95
labels: [staff, 1st-delivery]
sprint: 06
branch: feat/H4Y-95-remove-staff-service
---

# remove-staff-account: regras de remoção

História pai: remove-staff-account (H4Y-88). Usa `StaffService.removeStaff` e `HotelFacade.removeStaff` do diagrama. Só contas (Q-18). Nada com associações se remove (Q-36): só se recusa, não se desativa.
Responsável: a definir na planning do sprint 6.

## Checklist

- [ ] Escrever primeiro os testes com @Tag("H4Y-88-AC-n") e ver que falham (commit de testes antes do código).
- [ ] Antes do código: o diagrama tem `removeStaff(user: User)` sem `actor`. Os outros métodos de `StaffService` recebem `actor`. Decidir a assinatura e atualizar `docs/uml/` (regra 11 de `AGENTS.md`).
- [ ] AC-1: a conta removida deixa de existir e `AuthService.login` recusa-a com a mesma mensagem de login errado (sem revelar que a conta existiu).
- [ ] AC-2: conta inexistente mostra "A conta não existe" (`StaffNotFoundException`).
- [ ] AC-3: conta com registos associados não é removida e a mensagem explica porquê. Só contam as reservas (`reservations.created_by_user_id`). O movimento de stock não conta. Erro SQL 547 vira uma exceção própria, como em `ClientInUseException`.
- [ ] AC-4: o rececionista é recusado (`AccessDeniedException`, `Permission.MANAGE_STAFF` de H4Y-23).
- [ ] AC-5: o gestor não remove a própria conta (`actor` com o mesmo id da conta): recusado com uma mensagem e a conta mantém-se. Verificar antes de apagar.
- [ ] Dados de teste próprios: uma conta sem registos e uma com reserva associada (foundation T4). Testes só de lógica, com repositórios em memória.

## Interface (UI)

Sem ecrã nesta subtask. O ecrã está em remove-staff-account (ecrã).

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; jira-publisher, Claude Code (claude-sonnet-5-5), 2026-11-13. Reviewed by: pending.
