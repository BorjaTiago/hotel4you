---
type: story
summary: Iniciar sessão como rececionista
parent: epic-authentication-users
requirements: [RF-01, RF-02]
points: 5
points_suggested: 5
status: published
jira_key: H4Y-22
labels: [security, login, 1st-delivery]
sprint:
---

# Título: Iniciar sessão como rececionista

Como rececionista,
Eu quero iniciar sessão com nome de utilizador e palavra-passe,
Para que apenas utilizadores conhecidos usem as funções da receção.

## Critérios de aceite

### Cenário AC-1: Credenciais válidas
  Dado que existe um rececionista com nome de utilizador e palavra-passe
  Quando o rececionista introduz as credenciais corretas
  Então o sistema inicia a sessão
  E identifica o utilizador como rececionista

### Cenário AC-2: Palavra-passe errada
  Dado que existe um rececionista com nome de utilizador
  Quando o rececionista introduz uma palavra-passe errada
  Então o sistema recusa o acesso
  E mostra a mensagem genérica "Credenciais inválidas"

### Cenário AC-3: Utilizador desconhecido
  Dado que o nome de utilizador não existe
  Quando alguém tenta iniciar sessão
  Então o sistema recusa o acesso
  E mostra a mesma mensagem do AC-2

### Cenário AC-4: Campos vazios
  Quando o rececionista confirma sem nome de utilizador ou palavra-passe
  Então o sistema recusa o pedido
  E não inicia a sessão

## Critérios de aceite (checklist)

- [ ] AC-1: O rececionista com credenciais válidas inicia sessão com sucesso.
- [ ] AC-2: Uma palavra-passe errada é recusada sem iniciar sessão.
- [ ] AC-3: Um utilizador desconhecido é recusado com a mesma resposta da palavra-passe errada.
- [ ] AC-4: Campos vazios são recusados com mensagem de validação.

## Notas
Fase: 1.ª entrega (Q-06). Clientes e fornecedor não iniciam sessão (Q-13, Q-17). Erro igual para palavra-passe errada e conta desconhecida (epic). Gestão de contas está em epic-staff-management-access.
Dependências (auditoria 2026-10-16): o primeiro gestor vem dos dados iniciais (seed). As outras contas vêm de "Criar uma conta de funcionário" (create-staff-account).

points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: Builds the base login: user table, password hashing, session, login screen, generic error. Logic, data and GUI together. Other login stories reuse it.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by jira-publisher, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
