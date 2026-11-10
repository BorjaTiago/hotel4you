---
type: story
summary: Criar uma reserva em três passos: cliente, quarto e confirmação
parent: epic-foundation-architecture
requirements: [RNF-04, RNF-06]
points: 2
points_suggested: 2
status: published
jira_key: H4Y-80
labels: [ui, usability, javafx]
sprint: 05
assignee: BorjaTiago
---

# Título: Criar uma reserva em três passos: cliente, quarto e confirmação

Como rececionista,
Eu quero atender o cliente num só ecrã "Nova reserva",
Para que o atendimento ao balcão seja simples, sem mudar de ecrã.

## Critérios de aceite

### Cenário AC-1: Ecrã único com três separadores
  Dado que o rececionista tem sessão iniciada
  Quando o rececionista abre o ecrã "Nova reserva"
  Então o ecrã mostra os separadores Cliente, Quarto e Confirmar
  E o menu não tem os botões separados "Clientes" e "Quartos livres"

### Cenário AC-2: Separador Cliente
  Dado que o rececionista está no separador Cliente
  Quando o rececionista pesquisa por documento, nome ou telefone
  Então o ecrã lista os clientes que correspondem à pesquisa
  E o rececionista pode registar, editar e remover um cliente neste separador

### Cenário AC-3: Separador Quarto só depois de escolher o cliente
  Dado que nenhum cliente está escolhido
  Quando o rececionista tenta abrir o separador Quarto
  Então o separador não abre
  E depois de escolher um cliente, o separador Quarto abre

### Cenário AC-4: Escolha do quarto livre
  Dado que um cliente está escolhido
  Quando o rececionista indica o tipo de quarto e as datas no separador Quarto
  Então o ecrã lista os quartos livres para esse tipo e essas datas

### Cenário AC-5: Separador Confirmar só depois de escolher o quarto
  Dado que um cliente está escolhido e nenhum quarto está escolhido
  Quando o rececionista tenta abrir o separador Confirmar
  Então o separador não abre
  E depois de escolher um quarto, o separador Confirmar abre

### Cenário AC-6: Confirmação da reserva
  Dado que o cliente e o quarto estão escolhidos
  Quando o rececionista abre o separador Confirmar
  Então o ecrã mostra o cliente, o quarto, as datas, o número de noites, o preço por noite e o total
  E ao confirmar, a reserva fica criada

## Critérios de aceite (checklist)

- [ ] AC-1: O ecrã "Nova reserva" tem os separadores Cliente, Quarto e Confirmar, sem botões separados no menu.
- [ ] AC-2: O separador Cliente pesquisa por documento, nome ou telefone e permite registar, editar e remover.
- [ ] AC-3: O separador Quarto só abre depois de escolher um cliente.
- [ ] AC-4: O separador Quarto lista os quartos livres por tipo e datas.
- [ ] AC-5: O separador Confirmar só abre depois de escolher um quarto.
- [ ] AC-6: O separador Confirmar mostra cliente, quarto, datas, noites, preço por noite e total, e confirma a reserva.

## Notas
- Este fluxo abre-se pelo botão "+ Nova reserva" do ecrã principal "Reservas" (story-ver-reservas-ativas.md). Não mostra as reservas do hotel.
- O ecrã já está construído no PR #53 (ramo feat/reservation-single-screen). Não li o PR nem o diff nesta sessão (sem shell); confirmar os AC contra o PR #53 antes de aprovar.
- O utilizador remove as alterações de CSS do PR #53. Esta história não tem trabalho de estilo (ver task-estilo-comum-ui.md).
- Publicada em Jira como H4Y-80 (2026-11-06); o código está no PR #53.
- O PR #53 faz merge no dia 1, porque outras histórias acrescentam botões a este ecrã.
- O enunciado não define o número de separadores nem o fluxo: decisão da equipa (BorjaTiago, 2026-11-06), Q-60.
- O RNF-04 e o RNF-06 são "should": o teste de usabilidade é tarefa técnica, não história (Q-41).

points_suggested: 2
reference: Listar os quartos por tipo (2)
compared: equal
reasoning: O código e os testes já existem no PR #53 (24 ficheiros, cerca de 980 linhas). O trabalho que resta é rever e fazer merge, sem CSS. Se a equipa contar o tamanho total do ecrã (lógica, dados e GUI), seria 5 (Realizar check-in com cartão magnético).
split_needed: no

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, Claude Code (claude-sonnet-5-5), 2026-11-06; estimator, Claude Code (claude-sonnet-5-5), 2026-11-06; story-writer, Claude Code (claude-sonnet-5-5), 2026-11-06; screen rename to "Nova reserva" by Claude Code (claude-opus-5-5), 2026-11-09. Reviewed by: pending.
