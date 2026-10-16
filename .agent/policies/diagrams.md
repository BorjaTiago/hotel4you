# Diagram Policy

This policy sets how the team writes and checks diagrams. The Definition of Done item "Diagrams are updated" uses these rules. See [definition-of-done.md](./definition-of-done.md).

## Tool

1. Use Mermaid for all diagrams. This includes the domain model, class, ER, use case and sequence diagrams.
2. Do not use PlantUML, draw.io or image files (PNG, SVG, JPG). Do not add binary diagram files to the repository.
3. Put each diagram in a `mermaid` code block inside a `.md` file.

## Paths

| Diagram | Path |
|---------|------|
| Domain model | `docs/uml/` |
| Class diagram | `docs/uml/` |
| Use case diagram | `docs/uml/` |
| Sequence diagram | `docs/uml/` |
| ER diagram | `docs/database/` |

## Content rules

1. The domain model has no methods. It shows classes, attributes and relations only.
2. The class diagram shows types, visibility (`+`, `-`, `#`) and multiplicities.
3. Use the names from `docs/requirements/glossary.md` for classes, actors and use cases.
4. Each diagram file cites the requirement IDs it covers (RF-NN or RNF-NN), when they apply.

## Validation

Check the syntax before each commit. Run the Mermaid CLI on the changed file:

```bash
npx -y @mermaid-js/mermaid-cli -i <file>.md -o /tmp/out.md
```

Use `mmdc` instead when it is installed. A diagram must render on GitHub. A syntax error blocks the commit.

## AI use

Every diagram file ends with the footer line from `AGENTS.md`, rule 8 (Created by, Updated by, AI use, Reviewed by). Each agent adds its own name when it edits the file. Never put AI-use text in source code.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: Claude Code agent, Claude Haiku 5.5, 2026-10-16; docs-updater, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
