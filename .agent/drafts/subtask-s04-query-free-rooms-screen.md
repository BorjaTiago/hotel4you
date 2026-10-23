---
type: subtask
summary: Ecrã: pesquisa de quartos livres por tipo e datas
parent: H4Y-54
requirements: [RF-05, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-73
labels: [reservations, gui, 1st-delivery]
sprint: 04
branch: feat/H4Y-73-free-rooms-screen
---

# query-free-rooms: ecrã

História pai H4Y-54. Depende do serviço. Usa `RoomController.onSearchClicked`. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 4.

## Checklist

- [ ] O rececionista escolhe o tipo, a data de entrada e a data de saída.
- [ ] O ecrã mostra os quartos livres ou a mensagem do serviço.
- [ ] Nenhuma regra de datas no controlador (RNF-07).
- [ ] O ecrã deixa escolher um quarto. A reserva (sprint 5) usa essa escolha.

## Interface (UI)

- Botão "Quartos livres" no menu do rececionista.
- Ecrã `free-rooms.fxml`: "Tipo" (caixa de escolha), "Entrada" e "Saída" (seletores de data), botão "Procurar".
- Resultado: tabela com os quartos livres (número, piso, tipo, preço por noite e total das noites).
- Sem quartos: "Não há quartos livres para estas datas." Período inválido: "A data de saída tem de ser depois da data de entrada."
- Botão "Escolher quarto" na linha selecionada (usado pela reserva do sprint 5).

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
