---
type: story
summary: Limitar a receção aos serviços dos pisos 0 e 1
parent: epic-staff-management-access
requirements: [RF-24]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [access-control]
sprint:
---

# Título: Limitar a receção aos serviços dos pisos 0 e 1

Como rececionista,
Eu quero aceder apenas aos serviços dos pisos 0 e 1,
Para que o meu acesso corresponda ao meu papel.

## Critérios de aceite

### Cenário AC-1: Serviço do piso 0 ou 1
  Dado que o rececionista tem sessão iniciada
  Quando abrir um serviço dos pisos 0 e 1
  Então o sistema permite o acesso

### Cenário AC-2: Serviço fora dos pisos 0 e 1
  Dado que o rececionista tem sessão iniciada
  Quando tentar abrir um serviço de outro piso
  Então o sistema recusa o acesso
  E mostra uma mensagem de acesso não permitido

### Cenário AC-3: Quarto de hóspede sem cartão mestre
  Dado que o rececionista não usa um cartão mestre do gestor
  Quando tentar entrar num quarto de hóspede
  Então o sistema recusa a entrada

### Cenário AC-4: Gestor não limitado
  Dado que o utilizador autenticado é gestor
  Quando abrir um serviço de qualquer piso
  Então o sistema não aplica o limite dos pisos 0 e 1

## Notas
O limite aplica-se só à receção (Q-08). Os serviços de cada piso vêm de RF-03. AC-3 depende do cartão mestre (epic-master-card-audit, RF-23). Fase: Q-25.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Regra de acesso por piso para a receção, gestor sem limite, quarto só com cartão mestre. Depende das zonas e do cartão mestre. Lógica curta.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
