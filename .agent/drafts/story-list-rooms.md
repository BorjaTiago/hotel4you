---
type: story
summary: Consultar a lista de quartos
parent: epic-rooms-pricing
requirements: [RF-04, RF-06]
points: 2
points_suggested: 2
status: published
jira_key: H4Y-50
labels: [rooms, 1st-delivery]
sprint: 03
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

## Critérios de aceite (checklist)

- [ ] AC-1: A lista mostra todos os quartos registados.
- [ ] AC-2: A lista pode ser filtrada por tipo de quarto.
- [ ] AC-3: Sem quartos registados, a lista vem vazia sem erro.
- [ ] AC-4: Um pedido sem autenticação é recusado.

## Notas
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: quartos e preços base de teste próprios (foundation T4). Na demo, os quartos vêm de story-register-rooms e os preços de story-set-base-price (sprint 3). O rececionista só consulta, não altera.

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: Same as the reference, plus the base price column. Read-only list with a type filter.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: lukassperandio. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; sprint field by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
