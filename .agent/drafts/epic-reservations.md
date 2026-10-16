---
type: epic
summary: Disponibilidade de quartos, reserva, cancelamento e eliminação
parent:
requirements: [RF-05, RF-11, RF-12, RF-22, RNF-03]
points:
points_suggested:
status: published
jira_key: H4Y-15
labels: [reservations, 1st-delivery]
sprint:
---

# Épica: Reservas

## Objetivo
A receção cria e cancela reservas de quartos livres. Só o gestor apaga uma reserva, antes do check-in.

## Entrega
1.ª entrega (reservas).

## Âmbito
- Oferecer quartos livres por tipo e noites; esconder os quartos ocupados.
- Criar uma reserva (pode incluir restaurante e piscina).
- Cancelar = mudança de estado, mantida no histórico (Q-05).
- Apagar = só o gestor, só antes do check-in, de forma definitiva (Q-05).
- Registo de cliente mais reserva em 30 segundos ou menos (RNF-03, Q-12).

## Fora do âmbito
- Ecrãs de registo de clientes (epic-client-registration; ver Q-27).
- Check-in e check-out (epic-check-in-out).

## Atores
- Rececionista, gestor. Cliente (escolhe o tipo de quarto através da receção).

## Valor
O hotel reserva quartos sem reservas duplicadas.

## Ligações ao enunciado
Secções 2, 3c. RF-05, RF-11, RF-12, RF-22, RNF-03.

## Histórias candidatas
- Consultar quartos livres por tipo e noites (ator: rececionista; RF-05)
- Criar uma reserva para um cliente (ator: rececionista; RF-11)
- Incluir restaurante e piscina na reserva (ator: rececionista; RF-11)
- Cancelar uma reserva mantendo o histórico (ator: rececionista; RF-12)
- Apagar uma reserva antes do check-in (ator: gestor; RF-22)
- Recusar o apagar de reservas a outros papéis e após check-in (ator: rececionista; RF-22)
- Medir registo mais reserva abaixo de 30 segundos (ator: rececionista; RNF-03)

## Perguntas em aberto
- Q-27 (em aberto): a reserva precisa de um registo de cliente, o RF-10 não está no âmbito da 1.ª entrega.

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
