---
type: story
summary: Recusar o cartão em locais não permitidos
parent: epic-magnetic-card-access
requirements: [RF-18, RF-03]
points: 3
points_suggested: 3
status: approved
jira_key: H4Y-137
labels: [access-control, final-delivery]
sprint:
---

# Título: Recusar o cartão em locais não permitidos

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

### Cenário AC-4: Fluxo completo
  Dado que existe um cartão de cliente com check-in
  Quando o cliente entra com o cartão, abre o ecrã "O meu cartão" e clica num local restrito (com cadeado)
  Então o serviço aplica a regra dos locais restritos e recusa
  E o ecrã mostra "entrada recusada" com o motivo
  E nenhuma entrada fica registada nesse local

## Critérios de aceite (checklist)

- [ ] AC-1: O cartão de cliente com check-in é recusado na porta do bar.
- [ ] AC-2: O cartão de cliente é recusado nas salas de conferências, garagem, armazém, piscinas privadas e spa.
- [ ] AC-3: O cartão do cliente continua a abrir a porta do seu quarto.
- [ ] AC-4: Em "O meu cartão", um local restrito (com cadeado) mostra "entrada recusada" com o motivo e nenhuma entrada fica registada.

## Notas
Piscinas privadas, spa e salas de conferências são serviços extra pagos, sem acesso por cartão. Locais por piso: RF-03. Usa o CardAccessService e o login com o cartão e o ecrã "O meu cartão" da H4Y-109 e junta só a regra dos locais restritos e o ecrã desta história. Não cria tabelas. Locais dos dados de referência e cartões de teste.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Regra simples de acesso: locais fora das zonas são recusados. Sem dados novos, só consulta às zonas e ao cartão. A GUI de entrada simulada é partilhada com as outras histórias de acesso.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Jira sync (login and "O meu cartão" flow, H4Y-122) by story-writer, GitHub Copilot (claude-opus-5.5), 2026-11-24. Reviewed by: pending.
