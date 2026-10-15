# Laboratory III Project (LP3) - Course Statement

Source: "Enunciado LP3", ISEP, 2026/2027, dated 2026-10-02. Translated from Portuguese into Simplified Technical English. All rules, numbers and dates are kept.

## 1. Objective and context

The class builds one single software project during the Laboratory III course. The team uses the Scrum method.

Each team has 5/4 members. The team works in sprints of one to two weeks. The team has:
- 1 Team Leader. The Team Leader is the Scrum Master and the Product Owner.
- 4/3 development members.

During the semester, the responsibilities must change in each sprint.

The problem comes in iterations. The application architecture must support increments to the required functions.

## 2. Problem description

Hotel4You is a company. It needs a software development team. The team must computerize the company processes in its main business area: lodging for clients.

The Hotel4You building has 5 floors:
- Floor -1: garage and storeroom.
- Floor 0: reception, bar and conference rooms.
- Floor 1: restaurant and swimming pool.
- Floors 2 and 3: sleeping rooms.

Each of floors 2 and 3 has 10 rooms. The statement says: 7 single rooms, 10 double rooms and 3 family rooms (3 or 4 people). The price of a room per night must change by room type (single, double, family) and by extra room services.

Clients use all the services that Hotel4You offers. A client can choose any room type, if the room is free, for one or more nights. In each room, the client has the right to: towels, shower conditioner, shampoo, hair dryer, television, air conditioning, bed, sofa, sheets, safe, and so on. The manager can configure all of these.

These items add to the base price of a room:
- rooms with special views (sea view or mountain view);
- drink consumption;
- laundry.

Hotel4You also offers other services: food, cultural events, access to the private pools, spa and conference rooms. Each of these services can have an extra cost for the client.

Reception staff support the clients in everything they need. Daily tasks include:
- client registration;
- making and cancelling client reservations;
- doing check-ins and check-outs at the hotel;
- help with room services;
- other tasks.

After check-in, the client gets access to the room with a magnetic card. The card has an associated number. The client can also use the card in some places of the hotel (restaurant and pool), if these places are part of the reservation. The system must save this record. The record shows the place and the entry time.

Hotel managers must make sure the hotel works well. They manage the staff. They also receive suggestions or complaints from clients.

Only managers can delete reservations. They can do it only if the client has not done check-in. Managers can also open all client rooms with the master magnetic card. They can enter all zones of the hotel. Staff can access only the services of floors 0 and 1.

The manager also gets a list of records:
- the places of one given card;
- the card passes of one room.

Examples:
- Card 12345 entered the pool at 9:00, room 202 at 9:30, the restaurant at 9:45, and so on.
- In room 301, card 12345 entered at 10:00 and the master card entered at 12:30.

An external company provides the restaurant service. Each employee of that company can access only the kitchen and restaurant of the hotel. Nothing else.

The suppliers of Hotel4You give all the support that clients need for a good stay. Besides catering, some suppliers provide hygiene material, beds, sofas, sheets and the other room material.

When a supplier delivers goods, the supplier sends an XML file (example provided) with the material that enters the Hotel4You stock.

Hotel staff or managers must record all stock management in the system: entry, use and exit (where applicable).

All users must access the application with a login (user name and password).

For the first phase of the project, the IT team must build an application in Java, with all the constraints included. The team must use the SQL Server database management system.

## 3. Non-functional requirements

a. Users and roles
- The system must save all users. It must be possible to access any user at any time.

b. Security
- Critical data (passwords) must be saved encrypted.

c. Other requirements
- The registration process must be easy and fast (maximum 30 seconds).
- The system must be simple and intuitive, for all actors in their diversity.
- To maximize interoperability with other systems, the software must be written in JAVA.
- The class structure must be designed to support maintenance and increments in a simple way.
- A GUI in JavaFX is recommended. The business logic and the GUI logic must be clearly separated.
- The code must be documented with Javadoc.
- The implementation must include unit tests (JUnit) for the main functions of the application.
- The team must justify every external library it uses.

## 4. Development process

The project lasts 16 weeks. These weeks represent 13 sprints.

In the first sprint, the goal is to read and interpret the statement. The team also writes the Requirements Engineering documents for the 2nd sprint. In this first week, the team gives no responsibilities to members.

Theory-practice classes present concepts that support the project. Tutorial/practical classes are for doing and documenting the Scrum ceremonies:
- Sprint Review
  - Show the finished work to the teacher: DEMO.
- Sprint Retrospective
  - Keep doing.
  - Stop doing.
  - Start doing.
- Grooming / Backlog Refinement
- Sprint Planning
- Daily Scrum

