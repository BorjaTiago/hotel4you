---
type: epic
summary: Technical foundation - Java, JavaFX, SQL Server, layered architecture
parent:
requirements: [RNF-04, RNF-05, RNF-06, RNF-07, RNF-08, RNF-12]
points:
points_suggested:
status: approved
jira_key:
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

### T3: Rever a usabilidade da interface com teste simples (RNF-04)
- [ ] Com uma tarefa principal da receção já implementada, um utilizador de teste executa-a sem ajuda e o resultado fica registado.
- [ ] Se o utilizador não conclui a tarefa sem ajuda, a dificuldade fica registada e fica criada uma melhoria no backlog.
- [ ] Depois de uma melhoria aplicada, o teste repete-se com a mesma tarefa e o resultado novo fica registado ao lado do anterior.
- [ ] Nota: método do teste e critério de "concluir" são escolha da equipa. Só depois de existirem ecrãs de funções. Outros atores terão testes próprios.

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
