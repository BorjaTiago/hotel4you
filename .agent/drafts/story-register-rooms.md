---
type: story
summary: Registar os 20 quartos por tipo e piso
parent: epic-rooms-pricing
requirements: [RF-04]
points: 3
points_suggested: 3
status: published
jira_key: H4Y-25
labels: [rooms, 1st-delivery]
sprint: 03
---

# Título: Registar os 20 quartos por tipo e piso

Como gestor,
Eu quero registar cada quarto com o seu tipo e o seu piso,
Para que o hotel tenha os 20 quartos disponíveis para reserva.

## Critérios de aceite

### Cenário AC-1: Registar um quarto válido
  Dado que o gestor está autenticado
  E o piso 2 tem menos de 10 quartos
  Quando o gestor regista um quarto do tipo duplo no piso 2
  Então o quarto fica registado com o tipo e o piso indicados

### Cenário AC-2: Distribuição completa do hotel
  Dado que o gestor registou todos os quartos
  Quando o gestor consulta o resumo dos quartos
  Então existem 20 quartos: 10 no piso 2 e 10 no piso 3
  E existem 7 simples, 10 duplos e 3 familiares

### Cenário AC-3: Piso que não é de quartos
  Dado que o gestor está autenticado
  Quando o gestor tenta registar um quarto no piso 1
  Então o sistema recusa o registo
  E informa que só os pisos 2 e 3 têm quartos

### Cenário AC-4: Limite de quartos por piso
  Dado que o piso 3 já tem 10 quartos
  Quando o gestor tenta registar mais um quarto no piso 3
  Então o sistema recusa o registo

### Cenário AC-5: Editar um quarto
  Dado que o gestor está autenticado
  E existe um quarto do tipo duplo no piso 2
  Quando o gestor altera o número, o piso e o tipo do quarto e confirma a alteração
  Então o número, o piso e o tipo novos ficam guardados
  E quando o gestor altera só o tipo do quarto, o número mantém-se

### Cenário AC-6: Número de outro quarto
  Dado que existe um quarto com o número 202
  E existe outro quarto com o número 203
  Quando o gestor tenta mudar o número do quarto 202 para 203
  Então o sistema recusa a alteração com a mensagem "O número do quarto já existe."
  E o quarto mantém o número 202

### Cenário AC-7: Piso cheio
  Dado que o piso 3 já tem 10 quartos
  Quando o gestor tenta mudar um quarto do piso 2 para o piso 3
  Então o sistema recusa a alteração com a mensagem "O piso já tem 10 quartos."
  E uma alteração que fica no mesmo piso cheio funciona
  E o gestor não consegue mudar um quarto para um piso sem quartos, como o piso 1

### Cenário AC-8: Perfil sem permissão
  Dado que o utilizador é rececionista, ou que não tem sessão iniciada
  Quando tenta editar um quarto
  Então o sistema recusa a alteração com a mensagem "Só o gestor pode editar quartos."
  E o quarto fica como estava

### Cenário AC-9: Quarto inexistente
  Dado que o quarto indicado não existe
  Quando o gestor tenta editar esse quarto
  Então o sistema recusa a alteração com a mensagem "O quarto indicado não existe."

### Cenário AC-10: Remover um quarto
  Dado que o gestor está autenticado
  E existe um quarto sem associações
  Quando o gestor seleciona o quarto, clica em "Remover quarto" e confirma
  Então o quarto sai da lista
  E os outros quartos mantêm-se

### Cenário AC-11: Quarto com associações
  Dado que o gestor está autenticado
  E o quarto tem associações
  Quando o gestor tenta remover o quarto
  Então o sistema recusa a remoção com a mensagem "O quarto tem associações e não pode ser removido."
  E o quarto fica como estava

### Cenário AC-12: Perfil sem permissão para remover
  Dado que o utilizador é rececionista, ou que não tem sessão iniciada
  Quando tenta remover um quarto
  Então o sistema recusa a remoção com a mensagem "Só o gestor pode remover quartos."
  E o quarto fica como estava

### Cenário AC-13: Quarto inexistente na remoção
  Dado que o quarto indicado não existe
  Quando o gestor tenta remover esse quarto
  Então o sistema recusa a remoção com a mensagem "O quarto indicado não existe."

## Critérios de aceite (checklist)

- [ ] AC-1: Um quarto válido fica registado.
- [ ] AC-2: A distribuição completa dos quartos do hotel pode ser registada.
- [ ] AC-3: Registar um quarto num piso que não é de quartos é recusado.
- [ ] AC-4: Exceder o limite de quartos por piso é recusado.
- [ ] AC-5: Editar um quarto guarda o número, o piso e o tipo novos.
- [ ] AC-6: Um número que já existe noutro quarto é recusado.
- [ ] AC-7: Mudar um quarto para um piso cheio é recusado. Uma alteração no mesmo piso cheio funciona. Um piso sem quartos é recusado.
- [ ] AC-8: Editar um quarto sem permissão é recusado.
- [ ] AC-9: Editar um quarto inexistente é recusado.
- [ ] AC-10: Remover um quarto tira o quarto da lista. Os outros quartos mantêm-se.
- [ ] AC-11: Remover um quarto com associações é recusado.
- [ ] AC-12: Remover um quarto sem permissão é recusado.
- [ ] AC-13: Remover um quarto inexistente é recusado.

## Notas
Q-01 (decisão da equipa, não confirmada pelo professor): 20 quartos, 10 por piso, divisão 7/10/3 para o hotel inteiro. Quarto familiar: 3 ou 4 pessoas.
Dependências (auditoria 2026-10-16): depende da tarefa T5 (pisos e tipos de quarto). Não depende de story-model-hotel-floors.
Decisão do utilizador (2026-10-30): o gestor pode editar e remover quartos. Editar número, piso e tipo, e remover, não estão no enunciado.
Regra de associações: Q-36.

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: Validate and save rooms with floor and count limits (10 per floor, 7/10/3 split). Some rules, one or two layers.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by jira-publisher, Claude Code (claude-sonnet-5-5), 2026-10-16; sprint field by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30; room removal scenarios by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30. Reviewed by: pending.
