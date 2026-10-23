---
type: epic
summary: Movimentos de stock e importação XML de fornecedor
parent:
requirements: [RF-28, RF-29]
points:
points_suggested:
status: published
jira_key: H4Y-49
labels: [stock, xml, final-delivery]
sprint:
---

# Epic: Gestão de stock e importação XML

## Objetivo
Os funcionários e os gestores registam a entrada, o uso e a saída de stock. Um ficheiro XML do fornecedor dá a entrada.

## Entrega
Entrega final (Q-06).

## Âmbito
- Movimentos de stock com quantidade e data (Q-21).
- Carregamento de XML por funcionário ou gestor; o fornecedor não faz login (Q-17).
- Itens desconhecidos rejeitados e listados num relatório de importação; itens válidos importados (Q-10).
- A equipa define o formato XML.
- O gestor cria e edita itens de stock (nome, unidade). Decisão da equipa, 2026-10-16. Sem história de remoção.

## Fora de âmbito
- Acesso do fornecedor ao sistema.

## Atores
- Rececionista (como funcionário), gestor.

## Valor
O hotel conhece o material que tem em stock.

## Ligações ao enunciado
Secção 2. RF-28, RF-29. Q-10, Q-17, Q-21.

## Histórias candidatas
- Cadastrar item de stock (actor: manager; RF-28, RF-29)
- Editar item de stock (actor: manager; RF-28, RF-29)
- Definir o formato XML de entrega (actor: manager; RF-28)
- Carregar um ficheiro XML de fornecedor (actor: manager; RF-28)
- Ver o relatório de importação com itens rejeitados (actor: manager; RF-28)
- Registar entrada de stock (actor: receptionist; RF-29)
- Registar uso de stock (actor: receptionist; RF-29)
- Registar saída de stock (actor: receptionist; RF-29)

## Questões em aberto
- O ficheiro XML de exemplo não consta do enunciado. Q-10 é uma decisão da equipa.

Created by: BorjaTiago. Updated by: lukassperandio. AI use: epic-writer, general-purpose (tradução para PT-PT), Claude Code (claude-sonnet-5-5), 2026-10-16; Jira key by jira-publisher, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
