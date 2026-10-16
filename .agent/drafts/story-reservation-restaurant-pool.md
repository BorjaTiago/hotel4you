---
type: story
summary: Incluir restaurante e piscina na reserva
parent: epic-reservations
requirements: [RF-11, RF-18]
points: 2
points_suggested: 2
status: approved
jira_key:
labels: [reservations, 1st-delivery]
sprint:
---

# Título: Incluir restaurante e piscina na reserva

Como rececionista,
Eu quero indicar na reserva se o cliente inclui restaurante e piscina,
Para que o hotel saiba os locais a que o cliente tem direito.

## Critérios de aceite

### Cenário AC-1: Incluir restaurante e piscina
  Dado que o rececionista está a criar uma reserva
  Quando o rececionista seleciona restaurante e piscina
  Então a reserva fica com restaurante e piscina incluídos

### Cenário AC-2: Incluir só um local
  Dado que o rececionista está a criar uma reserva
  Quando o rececionista seleciona só a piscina
  Então a reserva fica com a piscina incluída
  E o restaurante não fica incluído

### Cenário AC-3: Reserva sem locais
  Dado que o rececionista cria uma reserva sem selecionar locais
  Quando a reserva é criada
  Então a reserva não inclui restaurante nem piscina

## Critérios de aceite (checklist)

- [ ] AC-1: A reserva pode incluir o restaurante e a piscina.
- [ ] AC-2: A reserva pode incluir apenas um dos dois locais.
- [ ] AC-3: Uma reserva sem locais continua válida e sem acesso a esses locais.

## Notas
O acesso efetivo com cartão fica na entrega final (RF-18, Q-06, Q-09). Piscinas privadas, spa e salas de conferência são extras pagos (Q-20) e ficam fora desta história. Alterar os locais depois de criada a reserva não está no enunciado e ficou fora desta história (os cenários de alteração foram removidos pelo refinador). Dependências (auditoria 2026-10-16): uma forte, story-create-reservation (acrescenta a escolha de locais ao mesmo formulário; sprint 5 antes do sprint 8). Restaurante e piscina vêm dos dados de referência (foundation T5), sem depender de story-criar-zona. Testar com reservas de teste (T4).

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: Two flags on the reservation, saved and shown. No access logic here.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
