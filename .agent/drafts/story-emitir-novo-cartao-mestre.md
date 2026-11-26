---
type: story
summary: Emitir um novo cartão mestre
parent: epic-master-card-audit
requirements: [RF-30]
points: 3
points_suggested: 3
status: approved
jira_key: H4Y-154
labels: [access-control, final-delivery]
sprint:
---

# Título: Emitir um novo cartão mestre

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

### Cenário AC-6: Fluxo completo
  Dado que o gestor tem sessão iniciada e o cartão mestre M1 está bloqueado
  Quando o gestor abre o ecrã de cartões mestre no menu e escolhe emitir um novo cartão
  Então o serviço cria na base de dados um cartão mestre com um número novo e o ecrã mostra esse número e o código de 6 caracteres uma única vez
  E o gestor entra com o novo cartão e vê todos os locais a verde, e o login com o cartão M1 é recusado

## Critérios de aceite (checklist)

- [ ] AC-1: Com o cartão mestre anterior bloqueado, o gestor emite um cartão com número diferente que abre todos os quartos e zonas.
- [ ] AC-2: As passagens e o bloqueio do cartão antigo continuam no registo.
- [ ] AC-3: O cartão antigo bloqueado continua a ser recusado nas portas.
- [ ] AC-4: Com o cartão mestre ainda ativo, a emissão é recusada com mensagem a pedir o bloqueio primeiro e nenhum cartão é criado.
- [ ] AC-5: O rececionista não consegue emitir cartões mestre.
- [ ] AC-6: No ecrã "Cartões mestre" o gestor emite um novo cartão; o cartão fica na base de dados, o ecrã mostra o número e o código de 6 caracteres uma única vez, o novo cartão vê todos os locais a verde em "O meu cartão" e o login com M1 é recusado.

## Notas
Jira (H4Y-154): o novo cartão tem um novo número e o número antigo fica no registo. Só se emite depois de o anterior estar bloqueado. É assim que o gestor recebe o código do cartão mestre (o cartão mestre inicial da H4Y-109 não tem código). Usa o CardAccessService (substituir cartão mestre) e o gerador de números da H4Y-109 e junta só a regra de permissão do gestor e o ecrã. Não cria tabelas.
Q-26 (decisão da equipa): novo número e número antigo no registo. Decisão da equipa (2026-10-16): um novo cartão mestre só se emite depois de o anterior estar bloqueado (ver "Bloquear um cartão mestre", H4Y-153). Entrega final (Q-06).
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: cartão mestre bloqueado e cartão mestre ativo de teste (foundation T4), sem depender de story-bloquear-cartao-mestre; número novo de `CardNumberGenerator` (T6).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Gera novo número, exige o cartão antigo bloqueado e mantém o histórico. Poucas regras, um formulário.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; sync with Jira H4Y-154 by doc-updater, GitHub Copilot (claude-opus-5.5), 2026-11-24. Reviewed by: pending.
