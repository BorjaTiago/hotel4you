---
type: story
summary: Configurar os itens de um quarto
parent: epic-rooms-pricing
requirements: [RF-08]
points: 5
points_suggested: 5
status: approved
jira_key:
labels: [rooms, 1st-delivery]
sprint:
---

# Título: Configurar os itens de um quarto

Como gestor,
Eu quero configurar os itens de cada quarto,
Para que cada quarto tenha o equipamento e o material corretos.

## Critérios de aceite

### Cenário AC-1: Adicionar um item
  Dado que o gestor está autenticado
  E o quarto 201 existe
  Quando o gestor adiciona o item "secador de cabelo" ao quarto 201
  Então o quarto 201 passa a listar o item "secador de cabelo"

### Cenário AC-2: Remover um item
  Dado que o quarto 201 tem o item "sofá"
  Quando o gestor remove o item "sofá" do quarto 201
  Então o quarto 201 deixa de listar o item "sofá"

### Cenário AC-3: Quarto inexistente
  Dado que o gestor está autenticado
  Quando o gestor configura itens de um quarto que não existe
  Então o sistema recusa a ação
  E informa que o quarto não existe

### Cenário AC-4: Remoção recusada por associações (Q-36)
  Dado que o item "sofá" do quarto 201 tem associações a outros dados do sistema
  Quando o gestor tenta remover o item "sofá"
  Então o sistema recusa a remoção
  E o item mantém-se no quarto 201
  E informa que o item tem associações

### Cenário AC-5: Papel sem permissão
  Dado que um rececionista está autenticado
  Quando o rececionista tenta alterar os itens de um quarto
  Então o sistema recusa a ação

## Notas
Itens do enunciado: toalhas, condicionador, champô, secador, televisão, ar condicionado, cama, sofá, lençóis, cofre e outros. O enunciado diz que o gestor pode configurar todos. Depende de story-register-rooms.

Q-36 (decisão do utilizador, 2026-10-16): nada com associações se remove; AC-4 acrescentado pelo refinador.
points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: New item data model, add and remove, block removal when associations exist, role check and GUI. Association rule needs a check across tables.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
