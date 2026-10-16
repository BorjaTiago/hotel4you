---
type: epic
summary: Technical foundation - Java, JavaFX, SQL Server, layered architecture
parent:
requirements: [RNF-04, RNF-05, RNF-06, RNF-07, RNF-08, RNF-12]
points:
points_suggested:
status: published
jira_key: H4Y-11
labels: [foundation, 1st-delivery]
sprint:
---

# Epic: Foundation and architecture

## Goal
Build the technical base. Every other epic adds features on top of it in small increments.

## Delivery
1st delivery (needed by login, rooms, reservations, check-in/out). Sprint placement: backlog prioritizer.

## Scope
- Java project, build, and package layout.
- SQL Server database and connection layer.
- JavaFX shell (main window, navigation by role).
- Separation of business logic and GUI logic.
- Extensible class structure.
- GUI usability checks (simple and intuitive).

## Out of scope
- Feature screens (they belong to the feature epics).

## Actors
- Developer (team). End users: all actors who use the GUI.

## Value
Features can grow sprint by sprint without rework. The 1st delivery runs on SQL Server with a JavaFX GUI.

## Statement links
Sections 1, 2, 3c. RNF-04, RNF-05, RNF-06, RNF-07, RNF-08, RNF-12. Q-22.

## Candidate stories
- Criar projeto Java com camadas de negócio e GUI separadas (actor: developer; RNF-05, RNF-07, RNF-08)
- Configurar ligação à base de dados SQL Server e esquema inicial (actor: developer; RNF-12)
- Criar a janela principal JavaFX com navegação (actor: receptionist; RNF-06)
- (Usabilidade RNF-04: tarefa técnica T3, ver abaixo)

## Open questions
- None new. The "simple and intuitive" test method is a team choice (RNF-04).

## Tarefas técnicas (candidatas a sub-tarefas)
Decisão da equipa (2026-10-16): trabalho técnico sem ator de negócio é tarefa, não história. Fica ligado a esta épica. História mantida: janela principal. O teste de usabilidade (T3) é tarefa técnica.

### T0: Esqueleto do projeto e classes base (RNF-05, RNF-07, RNF-08, RNF-12)
Primeira tarefa do sprint 2. Vem antes de todas as histórias e antes da tarefa de dados iniciais (epic-authentication-users T2). Objetivo: os 5 programadores trabalham em paralelo sem conflitos. Simples: projeto universitário, sem frameworks extra.

1. Estrutura do projeto Java (uma camada por pacote; a GUI só chama serviços):
   - `domain`: classes de domínio e enums, sem SQL nem JavaFX.
   - `service`: interfaces de serviço por área e as suas implementações (regras de negócio).
   - `data`: interfaces de repositório, implementação JDBC para SQL Server e implementação em memória para testes.
   - `gui`: JavaFX (ecrãs, controladores, registo de navegação).
   - `config`: leitura da configuração (ligação à base de dados, conta inicial). Valores fora do Git.
