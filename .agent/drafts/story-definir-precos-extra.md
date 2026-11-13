---
type: story
summary: Definir os preços extra por noite
parent: H4Y-86
requirements: [RF-07, RF-09]
points: 3
points_suggested: 3
status: published
jira_key: H4Y-93
labels: [pricing, services]
sprint: 06
---

# Título: Definir os preços extra por noite

Como gestor,
Eu quero definir o preço extra por noite de cada extra e serviço pago,
Para que o preço da reserva use valores corretos.

## Critérios de aceite

### Cenário AC-1: Preço de um extra de quarto
  Dado que o gestor está autenticado
  Quando o gestor define o preço extra da vista mar em 15,00 por noite
  Então o sistema guarda o preço 15,00 para a vista mar

### Cenário AC-2: Preço de um serviço pago
  Dado que o gestor está autenticado
  Quando o gestor define o preço extra do spa em 40,00 por noite
  Então o sistema guarda o preço 40,00 para o spa

### Cenário AC-3: Todos os extras previstos
  Dado que o gestor está autenticado
  Quando o gestor consulta os preços extra
  Então pode definir preço para vista (mar ou montanha), bebidas, lavandaria, comida, eventos culturais, piscinas privadas, spa e salas de conferências

### Cenário AC-4: Preço inválido
  Dado que o gestor está autenticado
  Quando o gestor define um preço negativo ou não numérico
  Então o sistema recusa o valor
  E o preço anterior mantém-se

### Cenário AC-5: Perfil sem permissão
  Dado que o utilizador é rececionista
  Quando tenta alterar um preço extra
  Então o sistema recusa a operação

## Critérios de aceite (checklist)

- [ ] AC-1: O gestor consegue definir e guardar o preço extra por noite de um extra de quarto (vista mar a 15,00).
- [ ] AC-2: O gestor consegue definir e guardar o preço extra por noite de um serviço pago (spa a 40,00).
- [ ] AC-3: A lista de preços extra permite definir vista (mar ou montanha), bebidas, lavandaria, comida, eventos culturais, piscinas privadas, spa e salas de conferências.
- [ ] AC-4: Um preço negativo ou não numérico é recusado e o preço anterior mantém-se.
- [ ] AC-5: O rececionista não consegue alterar preços extra.

## Notas
Preços fixos por noite definidos pelo gestor (Q-11, decisão da equipa). Piscinas privadas, spa e salas são extras pagos sem acesso por cartão (Q-20). Fase pelo priorizador (Q-25).
Dependências (auditoria 2026-10-16): nenhuma forte (antes ligada a story-set-base-price sem razão de código). A lista de extras e serviços pagos, sem preço, vem dos dados de referência (foundation T5). Construir e testar sozinha: essa lista e sessões simuladas (T0, T4).

Decisão do utilizador (2026-11-13): puxada do sprint 12 para o sprint 6, 3 pontos. Faltam: tabela `extra_prices` com a lista dos 9 itens (script SQL v14), a classe de domínio, o repositório (JDBC e em memória), o serviço, a permissão nova `Permission.MANAGE_EXTRA_PRICES` (só gestor), o controlador com FXML e os testes. As histórias que usam estes preços (adicionar-extras-quarto-reserva, adicionar-servicos-extra-reserva, calcular-preco-total-reserva) ficam no sprint 7 ou depois.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Tabela de preços por extra (cerca de 9 itens), validação do valor e perfil gestor. Formulário simples, sem cálculo.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; jira-publisher, Claude Code (claude-sonnet-5-5), 2026-11-13. Created by: BorjaTiago. Updated by: BorjaTiago. Reviewed by: pending.
