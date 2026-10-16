---
type: story
summary: Abrir restaurante e piscina só se estiverem na reserva
parent: epic-magnetic-card-access
requirements: [RF-18, RF-03]
points: 5
points_suggested: 5
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Título: Abrir restaurante e piscina só se estiverem na reserva

Como cliente,
Eu quero entrar no restaurante e na piscina com o meu cartão quando fazem parte da minha reserva,
Para que eu use os locais que reservei.

## Critérios de aceite

### Cenário AC-1: Restaurante incluído na reserva
  Dado que a zona "restaurante" está marcada como exigindo reserva
  E a reserva do cliente inclui o restaurante
  Quando o cliente passa o cartão na porta do restaurante (entrada simulada)
  Então o sistema permite a entrada

### Cenário AC-2: Piscina incluída na reserva
  Dado que a zona "piscina" está marcada como exigindo reserva
  E a reserva do cliente inclui a piscina
  Quando o cliente passa o cartão na porta da piscina (entrada simulada)
  Então o sistema permite a entrada

### Cenário AC-3: Restaurante não incluído
  Dado que a zona "restaurante" está marcada como exigindo reserva
  E a reserva do cliente não inclui o restaurante
  Quando o cliente passa o cartão na porta do restaurante
  Então o sistema recusa a entrada

### Cenário AC-4: Piscina não incluída
  Dado que a zona "piscina" está marcada como exigindo reserva
  E a reserva do cliente não inclui a piscina
  Quando o cliente passa o cartão na porta da piscina
  Então o sistema recusa a entrada

## Notas
Restaurante e piscina estão no piso 1 (RF-03). Depende de "Criar uma zona num piso": a marca "exige reserva" da zona decide o acesso (decisão da equipa, 2026-10-16). Entrega final (Q-06). Q-09 e Q-20 (decisão da equipa). Depende de a reserva guardar restaurante e piscina (RF-11, 1.ª entrega).

points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: Cruza cartão, reserva, serviços incluídos e a marca "exige reserva" da zona. Lógica, dados e GUI juntos. Depende de zonas e do modelo da reserva (risco de integração).
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
