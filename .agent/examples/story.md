> EXAMPLE only. Not real backlog. Do not publish.

---
type: story
summary: Realizar check-in com cartão magnético
parent: epic-card-access
requirements: [RF-12, RF-13]
points:
points_suggested: 5
status: draft
jira_key:
labels: [reservation, access-control]
sprint:
---

# Título: Realizar check-in com cartão magnético

Como rececionista,
Eu quero efetuar o check-in de um cliente com reserva e atribuir-lhe um cartão magnético,
Para que o cliente aceda ao seu quarto e aos locais incluídos na reserva.

## Critérios de aceite

### Cenário AC-1: Check-in com reserva válida
  Dado que existe uma reserva ativa para o quarto 202 sem check-in efetuado
  E existe um cartão magnético livre com o número 12345
  Quando o rececionista efetua o check-in e associa o cartão 12345 à reserva
  Então o estado da reserva passa a "check-in efetuado"
  E o cartão 12345 fica associado ao quarto 202

### Cenário AC-2: Check-in sem reserva
  Dado que não existe reserva ativa para o cliente
  Quando o rececionista tenta efetuar o check-in
  Então o sistema recusa o check-in
  E mostra uma mensagem a indicar que não existe reserva

## Notas
Depende de "Registar cliente" e "Criar reserva". Enunciado, secção 2 (cartão magnético).
Test names: "H4Y-NN AC-1 ...", "H4Y-NN AC-2 ...".
