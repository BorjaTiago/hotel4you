---
type: story
summary: Abrir o restaurante e a piscina reservados com o cartão
parent: epic-magnetic-card-access
requirements: [RF-18, RF-03]
points: 5
points_suggested: 5
status: approved
jira_key: H4Y-136
labels: [access-control, final-delivery]
sprint:
---

# Título: Abrir o restaurante e a piscina reservados com o cartão

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

### Cenário AC-5: Fluxo completo
  Dado que existe um cartão de uma reserva com check-in, que inclui o restaurante e não inclui a piscina
  Quando o cliente entra com o cartão, abre o ecrã "O meu cartão" e clica no restaurante e depois na piscina
  Então o serviço procura o cartão e a reserva, confere a zona e a marca "exige reserva" e decide
  E o ecrã mostra "entrada permitida" no restaurante e "entrada recusada" com o motivo na piscina

## Critérios de aceite (checklist)

- [ ] AC-1: O cartão abre o restaurante quando a reserva o inclui.
- [ ] AC-2: O cartão abre a piscina quando a reserva a inclui.
- [ ] AC-3: O cartão é recusado no restaurante quando a reserva não o inclui.
- [ ] AC-4: O cartão é recusado na piscina quando a reserva não a inclui.
- [ ] AC-5: Em "O meu cartão", o restaurante reservado mostra "entrada permitida" e a piscina não reservada mostra "entrada recusada" com o motivo.

## Notas
Restaurante e piscina estão no piso 1 (RF-03). A marca "exige reserva" da zona decide o acesso. Usa o CardAccessService e o login com o cartão e o ecrã "O meu cartão" da H4Y-109 e junta só a regra de acesso às zonas reservadas e o ecrã desta história. Não cria tabelas. Zonas com a marca e reservas de teste com e sem restaurante e piscina.

points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: Cruza cartão, reserva, serviços incluídos e a marca "exige reserva" da zona. Lógica, dados e GUI juntos. Depende de zonas e do modelo da reserva (risco de integração).
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Jira sync (login and "O meu cartão" flow, H4Y-122) by story-writer, GitHub Copilot (claude-opus-5.5), 2026-11-24. Reviewed by: pending.
