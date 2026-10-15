---
type: story
summary: Consultar os pisos e zonas do hotel
parent: epic-rooms-pricing
requirements: [RF-03]
points: 2
points_suggested: 2
status: approved
jira_key:
labels: [rooms, 1st-delivery]
sprint:
---

# Título: Consultar os pisos e zonas do hotel

Como gestor,
Eu quero consultar os pisos do hotel e as zonas de cada piso,
Para que os quartos e os serviços fiquem associados ao piso correto.

## Critérios de aceite

### Cenário AC-1: Consultar a estrutura do hotel
  Dado que o gestor está autenticado
  Quando o gestor consulta os pisos do hotel
  Então o sistema mostra os pisos -1, 0, 1, 2 e 3
  E o piso -1 inclui garagem e arrecadação
  E o piso 0 inclui receção, bar e salas de conferência
  E o piso 1 inclui restaurante e piscina
  E os pisos 2 e 3 são pisos de quartos

### Cenário AC-2: Piso inexistente
  Dado que o gestor está autenticado
  Quando o gestor pede o piso 4
  Então o sistema recusa o pedido
  E informa que o piso não existe

## Notas
Enunciado, secção 2. A criação, edição e remoção de zonas pelo gestor está em epic-magnetic-card-access (criar, editar e remover zona). O uso das zonas pelo cartão fica na épica epic-magnetic-card-access (entrega final, Q-06). Depende do login (RF-01).

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: Fixed floors and zones, read-only view and one error case. Seed data plus a simple list.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
