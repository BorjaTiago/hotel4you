---
type: epic
summary: Stock movements and supplier XML import
parent:
requirements: [RF-28, RF-29]
points:
points_suggested:
status: approved
jira_key:
labels: [stock, xml, final-delivery]
sprint:
---

# Epic: Stock management and XML import

## Goal
Staff and managers record stock entry, use and exit. A supplier XML file gives the entry.

## Delivery
Final delivery (Q-06).

## Scope
- Stock movements with quantity and date (Q-21).
- XML load by staff or manager; supplier does not log in (Q-17).
- Unknown items rejected and listed in an import report; valid items imported (Q-10).
- Team defines the XML format.
- Manager creates and edits stock items (name, unit). Team decision, 2026-10-16. No remove story.

## Out of scope
- Supplier access to the system.

## Actors
- Receptionist (as staff), manager.

## Value
The hotel knows its material in stock.

## Statement links
Section 2. RF-28, RF-29. Q-10, Q-17, Q-21.

## Candidate stories
- Cadastrar item de stock (actor: manager; RF-28, RF-29)
- Editar item de stock (actor: manager; RF-28, RF-29)
- Definir o formato XML de entrega (actor: manager; RF-28)
- Carregar um ficheiro XML de fornecedor (actor: manager; RF-28)
- Ver o relatório de importação com itens rejeitados (actor: manager; RF-28)
- Registar entrada de stock (actor: receptionist; RF-29)
- Registar uso de stock (actor: receptionist; RF-29)
- Registar saída de stock (actor: receptionist; RF-29)

## Open questions
- XML example file is not in the statement. Q-10 is a team decision.

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
