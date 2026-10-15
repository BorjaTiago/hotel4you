---
type: story
summary: Consultar as passagens de cartões num quarto
parent: epic-master-card-audit
requirements: [RF-21, RF-19]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Título: Consultar as passagens de cartões num quarto

Como gestor,
Eu quero ver a lista de passagens de cartões num quarto,
Para que eu saiba quem entrou nesse quarto e quando.

## Critérios de aceite

### Cenário AC-1: Lista de passagens do quarto
  Dado que o cartão 12345 do cliente entrou no quarto 301
  Quando o gestor consulta as passagens do quarto 301
  Então o sistema mostra o cartão 12345 com a hora da entrada

### Cenário AC-2: Cartão mestre incluído
  Dado que um cartão mestre entrou no quarto 301
  Quando o gestor consulta as passagens do quarto 301
  Então a lista inclui a passagem do cartão mestre

### Cenário AC-3: Quarto sem passagens
  Dado que ninguém entrou no quarto 302
  Quando o gestor consulta as passagens do quarto 302
  Então o sistema mostra uma lista vazia

### Cenário AC-4: Quarto inexistente
  Dado que não existe o quarto 999
  Quando o gestor consulta as passagens do quarto 999
  Então o sistema indica que o quarto não existe

### Cenário AC-5: Perfil sem permissão
  Dado que o utilizador é rececionista
  Quando tenta consultar as passagens de um quarto
  Então o sistema recusa a consulta

## Notas
Exemplo do enunciado: quarto 301. Entrega final (Q-06). Depende de registo de passagens, incluindo cartão mestre (Q-19).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Consulta por quarto com lista e ecrã. Junta passagens de clientes e do cartão mestre. Maior que "Listar os quartos por tipo" (2) por causa do perfil e do cartão mestre.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
