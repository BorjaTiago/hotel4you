---
type: story
summary: Adicionar vista especial, bebidas ou lavandaria à reserva
parent: epic-extra-services-pricing
requirements: [RF-07]
points: 5
points_suggested: 5
status: approved
jira_key:
labels: [pricing, services]
sprint:
---

# Título: Adicionar vista especial, bebidas ou lavandaria à reserva

Como rececionista,
Eu quero adicionar vista especial, bebidas ou lavandaria a uma reserva,
Para que o preço por noite inclua estes extras.

## Critérios de aceite

### Cenário AC-1: Adicionar vista especial
  Dado que existe uma reserva ativa e o preço extra da vista mar está definido
  Quando o rececionista adiciona a vista mar à reserva
  Então a reserva mostra a vista mar
  E o preço por noite da reserva aumenta o valor do preço extra da vista mar

### Cenário AC-2: Adicionar bebidas e lavandaria
  Dado que existe uma reserva ativa com preços extra definidos para bebidas e lavandaria
  Quando o rececionista adiciona bebidas e lavandaria à reserva
  Então a reserva mostra os dois extras
  E o preço por noite soma os dois preços extra

### Cenário AC-3: Remover um extra
  Dado que a reserva tem a vista mar
  Quando o rececionista remove a vista mar
  Então o preço por noite volta ao valor sem esse extra

### Cenário AC-4: Extra sem preço definido
  Dado que o gestor não definiu o preço extra da lavandaria
  Quando o rececionista tenta adicionar lavandaria à reserva
  Então o sistema recusa a operação
  E mostra uma mensagem a indicar que falta o preço

### Cenário AC-5: Reserva cancelada
  Dado que a reserva está cancelada
  Quando o rececionista tenta adicionar um extra
  Então o sistema recusa a operação

### Cenário AC-6: Só uma vista por reserva
  Dado que a reserva tem a vista mar
  Quando o rececionista adiciona a vista montanha
  Então a reserva passa a ter apenas a vista montanha
  E o preço por noite usa só o preço extra da vista montanha

### Cenário AC-7: Extras depois do check-in
  Dado que a reserva tem check-in efetuado
  Quando o rececionista adiciona ou remove vista especial, bebidas ou lavandaria
  Então o sistema recusa a operação
  E a reserva mantém os extras anteriores

## Critérios de aceite (checklist)

- [ ] AC-1: A vista especial adicionada aparece na reserva e o preço por noite aumenta o valor do seu preço extra.
- [ ] AC-2: Bebidas e lavandaria adicionadas aparecem na reserva e o preço por noite soma os dois preços extra.
- [ ] AC-3: Ao remover um extra, o preço por noite volta ao valor sem esse extra.
- [ ] AC-4: Um extra sem preço definido é recusado com uma mensagem a indicar que falta o preço.
- [ ] AC-5: Numa reserva cancelada, adicionar um extra é recusado.
- [ ] AC-6: Ao escolher uma segunda vista, a reserva fica só com a nova vista e o preço usa só o extra dela.
- [ ] AC-7: Depois do check-in, adicionar ou remover extras é recusado e a reserva mantém os extras anteriores.

## Notas
Q-11, Q-25. Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: reservas de teste (ativa, cancelada, com check-in) e preços extra de teste, um sem preço (foundation T4). Partilha `ReservationExtrasService` (T0) com story-adicionar-servicos-extra-reserva: cada história tem a sua classe. Decisões da equipa (2026-10-16): a vista especial é escolha única (mar ou montanha) por reserva; os extras podem ser alterados até ao check-in; datas e quarto não são editáveis; cancelar não é possível depois do check-in (ver story-cancel-reservation).

points_suggested: 5
reference: Realizar check-in com cartão magnético (5)
compared: equal
reasoning: Sete cenários: adicionar, remover, vista única, preço em falta, reserva cancelada e bloqueio após check-in. Lógica, dados e GUI juntos. Muito parecida com a história de serviços pagos; podem partilhar código.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
