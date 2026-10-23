---
type: subtask
summary: Janela principal com navegação por papel e proteção sem sessão
parent: H4Y-24
requirements: [RNF-06, RNF-07]
points:
points_suggested:
status: published
jira_key: H4Y-42
labels: [foundation, gui, 1st-delivery]
sprint: 02
branch: feat-024-main-window-navigation
---

# janela-principal: janela principal e navegação

História pai H4Y-24 (3 pontos). Baseia-se na estrutura de T0b, na sessão simulada e na política simulada (stub). Não espera pelo login. A chave 024 do ramo é a chave da história. A equipa pode preferir a chave da sub-tarefa depois de publicar.
Responsável proposto: Pedro Costa (a equipa decide).

## Checklist

- [ ] AC-1: Com uma sessão, a janela principal mostra a navegação do papel do utilizador.
- [ ] AC-2: O utilizador abre uma função a partir da navegação e volta à janela principal.
- [ ] AC-3: Uma função não permitida ao papel não aparece (usa o papel do utilizador em `HotelApp.currentUser`; a regra de funções por papel fica em `HotelFacade`).
- [ ] AC-4: Sem sessão, a aplicação mostra o ecrã de login e não a janela principal.
- [ ] A janela só mostra e navega. Nenhuma regra de negócio na GUI (RNF-07).
- [ ] Voltar a verificar AC-3 depois de `funcoes-por-papel` (sprint 3).

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-10-16. Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16; doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-19. Reviewed by: pending.
