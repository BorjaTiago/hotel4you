---
type: story
summary: Abrir todos os locais com o cartão mestre
parent: epic-master-card-audit
requirements: [RF-23]
points: 3
points_suggested: 3
status: approved
jira_key: H4Y-152
labels: [access-control, final-delivery]
sprint:
---

# Título: Abrir todos os locais com o cartão mestre

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

### Cenário AC-5: Fluxo completo
  Dado que o gestor tem um cartão mestre ativo com código
  Quando o gestor entra com o cartão mestre, abre o ecrã "O meu cartão" e clica num local (quarto ou zona)
  Então o serviço reconhece o cartão mestre, permite a entrada e guarda a passagem com o local e a hora
  E o ecrã mostra "entrada permitida"; com o cartão bloqueado o login é recusado e nada é guardado

## Critérios de aceite (checklist)

- [ ] AC-1: O cartão mestre ativo permite a entrada na porta de um quarto de cliente.
- [ ] AC-2: O cartão mestre ativo permite a entrada na garagem, armazém, bar, salas de conferências, restaurante e piscina.
- [ ] AC-3: Cada entrada com o cartão mestre guarda o local e a hora.
- [ ] AC-4: O cartão mestre bloqueado é recusado em qualquer porta.
- [ ] AC-5: Com o cartão mestre, "O meu cartão" permite a entrada em qualquer local e guarda a passagem com o local e a hora; com o cartão bloqueado o login é recusado e nada é guardado.

## Notas
Um cartão mestre por gestor, cada passagem registada. Entrada simulada na aplicação. As zonas abertas são as criadas pelo gestor; uma zona removida deixa de abrir. Usa o CardAccessService, o login com o cartão e o ecrã "O meu cartão" da H4Y-109 e junta só a regra do cartão mestre e o ecrã. O gestor recebe o código do cartão mestre no ecrã "Novo cartão mestre" (H4Y-153 e H4Y-154). Não cria tabelas.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Regra de acesso total para cartão mestre ativo, recusa se bloqueado, e grava a passagem. Reutiliza o motor de acesso das outras histórias.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Jira sync (login and "O meu cartão" flow, H4Y-122) by story-writer, GitHub Copilot (claude-opus-5.5), 2026-11-24. Reviewed by: pending.
