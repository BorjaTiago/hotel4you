---
type: epic
summary: Cartão magnético do cliente, acesso às portas e registo de acessos
parent:
requirements: [RF-03, RF-13, RF-16, RF-17, RF-18, RF-19]
points:
points_suggested:
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Epic: Cartão magnético do cliente e registo de acessos

## Objetivo
Após o check-in, o cliente recebe um cartão. O cartão abre o quarto e, se constarem da reserva, o restaurante e a piscina. O sistema regista cada entrada.

## Entrega
Entrega final (Q-06).

## Âmbito
- Emissão do cartão no check-in; um cartão por reserva (Q-09).
- O cartão abre o quarto; restaurante e piscina apenas se reservados.
- Sem acesso ao bar, salas de conferência, garagem, arrecadação, piscinas privadas e spa (Q-20).
- Registo: local e hora de entrada, incluindo as entradas nos quartos.
- Zonas do modelo de pisos (RF-03).

## Fora de âmbito
- Leitores físicos (entrada simulada na aplicação).
- Cartão mestre (epic-master-card-audit).

## Atores
- Rececionista, cliente.

## Valor
O hotel controla e regista quem entra em cada local.

## Ligações ao enunciado
Secção 2. RF-03, RF-13, RF-16, RF-17, RF-18, RF-19. Q-09, Q-20.

## Histórias candidatas
- Emitir um cartão com número ao fazer check-in (actor: receptionist; RF-13, RF-16)
- Abrir o quarto com o cartão (actor: client; RF-17)
- Abrir restaurante e piscina só se estiverem na reserva (actor: client; RF-18)
- Recusar o cartão nos locais não permitidos (actor: client; RF-18)
- Guardar cada passagem com local e hora (actor: client; RF-19)
- Criar uma zona num piso, com marca "exige reserva" (actor: manager; RF-03) - juntada a H4Y-28 (epic-rooms-pricing)
- Editar uma zona de um piso (actor: manager; RF-03) - juntada a H4Y-28 (epic-rooms-pricing)
- Remover uma zona de um piso (actor: manager; RF-03) - juntada a H4Y-28 (epic-rooms-pricing)

## Questões em aberto
- Estado do cartão no check-out: decidido em 2026-10-16. O cartão deixa de funcionar e o registo de acessos mantém-se (ver story-check-out AC-4).
- Zonas: decidido em 2026-10-16. O gestor cria, edita e remove zonas por piso e indica se uma zona exige reserva.
- Como se introduz uma entrada de porta sem hardware? Escolha da equipa: simulação na GUI.

AI use: epic-writer, general-purpose (tradução para PT-PT), Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
