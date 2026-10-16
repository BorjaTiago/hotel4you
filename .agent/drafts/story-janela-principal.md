---
type: story
summary: Abrir a janela principal com navegação por papel
parent: epic-foundation-architecture
requirements: [RNF-06, RNF-07]
points: 3
points_suggested: 3
status: published
jira_key: H4Y-24
labels: [foundation, gui, 1st-delivery]
sprint:
---

# Título: Abrir a janela principal com navegação por papel

Como rececionista,
Eu quero uma janela principal com navegação para as minhas funções,
Para que encontre e abra cada função da receção.

## Critérios de aceite

### Cenário AC-1: Janela após início de sessão
  Dado que o rececionista iniciou sessão
  Quando a aplicação abre a janela principal
  Então a janela mostra a navegação para as funções do rececionista

### Cenário AC-2: Navegar entre funções
  Dado que a janela principal está aberta
  Quando o rececionista escolhe uma função na navegação
  Então o sistema abre essa função
  E o rececionista pode voltar à janela principal

### Cenário AC-3: Função não permitida não aparece
  Dado que o rececionista iniciou sessão
  Quando a janela principal mostra a navegação
  Então não aparecem funções exclusivas de outros papéis

### Cenário AC-4: Sem sessão iniciada
  Dado que ninguém iniciou sessão
  Quando a aplicação arranca
  Então o sistema mostra o ecrã de início de sessão
  E não mostra a janela principal

## Critérios de aceite (checklist)

- [ ] AC-1: Após iniciar sessão, a janela principal abre com as funções do papel do utilizador.
- [ ] AC-2: O utilizador navega entre as funções disponíveis a partir da janela principal.
- [ ] AC-3: Uma função não permitida ao papel não aparece na janela.
- [ ] AC-4: Sem sessão iniciada, a janela principal não é acessível.

## Notas
RNF-06: interface JavaFX desde a 1.ª entrega (Q-22). RNF-07: a janela só mostra e navega, a lógica fica separada. Sem desenho de ecrãs aqui.
Dependências (auditoria 2026-10-16): não depende de "Iniciar sessão como rececionista" nem de "Mostrar apenas as funções permitidas ao papel". Usa uma sessão simulada (stub).

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: Shell JavaFX with navigation per role and a no-session guard. One layer, some logic. No data work.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by jira-publisher, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
