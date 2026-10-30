# UML documentation

This folder holds the Hotel4You UML diagrams and supporting documents.

- View the Mermaid diagram on GitHub, or paste it at mermaid.live.
- [domain-model.md](domain-model.md): domain model with business concepts only.
- [guide.md](guide.md): guide to defend each class and key flows.
- [class-diagram.md](class-diagram.md): UML class diagram with types, visibility and multiplicities.
- [classes.md](classes.md): all classes by layer, with attributes, methods and requirements.
- [traceability.md](traceability.md): requirements to classes.
- [use-cases.md](use-cases.md): use-case diagrams for all 53 use cases, by area, with include and extend relations (H4Y-7).
- [use-case-catalog.md](use-case-catalog.md): short descriptions, actors, permissions and requirement links.
- [component-diagram.md](component-diagram.md): components, interfaces and external systems (H4Y-9).
- [component-guide.md](component-guide.md): guide to defend the component diagram (H4Y-9).
- [sequence-diagrams.md](sequence-diagrams.md): SD-06 general supplier XML import and SD-04 detailed reservation deletion, with traceability and persistence gaps (H4Y-8).
- [sequence-guide.md](sequence-guide.md): guide to defend the sequence diagrams and their error cases (H4Y-8).
- Database: [../database/schema.md](../database/schema.md), [../database/erd.md](../database/erd.md).

## Differences from the database

- `RoomType` class vs `room_types` table.
- `ReservationExtra` vs `reservation_services`.
- No `Stay` class. The `Reservation` holds the stay times.
- `Client` class vs `customers` table.
- `Role` enum vs `roles` table.
- `RoomType.name` uses the stored values `individual`, `duplo`, `familiar`, as in the H4Y-26 draft (user decision, 2026-10-30). The English requirement terms are descriptions, not stored names.
- Card owners in the class diagram: `Reservation 0..1 -> 0..1 MagneticCard` and `User 0..1 -> * MagneticCard`. A card has a reservation or a staff user, never both. Check that the database has the same cardinality.

## Requirements sources

- `docs/requirements/actors.md`
- `docs/requirements/requirements.md`
- `docs/requirements/use-cases.md`
- `docs/requirements/questions.md`

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: Claude Code, claude-haiku-5-5, 2026-10-16; edited by Claude Code, claude-haiku-5-5, 2026-10-16; OpenAI Codex, GPT-6, 2026-10-16; class diagram link restored by OpenAI Codex, GPT-6, 2026-10-16; component diagram links by Cursor agent, Claude Opus 5.5, 2026-10-16; sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16. PR review corrections by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-16; PR-20 import decision review by sequence-diagram-editor, OpenAI Codex (GPT-6), 2026-10-22; use-case link text by use-case-diagram-editor, Claude Code (claude-opus-5-5), 2026-10-23. H4Y-26 base price by doc-updater, Claude Code (claude-haiku-5-5), 2026-10-30. Reviewed by: pending.
