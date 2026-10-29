# Use-case diagrams

Hotel4You has 5 actors and 53 use cases. The overview table shows which actor takes part in each functional area. Seven diagrams show each area in detail (area 1 has two diagrams). The [catalogue](use-case-catalog.md) gives the short description, the permission rules and the requirement links of each use case. The flows are in [../requirements/use-cases.md](../requirements/use-cases.md).

Review for H4Y-7 (teacher feedback, sprint 2): the old diagrams had 19 large use cases. The review splits them into the real user goals, adds the missing use cases, and adds the `«include»` and `«extend»` relations.

Sprint 2 retrospective fixes for H4Y-7 (sprint 3): UC-51 (included in UC-05), UC-52 (included in UC-06) and UC-53 (included in UC-50).

## Notation

| Element | UML meaning | Mermaid form |
|---------|-------------|--------------|
| Person symbol and name, outside the box | Actor | Node with no border |
| Rounded box (stadium) | Use case. Mermaid has no UML ellipse, so the stadium shape is used. | `UC01(["UC-01 Log in"])` |
| Large box with the title "Hotel4You" | System boundary | `subgraph` |
| Solid line, no arrow | Association: the actor takes part in the use case | `---` |
| Dashed arrow `«include»` | The base use case always runs the included use case | `-.->` from base to included |
| Dashed arrow `«extend»` | The extending use case adds optional behavior to the base use case | `-.->` from extending to base |

Reading rules:

- Primary actors (they start the use case) are on the left. Secondary actors (they take part, but do not use the application) are on the right.
- An included use case is linked to its actors through its base use case.
- Some diagrams have an invisible link (`~~~`). It only keeps a primary actor on the left or a secondary actor on the right. It has no UML meaning.
- The client does not log in (Q-13). The client uses the magnetic card, and asks reception for services. The supplier does not log in (Q-17). The supplier only sends the XML file.

## Overview

The table shows which actor takes part in each area. "P" = primary actor (starts the use case). "S" = secondary actor (takes part, does not use the application).

| Area (diagram) | Use cases | Receptionist | Manager | Restaurant staff | Client | Supplier |
|----------------|-----------|:---:|:---:|:---:|:---:|:---:|
| 1. Access and staff accounts | UC-01, UC-13, UC-17, UC-20 to UC-24, UC-45 | P | P | P | | |
| 2. Hotel structure and prices | UC-12, UC-25 to UC-32 | P | P | | | |
| 3. Clients and reservations | UC-02 to UC-05, UC-33 to UC-38, UC-51 | P | P | | | |
| 4. Stay and guest services | UC-06 to UC-08, UC-14, UC-39 to UC-41, UC-52 | P | P | | S | |
| 5. Magnetic cards | UC-09 to UC-11, UC-18, UC-42 to UC-44, UC-46 | | P | | P | |
| 6. Stock | UC-15, UC-16, UC-19, UC-47 to UC-50, UC-53 | P | P | | | S |

## 1. Access and staff accounts

### 1a. Session and place access

```mermaid
flowchart LR
  %% Requirements: RF-01, RF-02, RF-24, RF-25, RNF-02. Decisions: Q-08, Q-13, Q-17, Q-23.
  Receptionist["🧍<br/>Receptionist"]:::actor
  Manager["🧍<br/>Manager"]:::actor
  RestaurantStaff["🧍<br/>Restaurant staff"]:::actor

  subgraph Hotel4You["Hotel4You"]
    direction TB
    UC45(["UC-45 Access services of floors 0 and 1"]):::uc
    UC01(["UC-01 Log in"]):::uc
    UC21(["UC-21 Log out"]):::uc
    UC17(["UC-17 Access kitchen and restaurant"]):::uc
    UC20(["UC-20 Show the functions of the role"]):::uc
  end

  Receptionist --- UC45
  Receptionist --- UC01
  Manager --- UC01
  Manager --- UC21
  Receptionist --- UC21
  RestaurantStaff --- UC01
  RestaurantStaff --- UC21
  RestaurantStaff --- UC17

  UC01 -.->|"«include»"| UC20

  classDef actor fill:none,stroke:none,font-size:18px
  classDef uc fill:#ffffff,stroke:#333333,color:#000000
  style Hotel4You fill:none,stroke:#555555,stroke-width:2px
```

