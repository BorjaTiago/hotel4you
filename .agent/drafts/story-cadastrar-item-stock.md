---
type: story
summary: Cadastrar item de stock
parent: epic-stock-management
requirements: [RF-28, RF-29]
points: 2
points_suggested: 2
status: approved
jira_key:
labels: [stock, 1st-delivery]
sprint:
---

# Título: Cadastrar item de stock

Como gestor,
Eu quero cadastrar um item de stock com nome e unidade,
Para que o item possa ser carregado por XML e ter movimentos de stock.

## Critérios de aceite

### Cenário AC-1: Item cadastrado
  Dado que o gestor tem sessão iniciada
  E não existe o item "toalhas"
  Quando o gestor cadastra o item "toalhas" com a unidade "unidade"
  Então o item "toalhas" fica disponível no catálogo de stock
  E o item pode receber movimentos de stock

### Cenário AC-2: Dados em falta
  Dado que o gestor tem sessão iniciada
  Quando o gestor tenta cadastrar um item sem nome ou sem unidade
  Então o sistema recusa o registo
  E indica o campo em falta

### Cenário AC-3: Item repetido
  Dado que o item "toalhas" já existe
  Quando o gestor tenta cadastrar outro item "toalhas"
  Então o sistema recusa o registo
  E o catálogo não muda

### Cenário AC-4: Perfil sem permissão
  Dado que o utilizador não é gestor
  Quando tenta cadastrar um item de stock
  Então o sistema recusa a operação

## Critérios de aceite (checklist)

- [ ] AC-1: O item cadastrado com nome e unidade fica no catálogo de stock e pode receber movimentos.
- [ ] AC-2: Cadastrar um item sem nome ou sem unidade é recusado e o sistema indica o campo em falta.
- [ ] AC-3: Cadastrar um item com nome já existente é recusado e o catálogo não muda.
- [ ] AC-4: Quem não é gestor não consegue cadastrar itens de stock.

## Notas
Decisão da equipa (2026-10-16): o gestor cria e edita os itens de stock (nome, unidade). RF-28 usa itens conhecidos (Q-10); RF-29 regista movimentos de itens. Não há história de remover item. Dependência para: "Carregar um ficheiro XML de fornecedor" (gestor e rececionista), "Registar entrada de stock", "Registar uso de stock", "Registar saída de stock". Entrega final (Q-06).
Dependências (auditoria 2026-10-16): nenhuma forte. Proposta de ordem: passa para o sprint 4 (1.ª entrega), porque story-configure-room-items (RF-08, 1.ª entrega) usa o mesmo catálogo de itens (esquema H4Y-10). O PO decide. As outras histórias de stock já não dependem desta: usam itens de teste (foundation T4). Construir e testar sozinha: base de teste vazia e um item para o duplicado (T4), sessões simuladas (T0).

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: Criar item com nome e unidade, validar campos e nome único. Cria a tabela base do stock, mas a lógica é simples.
split_needed: no

AI use: story-writer, estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
