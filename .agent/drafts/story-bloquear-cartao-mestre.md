---
type: story
summary: Bloquear um cartão mestre
parent: epic-master-card-audit
requirements: [RF-30]
points: 3
points_suggested: 3
status: approved
jira_key: H4Y-153
labels: [access-control, final-delivery]
sprint:
---

# Título: Bloquear um cartão mestre

Como gestor,
Eu quero bloquear um cartão mestre perdido,
Para que ninguém o use para entrar nos locais do hotel.

## Critérios de aceite

### Cenário AC-1: Bloqueio imediato
  Dado que existe o cartão mestre com o número M1 ativo
  Quando o gestor bloqueia o cartão M1
  Então o cartão M1 fica bloqueado de imediato
  E o cartão M1 não abre nenhuma porta

### Cenário AC-2: Bloqueio registado e emissão desbloqueada
  Dado que o gestor bloqueou o cartão M1
  Quando o gestor consulta o registo
  Então existe um registo do bloqueio do cartão M1
  E o gestor passa a poder emitir um novo cartão mestre

### Cenário AC-3: Cartão já bloqueado
  Dado que o cartão M1 já está bloqueado
  Quando o gestor tenta bloquear o cartão M1 outra vez
  Então o sistema recusa a operação
  E mostra uma mensagem a indicar que o cartão já está bloqueado

### Cenário AC-4: Cartão inexistente
  Dado que não existe um cartão mestre com o número M9
  Quando o gestor tenta bloquear o cartão M9
  Então o sistema recusa a operação

### Cenário AC-5: Perfil sem permissão
  Dado que o utilizador é rececionista
  Quando tenta bloquear um cartão mestre
  Então o sistema recusa a operação

### Cenário AC-6: Fluxo completo
  Dado que o gestor tem sessão iniciada e existe o cartão mestre M1 ativo
  Quando o gestor abre o ecrã de cartões mestre no menu, escolhe M1 e confirma o bloqueio
  Então o serviço guarda o bloqueio na base de dados e o ecrã mostra a mensagem de cartão bloqueado
  E o login com o cartão M1 é recusado

## Critérios de aceite (checklist)

- [ ] AC-1: O cartão mestre bloqueado fica bloqueado de imediato e não abre nenhuma porta.
- [ ] AC-2: O bloqueio fica registado e o gestor passa a poder emitir um novo cartão mestre.
- [ ] AC-3: Bloquear um cartão já bloqueado é recusado com mensagem a indicar que já está bloqueado.
- [ ] AC-4: Bloquear um cartão mestre inexistente é recusado.
- [ ] AC-5: O rececionista não consegue bloquear cartões mestre.
- [ ] AC-6: No ecrã "Cartões mestre" o gestor escolhe M1 e confirma; o bloqueio fica na base de dados, o ecrã mostra a mensagem e o login com M1 é recusado.

## Notas
Jira (H4Y-153): o bloqueio é o passo obrigatório antes de emitir um novo cartão mestre. Usa o CardAccessService (bloquear) da H4Y-109 e junta só a regra de permissão do gestor e o ecrã. Não cria tabelas.
Decisão da equipa (2026-10-16): o bloqueio é o passo obrigatório antes de emitir um novo cartão mestre; a emissão está em "Emitir um novo cartão mestre com novo número" e recusa-se se o cartão antigo estiver ativo. Q-19 e Q-26 (decisão da equipa, não confirmada pelo professor). Entrega final (Q-06).
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: cartões mestre de teste ativo e bloqueado (foundation T4). O AC-1 ("não abre nenhuma porta") usa `AccessService` e o leitor simulado (T0, T6), não a história do cartão mestre.

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Muda o estado do cartão e regista o bloqueio. O efeito imediato vem do motor de acesso. Poucas regras.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; sync with Jira H4Y-153 by doc-updater, GitHub Copilot (claude-opus-5.5), 2026-11-24. Reviewed by: pending.
