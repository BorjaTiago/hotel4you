---
type: story
summary: Fazer check-out de uma estadia
parent: epic-check-in-out
requirements: [RF-14]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [check-out, 1st-delivery]
sprint:
---

# Título: Fazer check-out de uma estadia

Como rececionista,
Eu quero fazer o check-out de um cliente com check-in efetuado,
Para que a estadia termine e o quarto fique disponível.

## Critérios de aceite

### Cenário AC-1: Check-out de estadia ativa
  Dado que o rececionista está autenticado
  E a reserva tem check-in efetuado
  Quando o rececionista faz o check-out
  Então o estado da reserva passa a "check-out efetuado"

### Cenário AC-2: Sem check-in prévio
  Dado que a reserva não tem check-in efetuado
  Quando o rececionista tenta fazer o check-out
  Então o sistema recusa a ação
  E o estado da reserva não muda

### Cenário AC-3: Check-out duplicado
  Dado que a reserva já tem check-out efetuado
  Quando o rececionista tenta fazer de novo o check-out
  Então o sistema recusa a ação

### Cenário AC-4: Quarto livre depois do período
  Dado que o check-out foi efetuado
  Quando o rececionista consulta quartos livres para noites após a data de saída
  Então o quarto é oferecido

## Critérios de aceite (checklist)

- [ ] AC-1: O check-out de uma estadia ativa é registado.
- [ ] AC-2: O check-out sem check-in prévio é recusado.
- [ ] AC-3: Um check-out duplicado é recusado.
- [ ] AC-4: Depois do período da reserva, o quarto volta a ficar livre.

## Notas
Entrega 1 (Q-06). Decisão da equipa (2026-10-16): no check-out o cartão deixa de funcionar e o registo de passagens fica guardado. O efeito no cartão (deixa de funcionar, registo mantido) foi separado em story-desativar-cartao-check-out, porque o cartão só existe na entrega final. Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: reservas de teste próprias com e sem check-in e com check-out (foundation T4). O AC-4 verifica com a consulta de quartos livres (sprint 4).

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: State transition with guards (needs check-in, no duplicate). Card effect is in another story.
split_needed: no

ready: true
dor_failed: []
questions: []
split_note: cenários do cartão movidos para story-desativar-cartao-check-out

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
