---
type: story
summary: Registar as passagens do cartão
parent: epic-magnetic-card-access
requirements: [RF-19]
points: 3
points_suggested: 3
status: approved
jira_key: H4Y-150
labels: [access-control, final-delivery]
sprint:
---

# Título: Registar as passagens do cartão

Como cliente,
Eu quero que cada passagem do meu cartão fique registada com o local e a hora,
Para que o hotel tenha o registo das minhas entradas.

## Critérios de aceite

### Cenário AC-1: Entrada permitida registada
  Dado que o cliente tem um cartão válido
  Quando o cliente entra no restaurante com o cartão
  Então o sistema guarda um registo com o local "restaurante" e a hora da entrada

### Cenário AC-2: Entrada no quarto registada
  Dado que o cliente tem um cartão associado ao quarto 202
  Quando o cliente entra no quarto 202
  Então o sistema guarda um registo com o local "quarto 202" e a hora da entrada

### Cenário AC-3: Entrada recusada não cria registo de entrada
  Dado que o cartão não dá acesso ao bar
  Quando o cliente passa o cartão na porta do bar
  Então o sistema recusa a entrada
  E não guarda um registo de entrada nesse local

### Cenário AC-4: Fluxo completo
  Dado que existe um cartão válido com acesso ao quarto 202 e sem acesso ao bar
  Quando o cliente entra com o cartão, abre o ecrã "O meu cartão" e clica no quarto 202 e depois no bar
  Então o serviço guarda na base de dados uma passagem do quarto 202 com a hora, e nenhuma do bar
  E o ecrã mostra "entrada permitida" no quarto e "entrada recusada" no bar

## Critérios de aceite (checklist)

- [ ] AC-1: Uma entrada permitida no restaurante guarda um registo com o local e a hora da entrada.
- [ ] AC-2: Uma entrada no quarto do cliente guarda um registo com o quarto e a hora da entrada.
- [ ] AC-3: Uma entrada recusada no bar é recusada e não guarda nenhum registo de entrada.
- [ ] AC-4: Em "O meu cartão", clicar no quarto 202 e depois no bar guarda só a passagem do quarto 202 com a hora; o ecrã mostra "entrada permitida" no quarto e "entrada recusada" no bar.

## Notas
Registo de cada passagem, incluindo quartos. O RF-19 fala só de entradas; registar tentativas recusadas não está no enunciado e não foi assumido. Usa o CardAccessService (registar passagem) e a tabela de passagens da H4Y-109 e junta só a chamada do registo nas entradas permitidas e o ecrã. Não cria tabelas.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Nova tabela de passagens e gravação em cada entrada permitida. Sem GUI própria. Regra: recusa não grava.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Jira sync (login and "O meu cartão" flow, H4Y-122) by story-writer, GitHub Copilot (claude-opus-5.5), 2026-11-24. Reviewed by: pending.
