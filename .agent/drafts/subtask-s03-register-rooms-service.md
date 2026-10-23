---
type: subtask
summary: 25.1 Serviço: RoomService.saveRoom, repositório de quartos, regras de piso e testes
parent: H4Y-25
requirements: [RF-04]
points:
points_suggested:
status: published
jira_key: H4Y-63
labels: [rooms, 1st-delivery]
sprint: 03
branch: feat/H4Y-63-room-service
---

# register-rooms: regras e gravação

História pai H4Y-25 (3 pontos). Usa `RoomService.saveRoom` e `HotelFacade.saveRoom` do diagrama de classes. Usa a tabela `floors` de H4Y-28.
Responsável proposto: Pedro Costa (a equipa decide).

## Checklist

- [ ] Antes do código: acrescentar o repositório de quartos em `docs/uml/` (o diagrama só tem `UserRepository`).
- [ ] Script SQL em `database/`: tabela `rooms` de `docs/database/schema.md`. Os quartos não vêm no script: o gestor regista-os.
- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-25-AC-n")`.
- [ ] AC-1: um quarto válido no piso 2 fica gravado com o tipo e o piso.
- [ ] AC-2: o resumo dá 20 quartos, 10 por piso, 7 individuais, 10 duplos e 3 familiares (Q-01).
- [ ] AC-3: um quarto no piso 1 é recusado com "Só os pisos 2 e 3 têm quartos".
- [ ] AC-4: o 11.º quarto do piso 3 é recusado.
- [ ] Só o gestor regista quartos (regra de H4Y-23, ou `AccessDeniedException` se 23.1 ainda não estiver no main).

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
