---
type: epic
summary: Movimentos de stock e importação XML de fornecedor
parent:
requirements: [RF-28, RF-29]
points:
points_suggested:
status: approved
jira_key:
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
- Importação XML dinâmica (upsert, Q-48, substitui Q-10 e Q-38): o item que existe (nome igual, sem espaços nas pontas, sem distinguir maiúsculas) recebe a entrada de stock e o novo preço (a unidade tem de ser a mesma); o item que não existe é criado com a unidade do XML. Cada item do XML tem nome, quantidade, unidade e preço obrigatórios. Nada é rejeitado por ser desconhecido. Só itens ou ficheiros com formato inválido são inválidos.
- O relatório de importação lista itens criados, atualizados e inválidos (com o motivo) e as contagens.
- O mesmo ficheiro (mesmo conteúdo) não pode ser importado duas vezes. Um ficheiro recusado por inteiro mostra uma mensagem de erro e não regista nada (sem relatório).
- A equipa define o formato XML.
- O gestor cria e edita itens de stock (nome, unidade, preço), incluindo os criados por importação. Decisão da equipa, 2026-10-16. Sem história de remoção.

## Fora de âmbito
- Acesso do fornecedor ao sistema.

## Atores
- Rececionista (como funcionário), gestor.

## Valor
O hotel conhece o material que tem em stock.

## Ligações ao enunciado
Secção 2. RF-28, RF-29. Q-48 (substitui Q-10 e Q-38), Q-17, Q-21.

## Histórias candidatas
- Cadastrar item de stock (actor: manager; RF-28, RF-29)
- Editar item de stock (actor: manager; RF-28, RF-29)
- Definir o formato XML de entrega (actor: manager; RF-28)
- Carregar um ficheiro XML de fornecedor (actor: manager; RF-28)
- Ver o relatório de importação com itens criados, atualizados e inválidos (actor: manager; RF-28)
- Registar entrada de stock (actor: receptionist; RF-29)
- Registar uso de stock (actor: receptionist; RF-29)
- Registar saída de stock (actor: receptionist; RF-29)

## Questões em aberto
- O ficheiro XML de exemplo não consta do enunciado. Q-10 foi uma decisão da equipa, agora substituída por Q-48.

AI use: epic-writer, general-purpose (tradução para PT-PT), Claude Code (claude-sonnet-5-5), 2026-10-16; story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16 (Q-48). Unit/price rule by docs-updater, Claude Code (claude-haiku-5-5), 2026-10-16. Reviewed by: pending.
