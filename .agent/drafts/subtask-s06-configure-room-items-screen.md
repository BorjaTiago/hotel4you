---
type: subtask
summary: Ecrã: itens de um quarto (adicionar, alterar quantidade, remover), testes e verificação manual
parent: H4Y-91
requirements: [RF-08, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-101
labels: [rooms, gui, 1st-delivery]
sprint: 06
branch: feat/H4Y-101-room-items-screen
---

# configure-room-items: ecrã

História pai: configure-room-items (H4Y-91). Depende do serviço da mesma história (mesma pessoa, do serviço ao ecrã). Usa `RoomController` do diagrama ou um controlador novo: decidir e acrescentar a `docs/uml/` antes do código. Esta subtask inclui os testes do ecrã e a verificação manual (a subtask de testes e documentos foi junta ao serviço e a este ecrã).
Responsável: a definir na planning do sprint 6.

## Checklist

- [ ] Escrever primeiro os testes com @Tag("H4Y-91-AC-n") e ver que falham (commit de testes antes do código).
- [ ] Antes do código: acordar o controlador e o ficheiro FXML e atualizar `docs/uml/`.
- [ ] O gestor escolhe um quarto, vê a lista de itens com a quantidade e adiciona, altera ou remove um item (AC-1, AC-2, AC-6).
- [ ] Os itens a adicionar vêm do catálogo de stock (`StockService.listItems`).
- [ ] Os erros vêm do serviço: quarto inexistente, associações, quantidade inválida, item fora do catálogo (AC-3, AC-4, AC-7, AC-8). Nenhuma regra no controlador (RNF-07).
- [ ] O botão só aparece no menu do gestor (AC-5, `Permission.MANAGE_ROOMS`).
- [ ] Texto em português.
- [ ] Javadoc nas classes e métodos públicos novos do ecrã. O Javadoc compila.
- [ ] Verificação manual: o gestor adiciona 2 toalhas ao quarto 201, altera para 3, remove o sofá e vê a recusa de um item com associações.

## Interface (UI)

- Botão "Itens do quarto" no menu do gestor.
- Ecrã `room-items.fxml` (nome a confirmar): "Quarto" (caixa de escolha), tabela com os itens (nome, quantidade), "Item" (caixa de escolha do catálogo), "Quantidade" (campo de texto), botões "Adicionar", "Alterar quantidade" e "Remover".
- Mensagem de sucesso a verde e de erro a vermelho. Os textos vêm do serviço. "Remover" pede confirmação.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; jira-publisher, Claude Code (claude-sonnet-5-5), 2026-11-13. Reviewed by: pending.