2. Classes de domínio essenciais (fonte: `docs/requirements` e o esquema H4Y-10 do PR #4, branch `chore/db-schema-update`):
   - `Role` (enum de 5 tipos, RF-02: CLIENT, RECEPTIONIST, MANAGER, RESTAURANT_STAFF, SUPPLIER; só os 3 do pessoal têm conta, Q-13, Q-17), `User`, `Session`.
   - `Floor`, `Location` (zona: nome, piso, marca "exige reserva", Q-32), `RoomType`, `Room`, `RoomItem`.
   - `Customer`, `Reservation` (estado: RESERVED, CANCELLED, CHECKED_IN, CHECKED_OUT), locais incluídos na reserva.
   - `AdditionalService` (extras e serviços pagos, vista incluída, Q-37), `ReservationService` (extra ou pedido de serviço na reserva, preço copiado).
   - `AccessCard` (cliente ou mestre, ativo, bloqueado), `AccessLog`.
   - `Item`, `StockMovement` (IN, USE, OUT com motivo, Q-34), `StockImport`.
   - `Feedback` (sugestão ou reclamação).
3. Serviços essenciais por área (só interfaces e assinaturas nesta tarefa; cada história implementa a sua parte):
   - Autenticação: `AuthenticationService` (login, logout), `PasswordHasher` (RNF-02), `PermissionPolicy` (papel -> funções, RF-02), `UserService` (contas, RF-26, RNF-01).
   - Edifício e quartos: `BuildingService` (pisos, zonas), `RoomService` (quartos, tipos, preço base, itens do quarto).
   - Clientes e reservas: `CustomerService`, `RoomAvailability` (quartos livres e sobreposição de datas, uma só implementação), `ReservationService` (criar, cancelar, apagar, check-in, check-out, locais).
   - Preços: `PricingService` (preços extra, total), `ReservationExtrasService` (extras e serviços na reserva).
   - Cartões e acesso: `CardService` (emitir, desativar, bloquear, novo mestre), `CardNumberGenerator`, `AccessService.enter(cartão, local)` que percorre uma lista de `AccessRule` e chama `AccessLogger` quando a entrada é permitida. Cada história de acesso acrescenta uma regra numa classe própria.
   - Stock: `StockService` (catálogo, entrada, uso, saída), `XmlStockImporter`, `ImportReportService`.
   - Apoio ao cliente: `GuestSupportService` (pedidos de serviço de quarto, sugestões e reclamações).
   - Dados: um repositório por agregado (`UserRepository`, `RoomRepository`, `ReservationRepository`, `CardRepository`, `AccessLogRepository`, `StockRepository`, ...), com versão JDBC e versão em memória.
4. Diagrama de classes inicial curto (domínio, serviços e repositórios), em `docs/` com a referência de uso de IA (RNF-25). Ordem de criação: (a) enums e classes de domínio; (b) interfaces de repositório e versões em memória; (c) interfaces de serviço; (d) scripts SQL do esquema H4Y-10 (PR #4) e ligação JDBC (T2); (e) janela JavaFX vazia com registo de navegação e `Session` simulada. Depois desta ordem, cada história trabalha nos seus ficheiros.

- [ ] O projeto compila e arranca com os pacotes `domain`, `service`, `data`, `gui`, `config`.
- [ ] As classes de domínio e as interfaces de serviço e de repositório acima existem, com Javadoc curto (RNF-09).
- [ ] Um teste JUnit usa um repositório em memória e um serviço sem abrir a interface.
- [ ] O diagrama de classes inicial e a ordem de criação estão em `docs/` e a equipa revê-os na planning do sprint 2.
- [ ] Nenhuma credencial fica em ficheiros versionados.

### T1: Separar lógica de negócio e lógica da interface (RNF-05, RNF-07, RNF-08)
- [ ] A aplicação em Java arranca sem erros.
- [ ] Uma regra de negócio executa por teste automático sem abrir a interface.
- [ ] Uma nova função não quebra os testes das funções existentes.
- [ ] Quando uma regra recusa uma operação, a interface mostra o motivo e não decide a regra.

### T2: Guardar os dados em SQL Server (RNF-12)
- [ ] Com o SQL Server disponível, a aplicação liga-se e fica pronta a guardar e ler.
- [ ] Um registo guardado continua disponível após fechar e abrir a aplicação.
- [ ] Com o SQL Server indisponível, a aplicação mostra mensagem clara e não mostra dados internos da ligação.
- [ ] Com a base vazia, a estrutura necessária fica criada na primeira preparação.
- [ ] Credenciais da base de dados nunca ficam em ficheiros versionados.
- [ ] Os scripts de criação usam o esquema H4Y-10 do PR #4 (branch `chore/db-schema-update`), não a versão antiga em `main` (o PR #4 remove `users.active`, `reservations.price_per_night`, `rooms.view_*` e acrescenta `reservation_locations`, `stock_imports`, `locations.requires_reservation`, `locations.guest_card_access` (Q-45), `access_cards.blocked_at`, `items.unit`).

### T3: Rever a usabilidade da interface com teste simples (RNF-04)
- [ ] Com uma tarefa principal da receção já implementada, um utilizador de teste executa-a sem ajuda e o resultado fica registado.
- [ ] Se o utilizador não conclui a tarefa sem ajuda, a dificuldade fica registada e fica criada uma melhoria no backlog.
- [ ] Depois de uma melhoria aplicada, o teste repete-se com a mesma tarefa e o resultado novo fica registado ao lado do anterior.
- [ ] Nota: método do teste e critério de "concluir" são escolha da equipa. Só depois de existirem ecrãs de funções. Outros atores terão testes próprios.

### T4: Dados de teste por história (sprint 2, depois cresce em cada história)
Tira as dependências entre histórias: cada história testa-se sozinha com os seus dados.
- [ ] Existe um mecanismo simples para carregar dados de teste nos repositórios em memória e numa base de teste SQL Server.
- [ ] Cada história acrescenta os seus dados num ficheiro próprio (sem editar o ficheiro de outra história), por exemplo clientes, quartos, reservas em cada estado, cartões, passagens, itens, importações.
- [ ] Os dados de teste nunca correm na base de produção e não têm palavras-passe reais.

### T5: Dados de referência do enunciado (início do sprint 3)
Dados fixos do enunciado que nenhuma história de ator cria.
- [ ] Pisos -1, 0, 1, 2, 3 e os locais do enunciado: garagem e arrecadação (-1), receção, bar e salas de conferência (0), restaurante e piscina (1) (RF-03). Restaurante e piscina com a marca "exige reserva" (RF-18, Q-32).
- [ ] Tipos de quarto individual, duplo e familiar (RF-04), com preço base vazio até o gestor o definir (story-set-base-price).
- [ ] Lista de extras e serviços pagos sem preço: vista mar, vista montanha, bebidas, lavandaria, comida, eventos culturais, piscinas privadas, spa, salas de conferência (RF-07, RF-09, Q-33, Q-37).
- [ ] O script corre duas vezes sem duplicar dados.
- [ ] Os quartos não fazem parte destes dados: o gestor regista-os (story-register-rooms, RF-04).

### T6: Leitor de cartão simulado e números de cartão (sprint 8, antes das histórias de cartão)
- [ ] Um ecrã simples recebe um número de cartão e um local (quarto ou zona) e chama `AccessService.enter` (T0). Mostra "entrada permitida" ou "entrada recusada".
- [ ] `CardNumberGenerator` dá sempre um número novo e único (RF-16, Q-26).
- [ ] O gestor inicial (epic-authentication-users T2) recebe um cartão mestre ativo (Q-19: um cartão mestre por gestor). Ver pergunta nova no backlog sobre gestores criados depois.

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; tasks T0, T4, T5, T6 and T2 schema note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16. Reviewed by: pending.
