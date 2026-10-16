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

## Critérios de aceite (checklist)

- [ ] AC-1: O cartão abre o restaurante quando a reserva o inclui.
- [ ] AC-2: O cartão abre a piscina quando a reserva a inclui.
- [ ] AC-3: O cartão é recusado no restaurante quando a reserva não o inclui.
- [ ] AC-4: O cartão é recusado na piscina quando a reserva não a inclui.

## Notas
Restaurante e piscina estão no piso 1 (RF-03). A marca "exige reserva" da zona decide o acesso (decisão da equipa, 2026-10-16). Entrega final (Q-06). Q-09 e Q-20 (decisão da equipa).
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: zonas com a marca (dados de referência, foundation T5) e reservas de teste com e sem restaurante e piscina (T4), sem depender de story-criar-zona nem de story-reservation-restaurant-pool; regra própria (`AccessRule`, T0) e leitor simulado (T6).

points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: Cruza cartão, reserva, serviços incluídos e a marca "exige reserva" da zona. Lógica, dados e GUI juntos. Depende de zonas e do modelo da reserva (risco de integração).
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
