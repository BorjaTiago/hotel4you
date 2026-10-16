---
type: story
summary: Criar uma conta de funcionário
parent: epic-staff-management-access
requirements: [RF-26, RF-01]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [staff]
sprint:
---

# Título: Criar uma conta de funcionário

Como gestor,
Eu quero criar a conta de um funcionário com nome de utilizador, palavra-passe e tipo,
Para que o funcionário entre na aplicação com o acesso certo.

## Critérios de aceite

### Cenário AC-1: Conta criada
  Dado que o gestor tem sessão iniciada
  E o nome de utilizador "ana" não existe
  Quando o gestor criar uma conta "ana" do tipo rececionista
  Então a conta fica criada
  E a palavra-passe fica guardada cifrada

### Cenário AC-2: Nome de utilizador repetido
  Dado que já existe a conta "ana"
  Quando o gestor tentar criar outra conta "ana"
  Então o sistema recusa a criação
  E mostra uma mensagem a indicar que o nome já existe

### Cenário AC-3: Dados em falta
  Dado que o gestor tem sessão iniciada
  Quando tentar criar uma conta sem palavra-passe
  Então o sistema recusa a criação

### Cenário AC-4: Utilizador sem permissão
  Dado que o utilizador autenticado é rececionista
  Quando tentar criar uma conta de funcionário
  Então o sistema recusa a ação

## Notas
Só contas, sem horários (Q-18). Tipos: rececionista, funcionário do restaurante (Q-23) e gestor; outro pessoal não consta do enunciado. Palavra-passe cifrada: RNF-02. Login: epic-authentication-users. Fase: Q-25.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Validação de dados e nome único, mais cifra da palavra-passe (RNF-02). Um pouco de segurança além do registo normal, ainda uma camada de lógica e um formulário.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
