---
type: epic
summary: Javadoc, JUnit, Git flow, libraries and AI references
parent:
requirements: [RNF-09, RNF-10, RNF-11, RNF-20, RNF-21, RNF-25, RNF-26]
points:
points_suggested:
status: approved
jira_key:
labels: [quality, documentation, 1st-delivery, final-delivery]
sprint:
---

# Epic: Engineering quality and conduct

## Goal
The code is documented and tested. The repository and AI use follow the course rules.

## Delivery
Both deliveries. Ongoing in every sprint.

## Scope
- Javadoc on code.
- JUnit tests for main functions.
- Library justification in `docs/libraries.md` (Q-14).
- Git flow branch and commit rules.
- AI-use reference on code, docs and diagrams (Q-15).
- No code sharing between groups.

## Out of scope
- Delivery packaging (epic-delivery-packaging).

## Actors
- Developer, Team Leader.

## Value
Avoids penalties and raises documentation and source code scores.

## Statement links
Sections 3c, 4, 7. RNF-09, RNF-10, RNF-11, RNF-20, RNF-21, RNF-25, RNF-26.

## Candidate stories
- Configurar a geração de Javadoc (actor: developer; RNF-09)
- Configurar JUnit e testes das funções principais (actor: developer; RNF-10)
- Criar docs/libraries.md e justificar bibliotecas (actor: developer; RNF-11)
- Aplicar a regra de ramos e commits Git flow (actor: developer; RNF-20, RNF-21)
- Referenciar todo o uso de IA (actor: Team Leader; RNF-25)
- Rever o cumprimento do código de conduta (actor: Team Leader; RNF-26)

## Open questions
- Q-15 (open): exact AI reference format.

## Tarefas técnicas (candidatas a sub-tarefas)
Decisão da equipa (2026-10-16): qualidade e conduta são tarefas, não histórias. Todas ficam ligadas a esta épica. Esta épica não tem histórias.

### T1: Configurar a geração de Javadoc (RNF-09, RNF-24)
- [ ] Com Javadoc nas classes públicas, a saída é criada sem erros com um comando.
- [ ] Classe pública sem Javadoc faz a verificação falhar e indica a classe.

### T2: Configurar JUnit e testes das funções principais (RNF-10)
- [ ] Todos os testes passam com um comando e cada teste cita "H4Y-NN AC-n".
- [ ] Teste a falhar termina com erro e indica o teste.
- [ ] AC-n sem teste é assinalado.

### T3: Criar docs/libraries.md e justificar bibliotecas (RNF-11)
- [ ] Cada biblioteca nova tem linha com nome, versão, motivo e alternativa rejeitada.
- [ ] Biblioteca sem justificação: pedido de integração não aceite, com a biblioteca indicada (Q-14).

### T4: Aplicar a regra de ramos e commits Git flow (RNF-20, RNF-21)
- [ ] Ramo "(feat|fix)-NNN-descrição" e commit claro cumprem a regra (Q-16).
- [ ] Nome de ramo inválido: pedido não aceite, pede o formato.
- [ ] Commit genérico ("fix task X"): recusado, pede descrição clara.

### T5: Referenciar todo o uso de IA (RNF-25)
- [ ] Ficheiro feito com IA termina com a linha "AI use: ..." (Q-15 em aberto).
- [ ] Ficheiro sem a linha é assinalado e a equipa informada.
- [ ] Diagrama feito com IA tem referência no diagrama ou ficheiro associado.

### T6: Rever o cumprimento do código de conduta (RNF-26)
- [ ] No fim do sprint, regista-se na revisão que não há partilha de código entre grupos e os prazos próximos.
- [ ] Código copiado ou sem autoria fica registado e é removido antes da entrega.
- [ ] Prazo em risco (7 dias, tarefas obrigatórias por fechar) fica em `impediments.md`.

AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Reviewed by: pending.
