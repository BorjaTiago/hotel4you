---
type: story
summary: Abrir todos os quartos e zonas com o cartão mestre
parent: epic-master-card-audit
requirements: [RF-23, RF-19]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [access-control, final-delivery]
sprint:
---

# Título: Abrir todos os quartos e zonas com o cartão mestre

Como gestor,
Eu quero abrir todos os quartos de clientes e entrar em todas as zonas com o meu cartão mestre,
Para que eu aceda a qualquer local do hotel.

## Critérios de aceite

### Cenário AC-1: Cartão mestre abre um quarto
  Dado que o gestor tem um cartão mestre ativo
  Quando o gestor passa o cartão na porta do quarto 301 (entrada simulada)
  Então o sistema permite a entrada

### Cenário AC-2: Cartão mestre abre todas as zonas
  Dado que o gestor tem um cartão mestre ativo
  Quando o gestor passa o cartão na porta da garagem, do armazém, do bar, das salas de conferências, do restaurante ou da piscina
  Então o sistema permite a entrada em cada uma

### Cenário AC-3: Passagem registada
  Dado que o gestor tem um cartão mestre ativo
  Quando o gestor entra num local com o cartão mestre
  Então o sistema guarda o local e a hora da entrada

### Cenário AC-4: Cartão mestre bloqueado
  Dado que o cartão mestre do gestor está bloqueado
  Quando o gestor passa o cartão numa porta
  Então o sistema recusa a entrada

## Notas
Um cartão mestre por gestor, cada passagem registada (Q-19, decisão da equipa). Entrada simulada na GUI. As zonas abertas são as criadas pelo gestor (ver "Criar uma zona num piso"); zona removida deixa de abrir. Também permite ao pessoal entrar em quartos (Q-08). Entrega final (Q-06). Bloqueio: ver história "Bloquear um cartão mestre perdido".

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Regra de acesso total para cartão mestre ativo, recusa se bloqueado, e grava a passagem. Reutiliza o motor de acesso das outras histórias.
split_needed: no

ready: true
dor_failed: []
questions: []

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
