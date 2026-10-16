---
type: subtask
summary: T0a - Esqueleto do projeto Java, classes de domínio e interfaces base
parent: H4Y-11
requirements: [RNF-05, RNF-07, RNF-08, RNF-12]
points:
points_suggested:
status: published
jira_key: H4Y-32
labels: [foundation, backend, 1st-delivery]
sprint: 02
branch: feat-032-project-skeleton-core
---

# T0a: Esqueleto do projeto, classes de domínio e interfaces base

Origem: `epic-foundation-architecture.md`, tarefa T0, pontos 1 a 3. Primeiro cartão do sprint 2. Todos os outros cartões esperam só pelo fim deste cartão (objetivo: fim do dia 2). Chave NNN do ramo: a chave Jira ainda não existe. Escrever o número real depois de publicar.
Responsável proposto: Lukas Sperandio (a equipa decide).

## Checklist

- [ ] Existem o ficheiro de build e a organização de pacotes: `domain`, `service`, `data`, `gui`, `config`. O projeto compila.
- [ ] Existem as classes de domínio e os enums: `Role` (5 tipos), `User`, `Session`, `Floor`, `Location`, `RoomType`, `Room`, `Customer`, `Reservation`, `AccessCard`, `AccessLog`, `Item`, `StockMovement` e as outras de T0. Sem SQL e sem JavaFX em `domain`.
- [ ] Existem as interfaces de serviço: `AuthenticationService`, `PasswordHasher`, `PermissionPolicy`, `UserService` e as outras de T0. Só assinaturas.
- [ ] Existem as interfaces de repositório com uma versão em memória para testes (`UserRepository` primeiro).
- [ ] `config` lê os valores da base de dados e do primeiro gestor da configuração local ou do ambiente. Nenhuma credencial no Git.
- [ ] Javadoc curto em todas as classes públicas (RNF-09).
- [ ] Integrar no ramo partilhado até ao fim do dia 2. Avisar a equipa.

AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
