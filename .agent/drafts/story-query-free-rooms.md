---
type: story
summary: Consultar quartos livres por tipo e noites
parent: epic-reservations
requirements: [RF-05]
points:
points_suggested: 5
status: published
jira_key: H4Y-54
labels: [reservations, 1st-delivery]
sprint: 04
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

## Critérios de aceite (checklist)

- [ ] AC-1: Um quarto livre no período pedido é oferecido.
- [ ] AC-2: Um quarto ocupado no período pedido não é oferecido.
- [ ] AC-3: Um quarto com reserva cancelada volta a ser oferecido.
- [ ] AC-4: Um período inválido é recusado.
- [ ] AC-5: Sem quartos livres, a resposta indica que não há disponibilidade.

## Notas
AC-3: cancelar liberta o quarto (Q-05: cancelar é mudança de estado; ver story-cancel-reservation AC-2). Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: quartos e reservas de teste próprios, incluindo uma ativa e uma cancelada (foundation T4). Esta história implementa `RoomAvailability` (T0), a regra única de sobreposição de datas; story-create-reservation reutiliza-a.

points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: Date-overlap SQL query ignoring cancelled reservations, period validation, empty result and GUI. Edge cases on dates need care.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: lukassperandio. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; sprint field by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
