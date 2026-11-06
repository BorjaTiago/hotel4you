---
type: task
summary: Aplicar um estilo comum a todos os ecrãs
parent: H4Y-11
requirements: [RNF-04]
points: 1
status: published
jira_key: H4Y-83
labels: [ui, usability, javafx]
sprint: 05
assignee: pedrocostaisep
---

# Task: aplicar um estilo comum a todos os ecrãs

## Objetivo

Todos os ecrãs têm o mesmo aspeto: mesmas cores, tipos de letra, botões, tabelas e mensagens de erro.

## Âmbito

- [ ] Regras comuns no `style.css`: cores, tipos de letra, botões, tabelas e etiquetas de erro
- [ ] Acrescentar `styleClass` em todos os FXML
- [ ] Sem alteração de disposição (layout) em nenhum ecrã
- [ ] O `ScreenLoader` já carrega o `style.css` em todos os ecrãs; não é preciso mudar o carregamento

## Critério de conclusão

- [ ] Todos os botões, tabelas e etiquetas de erro de todos os ecrãs usam as classes comuns do `style.css`.
- [ ] As cores e os tipos de letra são os mesmos em todos os ecrãs.
- [ ] A mensagem de erro tem a mesma cor, o mesmo tipo de letra e o mesmo estilo em todos os ecrãs.
- [ ] Em cada ecrã, a disposição dos campos e botões é a mesma de antes; só mudaram o CSS e o `styleClass`.
- [ ] Sem alteração de lógica Java.

## Dependências

- Nenhuma. Os conflitos com outras edições de FXML são só conflitos de merge. Fazer o ecrã unificado depois de o PR #53 fazer merge.
- O enunciado não define o aspeto: decisão da equipa (BorjaTiago, 2026-11-06), Q-60.

Nota: esta tarefa tem 1 ponto (decisão da equipa, BorjaTiago, 2026-11-06).

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: story-writer, Claude Code (claude-sonnet-5-5), 2026-11-06. Reviewed by: pending.
