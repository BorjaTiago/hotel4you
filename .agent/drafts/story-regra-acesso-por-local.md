---
type: story
summary: Regra de acesso por local para a receção e o restaurante
parent: H4Y-12
requirements: [RF-24, RF-25, RF-01]
points: 3
points_suggested: 3
status: published
jira_key: H4Y-87
labels: [access-control, 1st-delivery]
sprint: 06
---

# Título: Regra de acesso por local para a receção e o restaurante

Como gestor,
Eu quero que a aplicação aplique uma só regra de acesso por local,
Para que a receção entre só nos pisos 0 e 1 e o restaurante só na cozinha e no restaurante.

## Critérios de aceite

### Cenário AC-1: Receção nos pisos 0 e 1
  Dado que o rececionista tem sessão iniciada
  Quando a regra de acesso é aplicada a um local do piso 0 ou do piso 1
  Então o acesso é permitido

### Cenário AC-2: Receção noutro piso
  Dado que o rececionista tem sessão iniciada
  Quando a regra de acesso é aplicada a um local de outro piso
  Então o acesso é recusado

### Cenário AC-3: Restaurante na cozinha e no restaurante
  Dado que o funcionário do restaurante tem sessão iniciada
  Quando a regra de acesso é aplicada à cozinha ou ao restaurante
  Então o acesso é permitido

### Cenário AC-4: Restaurante noutro local
  Dado que o funcionário do restaurante tem sessão iniciada
  Quando a regra de acesso é aplicada a qualquer outro local
  Então o acesso é recusado

### Cenário AC-5: Gestor sem limite
  Dado que o gestor tem sessão iniciada
  Quando a regra de acesso é aplicada a um local de qualquer piso
  Então o acesso é permitido

## Critérios de aceite (checklist)

- [ ] AC-1: A regra permite à receção os locais dos pisos 0 e 1.
- [ ] AC-2: A regra recusa à receção os locais de outros pisos.
- [ ] AC-3: A regra permite ao restaurante só a cozinha e o restaurante.
- [ ] AC-4: A regra recusa ao restaurante qualquer outro local.
- [ ] AC-5: A regra não limita o gestor.

## Notas
História nova, junta limit-reception-floors (RF-24) e limit-restaurant-staff-zones (RF-25, RF-01), que ficam `merged` (decisão do utilizador, 2026-11-13). Uma regra `canAccess(location, actor)` com testes em tabela (papel, local, resultado). Esta história não tem ecrã: o ecrã fica para as histórias de cartões (sprints 9 e 10), que chamam a regra. O limite aplica-se à receção (Q-08). Os locais por piso vêm de RF-03 e dos dados de referência (foundation T5). O piso da cozinha não está no enunciado: nos testes a cozinha é um local de teste.

Antigo AC-3 de limit-reception-floors (quarto de hóspede sem cartão mestre): os quartos ficam fora dos pisos 0 e 1, por isso o AC-2 já o recusa. O cartão mestre fica nas histórias de cartões.

Antigo AC-3 de limit-restaurant-staff-zones (o restaurante não cria nem apaga reservas): já está feito e testado em código, sem trabalho novo. `ReservationService.create` exige `Permission.MANAGE_RESERVATIONS` (rececionista e gestor) e `ReservationService.delete` exige `Permission.DELETE_RESERVATIONS` (só gestor). Testes existentes: `onCreate_throw_whenActorIsNotReceptionistOrManager` (`ReservationServiceTest`), `onDelete_throwAccessDenied_whenUserIsNotManager` (`ReservationDeleteTest`) e `onAllows_returnFalse_whenReceptionistDeletesReservations` (`PermissionTest`).

Dependências: nenhuma forte. Construir e testar sozinha: locais de teste por piso e utilizadores simulados (foundation T4). Fase: Q-25.

points_suggested: 3
reference: Registar um cliente (3)
compared: bigger
reasoning: Uma regra com dois papéis limitados e o gestor livre, testada em tabela. Sem ecrã e sem base de dados. Um pouco mais que 3 por juntar duas histórias e acordar o acesso ao piso do local no diagrama.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: sprint-planner, Claude Code (claude-sonnet-5-5), 2026-11-13; jira-publisher, Claude Code (claude-sonnet-5-5), 2026-11-13. Reviewed by: pending.
