---
type: epic
summary: Contas de funcionários e limites de acesso por piso e zona
parent:
requirements: [RF-24, RF-25, RF-26]
points:
points_suggested:
status: approved
jira_key:
labels: [staff, access-control]
sprint:
---

# Epic: Gestão de funcionários e limites de acesso

## Objetivo
O gestor gere as contas dos funcionários. A receção vê apenas os pisos 0 e 1. O pessoal do restaurante vê apenas a cozinha e o restaurante.

## Entrega
Planeada pelo priorizador (Q-25).

## Âmbito
- Criar, editar e remover contas de funcionários (sem horários, Q-18).
- Receção limitada aos serviços dos pisos 0 e 1 (Q-08).
- Pessoal do restaurante limitado à cozinha e ao restaurante.

## Fora de âmbito
- O login em si (epic-authentication-users).

## Atores
- Gestor, rececionista, pessoal do restaurante.

## Valor
Cada funcionário tem o acesso adequado.

## Ligações ao enunciado
Secção 2. RF-24, RF-25, RF-26. Q-08, Q-18, Q-23.

## Histórias candidatas
- Criar uma conta de funcionário (actor: manager; RF-26)
- Editar uma conta de funcionário (actor: manager; RF-26)
- Remover uma conta de funcionário (actor: manager; RF-26)
- Limitar a receção aos serviços dos pisos 0 e 1 (actor: receptionist; RF-24)
- Limitar o restaurante à cozinha e ao restaurante (actor: restaurant staff; RF-25)

## Questões em aberto
- Outro pessoal, como a limpeza, não consta do enunciado (notas de actors.md).

AI use: epic-writer, general-purpose (tradução para PT-PT), Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
