---
type: story
summary: Carregar um ficheiro XML ou JSON de fornecedor como pessoal do hotel
parent: epic-stock-management
requirements: [RF-28]
points: 3
points_suggested: 3
status: draft
jira_key:
labels: [stock, xml, json, final-delivery]
sprint:
---

# Título: Carregar um ficheiro XML ou JSON de fornecedor como pessoal do hotel

Como rececionista,
Eu quero carregar o ficheiro XML ou JSON de uma entrega de fornecedor,
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
  Dado que o ficheiro não é XML válido nem JSON válido
  Quando o rececionista carrega o ficheiro
  Então o sistema recusa o ficheiro
  E o stock não muda

### Cenário AC-4: JSON válido
  Dado que o ficheiro JSON segue as mesmas regras do ficheiro XML (Q-59) e lista só itens conhecidos
  Quando o rececionista carrega o ficheiro
  Então o sistema regista a entrada de cada item no stock com a quantidade e a data, como num ficheiro XML

## Critérios de aceite (checklist)

- [ ] AC-1: Um XML válido com itens conhecidos regista a entrada de cada item no stock com quantidade e data.
- [ ] AC-2: Num XML com item desconhecido, os itens conhecidos são importados e o desconhecido é rejeitado.
- [ ] AC-3: Um ficheiro que não é XML válido nem JSON válido é recusado e o stock não muda.
- [ ] AC-4: Um JSON válido com itens conhecidos regista a entrada de cada item no stock, como um XML.

## Notas
RF-28: "staff ou o gestor carrega o ficheiro". Mesma regra da história do gestor, outro ator. Q-10, Q-17. Entrega final (Q-06).
Dependências (auditoria 2026-10-16): uma forte, story-carregar-xml-fornecedor-gestor (reutiliza os leitores XML e JSON; os 3 pontos supõem essa reutilização; sprint 11 antes do sprint 12). Itens e ficheiros: dados de teste (foundation T4), sem depender das histórias do catálogo.
JSON (H4Y-84): a atualização do enunciado de 2026-11-06 (secção 2, "New stock entries") pede também ficheiros JSON, para fornecedores que não exportam XML. O ficheiro JSON segue os mesmos pressupostos e regras do ficheiro XML. Os nomes dos campos JSON ainda não são conhecidos (Q-59, aberta). A equipa volta a estimar esta história: por isso o estado voltou a draft e precisa de nova aprovação.

points_suggested: 3
reference: Importar ficheiro XML de stock de um fornecedor (8)
compared: smaller
reasoning: Reutiliza a leitura e as regras da história do gestor (que leva o custo do XML). Aqui só muda o ator e o acesso ao ecrã. Se for feita antes da do gestor, passa a 8.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: antonioprmartinho. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; H4Y-84 JSON stock import docs by Claude Code (claude-opus-5-5), 2026-11-12. Reviewed by: pending.
