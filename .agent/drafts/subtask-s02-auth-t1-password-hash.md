---
type: subtask
summary: T1 - Armazenamento cifrado de palavras-passe (PasswordHasher)
parent: H4Y-12
requirements: [RNF-02]
points:
points_suggested:
status: published
jira_key: H4Y-29
labels: [security, backend, 1st-delivery]
sprint: 02
branch: feat-029-password-hasher
---

# Auth T1: Palavras-passe cifradas

Origem: `epic-authentication-users.md`, tarefa T1. Precisa de T0a (interface `PasswordHasher`). Tem de terminar antes do seed (auth T2). Corre em paralelo com foundation T2.
Responsável proposto: Antonio Martinho (a equipa decide).

## Checklist

- [ ] `PasswordHasher` implementa um hash com salt. O valor guardado não é texto simples e difere do valor introduzido.
- [ ] Uma palavra-passe correta é verificada. Uma palavra-passe errada não é verificada.
- [ ] O mesmo erro cobre palavra-passe errada e conta desconhecida.
- [ ] Nenhuma palavra-passe aparece num log ou numa mensagem de erro.
- [ ] Se for usada uma biblioteca externa, acrescentá-la a `docs/libraries.md` com nome, versão, motivo e alternativa rejeitada (RNF-11).
- [ ] Testes JUnit e Javadoc da classe.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
