---
type: subtask
summary: 26.2 Ecrã: tabela de tipos de quarto com o preço base editável
parent: H4Y-26
requirements: [RF-06, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-66
labels: [rooms, pricing, gui, 1st-delivery]
sprint: 03
branch: feat/H4Y-66-base-price-screen
---

# set-base-price: ecrã

História pai H4Y-26. Depende de 26.1.
Responsável proposto: José Cruz (a equipa decide).

## Checklist

- [ ] O ecrã mostra os tipos de quarto com o preço base atual.
- [ ] O gestor altera um preço e grava. O ecrã mostra o valor novo.
- [ ] O erro de preço inválido vem do serviço. Nenhuma regra no controlador (RNF-07).
- [ ] Botão no menu do gestor. Texto em português.

## Interface (UI)

- Botão "Preços" no menu do gestor.
- Ecrã `prices.fxml`: tabela com os 3 tipos de quarto (tipo, capacidade, preço base por noite). Um tipo sem preço mostra "—".
- Ao escolher um tipo: campo "Novo preço (€)" e botão "Guardar". Depois de guardar, a tabela mostra o valor novo (AC-1, AC-2).
- Preço inválido: mensagem de erro e o valor antigo fica na tabela (AC-3).

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
