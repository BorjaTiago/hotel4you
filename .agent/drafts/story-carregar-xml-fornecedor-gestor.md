---
type: story
summary: Carregar um ficheiro XML de fornecedor
parent: epic-stock-management
requirements: [RF-28]
points: 8
points_suggested: 8
status: approved
jira_key:
labels: [stock, xml, final-delivery]
sprint:
---

# Título: Carregar um ficheiro XML de fornecedor

Como gestor,
Eu quero carregar o ficheiro XML de uma entrega de fornecedor,
Para que o material da entrega entre no stock.

## Critérios de aceite

### Cenário AC-1: XML válido
  Dado que o ficheiro XML segue o formato definido pela equipa e lista só itens conhecidos
  Quando o gestor carrega o ficheiro
  Então o sistema regista a entrada de cada item no stock com a quantidade e a data
  E mostra que a importação terminou

### Cenário AC-2: XML com item desconhecido
  Dado que o ficheiro lista itens conhecidos e um item desconhecido
  Quando o gestor carrega o ficheiro
  Então o sistema importa os itens conhecidos
  E rejeita o item desconhecido

### Cenário AC-3: Ficheiro inválido
  Dado que o ficheiro não é XML válido ou não segue o formato
  Quando o gestor carrega o ficheiro
  Então o sistema recusa o ficheiro
  E o stock não muda

### Cenário AC-4: Perfil sem permissão
  Dado que o utilizador é um funcionário do restaurante
  Quando tenta carregar um ficheiro XML
  Então o sistema recusa a operação

## Critérios de aceite (checklist)

- [ ] AC-1: Um XML válido com itens conhecidos regista a entrada de cada item no stock e mostra que a importação terminou.
- [ ] AC-2: Num XML com item desconhecido, os itens conhecidos são importados e o desconhecido é rejeitado.
- [ ] AC-3: Um ficheiro inválido ou fora do formato é recusado e o stock não muda.
- [ ] AC-4: O funcionário do restaurante não consegue carregar ficheiros XML.

## Notas
O fornecedor não faz login (Q-17). Item desconhecido rejeitado, válidos importados (Q-10). A equipa define o formato XML (o exemplo do enunciado não está no texto): o formato fica documentado nesta história/entrega, sem história própria. A lista de rejeitados: ver história "Ver o relatório de importação". Entrega final (Q-06). Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: itens de catálogo de teste e ficheiros XML de teste válido, com item desconhecido e inválido (foundation T4). Esta história é dona do leitor XML (`XmlStockImporter`, T0) e guarda a importação (`stock_imports`); story-carregar-xml-fornecedor-rececionista reutiliza-o.

points_suggested: 8
reference: Importar ficheiro XML de stock de um fornecedor (8)
compared: equal
reasoning: Define o formato XML, valida o ficheiro, trata itens conhecidos e desconhecidos e grava vários movimentos. Seletor de ficheiro na GUI. Pouco desconhecido: o formato é decisão da equipa.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
