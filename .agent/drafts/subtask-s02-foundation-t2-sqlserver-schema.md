---
type: subtask
summary: T2 - Restantes tabelas do esquema H4Y-10 (as tabelas de papéis e utilizadores estão em H4Y-32)
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

# T2: Restantes tabelas do esquema

Origem: `epic-foundation-architecture.md`, tarefa T2. Âmbito reduzido: H4Y-32 já traz a ligação JDBC (`Database`), as tabelas de papéis e utilizadores e o `JdbcUserRepository`. Este cartão (H4Y-33) cobre só as outras tabelas do esquema de `docs/database/`. Precisa de H4Y-32.
Responsável proposto: Lukas Sperandio (a equipa decide).

## Checklist

- [ ] Os scripts criam as restantes tabelas de `docs/database/` (todas menos papéis e utilizadores, que estão em H4Y-32). Verificar primeiro os ficheiros integrados.
- [ ] Com uma base de dados vazia, a preparação cria todas as tabelas, incluindo as de H4Y-32.
- [ ] Um registo guardado continua lá depois de a aplicação fechar e abrir.
- [ ] Com o SQL Server em baixo, a aplicação mostra uma mensagem clara e nenhum detalhe da ligação.
- [ ] Os passos de preparação para a equipa estão escritos em `docs/` (sem palavra-passe no texto).

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-19. Reviewed by: pending.
