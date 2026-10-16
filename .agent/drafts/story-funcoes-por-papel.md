---
type: story
summary: Mostrar apenas as funções permitidas ao papel
parent: epic-authentication-users
requirements: [RF-02]
points: 3
points_suggested: 3
status: published
jira_key: H4Y-23
labels: [security, login, 1st-delivery]
sprint:
---

# Título: Mostrar apenas as funções permitidas ao papel

Como rececionista,
Eu quero ver e usar apenas as funções do meu papel,
Para que não aceda a funções de outros papéis.

## Critérios de aceite

### Cenário AC-1: Funções do rececionista
  Dado que o rececionista iniciou sessão
  Quando o sistema mostra as opções disponíveis
  Então mostra apenas as funções permitidas ao rececionista

### Cenário AC-2: Função de gestor recusada
  Dado que o rececionista iniciou sessão
  Quando o rececionista tenta usar uma função exclusiva do gestor
  Então o sistema recusa o acesso
  E não executa a função

### Cenário AC-3: Sem sessão
  Dado que ninguém iniciou sessão
  Quando alguém tenta usar uma função da aplicação
  Então o sistema pede o início de sessão

## Critérios de aceite (checklist)

- [ ] AC-1: O rececionista consegue executar as funções do seu papel.
- [ ] AC-2: Uma função de gestor é recusada ao rececionista.
- [ ] AC-3: Sem sessão, qualquer função protegida é recusada.

## Notas
Cada tipo de ator tem o seu acesso (RF-02). Esta história cria a regra geral por papel. Limites por piso (RF-24) e do restaurante (RF-25) ficam em epic-staff-management-access. A lista exata de funções por papel vem das épicas de funcionalidade. Nenhuma pergunta nova.

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: General role-permission rule used by many features. Some logic, one or two layers. Function list per role is open.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
