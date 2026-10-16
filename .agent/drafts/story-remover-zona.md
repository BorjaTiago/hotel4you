---
type: story
summary: Remover uma zona de um piso
parent: epic-magnetic-card-access
requirements: [RF-03]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Título: Remover uma zona de um piso

Como gestor,
Eu quero remover uma zona de um piso,
Para que zonas que já não existem deixem de ser controladas pelo cartão.

## Critérios de aceite

### Cenário AC-1: Zona removida
  Dado que existe a zona "spa" no piso 0
  Quando o gestor remove a zona "spa"
  Então a zona deixa de aparecer nos pisos
  E nenhum cartão abre essa zona

### Cenário AC-2: Zona com associações não pode ser removida
  Dado que a zona "restaurante" tem reservas, quartos, passagens de cartão ou outras associações
  Quando o gestor tenta remover a zona "restaurante"
  Então o sistema recusa a remoção
  E mostra uma mensagem a indicar que a zona tem associações e não pode ser removida
  E a zona e as passagens registadas mantêm-se

### Cenário AC-3: Zona inexistente
  Dado que não existe a zona "heliporto"
  Quando o gestor tenta remover a zona "heliporto"
  Então o sistema informa que a zona não existe

### Cenário AC-4: Perfil sem permissão
  Dado que o utilizador é rececionista
  Quando tenta remover uma zona
  Então o sistema recusa a operação
  E a zona mantém-se

## Critérios de aceite (checklist)

- [ ] AC-1: A zona removida deixa de aparecer nos pisos e nenhum cartão a abre.
- [ ] AC-2: Uma zona com associações não é removida, o sistema explica porquê e a zona e as passagens mantêm-se.
- [ ] AC-3: Remover uma zona inexistente informa que a zona não existe.
- [ ] AC-4: O rececionista não consegue remover zonas e a zona mantém-se.

## Notas
Decisão da equipa (2026-10-16): ver "Criar uma zona num piso". Entrega final (Q-06). Q-36 (decisão da equipa): nada que tenha associações pode ser removido ou apagado; só se remove uma zona sem associações.
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: zonas de teste próprias, uma sem associações e uma com reserva ou passagem associada (foundation T4).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Remoção com verificação de associações (reservas, quartos, passagens) em várias tabelas. Um pouco mais de SQL, mas sem fluxo complexo.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
