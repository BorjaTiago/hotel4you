---
type: epic
summary: Cartão mestre do gestor, bloqueio e relatórios de acesso
parent:
requirements: [RF-20, RF-21, RF-23, RF-30]
points:
points_suggested:
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Epic: Cartão mestre e auditoria de acessos

## Objetivo
O gestor abre todos os quartos e zonas com um cartão mestre, bloqueia um cartão perdido e audita o uso dos cartões.

## Entrega
Entrega final (Q-06).

## Âmbito
- Um cartão mestre por gestor; cada passagem é registada (Q-19).
- Bloquear de imediato um cartão mestre perdido; emitir um novo cartão com novo número; o número antigo permanece no registo (Q-26).
- Lista dos locais de um cartão.
- Lista das passagens de cartões num quarto (cartão mestre incluído).

## Fora de âmbito
- Cartão do cliente (epic-magnetic-card-access).

## Atores
- Gestor.

## Valor
Controlo total e auditoria dos acessos.

## Ligações ao enunciado
Secção 2. RF-20, RF-21, RF-23, RF-30. Q-19, Q-26.

## Histórias candidatas
- Abrir todos os quartos e zonas com o cartão mestre (actor: manager; RF-23)
- Bloquear um cartão mestre perdido (actor: manager; RF-30)
- Emitir um novo cartão mestre com novo número (actor: manager; RF-30)
- Consultar os locais de um cartão (actor: manager; RF-20)
- Consultar as passagens de cartões num quarto (actor: manager; RF-21)

## Questões em aberto
- Nenhuma nova. Q-19 e Q-26 são decisões da equipa, não confirmadas pelo professor.

AI use: epic-writer, general-purpose (tradução para PT-PT), Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