### 1b. Staff accounts

```mermaid
flowchart LR
  %% Requirements: RF-01, RF-26, RNF-01, RNF-02. Decisions: Q-18, Q-36, Q-39, Q-46.
  Manager["🧍<br/>Manager"]:::actor

  subgraph Hotel4You["Hotel4You"]
    direction TB
    UC13(["UC-13 Create staff account"]):::uc
    UC22(["UC-22 Edit staff account"]):::uc
    UC23(["UC-23 Remove staff account"]):::uc
    UC24(["UC-24 List users"]):::uc
  end

  Manager --- UC13
  Manager --- UC22
  Manager --- UC23
  Manager --- UC24

  classDef actor fill:none,stroke:none,font-size:18px
  classDef uc fill:#ffffff,stroke:#333333,color:#000000
  style Hotel4You fill:none,stroke:#555555,stroke-width:2px
```

## 2. Hotel structure and prices

```mermaid
flowchart LR
  %% Requirements: RF-03, RF-04, RF-06, RF-07, RF-08, RF-09. Decisions: Q-01, Q-11, Q-32, Q-36, Q-45.
  Manager["🧍<br/>Manager"]:::actor
  Receptionist["🧍<br/>Receptionist"]:::actor

  subgraph Hotel4You["Hotel4You"]
    direction TB
    UC25(["UC-25 Consult floors and zones"]):::uc
    UC26(["UC-26 Create zone"]):::uc
    UC27(["UC-27 Edit zone"]):::uc
    UC28(["UC-28 Remove zone"]):::uc
    UC29(["UC-29 Register rooms"]):::uc
    UC30(["UC-30 List rooms"]):::uc
    UC12(["UC-12 Configure room items"]):::uc
    UC31(["UC-31 Set base price per room type"]):::uc
    UC32(["UC-32 Set extra prices"]):::uc
  end

  Manager --- UC25
  Manager --- UC26
  Manager --- UC27
  Manager --- UC28
  Manager --- UC29
  Manager --- UC12
  Manager --- UC31
  Manager --- UC32
  Receptionist --- UC30

  classDef actor fill:none,stroke:none,font-size:18px
  classDef uc fill:#ffffff,stroke:#333333,color:#000000
  style Hotel4You fill:none,stroke:#555555,stroke-width:2px
```

## 3. Clients and reservations

```mermaid
flowchart LR
  %% Requirements: RF-05, RF-06, RF-07, RF-09, RF-10, RF-11, RF-12, RF-18, RF-22, RNF-03. Decisions: Q-05, Q-12, Q-20, Q-29, Q-37, Q-40, Q-42.
  Receptionist["🧍<br/>Receptionist"]:::actor
  Manager["🧍<br/>Manager"]:::actor

  subgraph Hotel4You["Hotel4You"]
    direction TB
    UC02(["UC-02 Register client"]):::uc
    UC33(["UC-33 Search client"]):::uc
    UC03(["UC-03 Make reservation"]):::uc
    UC34(["UC-34 Query free rooms"]):::uc
    UC35(["UC-35 Calculate reservation price"]):::uc
    UC36(["UC-36 Add room extras to reservation"]):::uc
    UC37(["UC-37 Add paid services to reservation"]):::uc
    UC38(["UC-38 Include restaurant and pool in reservation"]):::uc
    UC04(["UC-04 Cancel reservation"]):::uc
    UC05(["UC-05 Delete reservation"]):::uc
    UC51(["UC-51 Check reservation has check-in"]):::uc
  end

  Receptionist --- UC02
  Receptionist --- UC03
  Receptionist --- UC36
  Receptionist --- UC37
  Receptionist --- UC38
  Receptionist --- UC04
  Manager --- UC05
  Manager ~~~ UC04

  UC03 -.->|"«include»"| UC33
  UC03 -.->|"«include»"| UC34
  UC03 -.->|"«include»"| UC35
  UC02 -.->|"«extend»<br/>client not registered"| UC03
  UC36 -.->|"«extend»"| UC03
  UC37 -.->|"«extend»"| UC03
  UC38 -.->|"«extend»"| UC03
  UC05 -.->|"«include»"| UC51

  classDef actor fill:none,stroke:none,font-size:18px
  classDef uc fill:#ffffff,stroke:#333333,color:#000000
  style Hotel4You fill:none,stroke:#555555,stroke-width:2px
```

