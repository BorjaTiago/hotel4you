---
type: story
summary: Editar uma zona de um piso
parent: epic-magnetic-card-access
requirements: [RF-03]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Título: Editar uma zona de um piso

Como gestor,
Eu quero editar o nome, o piso e a marca "exige reserva" de uma zona,
Para que as zonas reflitam a organização do hotel.

## Critérios de aceite

### Cenário AC-1: Marca alterada
  Dado que a zona "piscina" do piso 1 não exige reserva
  Quando o gestor marca que a zona exige reserva
  Então a zona fica com a marca "exige reserva"
  E o cartão passa a usar a nova regra nas próximas passagens

### Cenário AC-2: Nome ou piso alterado
  Dado que existe a zona "arrecadação" no piso -1
  Quando o gestor muda o nome para "armazém"
  Então a zona fica guardada com o novo nome no piso -1

### Cenário AC-3: Nome repetido no piso
  Dado que o piso 1 tem as zonas "restaurante" e "piscina"
  Quando o gestor muda o nome de "piscina" para "restaurante"
  Então o sistema recusa a alteração
  E a zona mantém o nome "piscina"

### Cenário AC-4: Zona inexistente
  Dado que não existe a zona "heliporto"
  Quando o gestor tenta editar a zona "heliporto"
  Então o sistema informa que a zona não existe

### Cenário AC-5: Perfil sem permissão
  Dado que o utilizador é rececionista
  Quando tenta editar uma zona
  Então o sistema recusa a operação

## Critérios de aceite (checklist)

- [ ] AC-1: A marca "exige reserva" alterada fica guardada e o cartão usa a nova regra nas próximas passagens.
- [ ] AC-2: O novo nome ou piso da zona fica guardado.
- [ ] AC-3: Mudar para um nome já usado no mesmo piso é recusado e a zona mantém o nome.
- [ ] AC-4: Editar uma zona inexistente informa que a zona não existe.
- [ ] AC-5: O rececionista não consegue editar zonas.

## Notas
Decisão da equipa (2026-10-16): ver "Criar uma zona num piso". Entrega final (Q-06). As passagens já registadas não mudam (RF-19).
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: zonas de teste próprias (foundation T4). O AC-1 ("o cartão usa a nova regra") testa-se com a regra de acesso quando existir (sprint 9); até lá, teste do valor guardado.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Edição com validação (nome único por piso, zona existente, perfil) e gravação. Formulário simples. Mesmo tamanho do registo de cliente.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
