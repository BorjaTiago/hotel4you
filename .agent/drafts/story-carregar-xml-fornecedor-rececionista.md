---
type: story
summary: Carregar um ficheiro XML de fornecedor como pessoal do hotel
parent: epic-stock-management
requirements: [RF-28]
points: 3
points_suggested: 3
status: approved
jira_key:
labels: [stock, xml, final-delivery]
sprint:
---

# Título: Carregar um ficheiro XML de fornecedor como pessoal do hotel

Como rececionista,
Eu quero carregar o ficheiro XML de uma entrega de fornecedor,
Para que o material recebido entre no stock sem esperar pelo gestor.

## Critérios de aceite

### Cenário AC-1: Item existente é atualizado
  Dado que o item "toalhas" existe com a unidade "unidade" e o preço 2,00
  E o ficheiro XML segue o formato definido pela equipa e lista "toalhas" com a quantidade 5, a unidade "unidade" e o preço 2,50
  Quando o rececionista carrega o ficheiro
  Então o sistema regista a entrada de 5 unidades no item "toalhas", com a data
  E o preço do item passa a 2,50

### Cenário AC-2: Item novo é criado
  Dado que o item "almofadas" não existe
  E o ficheiro XML lista "almofadas" com a quantidade 20, a unidade "unidade" e o preço 7,00
  Quando o rececionista carrega o ficheiro
  Então o sistema cria o item "almofadas" com a unidade "unidade" e o preço 7,00
  E regista a entrada de 20 unidades
  E nenhum item é rejeitado por ser desconhecido

### Cenário AC-3: Item com formato inválido ou quantidade negativa
  Dado que o ficheiro lista um item com campo obrigatório em falta (nome, quantidade, unidade ou preço), valor inválido, quantidade negativa ou unidade diferente da do item que existe (por exemplo "caixa" em vez de "unidade")
  E lista também itens com formato válido
  Quando o rececionista carrega o ficheiro
  Então o sistema importa os itens válidos
  E não importa o item inválido
  E o relatório lista o item inválido com o motivo

### Cenário AC-4: Ficheiro inválido
  Dado que o ficheiro não é XML válido ou não segue o formato
  Quando o rececionista carrega o ficheiro
  Então o sistema recusa o ficheiro
  E o sistema não regista nada
  E mostra uma mensagem de erro
  E não mostra relatório de importação
  E o stock e os preços não mudam

### Cenário AC-5: Ficheiro já importado
  Dado que o mesmo ficheiro (mesmo conteúdo) já foi importado
  Quando o rececionista carrega o ficheiro outra vez
  Então o sistema recusa a importação
  E mostra uma mensagem de erro e não regista nada nem mostra relatório
  E o stock e os preços não mudam

## Critérios de aceite (checklist)

- [ ] AC-1: Item existente recebe a entrada de stock e o novo preço.
- [ ] AC-2: Item que não existe é criado com a unidade e o preço do XML e a entrada; nada é rejeitado por ser desconhecido.
- [ ] AC-3: Item com formato inválido (unidade ou preço em falta ou inválido, unidade diferente, quantidade negativa) não é importado e aparece no relatório com o motivo; os válidos são importados.
- [ ] AC-4: Um ficheiro que não é XML válido ou não segue o formato é recusado com mensagem de erro, sem registar nada e sem relatório.
- [ ] AC-5: O mesmo ficheiro (mesmo conteúdo) é recusado com mensagem de erro, sem registar nada e sem relatório.

## Notas
RF-28: "staff ou o gestor carrega o ficheiro". Mesma regra da história do gestor, outro ator. Decisão da equipa (utilizador, 2026-10-16), Q-48, substitui Q-10 e Q-38: upsert dinâmico (cria o item que não existe com a unidade do XML, atualiza o que existe com stock e preço; só formato inválido é inválido; nome, quantidade, unidade e preço são obrigatórios no XML). Q-17. Entrega final (Q-06).
Dependências (auditoria 2026-10-16): uma forte, story-carregar-xml-fornecedor-gestor (reutiliza o leitor XML e a regra de upsert; os 3 pontos supõem essa reutilização; sprint 11 antes do sprint 12). Itens e ficheiros: dados de teste (foundation T4), sem depender das histórias do catálogo.

points_suggested: 3
reference: Importar ficheiro XML de stock de um fornecedor (8)
compared: smaller
reasoning: Reutiliza a leitura e as regras (incluindo o upsert) da história do gestor, que leva o custo do XML. Aqui só muda o ator e o acesso ao ecrã. Se for feita antes da do gestor, passa a 8.
split_needed: no

ready: true
dor_failed: []
questions: [Q-48]

AI use: story-writer, acceptance-criteria, Claude Code (claude-sonnet-5-5), 2026-10-16; refiner, Claude Code (claude-sonnet-5-5), 2026-10-16; estimator, Claude Code (claude-sonnet-5-5), 2026-10-16; Dependências note by backlog-prioritizer, Claude Code (claude-opus-5-5), 2026-10-16; checklist by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16; Q-48 rewrite by story-writer, Claude Code (claude-sonnet-5-5), 2026-10-16. Unit/price rule by docs-updater, Claude Code (claude-haiku-5-5), 2026-10-16. Reviewed by: pending.
