---
type: subtask
summary: Ecrã de login JavaFX com mensagens de validação
parent: H4Y-22
requirements: [RF-01, RF-02]
points:
points_suggested:
status: published
jira_key: H4Y-45
labels: [login, gui, 1st-delivery]
sprint: 02
branch: feat-022-login-screen
---

# login-rececionista: ecrã de login

História pai H4Y-22. Usa a estrutura de T0b e o resultado de login simulado (stub) até o serviço estar integrado. A chave 022 do ramo é a chave da história.
Responsável proposto: José Cruz (a equipa decide).

## Checklist

- [ ] O ecrã tem nome de utilizador, palavra-passe (mascarada) e um botão de login.
- [ ] Após um login válido, a aplicação abre a janela principal com a sessão (liga a `janela-principal`).
- [ ] O ecrã mostra "Credenciais inválidas" para AC-2 e AC-3, com o mesmo texto.
- [ ] O ecrã mostra uma mensagem de validação para campos vazios (AC-4).
- [ ] O ecrã não mostra a palavra-passe nem qualquer detalhe interno.
- [ ] O ecrã é o primeiro ecrã quando não existe sessão.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
