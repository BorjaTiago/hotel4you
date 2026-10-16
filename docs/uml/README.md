# UML documentation

This folder holds the Hotel4You UML diagrams and supporting documents.

- View the Mermaid diagram on GitHub, or paste it at mermaid.live.
- [domain-model.md](domain-model.md): domain model with business concepts only.
- [guide.md](guide.md): guide to defend each class and key flows.
- [class-diagram.md](class-diagram.md): UML class diagram with types, visibility and multiplicities.
- [classes.md](classes.md): all classes by layer, with attributes, methods and requirements.
- [traceability.md](traceability.md): requirements to classes.
- [use-cases.md](use-cases.md): use-case diagrams for all 19 use cases.
- [use-case-catalog.md](use-case-catalog.md): short descriptions, actors, permissions and requirement links.
- Database: [../database/schema.md](../database/schema.md), [../database/erd.md](../database/erd.md).

## Differences from the database

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

AI use: Claude Code, claude-haiku-5-5, 2026-10-16; edited by Claude Code, claude-haiku-5-5, 2026-10-16; OpenAI Codex, GPT-6, 2026-10-16; class diagram link restored by OpenAI Codex, GPT-6, 2026-10-16. Reviewed by: pending.
