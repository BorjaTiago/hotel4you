---
type: subtask
summary: Serviço: RoomService.isAvailable e searchRooms, regra de sobreposição de datas e testes
parent: H4Y-54
requirements: [RF-05]
points:
points_suggested:
status: published
jira_key: H4Y-72
labels: [reservations, 1st-delivery]
sprint: 04
branch: feat/H4Y-72-free-rooms-service
---

# query-free-rooms: regra de disponibilidade

História pai H4Y-54. Dona da regra única de sobreposição de datas. A reserva (sprint 5) reutiliza-a. Usa `RoomService.isAvailable` e `RoomService.searchRooms` do diagrama. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 4. Proposta: uma pessoa com experiência em datas e SQL.

## Checklist

- [ ] Antes do código: acrescentar o repositório de reservas (só leitura por agora) em `docs/uml/`.
- [ ] Script SQL em `database/`: tabela `reservations` de `docs/database/schema.md`.
- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-54-AC-n")`.
- [ ] AC-1: um quarto sem reservas no período é devolvido.
- [ ] AC-2: um quarto com reserva ativa que se sobrepõe não é devolvido. Testar as fronteiras: a noite de saída fica livre.
- [ ] AC-3: uma reserva cancelada não ocupa o quarto.
- [ ] AC-4: saída igual ou anterior à entrada é recusada com "Período inválido".
- [ ] AC-5: sem quartos livres, a resposta é uma lista vazia com mensagem.
- [ ] Reservas de teste próprias: uma ativa e uma cancelada.

## Interface (UI)

Sem ecrã nesta subtask. O ecrã está em H4Y-73.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
