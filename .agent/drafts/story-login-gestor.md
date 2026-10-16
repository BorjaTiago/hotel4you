---
type: story
summary: Iniciar sessão como gestor
parent: epic-authentication-users
requirements: [RF-01, RF-02]
points: 2
points_suggested: 2
status: published
jira_key: H4Y-20
labels: [security, login, 1st-delivery]
sprint:
---

# Título: Iniciar sessão como gestor

Como gestor,
Eu quero iniciar sessão com nome de utilizador e palavra-passe,
Para que apenas eu aceda às funções de gestão.

## Critérios de aceite

### Cenário AC-1: Credenciais válidas
  Dado que existe um gestor com nome de utilizador e palavra-passe
  Quando o gestor introduz as credenciais corretas
  Então o sistema inicia a sessão
  E identifica o utilizador como gestor

### Cenário AC-2: Credenciais inválidas
  Dado que existe um gestor com nome de utilizador
  Quando o gestor introduz uma palavra-passe errada
  Então o sistema recusa o acesso
  E mostra a mensagem genérica "Credenciais inválidas"

### Cenário AC-3: Utilizador desconhecido
  Dado que o nome de utilizador não existe
  Quando alguém tenta iniciar sessão
  Então o sistema recusa o acesso
  E mostra a mesma mensagem do AC-2

## Notas
Fase: 1.ª entrega (Q-06). Reutiliza o mecanismo de "Iniciar sessão como rececionista"; esta história confirma o papel gestor. Funções exclusivas do gestor (por exemplo apagar reservas, RF-22) ficam nas respetivas épicas.

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: Reuses the login mechanism of the receptionist story. Only confirms the manager role.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
