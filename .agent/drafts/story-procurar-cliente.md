---
type: story
summary: Procurar um cliente já registado
parent: epic-client-registration
requirements: [RF-10]
points:
points_suggested: 2
status: published
jira_key: H4Y-53
labels: [clients, 1st-delivery]
sprint: 04
---

# Título: Procurar um cliente já registado

Como rececionista,
Eu quero procurar um cliente já registado,
Para que não o registe duas vezes e possa usar os seus dados numa reserva.

## Critérios de aceite

### Cenário AC-1: Cliente encontrado
  Dado que o cliente está registado
  Quando o rececionista procura o cliente por um dado de identificação
  Então o sistema mostra o cliente encontrado
  E o rececionista pode escolher o cliente para uma reserva

### Cenário AC-2: Cliente não encontrado
  Dado que nenhum cliente corresponde à pesquisa
  Quando o rececionista procura o cliente
  Então o sistema indica que não há resultados
  E oferece o registo de um novo cliente

### Cenário AC-3: Pesquisa vazia
  Quando o rececionista pesquisa sem indicar nenhum dado
  Então o sistema recusa a pesquisa
  E pede um dado de identificação

## Critérios de aceite (checklist)

- [ ] AC-1: Procurar um cliente existente devolve os seus dados.
- [ ] AC-2: Procurar um cliente inexistente devolve uma resposta de não encontrado.
- [ ] AC-3: Uma pesquisa vazia é recusada.

## Notas
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: clientes de teste próprios (foundation T4). O AC-2 abre o ecrã de registo (mesmo sprint); a ligação ao ecrã faz-se no fim do sprint. Fase: 1.ª entrega (Q-27). O enunciado não define o dado de pesquisa; a equipa escolhe-o na implementação (detalhe negociável).

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: Simple search query and result list. The search field is open but small.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: lukassperandio. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; sprint field by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
