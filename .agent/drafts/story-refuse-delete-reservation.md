---
type: story
summary: Recusar o apagar de reservas a quem não é gestor
parent: epic-reservations
requirements: [RF-22]
points: 2
points_suggested: 2
status: approved
jira_key:
labels: [reservations, security, 1st-delivery]
sprint:
---

# Título: Recusar o apagar de reservas a quem não é gestor

Como rececionista,
Eu quero que o sistema me impeça de apagar reservas e me deixe apenas cancelá-las,
Para que só o gestor remova reservas de forma definitiva.

## Critérios de aceite

### Cenário AC-1: Rececionista tenta apagar
  Dado que o rececionista está autenticado
  E existe uma reserva sem check-in
  Quando o rececionista tenta apagar a reserva
  Então o sistema recusa a ação
  E a reserva mantém-se inalterada

### Cenário AC-2: Rececionista pode cancelar
  Dado que o rececionista está autenticado
  E existe uma reserva ativa sem check-in
  Quando o rececionista cancela a reserva
  Então a reserva passa a "cancelada"

### Cenário AC-3: Utilizador sem autenticação
  Dado que o utilizador não está autenticado
  Quando tenta apagar uma reserva
  Então o sistema recusa a ação

### Cenário AC-4: Funcionário do restaurante
  Dado que um funcionário do restaurante está autenticado
  Quando tenta apagar uma reserva
  Então o sistema recusa a ação

## Critérios de aceite (checklist)

- [ ] AC-1: O rececionista que tenta apagar uma reserva é recusado.
- [ ] AC-2: O rececionista continua a poder cancelar reservas.
- [ ] AC-3: Um utilizador sem autenticação é recusado.
- [ ] AC-4: Um funcionário do restaurante é recusado ao apagar reservas.

## Notas
Enunciado: só os gestores apagam reservas (RF-22). Q-05 (decisão da equipa). A mensagem de recusa não revela dados da reserva. Dependências (auditoria 2026-10-16): uma forte, story-delete-reservation (acrescenta a guarda de papel à mesma ação de apagar; sprint 5 antes do sprint 6). Construir e testar: sessões simuladas de rececionista e restaurante (foundation T0), reservas de teste (T4).

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: Role check on delete for three cases. One layer, simple rule. Mostly tests.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
