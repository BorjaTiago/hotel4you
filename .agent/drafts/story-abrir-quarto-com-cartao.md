---
type: story
summary: Abrir o quarto com o cartão
parent: epic-magnetic-card-access
requirements: [RF-17]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Título: Abrir o quarto com o cartão

Como cliente,
Eu quero abrir o meu quarto com o meu cartão magnético,
Para que eu entre no quarto da minha reserva.

## Critérios de aceite

### Cenário AC-1: Cartão abre o quarto da reserva
  Dado que o cliente tem um cartão associado a uma reserva com check-in efetuado no quarto 202
  Quando o cliente passa o cartão na porta do quarto 202 (entrada simulada na aplicação)
  Então o sistema permite a entrada

### Cenário AC-2: Cartão não abre outro quarto
  Dado que o cliente tem um cartão associado ao quarto 202
  Quando o cliente passa o cartão na porta do quarto 203
  Então o sistema recusa a entrada

### Cenário AC-3: Cartão desconhecido
  Dado que o número do cartão não existe no sistema
  Quando o cliente passa o cartão na porta de um quarto
  Então o sistema recusa a entrada

## Critérios de aceite (checklist)

- [ ] AC-1: O cartão associado a uma reserva com check-in abre a porta do quarto da reserva.
- [ ] AC-2: O cartão é recusado na porta de um quarto que não é o da sua reserva.
- [ ] AC-3: Um cartão que não existe no sistema é recusado na porta de um quarto.

## Notas
Leitores físicos fora de âmbito: entrada simulada na GUI. Entrega final (Q-06). Q-09 (decisão da equipa). Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: cartões de teste ligados a reservas com check-in (foundation T4), sem depender de story-emitir-cartao-check-in; regra própria (`AccessRule`, T0) e leitor simulado (T6). Estado do cartão após check-out: Q-30 (decisão da equipa), o cenário está em story-check-out (AC-4).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Procura o cartão, compara o quarto da reserva e decide. Inclui o ecrã de passagem simulada. Lógica curta, uma ou duas camadas.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
