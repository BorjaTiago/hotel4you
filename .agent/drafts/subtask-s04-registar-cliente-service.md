---
type: subtask
summary: Serviço: ClientService.register, repositório de clientes, duplicado e testes
parent: H4Y-52
requirements: [RF-10]
points:
points_suggested:
status: published
jira_key: H4Y-69
labels: [clients, 1st-delivery]
sprint: 04
branch: feat/H4Y-69-client-service
---

# registar-cliente: regras e gravação

História pai H4Y-52. Usa `ClientService.register(name, document, birthday)` e `HotelFacade.registerClient` do diagrama de classes. Previsão. Confirmar ou mudar no refinamento e na planning do sprint.
Responsável: a definir na planning do sprint 4.

## Checklist

- [ ] Antes do código: acrescentar o repositório de clientes em `docs/uml/` (o diagrama não tem nenhum).
- [ ] Script SQL em `database/`: tabela `customers` de `docs/database/schema.md`.
- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-52-AC-n")`.
- [ ] AC-1: um cliente com dados válidos fica gravado na tabela `customers`.
- [ ] AC-2: um dado obrigatório em falta é recusado com o nome do campo.
- [ ] AC-3: um cliente com o mesmo documento é recusado com "O cliente já existe".
- [ ] Só o rececionista regista clientes (Q-13).

## Interface (UI)

Sem ecrã nesta subtask. O ecrã está em H4Y-70.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
