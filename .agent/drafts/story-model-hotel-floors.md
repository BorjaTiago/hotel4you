---
type: story
summary: Consultar os pisos e gerir as zonas do hotel
parent: epic-rooms-pricing
requirements: [RF-03]
points: 2
points_suggested: 8
status: published
jira_key: H4Y-28
labels: [rooms, 1st-delivery]
sprint: 03
---

# Título: Consultar os pisos e gerir as zonas do hotel

Como gestor,
Eu quero consultar os pisos do hotel e criar, editar e remover as zonas de cada piso,
Para que os quartos, os serviços e o cartão fiquem associados ao piso e à zona corretos.

## Critérios de aceite

### Cenário AC-1: Consultar a estrutura do hotel
  Dado que o gestor está autenticado
  Quando o gestor consulta os pisos do hotel
  Então o sistema mostra os pisos -1, 0, 1, 2 e 3
  E o piso -1 inclui garagem e arrecadação
  E o piso 0 inclui receção, bar e salas de conferência
  E o piso 1 inclui restaurante e piscina
  E os pisos 2 e 3 são pisos de quartos

### Cenário AC-2: Piso inexistente
  Dado que o gestor está autenticado
  Quando o gestor pede o piso 4
  Então o sistema recusa o pedido
  E informa que o piso não existe

### Cenário AC-3: Zona criada com reserva obrigatória
  Dado que o gestor está autenticado
  E o piso 0 existe
  Quando o gestor cria a zona "spa" no piso 0 e marca que exige reserva
  Então a zona fica guardada no piso 0 com a marca "exige reserva"

### Cenário AC-4: Zona criada sem reserva obrigatória
  Dado que o gestor está autenticado
  Quando o gestor cria a zona "loja" no piso 0 sem marcar que exige reserva
  Então a zona fica guardada no piso 0 com a marca "não exige reserva"

### Cenário AC-5: Nome repetido no mesmo piso (criar)
  Dado que o piso 1 já tem a zona "restaurante"
  Quando o gestor cria outra zona "restaurante" no piso 1
  Então o sistema recusa a criação
  E informa "Já existe uma zona com este nome neste piso."
  E não cria nenhuma zona

### Cenário AC-6: Piso inexistente (criar)
  Dado que o piso 4 não existe
  Quando o gestor tenta criar uma zona no piso 4
  Então o sistema recusa a criação
  E informa "O piso não existe."

### Cenário AC-7: Perfil sem permissão (criar)
  Dado que o utilizador é rececionista
  Quando tenta criar uma zona
  Então o sistema recusa a operação
  E informa "Só o gestor pode gerir as zonas."

### Cenário AC-8: Marca alterada
  Dado que a zona "piscina" do piso 1 não exige reserva
  Quando o gestor marca que a zona exige reserva
  Então a zona fica com a marca "exige reserva"

### Cenário AC-9: Nome ou piso alterado
  Dado que existe a zona "arrecadação" no piso -1
  Quando o gestor muda o nome para "armazém"
  Então a zona fica guardada com o novo nome no piso -1

### Cenário AC-10: Nome repetido no piso (editar)
  Dado que o piso 1 tem as zonas "restaurante" e "piscina"
  Quando o gestor muda o nome de "piscina" para "restaurante"
  Então o sistema recusa a alteração
  E informa "Já existe uma zona com este nome neste piso."
  E a zona mantém o nome "piscina"

### Cenário AC-11: Zona inexistente (editar)
  Dado que não existe a zona com o identificador pedido
  Quando o gestor tenta editar essa zona
  Então o sistema informa "A zona não existe."

### Cenário AC-12: Perfil sem permissão (editar)
  Dado que o utilizador é rececionista
  Quando tenta editar uma zona
  Então o sistema recusa a operação
  E informa "Só o gestor pode gerir as zonas."

### Cenário AC-13: Zona removida
  Dado que existe a zona "spa" no piso 0 sem associações
  Quando o gestor remove a zona "spa"
  Então a zona deixa de aparecer na lista do piso 0

### Cenário AC-14: Zona com associações não pode ser removida
  Dado que a zona "restaurante" tem reservas, quartos, passagens de cartão ou outras associações
  Quando o gestor tenta remover a zona "restaurante"
  Então o sistema recusa a remoção
  E informa "A zona tem associações e não pode ser removida."
  E a zona e as passagens registadas mantêm-se

### Cenário AC-15: Zona inexistente (remover)
  Dado que não existe a zona com o identificador pedido
  Quando o gestor tenta remover essa zona
  Então o sistema informa "A zona não existe."

### Cenário AC-16: Perfil sem permissão (remover)
  Dado que o utilizador é rececionista
  Quando tenta remover uma zona
  Então o sistema recusa a operação
  E informa "Só o gestor pode gerir as zonas."
  E a zona mantém-se

## Critérios de aceite (checklist)

- [ ] AC-1: A estrutura do hotel por pisos pode ser consultada.
- [ ] AC-2: Consultar um piso inexistente devolve uma resposta de não encontrado.
- [ ] AC-3: Criar uma zona com a marca "exige reserva" guarda-a no piso escolhido com essa marca.
- [ ] AC-4: Criar uma zona sem a marca guarda-a com "não exige reserva".
- [ ] AC-5: Criar uma zona com nome já usado no mesmo piso é recusado e nenhuma zona é criada.
- [ ] AC-6: Criar uma zona num piso inexistente é recusado com "O piso não existe.".
- [ ] AC-7: O rececionista não consegue criar zonas.
- [ ] AC-8: A marca "exige reserva" alterada fica guardada.
- [ ] AC-9: O novo nome ou piso da zona fica guardado.
- [ ] AC-10: Mudar para um nome já usado no mesmo piso é recusado e a zona mantém o nome.
- [ ] AC-11: Editar uma zona inexistente informa que a zona não existe.
- [ ] AC-12: O rececionista não consegue editar zonas.
- [ ] AC-13: A zona removida deixa de aparecer na lista do piso.
- [ ] AC-14: Uma zona com associações não é removida, o sistema explica porquê e a zona mantém-se.
- [ ] AC-15: Remover uma zona inexistente informa que a zona não existe.
- [ ] AC-16: O rececionista não consegue remover zonas e a zona mantém-se.

## Notas
Enunciado, secção 2. Decisão do utilizador (2026-10-26): as histórias Criar, Editar e Remover uma zona juntam-se a esta história. O uso das zonas pelo cartão fica na épica epic-magnetic-card-access (entrega final, Q-06). Nada com associações pode ser removido (Q-36). Esta história cria as tabelas e os dados de referência. Construir e testar sozinha: sessão de gestor simulada.

points_suggested: 8
reference: Listar os quartos por tipo (2)
compared: larger
reasoning: Consulta (2) mais criar, editar e remover zonas (3+3+3, com validação partilhada). A equipa confirma os pontos.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: lukassperandio. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; sprint field by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-28 zone merge by doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-26. Reviewed by: pending.
