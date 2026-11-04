---
type: story
summary: Registar um novo cliente
parent: epic-client-registration
requirements: [RF-10]
points:
points_suggested: 3
status: published
jira_key: H4Y-52
labels: [clients, 1st-delivery]
sprint: 04
---

# Título: Registar um novo cliente

Como rececionista ou gestor,
Eu quero registar, consultar, editar e remover clientes,
Para que a reserva e o check-in tenham os dados do cliente corretos.

## Critérios de aceite

### Cenário AC-1: Registo com dados válidos
  Dado que o rececionista ou o gestor tem sessão iniciada
  E o cliente ainda não está registado
  Quando preenche os dados obrigatórios (nome, tipo e número de documento, data de nascimento, telefone, nacionalidade) e confirma o registo
  Então o sistema guarda o cliente
  E o cliente fica disponível para uma reserva

### Cenário AC-2: Dados obrigatórios em falta ou inválidos
  Dado que o rececionista ou o gestor tem sessão iniciada
  Quando confirma o registo sem um dado obrigatório ou com um dado inválido
  Então o sistema recusa o registo
  E indica qual dado falta ou está errado

### Cenário AC-3: Cliente já registado
  Dado que já existe um cliente com o mesmo tipo e número de documento
  Quando o rececionista ou o gestor tenta registar o mesmo cliente
  Então o sistema recusa o registo duplicado
  E indica que o cliente já existe

### Cenário AC-4: Listar e procurar clientes
  Dado que existem clientes registados
  Quando o rececionista ou o gestor procura por parte do nome ou do documento
  Então o sistema mostra os clientes que correspondem
  E uma pesquisa vazia mostra todos os clientes

### Cenário AC-5: Editar um cliente
  Dado que o cliente está registado
  Quando o rececionista ou o gestor muda os dados e guarda
  Então o sistema guarda os novos dados
  E recusa dados inválidos ou um documento de outro cliente

### Cenário AC-6: Remover um cliente
  Dado que o cliente está registado e não tem reservas
  Quando o rececionista ou o gestor remove o cliente e confirma
  Então o sistema remove o cliente

### Cenário AC-7: Cliente com reservas
  Dado que o cliente tem reservas
  Quando o rececionista ou o gestor tenta remover o cliente
  Então o sistema recusa a remoção
  E indica que o cliente tem reservas

### Cenário AC-8: Perfil sem permissão
  Dado que o utilizador não é rececionista nem gestor
  Quando tenta registar, consultar, editar ou remover um cliente
  Então o sistema recusa a operação

## Critérios de aceite (checklist)

- [ ] AC-1: Um cliente com dados válidos fica registado.
- [ ] AC-2: Dados obrigatórios em falta são recusados com indicação dos campos.
- [ ] AC-3: Registar um cliente já existente (mesmo tipo e número de documento) é recusado.
- [ ] AC-4: A pesquisa por parte do nome ou do documento mostra os clientes; vazia mostra todos.
- [ ] AC-5: Editar guarda os novos dados e recusa dados inválidos ou um documento repetido.
- [ ] AC-6: Remover um cliente sem reservas pede confirmação e remove-o.
- [ ] AC-7: Um cliente com reservas não é removido.
- [ ] AC-8: Só o rececionista e o gestor gerem clientes.

## Notas
Fase: 1.ª entrega (Q-27). O enunciado não lista os dados do cliente nem o critério de duplicado. A equipa define-os na implementação (detalhe negociável, não é requisito novo). Âmbito novo (Q-54, decisão de lukassperandio e BorjaTiago, 2026-11-04, não confirmada pelo professor): o rececionista e o gestor gerem clientes (CRUD completo); os clientes continuam sem sessão (Q-13). Dados obrigatórios: nome, tipo de documento (CC, PASSPORT, OTHER), número, data de nascimento, telefone, nacionalidade (código ISO). Opcionais: email e NIF. A pesquisa de H4Y-53 / H4Y-71 passa para esta história. O âmbito cresceu: a equipa deve reestimar os pontos. Tempo total: ver "Registar cliente em formulário rápido".
Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: base de teste vazia e um cliente de teste para o duplicado (foundation T4), sessão de rececionista simulada (T0).

points_suggested: 3
reference: Registar um cliente (validação de dados e gravação) (3)
compared: equal
reasoning: Same as the reference. Validation, duplicate check and save. Client data fields still open.
split_needed: no

ready: true
dor_failed: []
questions: [Q-54]

Created by: BorjaTiago. Updated by: lukassperandio. AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; sprint field by sprint-planner, Claude Code (claude-opus-5-5), 2026-10-23; H4Y-52 client CRUD (Q-54) by Claude Code (claude-opus-5-5), 2026-11-04. Reviewed by: pending.
