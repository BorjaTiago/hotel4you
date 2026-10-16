---
type: story
summary: Calcular o preço total da reserva
parent: epic-extra-services-pricing
requirements: [RF-06, RF-07, RF-09]
points: 5
points_suggested: 5
status: approved
jira_key:
labels: [pricing]
sprint:
---

# Título: Calcular o preço total da reserva

Como rececionista,
Eu quero ver o preço total de uma reserva com o preço base e os extras,
Para que eu diga ao cliente o valor correto da estadia.

## Critérios de aceite

### Cenário AC-1: Preço só com o base
  Dado que a reserva é de um quarto duplo com preço base 80,00 por noite durante 3 noites e sem extras
  Quando o rececionista consulta o preço total
  Então o sistema mostra 240,00

### Cenário AC-2: Preço com extras
  Dado que a reserva é de 3 noites com preço base 80,00, vista mar a 15,00 e lavandaria a 5,00 por noite
  Quando o rececionista consulta o preço total
  Então o sistema mostra 3 x (80,00 + 15,00 + 5,00) = 300,00

### Cenário AC-3: Preço com serviço pago
  Dado que a reserva de 2 noites tem preço base 80,00 e o spa a 40,00 por noite
  Quando o rececionista consulta o preço total
  Então o sistema mostra 240,00

### Cenário AC-4: Preço recalculado ao mudar extras
  Dado que a reserva tem a vista mar
  Quando o rececionista remove a vista mar
  Então o preço total é recalculado sem esse extra

### Cenário AC-5: Falta preço base
  Dado que o gestor não definiu o preço base do tipo de quarto da reserva
  Quando o rececionista consulta o preço total
  Então o sistema indica que não pode calcular o preço

## Notas
Preços fixos por noite (Q-11, decisão da equipa). Valores dos exemplos são só ilustrativos. Depende de "Definir os preços extra por noite" e das histórias de extras. Depende também de "Definir o preço base" (epic-rooms-pricing, RF-06). Fase pelo priorizador (Q-25).

points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: Cálculo por noites com preço base, extras e serviços, recalculo ao mudar extras e erro sem preço base. Junta dados de três histórias. Muitos casos de teste de valores.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
