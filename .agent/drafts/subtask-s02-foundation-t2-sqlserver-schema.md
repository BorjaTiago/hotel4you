---
type: subtask
summary: T2 - Ligação SQL Server e scripts do esquema H4Y-10 (versão do PR #4)
parent: H4Y-11
requirements: [RNF-12]
points:
points_suggested:
status: published
jira_key: H4Y-33
labels: [foundation, database, 1st-delivery]
sprint: 02
branch: feat-033-sqlserver-schema-scripts
---

# T2: Ligação SQL Server e scripts do esquema

Origem: `epic-foundation-architecture.md`, tarefa T2. Precisa de T0a (configuração e interfaces de repositório). Usar o esquema do PR #4 já integrado em `docs/database/`, não o antigo. Corre em paralelo com auth T1 (hash).
Responsável proposto: Lukas Sperandio (a equipa decide).

## Checklist

- [ ] A ligação JDBC lê as credenciais da configuração local ou do ambiente. Nenhuma credencial no Git (RNF-12).
- [ ] Os scripts criam o esquema de `docs/database/` (versão do PR #4). Verificar primeiro os ficheiros integrados.
- [ ] Com uma base de dados vazia, a primeira preparação cria todas as tabelas.
- [ ] Um registo guardado continua lá depois de a aplicação fechar e abrir.
- [ ] Com o SQL Server em baixo, a aplicação mostra uma mensagem clara e nenhum detalhe da ligação.
- [ ] Existe um `UserRepository` JDBC (necessário para o login e para create-staff-account).
- [ ] Os passos de preparação para a equipa estão escritos em `docs/` (sem palavra-passe no texto).
- [ ] Verificar os dados dos papéis: 5 papéis (Q-46). Comunicar qualquer falha ao Team Leader.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
