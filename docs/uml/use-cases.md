# Use-case diagrams

The diagrams show the actors and their associations with the 19 use cases in the requirements. Permission details and short descriptions are in the [catalogue](use-case-catalog.md).

## Reservations and guest services

```mermaid
usecase-beta
actor Client[Client]
actor Receptionist[Receptionist]
actor Manager[Manager]
actor RestaurantStaff["Restaurant staff"]

systemBoundary Hotel4You[Hotel4You] {
  UC01("UC-01 Log in")
  UC02("UC-02 Register client")
  UC03("UC-03 Make reservation")
  UC04("UC-04 Cancel reservation")
  UC05("UC-05 Delete reservation")
  UC06("UC-06 Check in")
  UC07("UC-07 Check out")
  UC08("UC-08 Help with room services")
  UC14("UC-14 Receive suggestion or complaint")
}

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
usecase-beta
actor Client[Client]
actor Manager[Manager]
actor RestaurantStaff["Restaurant staff"]

systemBoundary Hotel4You[Hotel4You] {
  UC09("UC-09 Use magnetic card at a place")
  UC10("UC-10 List places of one card")
  UC11("UC-11 List passes of one room")
  UC17("UC-17 Restrict restaurant staff access")
  UC18("UC-18 Block lost master card and issue new one")
}

Client --> UC09
Manager --> UC09
Manager --> UC10
Manager --> UC11
RestaurantStaff --> UC17
Manager --> UC18
```

## Room configuration and stock

```mermaid
usecase-beta
actor Receptionist[Receptionist]
actor Manager[Manager]
actor Supplier[Supplier]

systemBoundary Hotel4You[Hotel4You] {
  UC12("UC-12 Configure room items")
  UC13("UC-13 Manage staff")
  UC15("UC-15 Enter supplier delivery in stock")
  UC16("UC-16 Record stock use and exit")
  UC19("UC-19 Manage stock catalogue")
}

Manager --> UC12
Manager --> UC13
Supplier --> UC15
Receptionist --> UC15
Manager --> UC15
Receptionist --> UC16
Manager --> UC16
Manager --> UC19
```

AI use: OpenAI Codex, GPT-6, 2026-10-16. Reviewed by: pending.
