---
type: story
summary: Definir o preço base por noite de cada tipo de quarto
parent: epic-rooms-pricing
requirements: [RF-06]
points: 3
points_suggested: 3
status: published
jira_key: H4Y-26
labels: [rooms, pricing, 1st-delivery]
sprint: 03
---

# Título: Definir o preço base por noite de cada tipo de quarto

Como gestor,
Eu quero definir o preço base por noite de cada tipo de quarto,
Para que as reservas usem o preço correto do quarto.

## Critérios de aceite

### Cenário AC-1: Definir o preço de um tipo
  Dado que o gestor está autenticado
  Quando o gestor define o preço base do tipo duplo como 80,00 por noite
  Então o sistema guarda o preço base do tipo duplo
  E a consulta de preços mostra 80,00 para o tipo duplo

### Cenário AC-2: Alterar um preço existente
  Dado que o tipo individual tem preço base 50,00
  Quando o gestor altera o preço base do tipo individual para 55,00
  Então o sistema mostra 55,00 para o tipo individual

### Cenário AC-3: Preço inválido
  Dado que o gestor está autenticado
  Quando o gestor define um preço negativo ou não numérico
  Então o sistema recusa o preço
  E o preço anterior mantém-se

### Cenário AC-4: Papel sem permissão
  Dado que um rececionista está autenticado
  Quando o rececionista tenta alterar o preço base de um tipo
  Então o sistema recusa a ação

## Critérios de aceite (checklist)

- [ ] AC-1: O preço base de um tipo de quarto pode ser definido.
- [ ] AC-2: O preço base de um tipo de quarto pode ser alterado.
- [ ] AC-3: Um preço inválido é recusado.
- [ ] AC-4: Um papel sem permissão é recusado ao definir preços.

## Notas
Q-11 (decisão da equipa, não confirmada pelo professor): o gestor define o preço base por tipo. Preços extra (vista, bebidas, lavandaria) ficam em epic-extra-services-pricing.
Dependências (auditoria 2026-10-16): depende da tarefa T5 (tipos de quarto). Não depende de story-register-rooms.

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: Validate and save a price per room type, edit it, role check. Small data model.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by jira-publisher, Claude Code (claude-sonnet-5-5), 2026-10-16; sprint field by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. H4Y-26 base price by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30. Reviewed by: pending.
