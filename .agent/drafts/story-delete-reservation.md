---
type: story
summary: Apagar uma reserva antes do check-in
parent: epic-reservations
requirements: [RF-22]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [reservations, 1st-delivery]
sprint:
---

# Título: Apagar uma reserva antes do check-in

Como gestor,
Eu quero apagar uma reserva cujo cliente ainda não fez check-in,
Para que reservas indevidas desapareçam do sistema.

## Critérios de aceite

### Cenário AC-1: Apagar reserva sem check-in
  Dado que o gestor está autenticado
  E existe uma reserva sem check-in
  Quando o gestor apaga a reserva
  Então a reserva é removida de forma definitiva
  E a reserva já não aparece na consulta nem no histórico

### Cenário AC-2: Quarto volta a ficar livre
  Dado que o gestor apagou a reserva do quarto 201
  Quando se consultam quartos livres para o mesmo período
  Então o quarto 201 é oferecido

### Cenário AC-3: Reserva inexistente
  Dado que o gestor está autenticado
  Quando o gestor tenta apagar uma reserva que não existe
  Então o sistema informa que a reserva não existe

## Notas
Q-05 (decisão da equipa, não confirmada): apagar remove a reserva de vez. Decisão (2026-10-16): a recusa de apagar após check-in (antigo AC-3) fica só em story-block-delete-after-check-in, sem duplicação aqui. Recusa a outros papéis em story-refuse-delete-reservation. Depende de story-create-reservation.

Q-36: exceção RF-22, o gestor apaga a reserva antes do check-in; sem AC extra.
points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: Permanent delete with role check and room release. Check-in rule is in another story.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
