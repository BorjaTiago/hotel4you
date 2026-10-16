---
type: story
summary: Criar uma reserva para um cliente
parent: epic-reservations
requirements: [RF-11, RF-05]
points: 5
points_suggested: 5
status: approved
jira_key:
labels: [reservations, 1st-delivery]
sprint:
---

# Título: Criar uma reserva para um cliente

Como rececionista,
Eu quero criar uma reserva de um quarto livre para um cliente,
Para que o cliente fique com o quarto garantido nas noites escolhidas.

## Critérios de aceite

### Cenário AC-1: Reserva de quarto livre
  Dado que o rececionista está autenticado
  E o quarto 201 está livre nas noites de 10 a 12
  Quando o rececionista cria uma reserva do quarto 201 para essas noites
  Então a reserva fica criada em estado ativo
  E o quarto 201 deixa de estar livre nessas noites

### Cenário AC-2: Quarto já reservado
  Dado que o quarto 201 tem uma reserva ativa nas noites de 10 a 12
  Quando o rececionista tenta reservar o quarto 201 para as noites de 11 a 13
  Então o sistema recusa a reserva
  E não existe reserva duplicada

### Cenário AC-3: Dados em falta
  Dado que o rececionista está autenticado
  Quando o rececionista tenta criar uma reserva sem cliente ou sem período
  Então o sistema recusa a reserva
  E indica os dados em falta

### Cenário AC-4: Mostrar o preço
  Dado que o tipo do quarto tem preço base definido
  Quando o rececionista cria a reserva para 2 noites
  Então o sistema mostra o preço base por noite do tipo do quarto

## Notas
Q-27 (respondida, decisão da equipa): RF-10 está na 1.ª entrega, a reserva usa o cliente registado. Depende de story-registar-cliente, story-query-free-rooms e story-set-base-price. O total com extras fica em epic-extra-services-pricing.

points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: Core entity. Links client, room and nights, blocks overlap, shows price, plus GUI. Logic, data and GUI together.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