UC-36, UC-37 and UC-38 extend UC-03 when the receptionist creates the reservation. The receptionist can also start them alone, to change an existing reservation until check-in (Q-40). For this reason they also have a direct association.

UC-05 always includes UC-51. The system checks if the reservation has a check-in. If it has a check-in, the system refuses the delete (RF-22).

## 4. Stay and guest services

```mermaid
flowchart LR
  %% Requirements: RF-07, RF-09, RF-13, RF-14, RF-15, RF-16, RF-27. Decisions: Q-08, Q-09, Q-11, Q-18, Q-30, Q-33.
  Receptionist["🧍<br/>Receptionist"]:::actor
  Manager["🧍<br/>Manager"]:::actor

  subgraph Hotel4You["Hotel4You"]
    direction TB
    UC06(["UC-06 Check-in"]):::uc
    UC52(["UC-52 Check client has reservation"]):::uc
    UC39(["UC-39 Issue guest card"]):::uc
    UC07(["UC-07 Check-out"]):::uc
    UC40(["UC-40 Deactivate guest card"]):::uc
    UC08(["UC-08 Record room service request"]):::uc
    UC14(["UC-14 Record suggestion or complaint"]):::uc
    UC41(["UC-41 Consult suggestions and complaints"]):::uc
  end

  Client["🧍<br/>Client"]:::actor

  Receptionist --- UC06
  Receptionist --- UC07
  Receptionist --- UC08
  Receptionist --- UC14
  Manager --- UC41
  Manager ~~~ UC06
  UC08 --- Client
  UC14 --- Client
  UC40 ~~~ Client

  UC06 -.->|"«include»"| UC52
  UC06 -.->|"«include»"| UC39
  UC07 -.->|"«include»"| UC40

  classDef actor fill:none,stroke:none,font-size:18px
  classDef uc fill:#ffffff,stroke:#333333,color:#000000
  style Hotel4You fill:none,stroke:#555555,stroke-width:2px
```

The client is a secondary actor in UC-08 and UC-14. The client asks reception. The receptionist records the request in the application (Q-18, Q-33).

UC-06 always includes UC-52. The system checks if the client has a reservation. Without a reservation, the system refuses the check-in (RF-13).

## 5. Magnetic cards

```mermaid
flowchart LR
  %% Requirements: RF-17, RF-18, RF-19, RF-20, RF-21, RF-23, RF-30. Decisions: Q-09, Q-19, Q-20, Q-26, Q-31, Q-32, Q-44, Q-45.
  Client["🧍<br/>Client"]:::actor
  Manager["🧍<br/>Manager"]:::actor

  subgraph Hotel4You["Hotel4You"]
    direction TB
    UC09(["UC-09 Open room with guest card"]):::uc
    UC42(["UC-42 Open restaurant or pool with guest card"]):::uc
    UC43(["UC-43 Open any room or zone with master card"]):::uc
    UC44(["UC-44 Record card pass"]):::uc
    UC10(["UC-10 List places of one card"]):::uc
    UC11(["UC-11 List card passes of a room"]):::uc
    UC18(["UC-18 Block lost master card"]):::uc
    UC46(["UC-46 Issue new master card"]):::uc
  end

  Client --- UC09
  Client --- UC42
  Manager --- UC43
  Manager --- UC10
  Manager --- UC11
  Manager --- UC18
  Manager --- UC46

  UC09 -.->|"«include»"| UC44
  UC42 -.->|"«include»"| UC44
  UC43 -.->|"«include»"| UC44

  classDef actor fill:none,stroke:none,font-size:18px
  classDef uc fill:#ffffff,stroke:#333333,color:#000000
  style Hotel4You fill:none,stroke:#555555,stroke-width:2px
```

UC-46 has a precondition: the old master card is blocked first with UC-18 (Q-31). A manager created later also gets a master card with UC-46 (Q-44).

## 6. Stock

