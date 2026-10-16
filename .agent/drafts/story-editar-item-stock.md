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
Eu quero editar o nome, a unidade e o preço de um item de stock, incluindo os itens criados por importação XML,
Para que o catálogo de stock esteja correto.

## Critérios de aceite

### Cenário AC-1: Item editado
  Dado que o item "toalhas" existe com a unidade "unidade" e o preço 2,00
  Quando o gestor altera a unidade para "pacote" e o preço para 2,50
  Então o item "toalhas" passa a ter a unidade "pacote" e o preço 2,50
  E o stock e os movimentos do item mantêm-se

### Cenário AC-2: Dados em falta
  Dado que o item "toalhas" existe
  Quando o gestor tenta guardar o item sem nome, sem unidade ou sem preço
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

## Critérios de aceite (checklist)

- [ ] AC-1: O gestor consegue alterar a unidade e o preço de um item, e o stock e os movimentos do item mantêm-se.
- [ ] AC-2: Guardar um item sem nome, sem unidade ou sem preço é recusado e o item mantém os dados anteriores.
- [ ] AC-3: Mudar o nome para um nome já usado por outro item é recusado e os dois itens não mudam.
- [ ] AC-4: Editar um item inexistente mostra que o item não existe.
- [ ] AC-5: Quem não é gestor não consegue editar itens de stock.

## Notas
Decisão da equipa (2026-10-16): o gestor cria e edita os itens de stock (nome, unidade, preço). Q-48 (substitui Q-10 e Q-38): a importação XML cria itens que não existem; o gestor edita ou corrige esses itens aqui. Não há história de remover item; por isso a regra Q-36 (recusar remover item com movimentos) não tem cenário aqui. Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: itens de teste próprios (foundation T4). Antes ligada a: "Carregar um ficheiro XML de fornecedor" (gestor e rececionista), "Registar entrada de stock", "Registar uso de stock", "Registar saída de stock". Entrega final (Q-06).

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: CRUD simples de três campos (nome, unidade, preço) com nome único. Sem cálculos. Uma tabela e um formulário.
split_needed: no

AI use: story-writer, estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Q-48 price field by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
