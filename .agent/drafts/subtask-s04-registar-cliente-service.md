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

História pai H4Y-52. Âmbito mudado por Q-54: gestão completa de clientes pelo rececionista e pelo gestor.
Responsável: a definir na planning do sprint 4.

Construído:
- `Client` com nome, tipo de documento (CC, PASSPORT, OTHER), documento, data de nascimento (não pode ser no futuro), telefone e nacionalidade (código ISO) obrigatórios. Email e NIF são opcionais.
- `ClientService`: `register`, `listClients`, `searchClients`, `editClient` e `removeClient`. Cada método verifica `MANAGE_CLIENTS` (rececionista e gestor).
- Duplicado: mesmo tipo e número de documento.
- Erro SQL 547 vira `ClientInUseException`. Erros 2601 e 2627 viram `ClientExistsException`.
- Script `v10-alter-customers.sql` (tabela `customers`).

## Checklist

- [x] Antes do código: repositório de clientes em `docs/uml/`.
- [x] Script SQL em `database/`: `v10-alter-customers.sql`.
- [x] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-52-AC-n")`.
- [x] AC-1: um cliente com dados válidos fica gravado na tabela `customers`.
- [x] AC-2: um dado obrigatório em falta ou inválido é recusado com o nome do campo.
- [x] AC-3: um cliente com o mesmo tipo e número de documento é recusado com "O cliente já existe".
- [x] AC-4 a AC-7: listar, procurar, editar e remover; cliente com reservas não é removido.
- [x] AC-8: o rececionista e o gestor gerem clientes (Q-54, `MANAGE_CLIENTS`).

## Interface (UI)

Sem ecrã nesta subtask. O ecrã está em H4Y-70.

Created by: lukassperandio. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-52 client registration by main session, Claude Code (claude-opus-5-5), 2026-11-01; client scope (Q-54) by docs agent, Claude Code (claude-sonnet-5-5), 2026-11-05. Reviewed by: pending.
