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
sprint:
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
  Dado que o tipo simples tem preço base 50,00
  Quando o gestor altera o preço base do tipo simples para 55,00
  Então o sistema mostra 55,00 para o tipo simples

### Cenário AC-3: Preço inválido
  Dado que o gestor está autenticado
  Quando o gestor define um preço negativo ou não numérico
  Então o sistema recusa o preço
  E o preço anterior mantém-se

### Cenário AC-4: Papel sem permissão
  Dado que um rececionista está autenticado
  Quando o rececionista tenta alterar o preço base de um tipo
  Então o sistema recusa a ação

## Notas
Q-11 (decisão da equipa, não confirmada pelo professor): o gestor define o preço base por tipo. Preços extra (vista, bebidas, lavandaria) ficam em epic-extra-services-pricing. Depende de story-register-rooms (tipos).

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: Validate and save a price per room type, edit it, role check. Small data model.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
