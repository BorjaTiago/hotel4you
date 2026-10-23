---
type: subtask
summary: Fluxo rápido registo + reserva em 30 segundos: formulário mínimo, dados mantidos e medição
parent: H4Y-59
requirements: [RNF-03, RF-10]
points:
points_suggested:
status: published
jira_key: H4Y-79
labels: [clients, reservations, usability, 1st-delivery]
sprint: 05
branch: feat/H4Y-79-quick-client-reservation
---

# registar-cliente-rapido: 30 segundos

História pai H4Y-59. Último item do sprint 5. Precisa dos ecrãs de registar-cliente (sprint 4) e de create-reservation (sprint 5) no main. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 5.

## Checklist

- [ ] AC-2: o formulário pede só os dados obrigatórios do cliente.
- [ ] AC-3: com um dado inválido, o formulário mantém os outros dados já escritos.
- [ ] Do registo do cliente passa-se direto à reserva, sem voltar ao menu.
- [ ] AC-1: medir o fluxo com cronómetro, 3 vezes, desde a abertura do registo até à reserva criada. O tempo da escolha do quarto não conta (Q-12, Q-29). Registar os tempos no PR.
- [ ] AC-2 e AC-3 têm testes com `@Tag("H4Y-59-AC-n")`. O AC-1 é um teste manual.

## Interface (UI)

- Botão "Reserva rápida" no menu do rececionista, em destaque (primeiro botão).
- Um só ecrã `quick-booking.fxml` em 3 passos na mesma janela: 1) dados obrigatórios do cliente; 2) tipo, datas e escolha do quarto livre; 3) resumo com preço e botão "Confirmar".
- O cursor começa no primeiro campo. A tecla Enter passa ao campo seguinte e confirma no último.
- Erro num campo: o campo fica a vermelho e os outros dados ficam escritos (AC-3).
- Sucesso: "Reserva criada." e o ecrã fica pronto para o cliente seguinte.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
