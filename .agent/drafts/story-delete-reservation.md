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

## Critérios de aceite (checklist)

- [ ] AC-1: Uma reserva sem check-in pode ser apagada.
- [ ] AC-2: Depois de apagar a reserva, o quarto volta a ficar livre.
- [ ] AC-3: Apagar uma reserva inexistente devolve uma resposta de não encontrado.

## Notas
Q-05 (decisão da equipa, não confirmada): apagar remove a reserva de vez. Decisão (2026-10-16): a recusa de apagar após check-in (antigo AC-3) fica só em story-block-delete-after-check-in, sem duplicação aqui. Recusa a outros papéis em story-refuse-delete-reservation. Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: reservas de teste próprias (foundation T4), sessão de gestor simulada (T0). Esta história é dona da ação de apagar; story-refuse-delete-reservation e story-block-delete-after-check-in acrescentam guardas à mesma ação.

Q-36: exceção RF-22, o gestor apaga a reserva antes do check-in; sem AC extra.
points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: Permanent delete with role check and room release. Check-in rule is in another story.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
