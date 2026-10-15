---
type: story
summary: Registar os 20 quartos por tipo e piso
parent: epic-rooms-pricing
requirements: [RF-04]
points: 3
points_suggested: 3
status: published
jira_key: H4Y-25
labels: [rooms, 1st-delivery]
sprint:
---

# Título: Registar os 20 quartos por tipo e piso

Como gestor,
Eu quero registar cada quarto com o seu tipo e o seu piso,
Para que o hotel tenha os 20 quartos disponíveis para reserva.

## Critérios de aceite

### Cenário AC-1: Registar um quarto válido
  Dado que o gestor está autenticado
  E o piso 2 tem menos de 10 quartos
  Quando o gestor regista um quarto do tipo duplo no piso 2
  Então o quarto fica registado com o tipo e o piso indicados

### Cenário AC-2: Distribuição completa do hotel
  Dado que o gestor registou todos os quartos
  Quando o gestor consulta o resumo dos quartos
  Então existem 20 quartos: 10 no piso 2 e 10 no piso 3
  E existem 7 simples, 10 duplos e 3 familiares

### Cenário AC-3: Piso que não é de quartos
  Dado que o gestor está autenticado
  Quando o gestor tenta registar um quarto no piso 1
  Então o sistema recusa o registo
  E informa que só os pisos 2 e 3 têm quartos

### Cenário AC-4: Limite de quartos por piso
  Dado que o piso 3 já tem 10 quartos
  Quando o gestor tenta registar mais um quarto no piso 3
  Então o sistema recusa o registo

## Notas
Q-01 (decisão da equipa, não confirmada pelo professor): 20 quartos, 10 por piso, divisão 7/10/3 para o hotel inteiro. Quarto familiar: 3 ou 4 pessoas. Depende da história story-model-hotel-floors.

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: Validate and save rooms with floor and count limits (10 per floor, 7/10/3 split). Some rules, one or two layers.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
