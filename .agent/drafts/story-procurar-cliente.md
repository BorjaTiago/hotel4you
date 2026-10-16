---
type: story
summary: Procurar um cliente já registado
parent: epic-client-registration
requirements: [RF-10]
points: 2
points_suggested: 2
status: approved
jira_key:
labels: [clients, 1st-delivery]
sprint:
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

## Notas
Depende de "Registar um novo cliente". Fase: 1.ª entrega (Q-27). O enunciado não define o dado de pesquisa; a equipa escolhe-o na implementação (detalhe negociável).

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: Simple search query and result list. The search field is open but small.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
