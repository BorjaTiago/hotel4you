# UML - Class Diagram

This folder documents the class diagram in [class-diagram.md](class-diagram.md).

- View the Mermaid diagram on GitHub, or paste it at mermaid.live.
- [domain-model.md](domain-model.md): domain model with business concepts only.
- [guide.md](guide.md): guide to defend each class and key flows.
- [classes.md](classes.md): all classes by layer, with attributes, methods and requirements.
- [traceability.md](traceability.md): requirements to classes.
- Database: [../database/schema.md](../database/schema.md), [../database/erd.md](../database/erd.md).

Differences from the database:
- `RoomType` class vs `room_types` table.
- `ReservationExtra` vs `reservation_services`.
- No `Stay` class. The `Reservation` holds the stay times.
- `Client` class vs `customers` table.
- `Role` enum vs `roles` table.
- `RoomType.name` is "single" in requirements (RF-04) and "individual" in schema.md (docs/database). Align one side.
- Card owners in the diagram: `Reservation 0..1 -> 0..1 MagneticCard` and `User 0..1 -> * MagneticCard`. A card has a reservation OR a staff user, never both (XOR). Check that the DB has the same cardinality.

AI use: Claude Code, claude-haiku-5-5, 2026-10-16; edited by Claude Code, claude-haiku-5-5, 2026-10-16. Reviewed by: pending.
