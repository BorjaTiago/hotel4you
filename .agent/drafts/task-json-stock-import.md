---
type: task
summary: Rever diagramas e documentação da entrada de stock para aceitar ficheiros JSON além de XML
parent: H4Y-49
requirements: [RF-28, RF-29]
points: 1
status: published
jira_key: H4Y-84
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

- [x] `docs/requirements/requirements.md`: rever e atualizar a referência só a XML para XML ou JSON (RF-28: alterar ou acrescentar um ID de requisito; o requirements-analyst decide; citar Q-59)
- [x] `docs/requirements/use-cases.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/requirements/actors.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/requirements/glossary.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/uml/use-cases.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/uml/use-case-catalog.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/uml/domain-model.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/uml/class-diagram.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/uml/classes.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/uml/component-diagram.md`: rever e atualizar a referência só a XML para XML ou JSON (exceção PlantUML)
- [x] `docs/uml/component-guide.md`: rever e atualizar a referência só a XML para XML ou JSON (exceção PlantUML)
- [x] `docs/uml/sequence-diagrams.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/uml/sequence-guide.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/uml/traceability.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/uml/guide.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/uml/README.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/database/schema.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `docs/database/tables.md`: rever e atualizar a referência só a XML para XML ou JSON. Tabela `stock_imports`: decidir se é preciso uma coluna de formato do ficheiro
- [x] `docs/libraries.md`: escolha da biblioteca JSON e motivo
- [x] `.agent/context/project-context.md`: rever e atualizar a referência só a XML para XML ou JSON
- [x] `stories`: `story-carregar-xml-fornecedor-gestor.md`, `story-carregar-xml-fornecedor-rececionista.md`, `story-ver-relatorio-importacao.md` e `epic-stock-management.md`. Atualizar títulos, critérios e notas com critérios JSON. A equipa volta a estimar.

## Decisões (H4Y-84, 2026-11-12, por rever na PR)

- RF-28 muda de texto ("um ficheiro XML ou um ficheiro JSON"); não há ID novo. A nota cita a atualização do enunciado de 2026-11-06 e Q-59.
- UC-53 passa a "Read supplier stock file (XML or JSON)"; o ID fica.
- Classes futuras: `StockFileFormat` (XML, JSON; `fromFileName`), interface `StockFileReader` com `XmlStockFileReader` e `JsonStockFileReader`, record `StockFileLine`. Os dois leitores dão as mesmas linhas, por isso as regras da importação ficam num só sítio. `StockController.onImportXmlClicked` passa a `onImportStockFileClicked`.
- Tabela `stock_imports`: nova coluna `file_format` (`xml` ou `json`, CHECK). Mostra que leitor leu o ficheiro. Ainda não há script SQL desta tabela.
- Biblioteca JSON: Gson 2.11.0 (planeada, entra no `pom.xml` com a história de importação). XML usa `javax.xml.parsers` do JDK.
- Diagrama de componentes: o artefacto passa a "Supplier stock file (XML or JSON)" e a porta `xml` passa a `file`.
- As histórias de importação do gestor e do rececionista voltaram a `status: draft`: têm critérios JSON novos e a equipa volta a estimar.

## Fora do âmbito

- Sem código Java. O código de importação fica nas histórias de importação de stock.

## Critério de conclusão

- Nenhum documento diz só XML para a importação do fornecedor.
- Os diagramas são renderizados sem erro.
- Q-59 é citada onde o formato JSON é desconhecido.
- PR revista.

## Dependências

- Nenhuma na sprint. Corre em paralelo com as histórias da sprint.

Nota: esta tarefa tem 1 ponto (decisão da equipa, BorjaTiago, 2026-11-06).

Created by: BorjaTiago. Updated by: antonioprmartinho. AI use: general-purpose agent, Claude Code (claude-haiku-5-5), 2026-11-06; H4Y-84 JSON stock import docs by Claude Code (claude-opus-5-5), 2026-11-12. Reviewed by: pending.
