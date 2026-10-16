---
type: story
summary: Calcular o preço total da reserva
parent: epic-extra-services-pricing
requirements: [RF-06, RF-07, RF-09]
points: 5
points_suggested: 5
status: approved
jira_key:
labels: [pricing]
sprint:
---

# Título: Calcular o preço total da reserva

Como rececionista,
Eu quero ver o preço total de uma reserva com o preço base e os extras,
Para que eu diga ao cliente o valor correto da estadia.

## Critérios de aceite

### Cenário AC-1: Preço só com o base
  Dado que a reserva é de um quarto duplo com preço base 80,00 por noite durante 3 noites e sem extras
  Quando o rececionista consulta o preço total
  Então o sistema mostra 240,00

### Cenário AC-2: Preço com extras
  Dado que a reserva é de 3 noites com preço base 80,00, vista mar a 15,00 e lavandaria a 5,00 por noite
  Quando o rececionista consulta o preço total
  Então o sistema mostra 3 x (80,00 + 15,00 + 5,00) = 300,00

### Cenário AC-3: Preço com serviço pago
  Dado que a reserva de 2 noites tem preço base 80,00 e o spa a 40,00 por noite
  Quando o rececionista consulta o preço total
  Então o sistema mostra 240,00

### Cenário AC-4: Preço recalculado ao mudar extras
  Dado que a reserva tem a vista mar
  Quando o rececionista remove a vista mar
  Então o preço total é recalculado sem esse extra

### Cenário AC-5: Falta preço base
  Dado que o gestor não definiu o preço base do tipo de quarto da reserva
  Quando o rececionista consulta o preço total
  Então o sistema indica que não pode calcular o preço

## Critérios de aceite (checklist)

- [ ] AC-1: Uma reserva sem extras mostra o preço total igual ao preço base vezes as noites (240,00 no exemplo).
- [ ] AC-2: Uma reserva com extras mostra o total por noites vezes a soma do preço base e dos extras (300,00 no exemplo).
- [ ] AC-3: Uma reserva com serviço pago inclui o custo do serviço no total (240,00 no exemplo).
- [ ] AC-4: Ao remover um extra, o preço total é recalculado sem esse extra.
- [ ] AC-5: Sem preço base do tipo de quarto, o sistema indica que não pode calcular o preço.

## Notas
Preços fixos por noite (Q-11, decisão da equipa). Valores dos exemplos são só ilustrativos. Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: reservas de teste com preço base, extras e serviços já gravados, e um tipo sem preço base (foundation T4). O AC-4 testa o recálculo depois de retirar o extra nos dados de teste; o ecrã de remoção é de story-adicionar-extras-quarto-reserva (sprint 12). Fase pelo priorizador (Q-25).

points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: Cálculo por noites com preço base, extras e serviços, recalculo ao mudar extras e erro sem preço base. Junta dados de três histórias. Muitos casos de teste de valores.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
