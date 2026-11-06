---
type: story
summary: Fazer o check-in e o check-out de uma reserva
parent: epic-check-in-out
requirements: [RF-13, RF-14]
points: 5
points_suggested: 5
status: approved
jira_key:
labels: [check-in, check-out, 1st-delivery]
sprint: 05
assignee: lukassperandio
---

# Título: Fazer o check-in e o check-out de uma reserva

Como rececionista,
Eu quero fazer o check-in e o check-out de uma reserva a partir da lista de reservas ativas,
Para que o cliente fique instalado no quarto e a estadia termine quando sai.

## Critérios de aceite

### Cenário AC-1: Check-in de reserva ativa
  Dado que o rececionista está autenticado
  E existe uma reserva ativa sem check-in
  Quando o rececionista faz o check-in da reserva
  Então o estado da reserva passa a "check-in efetuado"

### Cenário AC-2: Check-in duplicado
  Dado que a reserva já tem check-in efetuado
  Quando o rececionista tenta fazer de novo o check-in
  Então o sistema recusa a ação
  E o estado mantém-se

### Cenário AC-3: Check-in de reserva cancelada
  Dado que a reserva está cancelada
  Quando o rececionista tenta fazer o check-in
  Então o sistema recusa o check-in
  E informa que a reserva está cancelada

### Cenário AC-4: Check-in de reserva inexistente
  Dado que não existe reserva com o identificador indicado
  Quando o rececionista tenta fazer o check-in
  Então o sistema informa que a reserva não existe

### Cenário AC-5: Check-out de estadia ativa
  Dado que o rececionista está autenticado
  E a reserva tem check-in efetuado
  Quando o rececionista faz o check-out
  Então o estado da reserva passa a "check-out efetuado"

### Cenário AC-6: Check-out sem check-in prévio
  Dado que a reserva não tem check-in efetuado
  Quando o rececionista tenta fazer o check-out
  Então o sistema recusa a ação
  E o estado da reserva não muda

### Cenário AC-7: Check-out duplicado
  Dado que a reserva já tem check-out efetuado
  Quando o rececionista tenta fazer de novo o check-out
  Então o sistema recusa a ação

### Cenário AC-8: Quarto livre depois do período
  Dado que o check-out foi efetuado
  Quando o rececionista consulta quartos livres para noites após a data de saída
  Então o quarto é oferecido

### Cenário AC-9: Sem linha escolhida
  Dado que o ecrã principal "Reservas" está aberto e nenhuma linha está escolhida
  Quando o rececionista olha para os botões
  Então "Fazer check-in" e "Fazer check-out" estão desativados

### Cenário AC-10: Check-in só para reserva RESERVED
  Dado que o rececionista escolhe uma linha
  Quando a linha tem o estado RESERVED
  Então "Fazer check-in" está ativo
  E "Fazer check-out" está desativado

### Cenário AC-11: Check-out só para reserva CHECKED_IN
  Dado que o rececionista escolhe uma linha
  Quando a linha tem o estado CHECKED_IN
  Então "Fazer check-out" está ativo
  E "Fazer check-in" está desativado

### Cenário AC-12: Check-in feito na lista
  Dado que o rececionista escolheu uma reserva RESERVED
  Quando o rececionista carrega em "Fazer check-in"
  Então a tabela atualiza
  E a reserva aparece com o estado CHECKED_IN

### Cenário AC-13: Check-out feito na lista
  Dado que o rececionista escolheu uma reserva CHECKED_IN
  Quando o rececionista carrega em "Fazer check-out"
  Então a tabela atualiza
  E a reserva deixa de aparecer na lista

## Critérios de aceite (checklist)

- [ ] AC-1: O check-in de uma reserva ativa é registado.
- [ ] AC-2: Um check-in duplicado é recusado.
- [ ] AC-3: O check-in de uma reserva cancelada é recusado.
- [ ] AC-4: O check-in de uma reserva inexistente devolve uma resposta de não encontrado.
- [ ] AC-5: O check-out de uma estadia ativa é registado.
- [ ] AC-6: O check-out sem check-in prévio é recusado.
- [ ] AC-7: Um check-out duplicado é recusado.
- [ ] AC-8: Depois do período da reserva, o quarto volta a ficar livre.
- [ ] AC-9: Sem linha escolhida, os dois botões estão desativados.
- [ ] AC-10: "Fazer check-in" só está ativo numa linha RESERVED.
- [ ] AC-11: "Fazer check-out" só está ativo numa linha CHECKED_IN.
- [ ] AC-12: Depois do check-in, a tabela atualiza e mostra CHECKED_IN.
- [ ] AC-13: Depois do check-out, a tabela atualiza e a reserva sai da lista.

## Notas
- Junta as histórias story-check-in e story-check-out (decisão do utilizador, 2026-11-06). Entrega 1 (Q-06).
- Os botões Check-in e Check-out ficam na barra de ações do ecrã principal "Reservas" (story-ver-reservas-ativas.md). Mesmo dono dessa história. Nenhuma outra história do sprint espera por esta.
- A entrega do cartão magnético (RF-13, RF-16) fica na entrega final (epic-magnetic-card-access): as histórias do cartão acrescentam depois a emissão do cartão no checkIn e o cartão deixa de funcionar no checkOut (story-desativar-cartao-check-out). No check-out o registo de passagens fica guardado (decisão da equipa, 2026-10-16).
- Depois do check-in o gestor deixa de poder apagar a reserva (RF-22).
- Dependências: nenhuma forte. Testar sozinha com reservas de teste próprias (ativa, cancelada, com check-in, com check-out) (foundation T4). O AC-8 verifica com a consulta de quartos livres (sprint 4).

points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: Um só serviço (checkIn e checkOut) com regras de estado e erros (duplicado, cancelada, inexistente, sem check-in). Os estados já existem em ReservationStatus. Há também dois botões que ativam conforme o estado da linha e atualizam a tabela. Não há cartão, o que baixa o esforço, mas há 13 critérios e dois fluxos.
split_needed: no

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, Claude Code (claude-sonnet-5-5), 2026-11-06; estimator, Claude Code (claude-sonnet-5-5), 2026-11-06. Reviewed by: pending.
