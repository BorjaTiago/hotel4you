---
type: subtask
summary: Ecrã: criar reserva a partir do cliente e do quarto livre escolhidos
parent: H4Y-56
requirements: [RF-11, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-76
labels: [reservations, gui, 1st-delivery]
sprint: 04
branch: feat/H4Y-76-reservation-screen
---

# create-reservation: ecrã

História pai H4Y-56. Depende do serviço. Liga a pesquisa de cliente e a pesquisa de quartos livres (mesmo sprint). Usa `ReservationController.onCreateReservationClicked`. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: António Martinho.

## Checklist

- [ ] O rececionista escolhe o cliente, o quarto livre e as datas, e confirma.
- [ ] O ecrã mostra o preço base por noite antes de confirmar (AC-4).
- [ ] Os erros vêm do serviço (RNF-07).
- [ ] Verificação manual: reservar e ver que o quarto sai da pesquisa de livres.

## Interface (UI)

Esta subtask cria o ecrã de reservas (ainda não existe). H4Y-74 e H4Y-77 acrescentam botões ao mesmo ecrã.
- Botão "Reservas" no menu do rececionista. A H4Y-77 (sprint 5) dá acesso ao gestor.
- Ecrã `reservations.fxml`: campo "Documento do cliente" e botão "Procurar"; tabela com as reservas do cliente (quarto, entrada, saída, estado), incluindo as canceladas (histórico).
- No mesmo ecrã: botão "Nova reserva".
- Formulário: cliente (o escolhido em "Usar na reserva", ou procurar pelo documento), quarto livre (o escolhido em "Quartos livres", ou procurar ali mesmo), datas de entrada e saída.
- Antes de confirmar: resumo com o preço base por noite e o número de noites (AC-4). Botão "Confirmar reserva".
- Dados em falta: a mensagem diz o que falta (AC-3). Quarto ocupado: "O quarto já está reservado nessas datas." (AC-2).
- Depois de confirmar: a nova reserva aparece na tabela com o estado "Reservada".

Created by: lukassperandio. Updated by: jmbcruz. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; sprint 4 scope swap by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-31. Reviewed by: pending.
