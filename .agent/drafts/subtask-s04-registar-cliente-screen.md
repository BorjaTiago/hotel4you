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

História pai H4Y-52. Depende do serviço. Âmbito mudado por Q-54: o ecrã é como o ecrã de quartos.
Responsável: a definir na planning do sprint 4.

## Checklist

- [x] Ecrã `client.fxml` e `ClientController`, como o ecrã de quartos.
- [x] A tabela mostra os 8 campos.
- [x] As mensagens de erro vêm do serviço. Nenhuma regra no controlador (RNF-07).
- [x] Botão "Clientes" no menu do rececionista e do gestor. Texto em português.
- [ ] Verificação manual dos cenários.

## Interface (UI)

- Botão "Clientes" no menu do rececionista e do gestor (Q-54).
- Ecrã `client.fxml`: campo de pesquisa e tabela com Nome, Tipo de documento, Documento, Data de nascimento, Telefone, Nacionalidade, Email e NIF.
- Formulário com DatePicker para a data de nascimento e ComboBox para o tipo de documento e a nacionalidade. Escrever letras seleciona a nacionalidade.
- Botões: Registar, Guardar alterações, Remover (pede confirmação) e Limpar.
- Um clique numa linha da tabela preenche o formulário.
- Erro: a mensagem indica o campo e o campo fica marcado a vermelho (`field-error`). Cada campo tem `labelFor`. Cliente repetido: "O cliente já existe." Cliente com reservas: a remoção é recusada.
- Sucesso: mensagem a verde e o formulário fica limpo.

Created by: lukassperandio. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-52 client registration by main session, Claude Code (claude-opus-5-5), 2026-11-01; client scope (Q-54) by docs agent, Claude Code (claude-sonnet-5-5), 2026-11-05. Reviewed by: pending.
