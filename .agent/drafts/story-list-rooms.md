---
type: story
summary: Consultar a lista de quartos
parent: epic-rooms-pricing
requirements: [RF-04, RF-06]
points: 2
points_suggested: 2
status: approved
jira_key:
labels: [rooms, 1st-delivery]
sprint:
---

# Título: Consultar a lista de quartos

Como rececionista,
Eu quero consultar a lista de quartos com tipo, piso e preço base,
Para que informe o cliente sobre os quartos do hotel.

## Critérios de aceite

### Cenário AC-1: Listar todos os quartos
  Dado que o rececionista está autenticado
  E os quartos estão registados
  Quando o rececionista consulta a lista de quartos
  Então o sistema mostra cada quarto com tipo, piso e preço base por noite

### Cenário AC-2: Filtrar por tipo
  Dado que o rececionista está autenticado
  Quando o rececionista filtra a lista pelo tipo familiar
  Então o sistema mostra apenas os quartos familiares

### Cenário AC-3: Sem quartos registados
  Dado que nenhum quarto está registado
  Quando o rececionista consulta a lista de quartos
  Então o sistema mostra uma lista vazia
  E informa que não há quartos

### Cenário AC-4: Sem autenticação
  Dado que o utilizador não está autenticado
  Quando tenta consultar a lista de quartos
  Então o sistema recusa o acesso

## Notas
Depende de story-register-rooms e story-set-base-price. O rececionista só consulta, não altera.

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: Same as the reference, plus the base price column. Read-only list with a type filter.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
