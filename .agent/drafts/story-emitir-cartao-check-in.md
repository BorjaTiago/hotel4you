---
type: story
summary: Emitir o cartão magnético no check-in
parent: epic-magnetic-card-access
requirements: [RF-13, RF-16]
points: 3
points_suggested: 3
status: approved
jira_key: H4Y-134
labels: [access-control, final-delivery]
sprint:
---

# Título: Emitir o cartão magnético no check-in

Como rececionista,
Eu quero emitir um cartão magnético com número ao efetuar o check-in de uma reserva,
Para que o cliente receba o cartão que lhe dá acesso aos locais da reserva.

## Critérios de aceite

### Cenário AC-1: Cartão emitido no check-in
  Dado que existe uma reserva ativa sem check-in efetuado e sem cartão
  Quando o rececionista efetua o check-in
  Então o sistema emite um cartão com um número
  E o cartão fica associado a essa reserva

### Cenário AC-2: Números de cartão distintos
  Dado que já existe um cartão emitido com o número N
  Quando o rececionista efetua o check-in de outra reserva
  Então o novo cartão tem um número diferente de N

### Cenário AC-3: Reserva já tem cartão
  Dado que a reserva já tem um cartão associado
  Quando o rececionista tenta emitir outro cartão para a mesma reserva
  Então o sistema recusa a emissão
  E mostra uma mensagem a indicar que a reserva já tem cartão

### Cenário AC-4: Check-in sem reserva ativa
  Dado que a reserva está cancelada ou não existe
  Quando o rececionista tenta efetuar o check-in
  Então o sistema recusa o check-in
  E não emite nenhum cartão

### Cenário AC-5: Fluxo completo
  Dado que existe uma reserva ativa sem cartão
  Quando o rececionista abre o ecrã de check-in, escolhe a reserva e confirma o check-in
  Então o serviço emite o cartão com um número único, guarda-o na base de dados associado à reserva e o ecrã mostra o número do cartão e o código de 6 caracteres uma única vez (o cliente usa-os para entrar na aplicação)
  E, se a reserva já tem cartão ou não está ativa, o ecrã mostra a mensagem de erro e nada é guardado

## Critérios de aceite (checklist)

- [ ] AC-1: O check-in de uma reserva ativa emite um cartão com número, associado a essa reserva.
- [ ] AC-2: Cada novo cartão emitido tem um número diferente dos já emitidos.
- [ ] AC-3: Emitir um segundo cartão para a mesma reserva é recusado com mensagem a indicar que já tem cartão.
- [ ] AC-4: O check-in de uma reserva cancelada ou inexistente é recusado e nenhum cartão é emitido.
- [ ] AC-5: O check-in no ecrã emite o cartão, guarda-o associado à reserva e mostra o número do cartão e o código de 6 caracteres uma única vez; em erro mostra a mensagem e nada é guardado.

## Notas
Um cartão por reserva. O cartão emitido só vale durante a estadia. Usa o CardAccessService e o gerador de números da H4Y-109 (tabelas, serviço e gerador já existem) e junta só a emissão dentro do fluxo de check-in e o ecrã. Não cria tabelas. Depende do check-in da Sprint 5.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Gera número único e liga o cartão à reserva dentro do check-in já existente. Regras: um cartão por reserva, reserva ativa. Menor que o check-in completo (5).
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; backlog-prioritizer, Claude Code (claude-sonnet-5-5), 2026-11-06; Jira sync (login and "O meu cartão" flow, H4Y-122) by story-writer, GitHub Copilot (claude-opus-5.5), 2026-11-24. Reviewed by: pending.
