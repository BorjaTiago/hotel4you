---
type: story
summary: Carregar um ficheiro XML de fornecedor
parent: epic-stock-management
requirements: [RF-28]
points: 8
points_suggested: 8
status: approved
jira_key:
labels: [stock, xml, final-delivery]
sprint:
---

# Título: Carregar um ficheiro XML de fornecedor

Como gestor,
Eu quero carregar o ficheiro XML de uma entrega de fornecedor,
Para que o material da entrega entre no stock e os preços fiquem atualizados.

## Critérios de aceite

### Cenário AC-1: Item existente é atualizado
  Dado que o item "toalhas" existe com a unidade "unidade", o preço 2,00 e o stock 10
  E o ficheiro XML segue o formato definido pela equipa e lista "  Toalhas " com a quantidade 5, a unidade "unidade" e o preço 2,50
  Quando o gestor carrega o ficheiro
  Então o sistema regista a entrada de 5 unidades no item "toalhas", com a data
  E o preço do item passa a 2,50
  E o histórico guarda o preço 2,50 dessa entrada
  E o relatório conta o item como atualizado

### Cenário AC-2: Item novo é criado
  Dado que o item "almofadas" não existe
  E o ficheiro XML lista "almofadas" com a quantidade 20, a unidade "unidade" e o preço 7,00
  Quando o gestor carrega o ficheiro
  Então o sistema cria o item "almofadas" com a unidade "unidade" e o preço 7,00
  E regista a entrada de 20 unidades, com a data
  E nenhum item é rejeitado por ser desconhecido
  E o relatório conta o item como criado

### Cenário AC-3: Item com formato inválido
  Dado que o ficheiro é XML válido e lista um item com campo obrigatório em falta (nome, quantidade, unidade ou preço) ou com valor inválido, por exemplo sem unidade, sem preço ou com a unidade "caixa" num item que existe com a unidade "unidade"
  E lista também itens com formato válido
  Quando o gestor carrega o ficheiro
  Então o sistema importa (cria ou atualiza) os itens com formato válido
  E não importa o item inválido
  E o relatório lista o item inválido com o motivo

### Cenário AC-4: Quantidade negativa
  Dado que o ficheiro lista um item com a quantidade -3
  Quando o gestor carrega o ficheiro
  Então o sistema trata esse item como inválido
  E o stock e o preço desse item não mudam
  E o relatório lista o item com o motivo "quantidade negativa"

### Cenário AC-5: Ficheiro inválido
  Dado que o ficheiro não é XML válido ou não segue o formato
  Quando o gestor carrega o ficheiro
  Então o sistema recusa o ficheiro
  E o sistema não regista nada
  E mostra uma mensagem de erro
  E não mostra relatório de importação
  E o stock e os preços não mudam

### Cenário AC-6: Ficheiro já importado
  Dado que o mesmo ficheiro (mesmo conteúdo) já foi importado
  Quando o gestor carrega o ficheiro outra vez
  Então o sistema recusa a importação
  E mostra uma mensagem de erro a indicar que o ficheiro já foi importado
  E não regista nada nem mostra relatório de importação
  E o stock e os preços não mudam

### Cenário AC-7: Perfil sem permissão
  Dado que o utilizador é um funcionário do restaurante
  Quando tenta carregar um ficheiro XML
  Então o sistema recusa a operação

## Critérios de aceite (checklist)

- [ ] AC-1: Item existente (nome sem espaços nas pontas e sem distinguir maiúsculas) recebe a entrada de stock e o novo preço, e o histórico guarda o preço.
- [ ] AC-2: Item que não existe é criado com a unidade e o preço do XML e a entrada de stock; nada é rejeitado por ser desconhecido.
- [ ] AC-3: Item com formato inválido não é importado e aparece no relatório com o motivo; os válidos são importados. Nome, quantidade, unidade e preço são obrigatórios; unidade ou preço em falta ou inválido, ou unidade diferente da do item que existe, tornam o item inválido.
- [ ] AC-4: Quantidade negativa torna o item inválido, sem mudar stock nem preço, e aparece no relatório.
- [ ] AC-5: Um ficheiro inválido ou fora do formato é recusado com mensagem de erro, sem registar nada, sem relatório e sem linha em `stock_imports`.
- [ ] AC-6: O mesmo ficheiro (mesmo conteúdo) é recusado com mensagem de erro, sem registar nada e sem relatório.
- [ ] AC-7: O funcionário do restaurante não consegue carregar ficheiros XML.

## Notas
Decisão da equipa (utilizador, 2026-10-16), Q-48, substitui Q-10 e Q-38: a importação XML é um upsert dinâmico. O item que existe (nome igual, sem espaços nas pontas e sem distinguir maiúsculas) é atualizado com a entrada de stock e o novo preço. O item que não existe é criado com a unidade do XML. Nome, quantidade, unidade e preço são obrigatórios no XML; o preço do XML define sempre o preço do item. Uma unidade diferente da do item que existe é formato inválido (a unidade nunca muda em silêncio). Nada é rejeitado por ser desconhecido. Só itens ou ficheiros com formato inválido são inválidos e aparecem no relatório com o motivo. O gestor pode editar ou corrigir os itens criados pela importação (história "Editar item de stock").
O fornecedor não faz login (Q-17). A equipa define o formato XML (o exemplo do enunciado não está no texto): o formato fica documentado nesta história/entrega, sem história própria. O relatório de criados, atualizados e inválidos: ver história "Ver o relatório de importação". Entrega final (Q-06). Dependências (auditoria 2026-10-16): nenhuma forte. Construir e testar sozinha: itens de catálogo de teste e ficheiros XML de teste (válido, com item novo, inválido, com quantidade negativa, repetido) (foundation T4). Esta história é dona do leitor XML (`XmlStockImporter`, T0) e guarda a importação (`stock_imports`); story-carregar-xml-fornecedor-rececionista reutiliza-o.
Tamanho: passou de 4 para 7 cenários (criar, atualizar preço, ficheiro repetido, quantidade negativa). Fica no limite de 8. Se a equipa achar maior, dividir (por exemplo, "ficheiro já importado" numa história própria).

points_suggested: 8
reference: Importar ficheiro XML de stock de um fornecedor (8)
compared: equal
reasoning: Define o formato XML, valida ficheiro e itens, cria ou atualiza itens com preço, deteta ficheiro repetido e grava vários movimentos. Seletor de ficheiro na GUI. Mais lógica do que antes (upsert), por isso fica no limite. Pouco desconhecido: o formato é decisão da equipa.
split_needed: no

ready: true
dor_failed: []
questions: [Q-48]

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Q-48 rewrite by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Unit/price rule by docs-updater, Claude Code (claude-haiku-5-5), 2026-10-16. Reviewed by: pending.
