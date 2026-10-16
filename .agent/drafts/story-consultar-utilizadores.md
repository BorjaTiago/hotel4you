---
type: story
summary: Guardar e consultar todos os utilizadores
parent: epic-authentication-users
requirements: [RNF-01]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [security, 1st-delivery]
sprint:
---

# Título: Guardar e consultar todos os utilizadores

Como gestor,
Eu quero consultar a qualquer momento qualquer utilizador do sistema,
Para que saiba quem tem acesso à aplicação.

## Critérios de aceite

### Cenário AC-1: Lista de utilizadores
  Dado que o gestor tem sessão iniciada
  E existem utilizadores guardados
  Quando o gestor pede a lista de utilizadores
  Então o sistema mostra todos os utilizadores guardados
  E mostra o papel de cada um

### Cenário AC-2: Utilizadores persistem após reiniciar
  Dado que existem utilizadores guardados
  Quando a aplicação é fechada e aberta de novo
  Então os utilizadores continuam disponíveis

### Cenário AC-3: Consultar um utilizador
  Dado que o utilizador "ana" existe
  Quando o gestor consulta o utilizador "ana"
  Então o sistema mostra os dados desse utilizador

### Cenário AC-4: Utilizador inexistente
  Dado que o utilizador "xpto" não existe
  Quando o gestor consulta o utilizador "xpto"
  Então o sistema indica que o utilizador não existe

## Critérios de aceite (checklist)

- [ ] AC-1: O gestor vê a lista de utilizadores existentes.
- [ ] AC-2: Os utilizadores continuam disponíveis depois de reiniciar a aplicação.
- [ ] AC-3: O gestor consulta os dados de um utilizador concreto.
- [ ] AC-4: Consultar um utilizador inexistente devolve uma resposta de não encontrado.

## Notas
RNF-01 (todos os utilizadores guardados e acessíveis). Decisão da equipa (2026-10-16): só o gestor consulta os utilizadores. Criar, editar e remover contas: epic-staff-management-access (RF-26).
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: utilizadores de teste próprios (foundation T4), sessão de gestor simulada (T0); na demo, o gestor inicial (auth T2).

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: List and view users from the database with a manager screen. Persistence check after restart. Simple rules.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
