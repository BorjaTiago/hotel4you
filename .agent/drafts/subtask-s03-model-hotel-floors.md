---
type: subtask
summary: 28.1 Pisos e zonas: consultar, criar, editar e remover — repositório, serviço, ecrã e testes
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

História pai H4Y-28 (consulta e gestão de zonas). Esta subtask cria as tabelas e os dados de que precisa. Os testes usam um repositório em memória (como `InMemoryUserRepository`).
Responsável proposto: Tiago Borja (a equipa decide).

## Checklist

- [ ] Antes do código: acrescentar o repositório de pisos e o método de consulta da fachada em `docs/uml/` (o diagrama não tem nenhum hoje).
- [ ] Script SQL em `database/`: tabelas `floors` e `locations` de `docs/database/schema.md`, com os pisos -1 a 3 e os locais do enunciado (restaurante e piscina com `requires_reservation = 1`). O script corre duas vezes sem duplicar dados.
- [ ] Escrever primeiro os testes e ver que falham, com `@Tag("H4Y-28-AC-1")` e `@Tag("H4Y-28-AC-2")`.
- [ ] AC-1: a consulta devolve os pisos -1 a 3 com os locais do enunciado.
- [ ] AC-2: o piso 4 dá a mensagem "O piso não existe".
- [ ] Ecrã simples aberto pelo menu do gestor. Texto em português.
- [ ] `LocationRepository` (interface) e `JdbcLocationRepository`: `findById`, `findByFloorAndName`, `save`, `update`, `delete` (devolve false com erro SQL 547).
- [ ] `LocationService` com 2 repositórios e os 3 métodos novos: `createLocation`, `editLocation`, `removeLocation`; exceções `LocationNotFoundException`, `LocationNameTakenException`, `LocationInUseException`; os 3 métodos também na `HotelFacade`.
- [ ] Escrever primeiro os testes AC-3 a AC-16 e ver que falham, com `@Tag("H4Y-28-AC-n")`.
- [ ] Formulário de zona no ecrã `floors.fxml` (ver secção Interface).

## Interface (UI)

- Botão "Pisos e zonas" no menu do gestor (regra de H4Y-23).
- Ecrã `floors.fxml` no centro da janela principal: lista dos pisos (-1 a 3) com a descrição; ao escolher um piso, mostra os locais desse piso (nome, "acesso com cartão do cliente": sim/não, "exige reserva": sim/não).
- Campo "Número do piso" e botão "Procurar": com o piso 4 mostra a mensagem "O piso não existe." (AC-2).
- Formulário de zona por baixo da tabela de zonas: campo "Nome da zona", lista "Piso" (-1 a 3), caixas "Acesso com cartão do cliente" e "Exige reserva", botões "Criar zona", "Guardar alterações" e "Remover zona". Ao escolher uma zona, o formulário fica preenchido. "Remover zona" pede confirmação. Depois de cada ação a lista recarrega e aparece uma mensagem.
- Nenhuma regra no controlador (RNF-07). Texto em português.

Created by: lukassperandio. Updated by: lukassperandio. AI use: sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; UI section by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-28 zone merge by doc-updater, Claude Code (claude-sonnet-5-5), 2026-10-26. Reviewed by: pending.
