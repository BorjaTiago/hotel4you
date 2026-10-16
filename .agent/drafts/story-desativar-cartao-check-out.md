---
type: story
summary: Desativar o cartão ao fazer check-out
parent: epic-check-in-out
requirements: [RF-14, RF-16]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [check-out, magnetic-card, final-delivery]
sprint:
---

# Título: Desativar o cartão ao fazer check-out

Como rececionista,
Eu quero que o cartão da reserva deixe de funcionar quando faço o check-out,
Para que o cliente não abra portas depois de sair.

## Critérios de aceite

### Cenário AC-1: Cartão desativado no check-out
  Dado que a reserva tem check-in efetuado e um cartão associado
  Quando o rececionista faz o check-out
  Então o cartão da reserva deixa de funcionar
  E o registo de passagens do cartão mantém-se

### Cenário AC-2: Porta recusa o cartão
  Dado que o check-out foi efetuado
  Quando alguém passa o cartão da reserva numa porta (entrada simulada)
  Então o sistema recusa a entrada
  E o registo de passagens anterior continua consultável

### Cenário AC-3: Check-out recusado não afeta o cartão
  Dado que a reserva não tem check-in efetuado
  Quando o rececionista tenta fazer o check-out
  Então o sistema recusa a ação
  E o cartão, se existir, mantém o seu estado

## Notas
Entrega final (Q-06). Q-30 (decisão da equipa): no check-out o cartão deixa de funcionar, nenhuma porta abre, o registo fica guardado. Depende de story-check-out e da emissão do cartão em epic-magnetic-card-access.
points_suggested: 3
reference: Realizar check-in com cartão magnético (5)
compared: smaller
reasoning: Deactivate the card inside the check-out flow and refuse simulated door entry. Keep the pass log. Depends on card stories that do not exist yet.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: refiner (split from story-check-out), Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
