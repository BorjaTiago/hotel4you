---
type: subtask
summary: Ecrã: tabela de preços extra por noite com controlador e FXML, e testes
parent: H4Y-93
requirements: [RF-07, RF-09, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-104
labels: [pricing, services, gui]
sprint: 06
branch: feat/H4Y-104-extra-prices-screen
---

# definir-precos-extra: ecrã

História pai: definir-precos-extra (H4Y-93). Depende do serviço da mesma história (mesma pessoa, do serviço ao ecrã). Segue o ecrã do preço base (H4Y-26).
Responsável: a definir na planning do sprint 6.

## Checklist

- [ ] Escrever primeiro os testes com @Tag("H4Y-93-AC-n") e ver que falham (commit de testes antes do código).
- [ ] Antes do código: acordar o controlador e o ficheiro FXML e atualizar `docs/uml/`.
- [ ] O gestor vê a tabela dos 9 itens com o preço por noite e define ou altera um preço (AC-1, AC-2, AC-3).
- [ ] Preço inválido mostra a mensagem do serviço e o preço anterior mantém-se (AC-4). Nenhuma regra no controlador (RNF-07).
- [ ] O botão só aparece no menu do gestor (AC-5, `Permission.MANAGE_EXTRA_PRICES`).
- [ ] Texto em português.
- [ ] Javadoc nas classes e métodos públicos novos do ecrã. O Javadoc compila.

## Interface (UI)

- Botão "Preços extra" no menu do gestor.
- Ecrã `extra-prices.fxml` (nome a confirmar): tabela (extra, preço por noite), campo "Preço por noite" e botão "Guardar preço".
- Mensagem de sucesso a verde e de erro a vermelho. Os textos vêm do serviço.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; jira-publisher, Claude Code (claude-sonnet-5-5), 2026-11-13. Reviewed by: pending.
