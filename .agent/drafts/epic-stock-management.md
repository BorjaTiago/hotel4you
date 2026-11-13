---
type: epic
summary: Movimentos de stock e importação XML ou JSON de fornecedor
parent:
requirements: [RF-28, RF-29]
points:
points_suggested:
status: published
jira_key: H4Y-49
labels: [stock, xml, json, final-delivery]
sprint:
---

# Epic: Gestão de stock e importação XML ou JSON

## Objetivo
Os funcionários e os gestores registam a entrada, o uso e a saída de stock. Um ficheiro XML ou JSON do fornecedor dá a entrada (JSON: atualização do enunciado de 2026-11-06).

## Entrega
Entrega final (Q-06).

## Âmbito
- Movimentos de stock com quantidade e data (Q-21).
- Carregamento do ficheiro XML ou JSON por funcionário ou gestor; o fornecedor não faz login (Q-17).
- Itens desconhecidos rejeitados e listados num relatório de importação; itens válidos importados (Q-10).
- A equipa define o formato XML. O ficheiro JSON segue os mesmos pressupostos e regras do XML (Q-59).
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
- Definir o formato XML de entrega e o formato JSON equivalente (actor: manager; RF-28, Q-59)
- Carregar um ficheiro XML ou JSON de fornecedor (actor: manager; RF-28)
- Ver o relatório de importação com itens rejeitados (actor: manager; RF-28)
- Registar entrada de stock (actor: receptionist; RF-29)
- Registar uso de stock (actor: receptionist; RF-29)
- Registar saída de stock (actor: receptionist; RF-29)

## Questões em aberto
- Os ficheiros XML e JSON de exemplo não constam do enunciado (JSON: Q-59). Q-10 é uma decisão da equipa.

Created by: BorjaTiago. Updated by: antonioprmartinho. AI use: epic-writer, general-purpose (tradução para PT-PT), Claude Code (claude-sonnet-5-5), 2026-10-16; Jira key by jira-publisher, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-84 JSON stock import docs by Claude Code (claude-opus-5-5), 2026-11-12. Reviewed by: pending.
