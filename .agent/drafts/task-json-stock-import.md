---
type: task
summary: Rever diagramas e documentação da entrada de stock para aceitar ficheiros JSON além de XML
parent: H4Y-49
requirements: [RF-28, RF-29]
points: 1
status: approved
jira_key:
labels: [stock, docs]
sprint: 05
assignee: antonioprmartinho
---

# Task: diagramas e documentação para entrada de stock em XML e JSON

## Contexto

A atualização do enunciado de 2026-11-06 acrescenta a secção "New stock entries" (`.agent/context/enunciado.md`, secção 2). Alguns fornecedores têm melhores condições, mas não conseguem exportar ficheiros XML. A aplicação tem de importar também ficheiros JSON. O exemplo JSON segue os mesmos pressupostos e regras do ficheiro XML.

A documentação e os diagramas descrevem hoje a importação só em XML. A dúvida sobre o formato JSON está na questão Q-59 (`docs/requirements/questions.md`).

## Objetivo

A documentação e os diagramas descrevem a importação do ficheiro do fornecedor em XML e em JSON. Assim, as histórias de importação podem ser construídas sem um novo desenho.

## Âmbito

- [ ] `docs/requirements/requirements.md`: rever e atualizar a referência só a XML para XML ou JSON (RF-28: alterar ou acrescentar um ID de requisito; o requirements-analyst decide; citar Q-59)
- [ ] `docs/requirements/use-cases.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/requirements/actors.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/requirements/glossary.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/uml/use-cases.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/uml/use-case-catalog.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/uml/domain-model.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/uml/class-diagram.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/uml/classes.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/uml/component-diagram.md`: rever e atualizar a referência só a XML para XML ou JSON (exceção PlantUML)
- [ ] `docs/uml/component-guide.md`: rever e atualizar a referência só a XML para XML ou JSON (exceção PlantUML)
- [ ] `docs/uml/sequence-diagrams.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/uml/sequence-guide.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/uml/traceability.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/uml/guide.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/uml/README.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/database/schema.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `docs/database/tables.md`: rever e atualizar a referência só a XML para XML ou JSON. Tabela `stock_imports`: decidir se é preciso uma coluna de formato do ficheiro
- [ ] `docs/libraries.md`: escolha da biblioteca JSON e motivo
- [ ] `.agent/context/project-context.md`: rever e atualizar a referência só a XML para XML ou JSON
- [ ] `stories`: `story-carregar-xml-fornecedor-gestor.md`, `story-carregar-xml-fornecedor-rececionista.md`, `story-ver-relatorio-importacao.md` e `epic-stock-management.md`. Atualizar títulos, critérios e notas com critérios JSON. A equipa volta a estimar.

## Fora do âmbito

- Sem código Java. O código de importação fica nas histórias de importação de stock.

## Critério de conclusão

- Nenhum documento diz só XML para a importação do fornecedor.
- Os diagramas são renderizados sem erro.
- Q-59 é citada onde o formato JSON é desconhecido.
- PR revista.

## Dependências

- Nenhuma na sprint. Corre em paralelo com as histórias da sprint.

Nota: as tarefas não têm pontos (regra do backlog).

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: general-purpose agent, Claude Code (claude-haiku-5-5), 2026-11-06. Reviewed by: pending.
