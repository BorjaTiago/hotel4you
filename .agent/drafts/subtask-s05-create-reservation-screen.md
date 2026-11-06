---
type: subtask
summary: Ecrã: criar reserva a partir do cliente e do quarto livre escolhidos
parent: H4Y-56
requirements: [RF-11, RNF-07]
points:
points_suggested:
status: removed
jira_key: H4Y-76
labels: [reservations, gui, 1st-delivery]
sprint: 04
branch: feat/H4Y-76-reservation-screen
---

# create-reservation: ecrã

Nota (2026-11-06): feita no sprint 4 como H4Y-75 e H4Y-76. Este rascunho s05 fica removido; o conteúdo abaixo é histórico.

História pai H4Y-56. Depende do serviço. Liga a pesquisa de cliente e a pesquisa de quartos livres (sprint 4). Usa `ReservationController.onCreateReservationClicked`. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: António Martinho (antonioprmartinho).

## Checklist

- [x] O rececionista escolhe o cliente, o quarto livre e as datas, e confirma.
- [x] O ecrã mostra o preço base por noite antes de confirmar (AC-4).
- [x] Os erros vêm do serviço (RNF-07).
- [ ] Verificação manual: reservar e ver que o quarto sai da pesquisa de livres. (Feito pelo `HotelFacade` em 2026-11-06, SQL Server local em Docker, scripts v1 a v10: quarto 201 livre antes, reserva `RESERVED`, quarto fora da pesquisa depois. Falta o clique no ecrã. Fica aberto até alguém clicar no ecrã.)

## Interface (UI)

- Ecrã `reservations.fxml` (H4Y-56): campo "Documento do cliente" e botão "Procurar"; tabela com as reservas do cliente, incluindo as canceladas; botão "Nova reserva".
- Botão "Reservas" no menu do rececionista e do gestor. O gestor tem `MANAGE_RESERVATIONS` porque pode usar todas as funções (H4Y-23 AC-1). A H4Y-77 não precisa de dar este acesso.
- Formulário: cliente (o escolhido em "Usar na reserva", ou procurar pelo documento), quarto livre (o escolhido em "Quartos livres", ou procurar ali mesmo), datas de entrada e saída.
- Antes de confirmar: resumo com o preço base por noite e o número de noites (AC-4). Botão "Confirmar reserva".
- Entrada antes de hoje: "A data de entrada não pode ser antes de hoje." (AC-5, Q-55).
- Dados em falta: a mensagem diz o que falta (AC-3). Quarto ocupado: "O quarto já está reservado nessas datas." (AC-2).
- Depois de confirmar: a nova reserva aparece na tabela com o estado "Reservada".

Created by: lukassperandio. Updated by: BorjaTiago. AI use: backlog-prioritizer, Claude Code (claude-sonnet-5-5), 2026-11-06; sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-56 implementation by Claude Code (claude-opus-5-5), 2026-11-06; PR #51 review round 2 by Claude Code (claude-opus-5-5), 2026-11-06. Reviewed by: pending.
