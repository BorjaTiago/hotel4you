---
type: subtask
summary: 23.2 Ecrã: menu da janela principal usa a regra por papel e pede login sem sessão
parent: H4Y-23
requirements: [RF-02, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-61
labels: [security, gui, 1st-delivery]
sprint: 03
branch: feat/H4Y-61-role-menu
---

# funcoes-por-papel: menu por papel

História pai H4Y-23. Depende de 23.1 (mesmo dono, faz-se a seguir). Fecha o ponto pendente de H4Y-42: "Voltar a verificar AC-3 depois de funcoes-por-papel".
Responsável proposto: Lukas Sperandio (a equipa decide).

## Checklist

- [ ] `MainController` mostra só os botões que a regra de 23.1 permite. Nenhuma regra na GUI.
- [ ] Os botões novos do sprint 3 (pisos, quartos, preços) seguem a mesma regra.
- [ ] Sem sessão, a aplicação abre o ecrã de login (AC-3).
- [ ] Verificação manual com as contas de gestor, rececionista e restaurante. Registar a evidência no PR.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
