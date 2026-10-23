---
type: subtask
summary: Ecrã: client.fxml e ClientController para registar clientes
parent: H4Y-52
requirements: [RF-10, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-70
labels: [clients, gui, 1st-delivery]
sprint: 04
branch: feat/H4Y-70-client-screen
---

# registar-cliente: ecrã

História pai H4Y-52. Depende do serviço. `ClientController.onRegisterClientClicked` já está no diagrama. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 4.

## Checklist

- [ ] O formulário pede só os campos do diagrama: nome, documento, data de nascimento.
- [ ] As mensagens de erro vêm do serviço. Nenhuma regra no controlador (RNF-07).
- [ ] Botão no menu do rececionista, com a regra de H4Y-23. Texto em português.
- [ ] Verificação manual dos 3 cenários.

## Interface (UI)

- Botão "Clientes" no menu do rececionista.
- Ecrã `client.fxml`, separador "Registar": campos "Nome", "Documento", "Data de nascimento" (seletor de data) e botão "Registar".
- Erro: a mensagem indica o campo em falta e o campo fica marcado a vermelho (AC-2). Cliente repetido: "O cliente já existe." (AC-3).
- Sucesso: mensagem a verde e o formulário fica limpo para o próximo cliente.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
