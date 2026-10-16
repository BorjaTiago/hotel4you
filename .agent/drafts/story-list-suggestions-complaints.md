---
type: story
summary: Consultar sugestões e reclamações
parent: epic-guest-support
requirements: [RF-27]
points: 2
points_suggested: 2
status: approved
jira_key:
labels: [guest-support]
sprint:
---

# Título: Consultar sugestões e reclamações

Como gestor,
Eu quero consultar as sugestões e reclamações registadas pela receção,
Para que eu conheça a opinião dos clientes.

## Critérios de aceite

### Cenário AC-1: Lista com registos
  Dado que a receção registou uma sugestão e uma reclamação
  Quando o gestor abrir a lista de sugestões e reclamações
  Então vê os dois registos com tipo, texto e data e hora

### Cenário AC-2: Lista vazia
  Dado que ainda não existem registos
  Quando o gestor abrir a lista
  Então o sistema mostra uma lista vazia com uma mensagem a indicar que não há registos

### Cenário AC-3: Utilizador sem permissão
  Dado que o utilizador autenticado é rececionista ou funcionário do restaurante
  Quando tentar abrir a lista
  Então o sistema recusa o acesso

## Critérios de aceite (checklist)

- [ ] AC-1: O gestor vê a sugestão e a reclamação registadas, cada uma com tipo, texto e data e hora.
- [ ] AC-2: Sem registos, a lista aparece vazia com uma mensagem a indicar que não há registos.
- [ ] AC-3: Rececionista e funcionário do restaurante não conseguem abrir a lista.

## Notas
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: sugestões e reclamações de teste próprias (foundation T4). Enunciado, secção 2. RF-27. Fase: Q-25.

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: Lista simples só de leitura, com lista vazia e verificação do perfil. Sem regras de escrita.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
