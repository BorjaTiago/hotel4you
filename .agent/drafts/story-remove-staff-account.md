---
type: story
summary: Remover uma conta de funcionário
parent: epic-staff-management-access
requirements: [RF-26]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [staff]
sprint:
---

# Título: Remover uma conta de funcionário

Como gestor,
Eu quero remover a conta de um funcionário que saiu,
Para que ele deixe de entrar na aplicação.

## Critérios de aceite

### Cenário AC-1: Conta removida
  Dado que existe a conta "ana" do tipo rececionista
  Quando o gestor remover a conta "ana"
  Então a conta deixa de existir
  E "ana" já não consegue iniciar sessão

### Cenário AC-2: Conta inexistente
  Dado que a conta "zé" não existe
  Quando o gestor tentar removê-la
  Então o sistema mostra uma mensagem a indicar que a conta não existe

### Cenário AC-3: Conta com registos associados
  Dado que a conta "ana" tem registos associados
  Quando o gestor tentar removê-la
  Então o sistema recusa a remoção
  E mostra uma mensagem a indicar que a conta tem registos associados e não pode ser removida
  E a conta mantém-se

### Cenário AC-4: Utilizador sem permissão
  Dado que o utilizador autenticado é rececionista
  Quando tentar remover uma conta de funcionário
  Então o sistema recusa a ação

## Critérios de aceite (checklist)

- [ ] AC-1: A conta removida deixa de existir e o funcionário já não consegue iniciar sessão.
- [ ] AC-2: Remover uma conta inexistente mostra que a conta não existe.
- [ ] AC-3: Uma conta com registos associados não é removida, o sistema explica porquê e a conta mantém-se.
- [ ] AC-4: O rececionista não consegue remover contas de funcionário.

## Notas
Só contas (Q-18). RF-26. Fase: Q-25. Q-36 (decisão da equipa): nada que tenha associações pode ser removido; o enunciado não prevê desativar contas, por isso só se recusa.
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: contas de teste próprias, uma sem registos e uma com reserva ou movimento de stock associado (foundation T4); sessões simuladas (T0).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Remoção com verificação de registos associados em várias tabelas e bloqueio de login. Mesma ordem de tamanho da remoção de zona.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
