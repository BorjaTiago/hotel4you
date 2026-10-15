---
type: story
summary: Guardar cada passagem do cartão com local e hora
parent: epic-magnetic-card-access
requirements: [RF-19]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Título: Guardar cada passagem do cartão com local e hora

Como cliente,
Eu quero que cada passagem do meu cartão fique registada com o local e a hora,
Para que o hotel tenha o registo das minhas entradas.

## Critérios de aceite

### Cenário AC-1: Entrada permitida registada
  Dado que o cliente tem um cartão válido
  Quando o cliente entra no restaurante com o cartão
  Então o sistema guarda um registo com o local "restaurante" e a hora da entrada

### Cenário AC-2: Entrada no quarto registada
  Dado que o cliente tem um cartão associado ao quarto 202
  Quando o cliente entra no quarto 202
  Então o sistema guarda um registo com o local "quarto 202" e a hora da entrada

### Cenário AC-3: Entrada recusada não cria registo de entrada
  Dado que o cartão não dá acesso ao bar
  Quando o cliente passa o cartão na porta do bar
  Então o sistema recusa a entrada
  E não guarda um registo de entrada nesse local

## Notas
Registo de cada passagem, incluindo quartos (Q-09, decisão da equipa). RF-19 fala só de entradas. Registar tentativas recusadas não está no enunciado: não foi assumido. Entrega final (Q-06).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Nova tabela de passagens e gravação em cada entrada permitida. Sem GUI própria. Regra: recusa não grava.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
