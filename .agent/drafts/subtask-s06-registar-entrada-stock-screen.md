---
type: subtask
summary: Ecrã: registar a entrada de um item de stock
parent: H4Y-90
requirements: [RF-29, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-99
labels: [stock, gui, 1st-delivery]
sprint: 06
branch: feat/H4Y-99-stock-entry-screen
---

# registar-entrada-stock: ecrã

História pai: registar-entrada-stock (H4Y-90). Depende do serviço da mesma história (mesma pessoa, do serviço ao ecrã). Usa `StockController` do diagrama e o ecrã de stock de H4Y-51.
Responsável: a definir na planning do sprint 6.

## Checklist

- [ ] Escrever primeiro os testes com @Tag("H4Y-90-AC-n") e ver que falham (commit de testes antes do código).
- [ ] O utilizador escolhe o item, escreve a quantidade e regista. A data é a de hoje.
- [ ] O ecrã mostra o stock novo do item depois de registar (AC-1).
- [ ] Quantidade inválida e item inexistente mostram a mensagem do serviço (AC-2, AC-3). Nenhuma regra no controlador (RNF-07).
- [ ] O botão só aparece no menu do gestor (`Permission.MANAGE_STOCK`, só gestor).
- [ ] Texto em português.

## Interface (UI)

- Botão "Entrada de stock" no ecrã de stock, no menu do gestor.
- Formulário: "Item" (caixa de escolha), "Quantidade" (campo de texto), "Data" (hoje, só leitura), botão "Registar entrada".
- Por baixo: a lista de itens com o stock atual. Mensagem de sucesso a verde e de erro a vermelho. Os textos vêm do serviço.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; jira-publisher, Claude Code (claude-sonnet-5-5), 2026-11-13. Reviewed by: pending.
