---
type: story
summary: Carregar um ficheiro XML de fornecedor como pessoal do hotel
parent: epic-stock-management
requirements: [RF-28]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [stock, xml, final-delivery]
sprint:
---

# Título: Carregar um ficheiro XML de fornecedor como pessoal do hotel

Como rececionista,
Eu quero carregar o ficheiro XML de uma entrega de fornecedor,
Para que o material recebido entre no stock sem esperar pelo gestor.

## Critérios de aceite

### Cenário AC-1: XML válido
  Dado que o ficheiro XML segue o formato definido pela equipa e lista só itens conhecidos
  Quando o rececionista carrega o ficheiro
  Então o sistema regista a entrada de cada item no stock com a quantidade e a data

### Cenário AC-2: XML com item desconhecido
  Dado que o ficheiro lista itens conhecidos e um item desconhecido
  Quando o rececionista carrega o ficheiro
  Então o sistema importa os itens conhecidos
  E rejeita o item desconhecido

### Cenário AC-3: Ficheiro inválido
  Dado que o ficheiro não é XML válido
  Quando o rececionista carrega o ficheiro
  Então o sistema recusa o ficheiro
  E o stock não muda

## Notas
RF-28: "staff ou o gestor carrega o ficheiro". Mesma regra da história do gestor, outro ator. Depende dela (formato XML). Q-10, Q-17. Entrega final (Q-06). Depende também de "Cadastrar item de stock" e "Editar item de stock" (catálogo de itens conhecidos).

points_suggested: 3
reference: Importar ficheiro XML de stock de um fornecedor (8)
compared: smaller
reasoning: Reutiliza a leitura e as regras da história do gestor (que leva o custo do XML). Aqui só muda o ator e o acesso ao ecrã. Se for feita antes da do gestor, passa a 8.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
