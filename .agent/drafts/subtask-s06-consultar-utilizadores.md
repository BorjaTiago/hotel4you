---
type: subtask
summary: Consultar um utilizador: StaffService.findUser com verificação do gestor, fachada, pesquisa no ecrã de contas e testes
parent: H4Y-89
requirements: [RNF-01]
points:
points_suggested:
status: published
jira_key: H4Y-97
labels: [security, 1st-delivery]
sprint: 06
branch: feat/H4Y-97-consultar-utilizadores
---

# consultar-utilizadores: consultar um utilizador

História pai: consultar-utilizadores (H4Y-89). A lista já existe (`StaffService.listUsers`, `StaffController`). Esta subtask acrescenta só o que falta. Sem script SQL. Uma pessoa faz tudo.
Responsável: a definir na planning do sprint 6.

## Checklist

- [ ] Escrever primeiro os testes com @Tag("H4Y-89-AC-n") e ver que falham (commit de testes antes do código).
- [ ] Antes do código: ler `StaffService`, `HotelFacade`, `StaffController` e `docs/uml/`. Atualizar `docs/uml/` com `findUser` e a nova assinatura de `listUsers(actor)` (regra 11 de `AGENTS.md`).
- [ ] `StaffService.findUser(username, actor)`: verifica `Permission.MANAGE_STAFF` e devolve o utilizador. Lança `AccessDeniedException` se o `actor` não é gestor.
- [ ] AC-4: utilizador inexistente lança a exceção de não encontrado (`StaffNotFoundException`, "A conta não existe").
- [ ] AC-3: consultar "ana" devolve os dados dessa conta (nome, nome de utilizador, papel; nunca o hash da palavra-passe).
- [ ] AC-1: `listUsers(actor)` verifica `Permission.MANAGE_STAFF`. Atualizar os chamadores (fachada, controlador, testes existentes). Um rececionista é recusado.
- [ ] Método na fachada (`HotelFacade`) para `findUser`.
- [ ] Ecrã de contas: um campo "Nome de utilizador" e um botão "Consultar" mostram os dados da conta ou a mensagem do serviço. Nenhuma regra no controlador (RNF-07).
- [ ] AC-2 (persistência): já coberto pelo repositório JDBC. Só confirmar com um teste de lógica se faltar. Sem testes de texto SQL.
- [ ] Dados de teste próprios: contas "ana" (rececionista) e um gestor (foundation T4). Testes só de lógica, com repositório em memória.
- [ ] Javadoc nos métodos novos. O Javadoc compila.

## Interface (UI)

- No ecrã de contas: campo "Nome de utilizador" e botão "Consultar". Resultado: nome, nome de utilizador e papel. Mensagem de erro a vermelho para conta inexistente.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; jira-publisher, Claude Code (claude-sonnet-5-5), 2026-11-13. Reviewed by: pending.
