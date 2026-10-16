# H4Y-5 - Class Diagram

This folder documents the class diagram in [uml/codeUml.md](uml/codeUml.md).

- View the Mermaid diagram on GitHub, or paste it at mermaid.live.
- [guide.md](guide.md): guide to defend each class and key flows.
- [classes.md](classes.md): all classes by layer, with attributes, methods and requirements.
- [traceability.md](traceability.md): requirements to classes.
- Database: [../H4Y-10/schema.md](../H4Y-10/schema.md), [../H4Y-10/erd.md](../H4Y-10/erd.md).

Differences from the database:
- `RoomType` class vs `room_types` table.
- `ReservationExtra` vs `reservation_services`.
- No `Stay` class. The `Reservation` holds the stay times.
- `Client` class vs `customers` table.
- `Role` enum vs `roles` table.
- `RoomType.name` is "single" in requirements (RF-04) and "individual" in schema.md (H4Y-10). Align one side.
- `Reservation 1 -> 0..1 MagneticCard` in the diagram. Check that the DB has the same cardinality.

AI use: Claude Code, claude-haiku-5-5, 2026-10-16; edited by Claude Code, claude-haiku-5-5, 2026-10-16. Reviewed by: pending.
