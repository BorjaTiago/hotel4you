---
type: story
summary: Consultar os locais de um cartão
parent: epic-master-card-audit
requirements: [RF-20]
points: 3
points_suggested: 3
status: approved
jira_key: H4Y-155
labels: [access-control, final-delivery]
sprint:
---

# Título: Consultar os locais de um cartão

Como gestor,
Eu quero ver a lista de locais por onde um cartão passou,
Para que eu audite o uso desse cartão.

## Critérios de aceite

### Cenário AC-1: Lista de locais do cartão
  Dado que o cartão 12345 entrou no quarto 202 e no restaurante
  Quando o gestor consulta os locais do cartão 12345
  Então o sistema mostra o quarto 202 e o restaurante
  E cada local aparece com a hora da entrada

### Cenário AC-2: Cartão sem passagens
  Dado que o cartão 12346 existe e nunca foi usado
  Quando o gestor consulta os locais do cartão 12346
  Então o sistema mostra uma lista vazia

### Cenário AC-3: Cartão inexistente
  Dado que não existe o cartão 99999
  Quando o gestor consulta os locais do cartão 99999
  Então o sistema indica que o cartão não existe

### Cenário AC-4: Perfil sem permissão
  Dado que o utilizador é rececionista
  Quando tenta consultar os locais de um cartão
  Então o sistema recusa a consulta

### Cenário AC-5: Fluxo completo
  Dado que o gestor tem sessão iniciada e o cartão 12345 tem passagens guardadas
  Quando o gestor abre o ecrã de consulta de cartões no menu, escreve o número 12345 e consulta
  Então o serviço lê as passagens da base de dados e o ecrã mostra a lista de locais com a hora de cada entrada
  E, se o cartão não existe ou a lista é vazia, o ecrã mostra a mensagem correspondente

## Critérios de aceite (checklist)

- [ ] AC-1: A consulta de um cartão mostra cada local por onde passou, com a hora da entrada.
- [ ] AC-2: Um cartão sem passagens mostra uma lista vazia.
- [ ] AC-3: Consultar um cartão inexistente mostra que o cartão não existe.
- [ ] AC-4: O rececionista não consegue consultar os locais de um cartão.
- [ ] AC-5: O gestor abre o ecrã no menu, consulta o cartão e vê os locais com a hora; um cartão inexistente ou sem passagens mostra a mensagem correspondente.

## Notas
Exemplo do enunciado: cartão 12345. Cartões antigos (bloqueados) continuam consultáveis (Q-26). Usa o CardAccessService (consultar por cartão) da H4Y-109 e junta só a regra de permissão do gestor e o ecrã. Não cria tabelas. Entrega final (Q-06).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Consulta por cartão com lista de locais e horas, mais perfil e cartão inexistente. Serve também cartões bloqueados. Mesma ordem de tamanho da consulta por quarto.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Jira sync (AC-5, notes) by story-writer, GitHub Copilot (claude-opus-5.5), 2026-11-24. Reviewed by: pending.