Team Leader responsibilities:
- Update and validate software documents (JavaDocs, UML and Scrum).
- Create the User Stories in the backlog.
- Test and validate the tasks that the team delivers.
- Validate the tasks the team developed, based on acceptance tests.
- Make processes and ceremonies agile.
- Make sure the team works well.

Development team responsibilities:
- Develop and test the stories of the sprint.
- Update the diagrams.

At the start of each practical class, each team must do its Daily Scrum.

Code control must use Git flow, with these naming rules:
- Branch name: (feat/fix)-(task_number)-description
  - Example: feat-004-create-database
- Commits must have clear and short descriptions.
  - Descriptions like "fix task X" or "general fix" are not tolerated.

Groups get a penalty for incorrect and inappropriate use of the repository.

## 5. Deliveries

This section gives all information about the delivery of the first phase and the final phase. Teams must submit the deliveries on Moodle by these deadlines:
- 1st delivery: 2026-12-01 at 23:55.
- Final delivery: 2027-01-31 at 23:55.

Both deliveries are a zip file submitted on Moodle. The name format is LP3-GY.zip (Y = group number). The zip has these documents:
- Application executables.
- SCRUM documentation.
- Domain model.
- Class diagram.
- Use case diagram.
- Sequence diagrams (at least one general and one detailed).
- Documentation generated by Javadoc.
- Unit tests.
- Source code.

## 6. Evaluation

The evaluation has two phases. The 1st delivery is 35% of the final grade. The final delivery is 65%. Each delivery has these evaluation elements:

- SCRUM practices: 20%
  - Keep the backlog and sprint boards up to date.
  - Quality of the user stories (format, specification, scoring).
  - Quality of the documentation (Planning and Retrospective).
- Documentation: 20%
  - Domain model.
  - Class diagram.
  - Use case diagram.
  - Sequence diagrams.
- Source code: 25%
  - Architecture and code quality.
  - Documentation.
  - Correct use of Git.
  - Unit tests.
- Application operation: 35%
  - Compliance with functional and non-functional requirements.
  - User interface.

## 7. Code of conduct

If a team misses a deadline, the delivery gets a penalty or is not accepted. The rules are on Moodle.

Code of conduct: No one must claim authorship of work that is not their own.

Use of code (AI included) or engineering diagrams without a reference is forbidden.

If code is shared between groups, all groups involved get a penalty.

Cases of use of code without the author's cooperation are reported to the ISEP Presidency.

## Ambiguities to ask the teacher

1. Room count. Each floor (2 and 3) has 10 rooms, but 7 single + 10 double + 3 family = 20. Is it 20 rooms per floor, or 10 rooms per floor with other counts? Does the 7/10/3 split apply per floor or to the whole hotel?
2. Team size. The statement says "5/4" members, with "4/3" developers. The team agreed on 3 to 5 people. Is a team of 3 allowed? What does "5/4" mean (5 or 4)?
3. Sprint length. "One to two weeks". Which length is correct? The team chose 1 week.
4. Sprint count. 16 weeks are said to be 13 sprints. With 1-week sprints this does not match 16. What are the exact sprint dates? Which weeks are not sprints?
5. Reservation deletion. The text says only managers can delete reservations (only before check-in). Can reception staff only cancel? What is the difference between "cancel" (reception) and "delete" (manager)? Who is not allowed to delete: reception, clients, restaurant staff, suppliers?
6. Dates. Confirm the 1st delivery (2026-12-01 23:55) and the final delivery (2027-01-31 23:55). What must the 1st delivery contain as scope of "first phase"?
7. "Sprint 1 has no responsibility assignment". Does the Team Leader role start in sprint 2?
8. Staff access. "Staff only access floors 0 and 1". Does this apply to the reception staff only, or also to cleaning and other staff? Can staff enter guest rooms for room service?
9. Card rules. Is a card valid only for the reserved places (restaurant, pool)? Does each room entry need a log entry? Can a client have more than one card?
10. Stock. What does the XML file look like exactly (the example file is not in this text)? What happens when the XML has an unknown item?
11. Price rules. How do the extra services change the price (fixed or per unit)? Who sets the prices?
12. Registration time. Does the "30 seconds maximum" apply to client registration only, or also to the reservation?
13. Client access. Do clients log in to the application? The text says "all users" must log in.
14. External libraries. What is the approval process to justify a library?
15. AI use. What is the exact reference format for AI-generated code or diagrams?
16. Chore and docs branches. The branch rule is `(feat|fix)-NNN-description`. Which prefix do docs and chore work use? The team uses `feat-NNN-...` for them.
17. Sprint length. Confirm that 1-week sprints are accepted (the statement says one to two weeks).
18. Team Leader in sprint 1. Sprint 1 has no roles. Who holds the Team Leader role for the sprint 1 documents, and does the rotation start in sprint 2?
