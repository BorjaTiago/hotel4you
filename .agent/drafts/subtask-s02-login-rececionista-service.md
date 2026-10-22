---
type: subtask
summary: Serviço de login - verificar credenciais, criar sessão, erro genérico
parent: H4Y-22
requirements: [RF-01, RF-02]
points:
points_suggested:
status: published
jira_key: H4Y-46
labels: [security, backend, login, 1st-delivery]
sprint: 02
branch: feat-022-login-service
---

# login-rececionista: serviço de login

História pai H4Y-22 (5 pontos). Precisa de H4Y-32, que já traz a lógica de login (`AuthService.login`), o hash e o `JdbcUserRepository`. Este cartão acrescenta só o que H4Y-22 pede por cima. Os testes usam utilizadores de teste próprios, por isso o cartão não espera pelo seed. A chave 022 do ramo é a chave da história.
Responsável proposto: Antonio Martinho (a equipa decide).

## Checklist

- [ ] `AuthService.login` (de H4Y-32) verifica o nome de utilizador e a palavra-passe com hash (AC-1). O utilizador com sessão iniciada fica em `HotelApp.currentUser` e tem o papel.
- [ ] Uma palavra-passe errada e um utilizador desconhecido dão o mesmo erro "Credenciais inválidas" (AC-2, AC-3).
- [ ] Nome de utilizador ou palavra-passe vazios são recusados antes de qualquer pesquisa (AC-4).
- [ ] O serviço não tem código JavaFX. Os logins do gestor e do restaurante reutilizam-no.
- [ ] Um ficheiro de dados de teste com um rececionista.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-19. Reviewed by: pending.
