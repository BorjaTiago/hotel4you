---
type: epic
summary: Registo de clientes pela receção
parent:
requirements: [RF-10, RNF-03]
points:
points_suggested:
status: published
jira_key: H4Y-13
labels: [clients, 1st-delivery]
sprint:
---

# Épica: Registo de clientes

## Objetivo
A receção e o gestor registam um cliente depressa: registo mais reserva em 30 segundos ou menos.

## Entrega
1.ª entrega (Q-27, decisão da equipa, utilizador, 2026-10-16). As reservas precisam de um cliente. Colocação em sprint: backlog prioritizer.

## Âmbito
- Gerir clientes: registar, listar e procurar, editar e remover (CRUD completo, Q-54).
- Um cliente com reservas não é removido.
- Formulário curto e rápido.

## Fora do âmbito
- Login de clientes (os clientes não iniciam sessão, Q-13).

## Atores
- Rececionista.
- Gestor (Q-54).

## Valor
As reservas e o check-in têm os dados do cliente.

## Ligações ao enunciado
Secções 2, 3c. RF-10, RNF-03. Q-12, Q-25, Q-54.

## Histórias candidatas
- Registar um novo cliente (H4Y-52; atores: rececionista e gestor; RF-10). Inclui listar, procurar, editar e remover (Q-54).
- Procurar um cliente já registado (H4Y-53): fundida em H4Y-52 (Q-54).
- Registar cliente em formulário rápido (ator: rececionista; RNF-03)

## Perguntas em aberto
- Nenhuma. Q-27 respondida: o RF-10 está na 1.ª entrega.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16; client scope (Q-54) by docs agent, Claude Code (claude-sonnet-5-5), 2026-11-05. Reviewed by: pending.
