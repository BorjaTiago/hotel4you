---
type: story
summary: Ver e gerir as reservas ativas no ecrã principal da receção
parent: epic-reservations
requirements: [RF-13, RF-14, RNF-04]
points: 8
points_suggested: 8
status: published
jira_key: H4Y-81
labels: [reservations, reception]
sprint: 05
assignee: BorjaTiago
---

# Título: Ver e gerir as reservas ativas no ecrã principal da receção

Como rececionista,
Eu quero um ecrã principal "Reservas" com todas as reservas ativas do hotel, pesquisa, filtros rápidos e a criação de reservas,
Para que veja, por exemplo, quem chega hoje e atenda o cliente sem mudar de ecrã.

## Critérios de aceite

### Cenário AC-1: Lista das reservas ativas
  Dado que o rececionista tem sessão iniciada
  E existem reservas nos estados RESERVED e CHECKED_IN
  Quando o rececionista abre o ecrã "Reservas"
  Então a tabela mostra cliente, documento, quarto, entrada, saída e estado
  E mostra todas as reservas RESERVED e CHECKED_IN

### Cenário AC-2: Reservas canceladas e com check-out não aparecem
  Dado que existem reservas canceladas e reservas com check-out
  Quando o rececionista abre o ecrã "Reservas"
  Então essas reservas não aparecem na tabela

### Cenário AC-3: Pesquisa por cliente
  Dado que a tabela mostra reservas de vários clientes
  Quando o rececionista escreve no campo de pesquisa um nome ou um documento
  Então a tabela mostra só as reservas dos clientes que correspondem

### Cenário AC-4: Filtro rápido "Chegam hoje"
  Dado que existem reservas com datas de entrada diferentes
  Quando o rececionista escolhe o filtro "Chegam hoje"
  Então a tabela mostra só as reservas RESERVED com entrada hoje

### Cenário AC-5: Filtro rápido "Hospedados"
  Dado que a tabela mostra reservas RESERVED e CHECKED_IN
  Quando o rececionista escolhe o filtro "Hospedados"
  Então a tabela mostra só as reservas CHECKED_IN

### Cenário AC-6: Filtro rápido "Todas ativas"
  Dado que um filtro rápido ou a pesquisa estão aplicados
  Quando o rececionista escolhe o filtro "Todas ativas"
  Então a tabela mostra todas as reservas RESERVED e CHECKED_IN

### Cenário AC-7: Lista vazia
  Dado que não existe nenhuma reserva ativa, ou a pesquisa ou o filtro não encontram nenhuma
  Quando o rececionista olha para o ecrã
  Então o ecrã mostra uma mensagem a dizer que não há reservas ativas
  E não mostra erro

### Cenário AC-8: Nova reserva
  Dado que o rececionista está no ecrã "Reservas"
  Quando o rececionista carrega em "+ Nova reserva"
  Então abre o fluxo de reserva: cliente, quarto e confirmação (story-ecra-unico-clientes-reservas)

### Cenário AC-9: Regresso à lista depois de confirmar
  Dado que o rececionista confirmou uma nova reserva
  Quando a reserva fica criada
  Então o ecrã volta à lista
  E a nova reserva aparece na tabela

### Cenário AC-10: Gestor também vê o ecrã
  Dado que o gestor tem sessão iniciada
  Quando o gestor abre o ecrã "Reservas"
  Então vê as mesmas reservas que o rececionista

### Cenário AC-11: Papel sem permissão
  Dado que um funcionário de restaurante tem sessão iniciada
  Quando tenta abrir o ecrã "Reservas"
  Então o sistema recusa o acesso
  E não mostra nenhuma reserva

## Critérios de aceite (checklist)

- [ ] AC-1: A tabela mostra cliente, documento, quarto, entrada, saída e estado das reservas RESERVED e CHECKED_IN.
- [ ] AC-2: Reservas canceladas e com check-out não aparecem.
- [ ] AC-3: A pesquisa por nome ou documento do cliente filtra a tabela.
- [ ] AC-4: O filtro "Chegam hoje" funciona.
- [ ] AC-5: O filtro "Hospedados" mostra só CHECKED_IN.
- [ ] AC-6: O filtro "Todas ativas" mostra RESERVED e CHECKED_IN.
- [ ] AC-7: A lista vazia mostra uma mensagem.
- [ ] AC-8: "+ Nova reserva" abre o fluxo cliente, quarto e confirmação.
- [ ] AC-9: Depois de confirmar, o ecrã volta à lista e mostra a nova reserva.
- [ ] AC-10: O gestor vê as mesmas reservas.
- [ ] AC-11: O funcionário de restaurante não abre o ecrã.

## Notas
- Este é o ecrã principal da receção. Barra de ações sob a tabela: Check-in, Check-out, Cancelar, Apagar (gestor). Um botão só está ativo quando serve ao estado da linha escolhida.
- O dono desta história faz merge no dia 1 de um esqueleto do ecrã (tabela e barra de ações vazia) junto com o PR #53. As outras histórias acrescentam o seu botão e esperam no máximo um dia.
- As regras dos botões ficam nas suas histórias: story-check-in-check-out.md (Check-in, Check-out), story-cancel-reservation.md (Cancelar, H4Y-55) e story-delete-reservation.md (Apagar, H4Y-57).
- O fluxo "+ Nova reserva" é story-ecra-unico-clientes-reservas.md (PR #53).
- O RF-13 e o RF-14 definem os estados RESERVED e CHECKED_IN mostrados aqui.
- Usa reservas de teste próprias (foundation T4).
- O âmbito cresceu: o estimador volta a estimar.

points_suggested: 8
reference: Importar ficheiro XML de stock de um fornecedor (8)
compared: equal
reasoning: Muitas partes mas regras simples: consulta com 3 filtros e pesquisa, tabela, estado vazio, controlo de papel, esqueleto da barra de ações e integração com o fluxo do PR #53 e regresso à lista. Maior que Realizar check-in (5) por ter 11 AC e várias peças de GUI. Os botões ficam noutras histórias. No limite de 8: se crescer, divide.
split_needed: no (se o limite apertar: 1) lista + papel + estado vazio, AC-1, 2, 7, 10, 11; 2) pesquisa e filtros, AC-3 a 6; 3) Nova reserva e regresso, AC-8, 9)
- O enunciado não pede este ecrã, a pesquisa nem os filtros: decisão da equipa (BorjaTiago, 2026-11-06), Q-60.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, Claude Code (claude-sonnet-5-5), 2026-11-06; correction by story-writer, Claude Code (claude-sonnet-5-5), 2026-11-06; story-writer, Claude Code (claude-sonnet-5-5), 2026-11-06; story-writer, Claude Code (claude-sonnet-5-5), 2026-11-06; estimator, Claude Code (claude-sonnet-5-5), 2026-11-06. Reviewed by: pending.
