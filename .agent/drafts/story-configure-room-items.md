---
type: story
summary: Configurar os itens de um quarto
parent: epic-rooms-pricing
requirements: [RF-08]
points: 
points_suggested: 5
status: draft
jira_key:
labels: [rooms, 1st-delivery]
sprint:
---

# Título: Configurar os itens de um quarto

Como gestor,
Eu quero configurar os itens de cada quarto,
Para que cada quarto tenha o equipamento e o material corretos.

## Critérios de aceite

### Cenário AC-1: Adicionar um item com quantidade
  Dado que o gestor está autenticado
  E o quarto 201 existe
  E o item "toalhas" existe no catálogo de stock
  Quando o gestor adiciona 2 unidades do item "toalhas" ao quarto 201
  Então o quarto 201 passa a listar o item "toalhas" com quantidade 2

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

### Cenário AC-6: Alterar a quantidade de um item
  Dado que o gestor está autenticado
  E o quarto 201 tem 2 unidades do item "toalhas"
  Quando o gestor altera a quantidade do item "toalhas" do quarto 201 para 3
  Então o quarto 201 lista o item "toalhas" com quantidade 3

### Cenário AC-7: Quantidade inválida
  Dado que o gestor está autenticado
  E o quarto 201 tem 2 unidades do item "toalhas"
  Quando o gestor define a quantidade do item "toalhas" como 0, um número negativo ou um valor que não é número
  Então o sistema recusa a ação
  E informa que a quantidade é inválida
  E o quarto 201 mantém o item "toalhas" com quantidade 2

### Cenário AC-8: Item fora do catálogo de stock
  Dado que o gestor está autenticado
  E o quarto 201 existe
  E o item "piano" não existe no catálogo de stock
  Quando o gestor adiciona o item "piano" ao quarto 201
  Então o sistema recusa a ação
  E informa que o item não existe no catálogo
  E o quarto 201 não muda

## Critérios de aceite (checklist)

- [ ] AC-1: Um item do catálogo pode ser adicionado a um quarto com quantidade (2 toalhas no quarto 201).
- [ ] AC-2: Um item pode ser removido de um quarto.
- [ ] AC-3: Configurar um quarto inexistente é recusado.
- [ ] AC-4: A remoção de um item com associações é recusada.
- [ ] AC-5: Um papel sem permissão é recusado ao configurar itens.
- [ ] AC-6: A quantidade de um item já no quarto pode ser alterada (2 para 3).
- [ ] AC-7: Quantidade 0, negativa ou que não é número é recusada e o quarto não muda.
- [ ] AC-8: Um item fora do catálogo de stock é recusado.

## Notas
Itens do enunciado: toalhas, condicionador, champô, secador, televisão, ar condicionado, cama, sofá, lençóis, cofre e outros. O enunciado diz que o gestor pode configurar todos. Dependências (auditoria 2026-10-16): nenhuma forte. Bloqueio escondido corrigido: os itens do quarto usam o catálogo de itens (esquema H4Y-10, `room_items` -> `items`), que só existia no sprint 11; story-cadastrar-item-stock passa para o sprint 4. Construir e testar sozinha: quartos e itens de teste próprios (foundation T4). Na demo: quartos de story-register-rooms (sprint 3) e itens do catálogo (sprint 4).

Q-36 (decisão do utilizador, 2026-10-16): nada com associações se remove; AC-4 acrescentado pelo refinador.

Quantidade (2026-11-06): o esquema `room_items` guarda a quantidade de cada item por quarto (`quantity TINYINT NOT NULL`, `quantity > 0`). AC-1 passa a indicar a quantidade; AC-6, AC-7 e AC-8 são novos. Os itens vêm do catálogo de stock (H4Y-51, story-cadastrar-item-stock). Relacionado: RF-29 (nota apenas; não consta em `requirements`).

Q-58 (answered: No): a quantidade de um item no quarto não mexe no stock. Sem dependência de registar-entrada-stock. Sem critério novo.

Reestimar: o âmbito cresceu (quantidade). A equipa reestima e define `points`; `points_suggested` mantém-se. Estado `draft` porque um humano deve aprovar os critérios alterados.
points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: New item data model, add and remove, block removal when associations exist, role check and GUI. Association rule needs a check across tables.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-11-06; backlog-prioritizer, Claude Code (claude-sonnet-5-5), 2026-11-06. Updated by: BorjaTiago. Reviewed by: pending.
