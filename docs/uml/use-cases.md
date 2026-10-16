# Use-case diagrams

The diagrams show every project actor and all 19 use cases from the requirements. Each use case links to at least one actor. The [catalogue](use-case-catalog.md) gives the short description, permission rules, and requirement references.

## Reservations and guest services

```mermaid
flowchart LR
  %% Requirements: RF-01, RF-02, RF-05, RF-06, RF-07, RF-09, RF-10, RF-11, RF-12, RF-13, RF-14, RF-15, RF-16, RF-17, RF-18, RF-22, RF-27, RNF-02, RNF-03.
  Client([Client])
  Receptionist([Receptionist])
  Manager([Manager])
  RestaurantStaff([Restaurant staff])

  subgraph Hotel4You[Hotel4You]
    UC01[UC-01 Log in]
    UC02[UC-02 Register client]
    UC03[UC-03 Make reservation]
    UC04[UC-04 Cancel reservation]
    UC05[UC-05 Delete reservation]
    UC06[UC-06 Check-in]
    UC07[UC-07 Check-out]
    UC08[UC-08 Help with room services]
    UC14[UC-14 Receive suggestion or complaint]
  end

  Receptionist --> UC01
  Manager --> UC01
  RestaurantStaff --> UC01
  Receptionist --> UC02
  Receptionist --> UC03
  Receptionist --> UC04
  Manager --> UC05
  Receptionist --> UC06
  Receptionist --> UC07
  Client --> UC08
  Receptionist --> UC08
  Client --> UC14
  Receptionist --> UC14
  Manager --> UC14
```

## Cards and restricted access

```mermaid
flowchart LR
  %% Requirements: RF-01, RF-17, RF-18, RF-19, RF-20, RF-21, RF-23, RF-24, RF-25, RF-30.
  Client([Client])
  Manager([Manager])
  RestaurantStaff([Restaurant staff])

  subgraph Hotel4You[Hotel4You]
    UC09[UC-09 Use magnetic card at a place]
    UC10[UC-10 List places of one card]
    UC11[UC-11 List card passes of a room]
    UC17[UC-17 Restricted access for external restaurant staff]
    UC18[UC-18 Block lost master card and issue a new one]
  end

  Client --> UC09
  Manager --> UC09
  Manager --> UC10
  Manager --> UC11
  RestaurantStaff --> UC17
  Manager --> UC18
```

## Room configuration and stock

```mermaid
flowchart LR
  %% Requirements: RF-01, RF-08, RF-24, RF-25, RF-26, RF-28, RF-29.
  Receptionist([Receptionist])
  Manager([Manager])
  Supplier([Supplier])

  subgraph Hotel4You[Hotel4You]
    UC12[UC-12 Configure room items]
    UC13[UC-13 Manage staff]
    UC15[UC-15 Enter supplier delivery in stock]
    UC16[UC-16 Record stock use and exit]
    UC19[UC-19 Manage stock catalogue]
  end

  Manager --> UC12
  Manager --> UC13
  Supplier --> UC15
  Receptionist --> UC15
  Manager --> UC15
  Receptionist --> UC16
  Manager --> UC16
  Manager --> UC19
```

AI use: OpenAI Codex, GPT-6, 2026-10-16; edited by OpenAI Codex, GPT-6, 2026-10-16. Reviewed by: pending.
