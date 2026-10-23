---
type: subtask
summary: Procurar cliente por documento: serviço, ecrã e testes
parent: H4Y-53
requirements: [RF-10]
points:
points_suggested:
status: published
jira_key: H4Y-71
labels: [clients, 1st-delivery]
sprint: 04
branch: feat/H4Y-71-client-search
---

# procurar-cliente: pesquisa

História pai H4Y-53. Usa `ClientService.find(document)` e `ClientController.onSearchClientClicked`. O dado de pesquisa é o documento, porque é o que o diagrama define. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 4.

## Checklist

- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-53-AC-n")`.
- [ ] AC-1: um documento registado devolve o cliente.
- [ ] AC-2: um documento sem resultado mostra "Sem resultados" e um botão para registar o cliente.
- [ ] AC-3: uma pesquisa vazia é recusada.
- [ ] A ligação ao ecrã de registo faz-se no fim do sprint, quando o ecrã de registo estiver no main.

## Interface (UI)

- No mesmo ecrã `client.fxml` de H4Y-70, separador "Procurar": campo "Documento" e botão "Procurar".
- Resultado: nome, documento e data de nascimento do cliente, e botão "Usar na reserva" (guarda o cliente escolhido para o ecrã de reserva do sprint 5).
- Sem resultado: "Sem resultados." e botão "Registar novo cliente", que abre o separador "Registar" com o documento já escrito (AC-2).
- Pesquisa vazia: "Indique o documento do cliente." (AC-3).

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
