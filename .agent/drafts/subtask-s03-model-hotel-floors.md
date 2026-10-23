---
type: subtask
summary: 28.1 Consultar pisos e zonas: repositório, serviço, ecrã e testes
parent: H4Y-28
requirements: [RF-03]
points:
points_suggested:
status: published
jira_key: H4Y-62
labels: [rooms, 1st-delivery]
sprint: 03
branch: feat/H4Y-62-hotel-floors-view
---

# model-hotel-floors: consultar a estrutura do hotel

História pai H4Y-28 (2 pontos). Só leitura. Esta subtask cria as tabelas e os dados de que precisa. Os testes usam um repositório em memória (como `InMemoryUserRepository`).
Responsável proposto: Tiago Borja (a equipa decide).

## Checklist

- [ ] Antes do código: acrescentar o repositório de pisos e o método de consulta da fachada em `docs/uml/` (o diagrama não tem nenhum hoje).
- [ ] Script SQL em `database/`: tabelas `floors` e `locations` de `docs/database/schema.md`, com os pisos -1 a 3 e os locais do enunciado (restaurante e piscina com `requires_reservation = 1`). O script corre duas vezes sem duplicar dados.
- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-28-AC-1")` e `@Tag("H4Y-28-AC-2")`.
- [ ] AC-1: a consulta devolve os pisos -1 a 3 com os locais do enunciado.
- [ ] AC-2: o piso 4 dá a mensagem "O piso não existe".
- [ ] Ecrã simples aberto pelo menu do gestor. Texto em português.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23. Reviewed by: pending.
