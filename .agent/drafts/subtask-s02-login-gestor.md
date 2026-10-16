---
type: subtask
summary: Login do gestor - verificação do papel, testes e Javadoc
parent: H4Y-20
requirements: [RF-01, RF-02]
points:
points_suggested:
status: published
jira_key: H4Y-44
labels: [login, tests, 1st-delivery]
sprint: 02
branch: feat-020-login-manager
---

# login-gestor: login do gestor

História pai H4Y-20 (2 pontos). Ligação forte: reutiliza o serviço de login de H4Y-22. Começar quando o serviço de login estiver integrado (meio do sprint). A chave 020 do ramo é a chave da história.
Responsável proposto: José Cruz (a equipa decide).

## Checklist

- [ ] AC-1: Um gestor com credenciais válidas inicia sessão e o papel da sessão é gestor.
- [ ] AC-2: Uma palavra-passe errada é recusada com "Credenciais inválidas".
- [ ] AC-3: Um utilizador desconhecido recebe a mesma mensagem de AC-2.
- [ ] Sem um segundo caminho de código de login. Reutilizar o serviço e o ecrã de H4Y-22.
- [ ] Dados de teste: um gestor (T4). Os testes citam "H4Y-20 AC-n".
- [ ] Javadoc em qualquer nova classe pública. O Javadoc compila.
- [ ] Depois de auth T2: o gestor do seed consegue iniciar sessão (verificação manual, o segredo fica local).

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
