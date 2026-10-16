---
type: epic
summary: Login, papéis de utilizador e armazenamento seguro de utilizadores
parent:
requirements: [RF-01, RF-02, RNF-01, RNF-02]
points:
points_suggested:
status: published
jira_key: H4Y-12
labels: [security, login, 1st-delivery]
sprint:
---

# Épica: Autenticação, papéis e utilizadores

## Objetivo
O pessoal do hotel, o pessoal do restaurante e os gestores iniciam sessão com nome de utilizador e palavra-passe. Cada papel tem o seu próprio acesso.

## Entrega
1.ª entrega (login).

## Âmbito
- Login do rececionista, do gestor e do pessoal do restaurante (Q-23).
- Cinco tipos de ator e as suas regras de acesso.
- Todos os utilizadores guardados e consultáveis a qualquer momento.
- Palavras-passe guardadas cifradas.
- Mesmo erro para palavra-passe errada e conta desconhecida.

## Fora do âmbito
- Login de clientes e fornecedores (não iniciam sessão, Q-13, Q-17).
- Ecrãs de gestão de contas do pessoal (epic-staff-management).
- Limites por piso e zona (epic-staff-access-rules).

## Atores
- Rececionista, gestor, pessoal do restaurante.

## Valor
Só utilizadores conhecidos entram no sistema. As palavras-passe estão seguras.

## Ligações ao enunciado
Secções 2, 3a, 3b. RF-01, RF-02, RNF-01, RNF-02.

## Histórias candidatas
- Iniciar sessão com utilizador e palavra-passe (ator: rececionista; RF-01)
- Iniciar sessão como gestor (ator: gestor; RF-01)
- Iniciar sessão como pessoal do restaurante (ator: pessoal do restaurante; RF-01)
- Guardar palavras-passe cifradas (ator: gestor; RNF-02)
- Guardar e consultar todos os utilizadores (ator: gestor; RNF-01)
- Mostrar apenas as funções permitidas a cada papel (ator: rececionista; RF-02)

## Perguntas em aberto
- Nenhuma nova. Q-13, Q-17 e Q-23 são decisões da equipa, não confirmadas pelo professor.

## Tarefas técnicas (candidatas a sub-tarefas)
Decisão da equipa (2026-10-16): trabalho técnico sem ator de negócio é tarefa, não história. Fica ligado a esta épica.

### T1: Guardar palavras-passe cifradas (RNF-02)
- [ ] Ao criar um utilizador, a palavra-passe guardada não está em texto simples e difere da introduzida.
- [ ] Com a palavra-passe correta, o início de sessão é aceite.
- [ ] Com palavra-passe errada, o acesso é recusado (mesmo erro que conta desconhecida).
- [ ] O gestor, ao consultar um utilizador, não vê a palavra-passe (confirmar no refinamento).
- [ ] Se usar biblioteca externa, justificar em docs/libraries.md (RNF-11).

### T2: Dados iniciais: 5 papéis e a conta do primeiro gestor (RF-02, RF-01, RNF-02)
Sprint 2, depois de epic-foundation-architecture T0 e T2 e depois de T1 (cifra). Antes das histórias de login. Decisão do utilizador (2026-10-16).
- [ ] Com a base vazia, o script cria os 5 papéis do RF-02: cliente, rececionista, gestor, funcionário do restaurante, fornecedor. Cliente e fornecedor não recebem conta (Q-13, Q-17).
- [ ] O script cria 1 conta de gestor. O nome de utilizador e a palavra-passe vêm da configuração local ou de variáveis de ambiente, nunca do código, do Git ou da documentação.
- [ ] A palavra-passe fica guardada só como hash (T1). O valor em texto não aparece em logs.
- [ ] Sem configuração da conta inicial, o script para com uma mensagem clara e não cria uma conta com palavra-passe por omissão.
- [ ] O script corre duas vezes sem duplicar papéis nem a conta.
- [ ] O gestor inicial cria as outras contas com story-create-staff-account (sprint 2).

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; task T2 by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
