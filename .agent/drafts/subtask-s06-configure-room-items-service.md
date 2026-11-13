---
type: subtask
summary: Serviço: script SQL v11 de room_items, RoomService.configureItem, remover e alterar itens do quarto, regra de associações, Javadoc, diagramas e testes
parent: H4Y-91
requirements: [RF-08]
points:
points_suggested:
status: published
jira_key: H4Y-100
labels: [rooms, 1st-delivery]
sprint: 06
branch: feat/H4Y-100-room-items-service
---

# configure-room-items: tabela e regras dos itens do quarto

História pai: configure-room-items (H4Y-91). Usa `RoomService.configureItem` e `HotelFacade.configureRoomItem` do diagrama. A tabela `room_items` (`room_id`, `item_id`, `quantity > 0`) está em `docs/database/schema.md`, mas o código só chega à versão v10. Esta subtask cria o script **v11** (`database/v11-create-room-items.sql`). Números reservados no sprint 6: v11 room_items, v12 stock_movements, v13 reservation_locations, v14 extra_prices. Não usar outro número. A quantidade de um item do quarto não mexe no stock (Q-58). Reutiliza `Permission.MANAGE_ROOMS` (só gestor): sem permissão nova.
Esta subtask junta o serviço e os documentos (Javadoc, diagramas). A subtask do ecrã fica à parte.
Responsável: a definir na planning do sprint 6.

## Checklist

- [ ] Escrever primeiro os testes com @Tag("H4Y-91-AC-n") e ver que falham (commit de testes antes do código).
- [ ] Antes do código: o diagrama só tem `configureItem`. Acrescentar a `docs/uml/` o que falta: remover item, alterar quantidade, listar os itens de um quarto, e os métodos do repositório (`RoomRepository` ou `ItemRepository`). Seguir `docs/database/schema.md`.
- [ ] Script SQL `database/v11-create-room-items.sql`: tabela `room_items` (`room_id`, `item_id`, `quantity TINYINT NOT NULL`, `quantity > 0`) de `docs/database/schema.md`. Corre depois de v10.
- [ ] AC-1: adicionar um item do catálogo com quantidade (2 toalhas no quarto 201).
- [ ] AC-2: remover um item do quarto.
- [ ] AC-3: quarto inexistente é recusado com "O quarto não existe".
- [ ] AC-4: remover um item com associações é recusado e o item mantém-se (Q-36). Erro SQL 547 vira uma exceção própria, como em `ClientInUseException`.
- [ ] AC-5: o rececionista é recusado (`AccessDeniedException`) com `Permission.MANAGE_ROOMS`. Não criar permissão nova.
- [ ] AC-6: alterar a quantidade (2 para 3).
- [ ] AC-7: quantidade 0, negativa ou que não é número é recusada e o quarto não muda.
- [ ] AC-8: item fora do catálogo é recusado e o quarto não muda.
- [ ] Dados de teste próprios: quarto 201, itens "toalhas" e "sofá" (foundation T4). Testes só de lógica, com repositórios em memória. Sem testes de texto SQL.
- [ ] Cada AC-1 a AC-8 tem pelo menos um teste com `@Tag("H4Y-91-AC-n")`. Nomes `on<Ação>_<resultado>_<condição>`.
- [ ] Javadoc nas classes e métodos públicos novos. O Javadoc compila.
- [ ] `docs/uml/class-diagram.md`, `docs/uml/classes.md` e `docs/database/` iguais ao código (regra 11 de `AGENTS.md`).

## Interface (UI)

Sem ecrã nesta subtask. O ecrã está em configure-room-items (ecrã).

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; jira-publisher, Claude Code (claude-sonnet-5-5), 2026-11-13. Reviewed by: pending.
