---
type: epic
summary: Deliverables, diagrams and delivery zip
parent:
requirements: [RNF-22, RNF-23, RNF-24]
points:
points_suggested:
status: approved
jira_key:
labels: [delivery, documentation, 1st-delivery, final-delivery]
sprint:
---

# Epic: Delivery packaging and diagrams

## Goal
Each delivery is a complete `LP3-GY.zip` on Moodle on time.

## Delivery
1st delivery (2026-12-01 23:55) and final delivery (2027-01-31 23:55).

## Scope
- Domain model, class diagram, use case diagram, sequence diagrams (one general, one detailed).
- Executables, Scrum documents, Javadoc output, unit tests, source code.
- Zip naming and Moodle upload.

## Out of scope
- Writing Javadoc and tests (epic-engineering-quality).

## Actors
- Team Leader, developer.

## Value
Complete deliveries. Documentation scores 20% each delivery.

## Statement links
Sections 5, 6. RNF-22, RNF-23, RNF-24.

## Candidate stories
- Criar o modelo de domínio e o diagrama de casos de uso (actor: developer; RNF-24)
- Criar o diagrama de classes (actor: developer; RNF-24)
- Criar os diagramas de sequência geral e detalhado (actor: developer; RNF-24)
- Gerar executáveis e saída Javadoc (actor: developer; RNF-24)
- Montar e validar o zip LP3-GY.zip da 1.ª entrega (actor: Team Leader; RNF-22, RNF-23, RNF-24)
- Montar e submeter o zip da entrega final (actor: Team Leader; RNF-22, RNF-23, RNF-24)

## Open questions
- None new. Group number Y: team fills in.

## Tarefas técnicas (candidatas a sub-tarefas)
Decisão da equipa (2026-10-16): entregáveis são tarefas, não histórias. Todas ficam ligadas a esta épica. Esta épica não tem histórias.

### T1: Criar o modelo de domínio e o diagrama de casos de uso (RNF-24)
- [ ] Existem o modelo de domínio e o diagrama de casos de uso com os cinco tipos de ator.
- [ ] Diagrama sem um caso de uso novo é assinalado para atualização.
- [ ] Diagrama feito com IA tem a referência de IA.

### T2: Criar o diagrama de classes (RNF-24, RNF-08)
- [ ] O diagrama mostra as classes do código atual, com lógica de negócio separada da interface.
- [ ] Classe em falta: diagrama assinalado para atualização.
- [ ] Referência de IA presente.

### T3: Criar os diagramas de sequência geral e detalhado (RNF-24)
- [ ] Existem um diagrama geral e um detalhado.
- [ ] Se falta um, a validação da entrega assinala-o.
- [ ] Referência de IA presente.

### T4: Gerar executáveis e saída Javadoc (RNF-24, RNF-05)
- [ ] Com código a compilar e testes a passar, existem os executáveis e a saída Javadoc.
- [ ] Se o build falha, termina com erro e nenhum artefacto é marcado para entrega.

### T5: Montar e validar o zip LP3-GY.zip da 1.ª entrega (RNF-22, RNF-23, RNF-24)
- [ ] Antes de 2026-12-01 23:55, o zip "LP3-GY.zip" tem executáveis, documentação Scrum, modelo de domínio, classes, casos de uso, sequência (geral e detalhado), Javadoc, testes e código; âmbito: login, quartos, reservas, check-in e check-out (Q-06).
- [ ] Item em falta: validação falha e indica o item.
- [ ] Nome inválido: validação falha e pede "LP3-GY.zip".
- [ ] Submissão no Moodle registada a tempo.

### T6: Montar e submeter o zip da entrega final (RNF-22, RNF-23, RNF-24)
- [ ] Antes de 2027-01-31 23:55, o zip tem todos os itens e o âmbito inclui cartão magnético, passagens, stock e XML.
- [ ] Item em falta: validação falha e indica o item.
- [ ] Submissão fora de prazo: fica registada a penalização possível.

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
