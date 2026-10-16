---
type: subtask
summary: T2 - Script seed dos 5 papéis e da conta do primeiro gestor
parent: H4Y-12
requirements: [RF-01, RF-02, RNF-02]
points:
points_suggested:
status: published
jira_key: H4Y-30
labels: [security, database, 1st-delivery]
sprint: 02
branch: feat-030-seed-roles-first-manager
---

# Auth T2: Seed dos papéis e do primeiro gestor

Origem: `epic-authentication-users.md`, tarefa T2. Precisa de foundation T2 (esquema) e auth T1 (hash). Os logins usam dados de teste nos seus testes, por isso não esperam por este cartão. O login real de ponta a ponta precisa dele.
Responsável proposto: Antonio Martinho (a equipa decide).

## Checklist

- [ ] Com a base de dados vazia, o script cria os 5 papéis do RF-02. Cliente e fornecedor não recebem conta (Q-13, Q-17).
- [ ] O script cria uma conta de gestor. O nome de utilizador e a palavra-passe vêm da configuração local ou do ambiente. Nunca do código, do Git ou da documentação.
- [ ] A palavra-passe é guardada só como hash. O valor em texto não aparece em nenhum log.
- [ ] Sem os valores de configuração, o script para com uma mensagem clara. Não cria uma palavra-passe por omissão.
- [ ] O script corre duas vezes sem duplicar papéis nem contas.
- [ ] O `.gitignore` cobre o ficheiro de configuração local. Um ficheiro de exemplo não tem nenhum valor real.
- [ ] Teste JUnit da lógica do seed no repositório em memória.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
