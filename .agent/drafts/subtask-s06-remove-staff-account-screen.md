---
type: subtask
summary: Ecrã: botão de remover conta de funcionário com confirmação e mensagens
parent: H4Y-88
requirements: [RF-26, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-96
labels: [staff, gui, 1st-delivery]
sprint: 06
branch: feat/H4Y-96-remove-staff-screen
---

# remove-staff-account: ecrã

História pai: remove-staff-account (H4Y-88). Depende do serviço da mesma história (mesma pessoa, do serviço ao ecrã). Usa `StaffController` do diagrama e o ecrã de contas de H4Y-27.
Responsável: a definir na planning do sprint 6.

## Checklist

- [ ] Escrever primeiro os testes com @Tag("H4Y-88-AC-n") e ver que falham (commit de testes antes do código).
- [ ] O gestor seleciona uma conta na lista e remove-a depois de confirmar (AC-1).
- [ ] A lista atualiza-se. A conta removida sai da tabela.
- [ ] Conta inexistente, conta com registos associados e a própria conta do gestor mostram a mensagem do serviço (AC-2, AC-3, AC-5). Nenhuma regra no controlador (RNF-07).
- [ ] O botão só aparece no menu do gestor (AC-4, `Permission.MANAGE_STAFF`).
- [ ] Texto em português.

## Interface (UI)

- Botão "Remover conta" no ecrã de contas, para a linha selecionada, só para o gestor.
- Pergunta de confirmação: "Remover a conta de ana? Esta ação não pode ser desfeita."
- Recusa por registos associados: "A conta tem registos associados e não pode ser removida." Mensagem de erro a vermelho. Os textos vêm do serviço.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; jira-publisher, Claude Code (claude-sonnet-5-5), 2026-11-13. Reviewed by: pending.
