---
type: story
summary: Registar um novo cliente
parent: epic-client-registration
requirements: [RF-10]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [clients, 1st-delivery]
sprint:
---

# Título: Registar um novo cliente

Como rececionista,
Eu quero registar um novo cliente,
Para que a reserva e o check-in tenham os dados do cliente.

## Critérios de aceite

### Cenário AC-1: Registo com dados válidos
  Dado que o rececionista tem sessão iniciada
  E o cliente ainda não está registado
  Quando o rececionista preenche os dados obrigatórios e confirma o registo
  Então o sistema guarda o cliente
  E o cliente fica disponível para uma reserva

### Cenário AC-2: Dados obrigatórios em falta
  Dado que o rececionista tem sessão iniciada
  Quando o rececionista confirma o registo sem um dado obrigatório
  Então o sistema recusa o registo
  E indica qual dado falta

### Cenário AC-3: Cliente já registado
  Dado que o cliente já está registado
  Quando o rececionista tenta registar o mesmo cliente
  Então o sistema recusa o registo duplicado
  E indica que o cliente já existe

## Notas
Fase: 1.ª entrega (Q-27). O enunciado não lista os dados do cliente nem o critério de duplicado. A equipa define-os na implementação (detalhe negociável, não é requisito novo). Apenas o rececionista regista (Q-13: clientes não iniciam sessão). Tempo total: ver "Registar cliente em formulário rápido".

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: Same as the reference. Validation, duplicate check and save. Client data fields still open.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
