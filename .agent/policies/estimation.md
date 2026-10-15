# Estimation

## Scale

Use Fibonacci story points: 1, 2, 3, 5, 8, 13.

| Points | Meaning | Reference story |
|--------|---------|-----------------|
| 1 | Trivial. One small change. | Mostrar o nome do utilizador autenticado no ecrã principal |
| 2 | Small. One layer, simple rules. | Listar os quartos por tipo |
| 3 | Medium. Some logic, one or two layers. | Registar um cliente (validação de dados e gravação) |
| 5 | Large. Logic, data and GUI together. | Realizar check-in com cartão magnético |
| 8 | Very large. Many rules or many parts. | Importar ficheiro XML de stock de um fornecedor |
| 13 | Too large. Must be split before a sprint. | Gestão completa de reservas |

## Rules

- A story over 8 points is not ready. Split it (by scenario, by actor step, or by data type).
- Estimate size and effort together, not hours.
- Compare with the reference stories. Update the references when the team agrees.
- If the team cannot estimate, ask a question or add a spike story. Do not guess.
- Agent suggests, team decides. The agent gives a number and a short reason. The team picks the final value.
- Record the final value in the draft front-matter field `points`.
