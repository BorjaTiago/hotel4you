---
type: epic
summary: Javadoc, JUnit, Git flow, bibliotecas e referências de IA
parent:
requirements: [RNF-09, RNF-10, RNF-11, RNF-20, RNF-21, RNF-25, RNF-26]
points:
points_suggested:
status: published
jira_key: H4Y-18
labels: [quality, documentation, 1st-delivery, final-delivery]
sprint:
---

# Épica: Qualidade de engenharia e conduta

## Objetivo
O código está documentado e testado. O repositório e o uso de IA seguem as regras da unidade curricular.

## Entrega
As duas entregas. Contínuo em todos os sprints.

## Âmbito
- Javadoc no código.
- Testes JUnit das funções principais.
- Justificação das bibliotecas em `docs/libraries.md` (Q-14).
- Regras de ramos e commits do Git flow.
- Referência de uso de IA em código, documentos e diagramas (Q-15).
- Sem partilha de código entre grupos.

## Fora do âmbito
- Empacotamento da entrega (epic-delivery-packaging).

## Atores
- Programador, Team Leader.

## Valor
Evita penalizações e melhora as notas de documentação e de código-fonte.

## Ligações ao enunciado
Secções 3c, 4, 7. RNF-09, RNF-10, RNF-11, RNF-20, RNF-21, RNF-25, RNF-26.

## Histórias candidatas
- Configurar a geração de Javadoc (ator: programador; RNF-09)
- Configurar JUnit e testes das funções principais (ator: programador; RNF-10)
- Criar docs/libraries.md e justificar bibliotecas (ator: programador; RNF-11)
- Aplicar a regra de ramos e commits Git flow (ator: programador; RNF-20, RNF-21)
- Referenciar todo o uso de IA (ator: Team Leader; RNF-25)
- Rever o cumprimento do código de conduta (ator: Team Leader; RNF-26)

## Perguntas em aberto
- Q-15 (em aberto): formato exato da referência de IA.

## Tarefas técnicas (candidatas a sub-tarefas)
Decisão da equipa (2026-10-16): qualidade e conduta são tarefas, não histórias. Todas ficam ligadas a esta épica. Esta épica não tem histórias.

### T1: Configurar a geração de Javadoc (RNF-09, RNF-24)
- [ ] Com Javadoc nas classes públicas, a saída é criada sem erros com um comando.
- [ ] Classe pública sem Javadoc faz a verificação falhar e indica a classe.

### T2: Configurar JUnit e testes das funções principais (RNF-10)
- [ ] Todos os testes passam com um comando e cada teste tem a tag `@Tag("H4Y-NN-AC-n")`.
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

Created by: BorjaTiago. Updated by: BorjaTiago. AI use: epic-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Portuguese translation by general-purpose agent, Claude Code (claude-sonnet-5-5), 2026-10-16; doc-updater, Claude Code (claude-haiku-5-5), 2026-10-17. Reviewed by: pending.
