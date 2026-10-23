---
type: subtask
summary: 25.2 Ecrã: rooms.fxml e RoomController para registar quartos
parent: H4Y-25
requirements: [RF-04, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-64
labels: [rooms, gui, 1st-delivery]
sprint: 03
branch: feat/H4Y-64-room-screen
---

# register-rooms: ecrã

História pai H4Y-25. Depende de 25.1. `RoomController` já está no diagrama de classes.
Responsável proposto: Pedro Costa (a equipa decide).

## Checklist

- [ ] O gestor escolhe piso e tipo, escreve o número e grava.
- [ ] O ecrã mostra a lista de quartos e o resumo por piso e por tipo (AC-2).
- [ ] As mensagens de erro vêm do serviço. Nenhuma regra no controlador (RNF-07).
- [ ] Botão no menu do gestor. Texto em português.
- [ ] Verificação manual: registar os 20 quartos e ver o resumo.

## Interface (UI)

- Botão "Registar quartos" no menu do gestor.
- Ecrã `rooms.fxml`: formulário com "Número" (campo de texto), "Piso" (caixa de escolha só com 2 e 3) e "Tipo" (caixa de escolha: individual, duplo, familiar), botão "Registar".
- Por baixo: tabela com os quartos registados (número, piso, tipo) e um resumo "Piso 2: x/10 · Piso 3: y/10 · Individuais x/7 · Duplos y/10 · Familiares z/3" (AC-2).
- Mensagem de sucesso a verde e de erro a vermelho, por baixo do formulário. Os textos vêm do serviço.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
