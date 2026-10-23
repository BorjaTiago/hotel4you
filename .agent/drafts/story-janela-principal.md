---
type: story
summary: Abrir a janela principal com navegação por papel
parent: epic-foundation-architecture
requirements: [RNF-06, RNF-07]
points: 3
points_suggested: 3
status: draft
jira_key: H4Y-24
labels: [foundation, gui, 1st-delivery]
sprint:
---

# Título: Abrir a janela principal com navegação por papel

Como utilizador autenticado,
Eu quero uma janela principal com navegação para as minhas funções,
Para que encontre e abra cada função do meu papel.

## Critérios de aceite

### Cenário AC-1: Janela após início de sessão
  Dado que o utilizador iniciou sessão
  Quando a aplicação abre a janela principal
  Então a janela mostra o menu com as funções do papel do utilizador

### Cenário AC-2: Navegar entre funções
  Dado que a janela principal está aberta
  Quando o utilizador escolhe uma função no menu
  Então o sistema abre essa função no centro da janela
  E o menu fica sempre visível para voltar
  E nesta sprint a função "Funcionários" abre um ecrã provisório

### Cenário AC-3: Função não permitida não aparece
  Dado que o utilizador iniciou sessão
  Quando a janela principal mostra o menu
  Então não aparecem funções não permitidas ao papel do utilizador
  E nesta sprint o botão "Funcionários" só aparece ao gestor

### Cenário AC-4: Sem sessão iniciada
  Dado que ninguém iniciou sessão
  Quando a aplicação arranca
  Então o sistema mostra o ecrã de login
  E não mostra a janela principal

## Critérios de aceite (checklist)

- [ ] AC-1: Após iniciar sessão, a janela principal abre com as funções do papel do utilizador.
- [ ] AC-2: O utilizador abre uma função a partir do menu e o menu fica sempre visível para voltar. Nesta sprint: o botão "Funcionários" abre um ecrã provisório.
- [ ] AC-3: Uma função não permitida ao papel não aparece no menu. Nesta sprint: o botão "Funcionários" só aparece ao gestor (`HotelFacade.canCreateStaff`).
- [ ] AC-4: Sem sessão iniciada, a aplicação mostra o ecrã de login e não a janela principal.

## Notas
RNF-06: interface JavaFX desde a 1.ª entrega (Q-22). RNF-07: a janela só mostra e navega. Nenhum `if` de papel no ecrã: usar só os métodos do `HotelFacade` (por agora `canCreateStaff`).

O que já existe (H4Y-32): `HotelApp`, `ScreenLoader` e `HotelApp.currentUser` (o utilizador que o login devolveu). Não criar `Session` nem `PermissionPolicy`: não existem no diagrama.

Esta história faz: `main.fxml` (topo: texto de boas-vindas e botão "Terminar sessão"; esquerda: menu; centro: a função aberta), `MainController` e a tag `@Tag("H4Y-24-AC-3")` nos testes de `canCreateStaff`. Acrescentar `MainController` ao diagrama de classes. Os AC-1, AC-2 e AC-4 são ecrãs e verificam-se à mão (a equipa não usa TestFX).

Dependências: nenhuma. É a primeira a fazer, a seguir ao H4Y-32.

AC-2 (decisão do Team Leader, Tiago Borja, 2026-10-23): o ecrã provisório é aceite nesta sprint. O ecrã real de contas de funcionários fica com o H4Y-27, que troca o ecrã provisório.

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: Shell JavaFX with navigation per role and a no-session guard. One layer, some logic. No data work.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: lukassperandio. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by jira-publisher, Claude Code (claude-sonnet-5-5), 2026-10-16; story-writer, Claude Code (claude-opus-5-5), 2026-10-23; doc-updater, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
