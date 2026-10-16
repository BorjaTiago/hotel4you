---
type: epic
summary: Check-in e check-out de clientes
parent:
requirements: [RF-13, RF-14]
points:
points_suggested:
status: published
jira_key: H4Y-16
labels: [check-in, check-out, 1st-delivery]
sprint:
---

# Épica: Check-in e check-out

## Objetivo
A receção faz o check-in e o check-out do cliente numa reserva.

## Entrega
1.ª entrega. A parte de emissão de cartão do RF-13 está em epic-magnetic-card-access (entrega final).

## Âmbito
- Check-in de uma reserva (mudança de estado; bloqueia o apagar pelo gestor).
- Check-out de uma estadia.

## Fora do âmbito
- Emissão de cartão e estado do cartão no check-out (entrega final; Q-09 não responde ao estado do cartão no check-out).

## Atores
- Rececionista.

## Valor
O ciclo de vida da estadia funciona de ponta a ponta para a demo da 1.ª entrega.

## Ligações ao enunciado
Secção 2. RF-13, RF-14. Q-06.

## Histórias candidatas
- Fazer check-in de uma reserva (ator: rececionista; RF-13)
- Fazer check-out de uma estadia (ator: rececionista; RF-14)
- Impedir apagar reservas após check-in (ator: gestor; RF-13, RF-22)

## Perguntas em aberto
- Estado do cartão no check-out: respondido por Q-30 (o cartão deixa de funcionar, nenhuma porta abre, o registo é mantido).

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; stale note fix by doc-editor, Claude Code (claude-haiku-5-5), 2026-10-16; Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
