---
type: story
summary: Criar uma zona num piso
parent: epic-magnetic-card-access
requirements: [RF-03]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Título: Criar uma zona num piso

Como gestor,
Eu quero criar uma zona num piso e marcar se a zona exige reserva,
Para que o cartão saiba que locais controla e quais só abrem com reserva.

## Critérios de aceite

### Cenário AC-1: Zona criada
  Dado que o gestor está autenticado
  E o piso 1 existe
  Quando o gestor cria a zona "restaurante" no piso 1 e marca que exige reserva
  Então a zona fica guardada no piso 1 com a marca "exige reserva"

### Cenário AC-2: Zona sem reserva obrigatória
  Dado que o gestor está autenticado
  Quando o gestor cria a zona "receção" no piso 0 sem marcar que exige reserva
  Então a zona fica guardada no piso 0 com a marca "não exige reserva"

### Cenário AC-3: Nome repetido no mesmo piso
  Dado que o piso 1 já tem a zona "restaurante"
  Quando o gestor cria outra zona "restaurante" no piso 1
  Então o sistema recusa a criação
  E não cria nenhuma zona

### Cenário AC-4: Piso inexistente
  Dado que o piso 4 não existe
  Quando o gestor tenta criar uma zona no piso 4
  Então o sistema recusa a criação
  E informa que o piso não existe

### Cenário AC-5: Perfil sem permissão
  Dado que o utilizador é rececionista
  Quando tenta criar uma zona
  Então o sistema recusa a operação

## Critérios de aceite (checklist)

- [ ] AC-1: A zona criada com a marca "exige reserva" fica guardada no piso escolhido com essa marca.
- [ ] AC-2: A zona criada sem a marca fica guardada com "não exige reserva".
- [ ] AC-3: Criar uma zona com nome já usado no mesmo piso é recusado e nenhuma zona é criada.
- [ ] AC-4: Criar uma zona num piso inexistente é recusado com a indicação de que o piso não existe.
- [ ] AC-5: O rececionista não consegue criar zonas.

## Notas
Decisão da equipa (2026-10-16): o gestor cria, edita e remove zonas por piso e marca se a zona exige reserva. Pisos fixos do enunciado (RF-03): -1, 0, 1, 2 e 3. Substitui a história "Associar cada local a um piso e zona". Entrega final (Q-06). Ver também "Editar uma zona" e "Remover uma zona".
Dependências (auditoria 2026-10-16): nenhuma forte, e já não bloqueia outras histórias: os locais do enunciado (restaurante e piscina incluídos) vêm dos dados de referência (foundation T5). Construir e testar sozinha: pisos de T5 e base de teste sem zonas para o AC-1 (T4), sessão simulada (T0). Na base de produção o restaurante já existe; a demo do AC-1 usa outro nome ou a base de teste.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Nova tabela de zonas, validação (piso existente, nome único) e formulário. Cria a base das outras histórias de acesso, mas a lógica é curta.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
