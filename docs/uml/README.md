# UML documentation

This folder holds the Hotel4You UML diagrams and supporting documents.

## Diagrams and catalogue

- [Class diagram](class-diagram.md): Mermaid class diagram for the proposed design.
- [Use-case diagrams](use-cases.md): Mermaid UML use-case diagrams for all 19 use cases, grouped by workflow.
- [Use-case catalogue](use-case-catalog.md): actors, descriptions, permissions, and requirement references for all 19 use cases.

The use-case diagrams use Mermaid's `usecase-beta` syntax and need Mermaid 12 or later. GitHub's renderer may use a different Mermaid version. If a diagram does not render there, paste its Mermaid block into the [Mermaid Live Editor](https://mermaid.live/).

## Class model documents

- [guide.md](guide.md): explanation of classes and key flows.
- [classes.md](classes.md): classes by layer, with attributes, methods, and requirements.
- [traceability.md](traceability.md): requirements mapped to classes.
- Database model: [schema](../database/schema.md) and [ER diagram](../database/erd.md).

## Class model notes

- `RoomType` class vs `room_types` table.
- `ReservationExtra` vs `reservation_services`.
- No `Stay` class. The `Reservation` holds the stay times.
- `Client` class vs `customers` table.
- `Role` enum vs `roles` table.
- `RoomType.name` is "single" in requirements (RF-04) and "individual" in the database schema. Align one side.
- Card owners in the class diagram: `Reservation 0..1 -> 0..1 MagneticCard` and `User 0..1 -> * MagneticCard`. A card has a reservation or a staff user, never both. Check that the database has the same cardinality.

## Requirements sources

- `docs/requirements/actors.md`
- `docs/requirements/requirements.md`
- `docs/requirements/use-cases.md`
- `docs/requirements/questions.md`

AI use: Claude Code, claude-haiku-5-5, 2026-10-16; edited by Claude Code, claude-haiku-5-5, 2026-10-16; OpenAI Codex, GPT-6, 2026-10-16. Reviewed by: pending.
