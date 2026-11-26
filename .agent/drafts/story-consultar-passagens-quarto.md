---
type: story
summary: Consultar as passagens de cartões num quarto
parent: epic-master-card-audit
requirements: [RF-21]
points: 3
points_suggested: 3
status: approved
jira_key: H4Y-156
labels: [access-control, final-delivery]
sprint:
---

# Título: Consultar as passagens de cartões num quarto

Como gestor,
Eu quero ver a lista de passagens de cartões num quarto,
Para que eu saiba quem entrou nesse quarto e quando.

## Critérios de aceite

### Cenário AC-1: Lista de passagens do quarto
  Dado que o cartão 12345 do cliente entrou no quarto 301
  Quando o gestor consulta as passagens do quarto 301
  Então o sistema mostra o cartão 12345 com a hora da entrada

### Cenário AC-2: Cartão mestre incluído
  Dado que um cartão mestre entrou no quarto 301
  Quando o gestor consulta as passagens do quarto 301
  Então a lista inclui a passagem do cartão mestre

### Cenário AC-3: Quarto sem passagens
  Dado que ninguém entrou no quarto 302
  Quando o gestor consulta as passagens do quarto 302
  Então o sistema mostra uma lista vazia

### Cenário AC-4: Quarto inexistente
  Dado que não existe o quarto 999
  Quando o gestor consulta as passagens do quarto 999
  Então o sistema indica que o quarto não existe

### Cenário AC-5: Perfil sem permissão
  Dado que o utilizador é rececionista
  Quando tenta consultar as passagens de um quarto
  Então o sistema recusa a consulta

### Cenário AC-6: Fluxo completo
  Dado que o gestor tem sessão iniciada e o quarto 301 tem passagens de um cartão de cliente e de um cartão mestre
  Quando o gestor abre o ecrã de consulta de quartos no menu, escolhe o quarto 301 e consulta
  Então o serviço lê as passagens da base de dados e o ecrã mostra cada cartão com a hora da entrada
  E, se o quarto não existe ou não tem passagens, o ecrã mostra a mensagem correspondente

## Critérios de aceite (checklist)

- [ ] AC-1: A consulta de um quarto mostra o cartão do cliente que entrou com a hora da entrada.
- [ ] AC-2: A lista de passagens do quarto inclui as passagens de cartão mestre.
- [ ] AC-3: Um quarto sem passagens mostra uma lista vazia.
- [ ] AC-4: Consultar um quarto inexistente mostra que o quarto não existe.
- [ ] AC-5: O rececionista não consegue consultar as passagens de um quarto.
- [ ] AC-6: O gestor abre o ecrã no menu, escolhe o quarto e vê cada cartão com a hora; um quarto inexistente ou sem passagens mostra a mensagem correspondente.

## Notas
Exemplo do enunciado: quarto 301. Usa o CardAccessService (consultar por quarto) da H4Y-109 e junta só a regra de permissão do gestor e o ecrã. Não cria tabelas. Entrega final (Q-06).

points_suggested: 3
reference: Registar um cliente (3)
compared: equal
reasoning: Consulta por quarto com lista e ecrã. Junta passagens de clientes e do cartão mestre. Maior que "Listar os quartos por tipo" (2) por causa do perfil e do cartão mestre.
split_needed: no

ready: true
dor_failed: []
questions: []

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Jira sync (AC-6, notes) by story-writer, GitHub Copilot (claude-opus-5.5), 2026-11-24. Reviewed by: pending.
