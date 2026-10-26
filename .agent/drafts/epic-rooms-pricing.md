---
type: epic
summary: Modelo do edifício, quartos, tipos de quarto, preços base e itens do quarto
parent:
requirements: [RF-03, RF-04, RF-06, RF-08]
points:
points_suggested:
status: published
jira_key: H4Y-14
labels: [rooms, 1st-delivery]
sprint:
---

# Épica: Quartos e preços base

## Objetivo
O sistema conhece o edifício e os 20 quartos. O gestor define os preços base e os itens dos quartos.

## Entrega
1.ª entrega (quartos). O uso de zonas por cartão está em epic-magnetic-card-access (entrega final).

## Âmbito
- Modelo de pisos: -1, 0, 1, 2, 3. H4Y-28: Consultar os pisos e gerir as zonas do hotel (inclui criar, editar e remover zonas).
- 20 quartos: 10 no piso 2, 10 no piso 3; 7 individuais, 10 duplos, 3 familiares (Q-01).
- Preço base por noite por tipo de quarto (gestor, Q-11).
- O gestor configura os itens do quarto.

## Fora do âmbito
- Preços de extras (vista, bebidas, lavandaria) - epic-extra-services-pricing.
- Disponibilidade e reserva - epic-reservations.

## Atores
- Gestor, rececionista.

## Valor
Os quartos e os preços existem antes de qualquer reserva.

## Ligações ao enunciado
Secção 2. RF-03, RF-04, RF-06, RF-08. Q-01, Q-11.

## Histórias candidatas
- Modelar os pisos do hotel (ator: gestor; RF-03)
- Registar os 20 quartos por tipo e piso (ator: gestor; RF-04)
- Definir o preço base por noite de cada tipo de quarto (ator: gestor; RF-06)
- Configurar os itens de um quarto (ator: gestor; RF-08)
- Consultar a lista de quartos (ator: rececionista; RF-04)

## Perguntas em aberto
- Q-01 e Q-11 são decisões da equipa, não confirmadas pelo professor.

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
