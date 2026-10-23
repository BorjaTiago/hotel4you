---
type: subtask
summary: 26.1 Serviço: RoomService.setBasePrice, validação, papel e testes
parent: H4Y-26
requirements: [RF-06]
points:
points_suggested:
status: published
jira_key: H4Y-65
labels: [rooms, pricing, 1st-delivery]
sprint: 03
branch: feat/H4Y-65-base-price-service
---

# set-base-price: regras e gravação

História pai H4Y-26 (3 pontos). Usa `RoomService.setBasePrice` e `HotelFacade.setBasePrice` do diagrama de classes. Esta subtask cria a tabela dos tipos de quarto. Não depende de H4Y-25.
Responsável proposto: José Cruz (a equipa decide).

## Checklist

- [ ] Antes do código: acrescentar o repositório de tipos de quarto em `docs/uml/` se faltar.
- [ ] Script SQL em `database/`: tabela `room_types` de `docs/database/schema.md`, com individual, duplo e familiar e o preço base vazio. Correr antes do script de `rooms` (chave estrangeira).
- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-26-AC-n")`.
- [ ] AC-1 e AC-2: definir e alterar o preço base guarda o valor (`BigDecimal`, duas casas).
- [ ] AC-3: preço negativo ou não numérico é recusado e o preço anterior fica.
- [ ] AC-4: o rececionista é recusado (`AccessDeniedException`).

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
