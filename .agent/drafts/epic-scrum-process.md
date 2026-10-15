---
type: epic
summary: Scrum process, ceremonies and Team Leader rotation
parent:
requirements: [RNF-13, RNF-14, RNF-15, RNF-16, RNF-17, RNF-18, RNF-19, RNF-27]
points:
points_suggested:
status: approved
jira_key:
labels: [scrum, process, 1st-delivery, final-delivery]
sprint:
---

# Epic: Scrum process and ceremonies

## Goal
The team runs Scrum in 13 one-week sprints and documents every ceremony.

## Delivery
Both deliveries. Scrum documents are graded 20% in each (RNF-27).

## Scope
- Sprint 1 requirements; no roles in sprint 1 (Q-24).
- Planning, daily scrum, review (demo), retrospective, refinement per sprint.
- Team Leader rotation and duties (`team.md`).
- Documents in `docs/scrum/sprint-NN/`.

## Out of scope
- Product features.

## Actors
- Team Leader, developer.

## Value
Good Scrum score and a steady delivery pace.

## Statement links
Sections 1, 4, 6. RNF-13 to RNF-19, RNF-27. Q-03, Q-04, Q-07, Q-24.

## Candidate stories
- Planear cada sprint e registar o planeamento (actor: Team Leader; RNF-13, RNF-15)
- Registar o Daily Scrum de cada aula prática (actor: developer; RNF-16)
- Fazer e registar a Sprint Review com demo (actor: Team Leader; RNF-15)
- Fazer e registar a retrospetiva keep/stop/start (actor: Team Leader; RNF-15)
- Refinar o backlog (actor: Team Leader; RNF-15, RNF-18)
- Rodar o Team Leader em cada sprint (actor: Team Leader; RNF-17)
- Validar tarefas contra testes de aceitação (actor: Team Leader; RNF-18)
- Desenvolver, testar e atualizar diagramas das histórias (actor: developer; RNF-19)

## Open questions
- Q-04 (open): which 3 weeks have no sprint.

## Tarefas técnicas (candidatas a sub-tarefas)
Decisão da equipa (2026-10-16): processo Scrum é tarefa, não história. Todas ficam ligadas a esta épica. Esta épica não tem histórias. Q-28 (atores) fica sem efeito para estas tarefas.

### T1: Planear cada sprint e registar o planeamento (RNF-13, RNF-15)
- [ ] Existe `docs/scrum/sprint-NN/planning.md` com objetivo, capacidade, Team Leader e histórias com pontos.
- [ ] Sprint 1: sem responsabilidades atribuídas; objetivo é escrever os requisitos.
- [ ] Sem ficheiro de planeamento, o sprint é marcado não conforme.

### T2: Registar o Daily Scrum de cada aula prática (RNF-16, RNF-15)
- [ ] Nota em `docs/scrum/sprint-NN/impediments.md` com data, o que cada membro fez, vai fazer e impedimentos.
- [ ] Sem impedimentos, a nota indica "sem impedimentos".
- [ ] Aula sem nota é assinalada não conforme.

### T3: Fazer e registar a Sprint Review com demo (RNF-15)
- [ ] Existe `review.md` com histórias demonstradas e não concluídas.
- [ ] História não concluída aparece com o motivo.
- [ ] Sem `review.md`, o sprint é marcado não conforme.

### T4: Fazer e registar a retrospetiva keep/stop/start (RNF-15)
- [ ] Existe `retrospective.md` com Keep, Stop e Start.
- [ ] Cada ação de "Start" tem responsável.
- [ ] Secção vazia indica "nenhum item".

### T5: Refinar o backlog (RNF-15, RNF-18)
- [ ] Histórias que cumprem a Definition of Ready ficam prontas e o refinamento fica registado com data.
- [ ] História com mais de 8 pontos é dividida.
- [ ] História sem critérios não fica pronta e a falta fica registada.

### T6: Rodar o Team Leader em cada sprint (RNF-17, RNF-14)
- [ ] No início do sprint seguinte, o Team Leader é o membro seguinte da ordem; `team.md` e o planeamento indicam o nome.
- [ ] Sprint 1 não tem Team Leader.
- [ ] Repetir um líder antes da ronda completa é recusado.

### T7: Validar tarefas contra testes de aceitação (RNF-18)
- [ ] Com todos os AC-n a passar, a história passa a concluída com nome do Team Leader e data.
- [ ] Se um AC-n falha, a história volta ao desenvolvimento com o cenário indicado.

### T8: Atualizar os diagramas com as histórias do sprint (RNF-19)
- [ ] Diagramas afetados atualizados no mesmo pedido de integração, com referência de IA se usada.
- [ ] Alteração sem testes não é aceite.
- [ ] Sem impacto na estrutura: nota "sem impacto".

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
