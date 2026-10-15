---
type: story
summary: Consultar quartos livres por tipo e noites
parent: epic-reservations
requirements: [RF-05]
points: 5
points_suggested: 5
status: approved
jira_key:
labels: [reservations, 1st-delivery]
sprint:
---

# Título: Consultar quartos livres por tipo e noites

Como rececionista,
Eu quero consultar os quartos livres de um tipo para um período de uma ou mais noites,
Para que ofereça ao cliente apenas quartos disponíveis.

## Critérios de aceite

### Cenário AC-1: Quarto livre é oferecido
  Dado que existe um quarto duplo sem reservas nas noites de 10 a 12
  Quando o rececionista consulta quartos duplos livres para as noites de 10 a 12
  Então o sistema oferece esse quarto

### Cenário AC-2: Quarto ocupado não é oferecido
  Dado que o quarto 201 tem uma reserva ativa nas noites de 10 a 12
  Quando o rececionista consulta quartos livres desse tipo para as noites de 11 a 13
  Então o sistema não oferece o quarto 201

### Cenário AC-3: Reserva cancelada liberta o quarto
  Dado que o quarto 201 tem uma reserva cancelada nas noites de 10 a 12
  Quando o rececionista consulta quartos livres para as noites de 10 a 12
  Então o sistema oferece o quarto 201

### Cenário AC-4: Período inválido
  Dado que o rececionista está autenticado
  Quando o rececionista indica uma data de saída igual ou anterior à de entrada
  Então o sistema recusa a consulta
  E informa que o período é inválido

### Cenário AC-5: Nenhum quarto livre
  Dado que todos os quartos do tipo familiar estão ocupados no período
  Quando o rececionista consulta quartos familiares livres
  Então o sistema informa que não há quartos livres

## Notas
AC-3: cancelar liberta o quarto (Q-05: cancelar é mudança de estado; ver story-cancel-reservation AC-2). Depende de story-register-rooms.

points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: Date-overlap SQL query ignoring cancelled reservations, period validation, empty result and GUI. Edge cases on dates need care.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
