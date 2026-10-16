---
type: epic
summary: Login, user roles and secure user storage
parent:
requirements: [RF-01, RF-02, RNF-01, RNF-02]
points:
points_suggested:
status: published
jira_key: H4Y-12
labels: [security, login, 1st-delivery]
sprint:
---

# Epic: Authentication, roles and users

## Goal
Hotel staff, restaurant staff and managers log in with user name and password. Each role has its own access.

## Delivery
1st delivery (login).

## Scope
- Login for receptionist, manager and restaurant staff (Q-23).
- Five actor types and their access rules.
- All users saved and readable at any time.
- Passwords saved encrypted.
- Same error for wrong password and unknown account.

## Out of scope
- Client and supplier login (they do not log in, Q-13, Q-17).
- Staff account management screens (epic-staff-management).
- Floor and zone limits (epic-staff-access-rules).

## Actors
- Receptionist, manager, restaurant staff.

## Value
Only known users enter the system. Passwords are safe.

## Statement links
Section 2, 3a, 3b. RF-01, RF-02, RNF-01, RNF-02.

## Candidate stories
- Iniciar sessão com utilizador e palavra-passe (actor: receptionist; RF-01)
- Iniciar sessão como gestor (actor: manager; RF-01)
- Iniciar sessão como pessoal do restaurante (actor: restaurant staff; RF-01)
- Guardar palavras-passe cifradas (actor: manager; RNF-02)
- Guardar e consultar todos os utilizadores (actor: manager; RNF-01)
- Mostrar apenas as funções permitidas a cada papel (actor: receptionist; RF-02)

## Open questions
- None new. Q-13, Q-17, Q-23 are team decisions, not confirmed by the teacher.

## Tarefas técnicas (candidatas a sub-tarefas)
Decisão da equipa (2026-10-16): trabalho técnico sem ator de negócio é tarefa, não história. Fica ligado a esta épica.

### T1: Guardar palavras-passe cifradas (RNF-02)
- [ ] Ao criar um utilizador, a palavra-passe guardada não está em texto simples e difere da introduzida.
- [ ] Com a palavra-passe correta, o início de sessão é aceite.
- [ ] Com palavra-passe errada, o acesso é recusado (mesmo erro que conta desconhecida).
- [ ] O gestor, ao consultar um utilizador, não vê a palavra-passe (confirmar no refinamento).
- [ ] Se usar biblioteca externa, justificar em docs/libraries.md (RNF-11).

### T2: Dados iniciais: 5 papéis e a conta do primeiro gestor (RF-02, RF-01, RNF-02)
Sprint 2, depois de epic-foundation-architecture T0 e T2 e depois de T1 (cifra). Antes das histórias de login. Decisão do utilizador (2026-10-16).
- [ ] Com a base vazia, o script cria os 5 papéis do RF-02: cliente, rececionista, gestor, funcionário do restaurante, fornecedor. Cliente e fornecedor não recebem conta (Q-13, Q-17).
- [ ] O script cria 1 conta de gestor. O nome de utilizador e a palavra-passe vêm da configuração local ou de variáveis de ambiente, nunca do código, do Git ou da documentação.
- [ ] A palavra-passe fica guardada só como hash (T1). O valor em texto não aparece em logs.
- [ ] Sem configuração da conta inicial, o script para com uma mensagem clara e não cria uma conta com palavra-passe por omissão.
- [ ] O script corre duas vezes sem duplicar papéis nem a conta.
- [ ] O gestor inicial cria as outras contas com story-create-staff-account (sprint 2).

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; task T2 by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16. Reviewed by: pending.
