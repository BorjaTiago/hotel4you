---
type: story
summary: Consultar os locais de um cartão
parent: epic-master-card-audit
requirements: [RF-20, RF-19]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Título: Consultar os locais de um cartão

Como gestor,
Eu quero ver a lista de locais por onde um cartão passou,
Para que eu audite o uso desse cartão.

## Critérios de aceite

### Cenário AC-1: Lista de locais do cartão
  Dado que o cartão 12345 entrou no quarto 202 e no restaurante
  Quando o gestor consulta os locais do cartão 12345
  Então o sistema mostra o quarto 202 e o restaurante
  E cada local aparece com a hora da entrada

### Cenário AC-2: Cartão sem passagens
  Dado que o cartão 12346 existe e nunca foi usado
  Quando o gestor consulta os locais do cartão 12346
  Então o sistema mostra uma lista vazia

### Cenário AC-3: Cartão inexistente
  Dado que não existe o cartão 99999
  Quando o gestor consulta os locais do cartão 99999
  Então o sistema indica que o cartão não existe

### Cenário AC-4: Perfil sem permissão
  Dado que o utilizador é rececionista
  Quando tenta consultar os locais de um cartão
  Então o sistema recusa a consulta

## Notas
Exemplo do enunciado: cartão 12345. Cartões antigos (bloqueados) continuam consultáveis (Q-26). Depende de "Guardar cada passagem do cartão com local e hora". Entrega final (Q-06).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Consulta por cartão com lista de locais e horas, mais perfil e cartão inexistente. Serve também cartões bloqueados. Mesma ordem de tamanho da consulta por quarto.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
