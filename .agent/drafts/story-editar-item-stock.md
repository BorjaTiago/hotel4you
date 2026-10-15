---
type: story
summary: Editar item de stock
parent: epic-stock-management
requirements: [RF-28, RF-29]
points: 2
points_suggested: 2
status: approved
jira_key:
labels: [stock, final-delivery]
sprint:
---

# Título: Editar item de stock

Como gestor,
Eu quero editar o nome e a unidade de um item de stock,
Para que o catálogo de stock esteja correto.

## Critérios de aceite

### Cenário AC-1: Item editado
  Dado que o item "toalhas" existe com a unidade "unidade"
  Quando o gestor altera a unidade para "pacote"
  Então o item "toalhas" passa a ter a unidade "pacote"
  E o stock e os movimentos do item mantêm-se

### Cenário AC-2: Dados em falta
  Dado que o item "toalhas" existe
  Quando o gestor tenta guardar o item sem nome ou sem unidade
  Então o sistema recusa a alteração
  E o item mantém os dados anteriores

### Cenário AC-3: Nome já usado
  Dado que existem os itens "toalhas" e "lençóis"
  Quando o gestor tenta mudar o nome de "lençóis" para "toalhas"
  Então o sistema recusa a alteração
  E os dois itens mantêm os dados anteriores

### Cenário AC-4: Item inexistente
  Dado que o item "foguetes" não existe
  Quando o gestor tenta editar o item "foguetes"
  Então o sistema indica que o item não existe

### Cenário AC-5: Perfil sem permissão
  Dado que o utilizador não é gestor
  Quando tenta editar um item de stock
  Então o sistema recusa a operação

## Notas
Decisão da equipa (2026-10-16): o gestor cria e edita os itens de stock (nome, unidade). Não há história de remover item; por isso a regra Q-36 (recusar remover item com movimentos) não tem cenário aqui. Depende de "Cadastrar item de stock". Dependência para: "Carregar um ficheiro XML de fornecedor" (gestor e rececionista), "Registar entrada de stock", "Registar uso de stock", "Registar saída de stock". Entrega final (Q-06).

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: CRUD simples de dois campos (nome, unidade) com nome único. Sem cálculos. Uma tabela e um formulário.
split_needed: no

AI use: story-writer, estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
