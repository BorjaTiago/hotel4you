---
type: story
summary: Recusar o cartão nos locais não permitidos
parent: epic-magnetic-card-access
requirements: [RF-18, RF-03]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Título: Recusar o cartão nos locais não permitidos

Como cliente,
Eu quero que o meu cartão não abra locais que não são para clientes,
Para que o hotel mantenha os locais restritos fechados.

## Critérios de aceite

### Cenário AC-1: Bar recusado
  Dado que o cliente tem um cartão válido com check-in efetuado
  Quando o cliente passa o cartão na porta do bar (entrada simulada)
  Então o sistema recusa a entrada

### Cenário AC-2: Outros locais recusados
  Dado que o cliente tem um cartão válido com check-in efetuado
  Quando o cliente passa o cartão na porta de uma sala de conferências, da garagem, do armazém, de uma piscina privada ou do spa
  Então o sistema recusa a entrada em cada um desses locais

### Cenário AC-3: Local permitido não é afetado
  Dado que o cliente tem um cartão válido associado ao quarto 202
  Quando o cliente passa o cartão na porta do quarto 202
  Então o sistema permite a entrada

## Notas
Piscinas privadas, spa e salas de conferências são serviços extra pagos, sem acesso por cartão (Q-20, decisão da equipa). Entrega final (Q-06). Locais por piso: RF-03. Os locais vêm das zonas criadas pelo gestor (ver "Criar uma zona num piso"); uma zona que exige reserva só abre se a reserva a incluir.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Regra simples de acesso: locais fora das zonas são recusados. Sem dados novos, só consulta às zonas e ao cartão. A GUI de entrada simulada é partilhada com as outras histórias de acesso.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
