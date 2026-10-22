# Project Context - H4Y (Hotel4You)

Short facts sheet. Full source: `enunciado.md`. Never add a requirement that is not in the statement.

## Actors
- Client: stays in the hotel. Uses rooms and services. Has a magnetic card after check-in.
- Receptionist (staff): registers clients, makes and cancels reservations, does check-in and check-out, helps with room services. Access only to floors 0 and 1 services.
- Manager: runs the hotel, manages staff, receives suggestions and complaints, configures room items, deletes reservations (only before check-in), uses the master card (all rooms, all zones), sees card and room logs.
- Restaurant staff: works for an external company. Access only to the kitchen and restaurant.
- Supplier: delivers goods (food, hygiene material, beds, sofas, sheets, room material). Sends an XML stock file with each delivery.

## Key business rules
- Building: floor -1 garage and storeroom; 0 reception, bar, conference rooms; 1 restaurant and pool; 2 and 3 rooms.
- Room types: single, double, family (3 or 4 people). Price per night depends on type and extra services.
- Extra cost: special view (sea or mountain), drinks, laundry. Other services (food, cultural events, private pools, spa, conference rooms) can cost extra.
- Room items (towels, shampoo, TV, safe, and so on) are configurable by the manager.
- After check-in the client gets a magnetic card with a number. The card opens the room. It also opens the restaurant and pool if they are in the reservation.
- The system saves each card entry: place and time.
- The manager sees: places of one card; card passes of one room (master card included).
- Only managers delete reservations, and only before check-in.
- Stock: staff or managers record entry, use and exit. Supplier XML file gives the entry.
- All users log in with user name and password.

## Non-functional requirements
- Passwords saved encrypted.
- All users saved and accessible at any time.
- Registration process: 30 seconds maximum. Simple and intuitive.
- Java. GUI in JavaFX recommended. Business logic separate from GUI logic.
- Class structure easy to maintain and extend.
- Javadoc on the code. JUnit tests on the main functions.
- Every external library must be justified.
- Database: SQL Server.

## Scrum rules
- 16 weeks, 13 sprints. Sprint 1 = read the statement and write the Requirements Engineering documents. No roles in sprint 1.
- Sprint weeks: 2026-10-12 to 2027-01-31. No sprint in the weeks of 2026-12-21, 2026-12-28, 2027-01-04 (Christmas and New Year, Q-04, team decision, user, 2026-10-16). Sprint 13 ends on the final delivery day.
- Team Leader = Scrum Master + Product Owner. The role rotates every sprint.
- Team Leader: updates and validates docs (JavaDoc, UML, Scrum), creates user stories, tests and validates tasks against acceptance tests, runs the ceremonies.
- Developers: develop and test stories, update diagrams.
- Ceremonies: Sprint Review (DEMO to the teacher), Sprint Retrospective (keep / stop / start), Backlog Refinement (grooming), Sprint Planning, Daily Scrum (at the start of each practical class).
- Git flow. Branch: `(feat|fix|chore)/H4Y-N-description`. Clear, short commits.
- Team decision (user, 2026-10-16): the branch rule above differs from `enunciado.md`, which uses `(feat|fix)-NNN-description`. `enunciado.md` is the source of truth. The team follows the team rule.

## Grading
- 1st delivery 35%, final delivery 65%.
- Each delivery: Scrum practices 20%, Documentation 20%, Source code 25%, Application operation 35%.

## Deliveries
- 1st: 2026-12-01 23:55. Final: 2027-01-31 23:55. Zip `LP3-GY.zip` on Moodle.
- Content: executables, Scrum docs, domain model, class diagram, use case diagram, sequence diagrams (at least one general and one detailed), Javadoc output, unit tests, source code.

## AI-use rule
- Code or engineering diagrams from AI without a reference are forbidden (code of conduct).
- Team rule (teacher agreed, 2026-10-16): reference AI use only in documentation files. Never in source code.
- Agents must tell the team to reference every AI use (tool, prompt, where used) in the doc footer.

## Tech
Java, JavaFX, SQL Server, JUnit, Javadoc, XML stock file.

## Team decisions
- Jira key: H4Y. Hierarchy: Epic > Story > Subtask (Jira issue type name).
- Sprint length: 1 week. Team size: 5 members, all developers (Q-02, team decision, user, 2026-10-16). The Team Leader also develops.
- Estimation: Fibonacci story points (1, 2, 3, 5, 8, 13). Stories above 8 are split.
- Stories in Portuguese. Format: "Como <papel>, Eu quero <ação>, Para que <benefício>".
- Code and docs in English, Simplified Technical English.
- Agents write local drafts in `.agent/drafts/`. A human approves before anything goes to Jira.
- Points stay empty in a draft until the team decides. Sprint capacity is committed in points.
- Team roster and Team Leader rotation: `team.md`.

## Document paths
- Requirements: `docs/requirements/{requirements,actors,use-cases,glossary,questions}.md`.
- Requirement IDs: RF-NN (functional), RNF-NN (non-functional). Each has a source section of the statement.
- Scrum documents (graded): `docs/scrum/sprint-NN/{planning,review,retrospective,impediments}.md`.
- Drafts: `.agent/drafts/<type>-<slug>.md`. Examples: `.agent/examples/`.
- Acceptance scenarios are numbered AC-1, AC-2 inside the story. Each test has `@Tag("H4Y-NN-AC-n")`.

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: docs-updater, Claude Code (claude-sonnet-5-5), 2026-10-16; doc-editor, Claude Code (claude-haiku-5-5), 2026-10-16; sprint weeks note (Q-04); branch rule (2026-10-16); doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17. Reviewed by: pending.
