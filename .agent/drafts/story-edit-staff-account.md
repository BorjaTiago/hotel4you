---
type: story
summary: Editar uma conta de funcionário
parent: epic-staff-management-access
requirements: [RF-26]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [staff]
sprint:
---

# Título: Editar uma conta de funcionário

Como gestor,
Eu quero editar os dados de uma conta de funcionário,
Para que a conta fique correta quando o funcionário muda.

## Critérios de aceite

### Cenário AC-1: Conta editada
  Dado que existe a conta "ana" do tipo rececionista
  Quando o gestor mudar o tipo da conta para funcionário do restaurante
  Então a conta fica com o novo tipo
  E o acesso de "ana" segue o novo tipo no próximo login

### Cenário AC-2: Nome já em uso
  Dado que existem as contas "ana" e "rui"
  Quando o gestor tentar mudar o nome de "ana" para "rui"
  Então o sistema recusa a alteração
  E a conta "ana" não muda

### Cenário AC-3: Conta inexistente
  Dado que a conta "zé" não existe
  Quando o gestor tentar editá-la
  Então o sistema mostra uma mensagem a indicar que a conta não existe

### Cenário AC-4: Utilizador sem permissão
  Dado que o utilizador autenticado não é gestor
  Quando tentar editar uma conta de funcionário
  Então o sistema recusa a ação

## Notas
Depende de "Criar uma conta de funcionário". Só contas, sem horários (Q-18). RF-26. Fase: Q-25.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Edição de tipo e nome com nome único, conta existente e perfil. O novo tipo aplica-se no login seguinte. Formulário simples.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