```mermaid
flowchart LR
  %% Requirements: RF-28, RF-29. Decisions: Q-10, Q-17, Q-21, Q-34, Q-38, PR #20 decision (2026-10-21).
  Manager["🧍<br/>Manager"]:::actor
  Receptionist["🧍<br/>Receptionist"]:::actor

  subgraph Hotel4You["Hotel4You"]
    direction TB
    UC19(["UC-19 Create stock item"]):::uc
    UC48(["UC-48 Edit stock item"]):::uc
    UC15(["UC-15 Enter supplier delivery in stock"]):::uc
    UC47(["UC-47 Show import report"]):::uc
    UC50(["UC-50 Record stock entry"]):::uc
    UC53(["UC-53 Read supplier XML stock file"]):::uc
    UC16(["UC-16 Record stock use"]):::uc
    UC49(["UC-49 Record stock exit"]):::uc
  end

  Supplier["🧍<br/>Supplier"]:::actor

  Manager --- UC19
  Manager --- UC48
  Manager --- UC15
  Receptionist --- UC15
  Manager --- UC50
  Receptionist --- UC50
  Manager --- UC16
  Receptionist --- UC16
  Manager --- UC49
  Receptionist --- UC49
  UC15 --- Supplier
  UC47 ~~~ Supplier

  UC15 -.->|"«include»"| UC47
  UC50 -.->|"«include»"| UC53
  UC53 ~~~ Supplier

  classDef actor fill:none,stroke:none,font-size:18px
  classDef uc fill:#ffffff,stroke:#333333,color:#000000
  style Hotel4You fill:none,stroke:#555555,stroke-width:2px
```

The supplier is a secondary actor in UC-15. The supplier sends the XML file. The receptionist or the manager loads it (Q-17).

UC-50 always includes UC-53. The supplier XML stock file gives the stock entry (RF-28).

## Changes from the first version

| Old use case | New use cases |
|--------------|---------------|
| UC-01 Log in | UC-01 Log in, UC-20 Show the functions of the role (included), UC-21 Log out |
| UC-03 Make reservation | UC-03 Make reservation, with UC-33, UC-34, UC-35 (included) and UC-02, UC-36, UC-37, UC-38 (extend) |
| UC-06 Check-in | UC-06 Check-in, UC-39 Issue guest card (included) |
| UC-07 Check-out | UC-07 Check-out, UC-40 Deactivate guest card (included) |
| UC-08 Help with room services | UC-08 Record room service request |
| UC-09 Use magnetic card at a place | UC-09 Open room with guest card, UC-42, UC-43, UC-44 Record card pass (included) |
| UC-13 Manage staff | UC-13 Create staff account, UC-22 Edit, UC-23 Remove, UC-24 List users |
| UC-14 Receive suggestion or complaint | UC-14 Record suggestion or complaint, UC-41 Consult suggestions and complaints |
| UC-15 Enter supplier delivery in stock | UC-15, UC-47 Show import report (included) |
| UC-16 Record stock use and exit | UC-16 Record stock use, UC-49 Record stock exit, UC-50 Record stock entry |
| UC-17 Restricted access for external restaurant staff | UC-17 Access kitchen and restaurant, UC-45 Access services of floors 0 and 1 |
| UC-18 Block lost master card and issue a new one | UC-18 Block lost master card, UC-46 Issue new master card |
| UC-19 Manage stock catalogue | UC-19 Create stock item, UC-48 Edit stock item |
| (missing) | UC-25 to UC-32: floors, zones, rooms and prices |

UC-02, UC-04, UC-05, UC-10, UC-11 and UC-12 keep their name. The IDs UC-01 to UC-19 do not change, because the sequence diagrams and the requirements use them.

Created by: antonioprmartinho. Updated by: antonioprmartinho. AI use: OpenAI Codex, GPT-6, 2026-10-16; edited by OpenAI Codex, GPT-6, 2026-10-16; ellipse use-case and actor-symbol notation update by OpenAI Codex, GPT-6, 2026-10-16; H4Y-7 review (split and new use cases, include and extend relations, overview diagram) by use-case-diagram-editor, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-7 retrospective fixes (UC-51, UC-52, UC-53) by use-case-diagram-editor, Claude Code (claude-opus-5-5), 2026-10-29. Reviewed by: pending.
