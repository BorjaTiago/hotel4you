---
type: story
summary: Emitir um novo cartão mestre com novo número
parent: epic-master-card-audit
requirements: [RF-30, RF-23]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Título: Emitir um novo cartão mestre com novo número

Como gestor,
Eu quero emitir um novo cartão mestre depois de bloquear o perdido,
Para que eu volte a aceder a todos os locais.

## Critérios de aceite

### Cenário AC-1: Novo cartão com novo número
  Dado que o cartão mestre M1 do gestor está bloqueado
  Quando o gestor emite um novo cartão mestre
  Então o sistema cria um cartão mestre com um número diferente de M1
  E o novo cartão abre todos os quartos e zonas

### Cenário AC-2: Número antigo mantém-se no registo
  Dado que o gestor emitiu um novo cartão para substituir M1
  Quando o gestor consulta o registo de passagens do cartão M1
  Então as passagens e o bloqueio de M1 continuam no registo

### Cenário AC-3: Cartão bloqueado continua recusado
  Dado que o gestor emitiu um novo cartão
  Quando alguém passa o cartão antigo M1 numa porta
  Então o sistema recusa a entrada

### Cenário AC-4: Cartão antigo ainda ativo
  Dado que o cartão mestre M1 do gestor ainda está ativo
  Quando o gestor tenta emitir um novo cartão mestre
  Então o sistema recusa a emissão
  E mostra uma mensagem a indicar que o cartão atual tem de ser bloqueado primeiro
  E não cria nenhum cartão

### Cenário AC-5: Perfil sem permissão
  Dado que o utilizador é rececionista
  Quando tenta emitir um cartão mestre
  Então o sistema recusa a operação

## Notas
Q-26 (decisão da equipa): novo número e número antigo no registo. Decisão da equipa (2026-10-16): um novo cartão mestre só se emite depois de o anterior estar bloqueado (ver "Bloquear um cartão mestre perdido"). Entrega final (Q-06).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Gera novo número, exige o cartão antigo bloqueado e mantém o histórico. Poucas regras, um formulário.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
