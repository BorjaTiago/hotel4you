---
type: subtask
summary: 50.1 Lista de quartos: serviço, ecrã com filtro por tipo e botão do rececionista
parent: H4Y-50
requirements: [RF-04, RF-06]
points:
points_suggested:
status: published
jira_key: H4Y-67
labels: [rooms, 1st-delivery]
sprint: 03
branch: feat/H4Y-67-list-rooms
---

# list-rooms: lista de quartos

História pai H4Y-50 (2 pontos). Reutiliza o repositório de quartos de H4Y-25.
Comprometida no sprint 3 (decisão do Team Leader, 2026-10-23).
Responsável proposto: Antonio Martinho (a equipa decide).

## Checklist

- [ ] Antes do código: o diagrama não tem um método para listar quartos. Acrescentar o método em `docs/uml/`.
- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-50-AC-n")`.
- [ ] AC-1: cada quarto mostra tipo, piso e preço base por noite.
- [ ] AC-2: o filtro por tipo mostra só esse tipo.
- [ ] AC-3: sem quartos, a lista vazia mostra "Não há quartos".
- [ ] AC-4: sem sessão, o acesso é recusado (regra de H4Y-23).
- [ ] Ecrã: tabela com número, tipo, piso e preço base, e uma caixa para filtrar por tipo. Só leitura. Nenhuma regra no controlador (RNF-07).
- [ ] Botão "Lista de quartos" no menu do rececionista (e do gestor), com a regra de H4Y-23. Texto em português.
- [ ] Verificação manual: entrar como rececionista, abrir a lista, filtrar por familiar.

## Interface (UI)

- Botão "Lista de quartos" no menu do rececionista e do gestor.
- Ecrã `room-list.fxml`: tabela só de leitura (número, piso, tipo, preço base por noite) e caixa de escolha "Tipo" com "Todos", individual, duplo, familiar.
- Sem quartos: a tabela vazia mostra "Não há quartos." (AC-3).

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
