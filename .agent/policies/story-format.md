# Story Format

Stories are written in Portuguese. Policies and notes are in English.

## Template

```
Título: <verbo + objeto, curto>

Como <papel>,
Eu quero <ação>,
Para que <benefício>.

Critérios de aceitação:

Cenário: <nome do cenário>
  Dado <contexto inicial>
  E <contexto adicional, opcional>
  Quando <ação do ator>
  Então <resultado observável>
  E <resultado adicional, opcional>

Notas: <dependências, perguntas, referência ao enunciado>
Pontos: <1|2|3|5|8>
```

## Rules

- One actor per story. Allowed actors: cliente, rececionista, gestor, funcionário do restaurante, fornecedor.
- Size 8 points or less. If larger, split the story (see `estimation.md`).
- The story must be testable. Each criterion has a clear, observable result.
- Write at least one success scenario and one failure scenario.
- Use only rules from the statement. Do not invent requirements.
- Criteria use Gherkin keywords in Portuguese: Dado, Quando, Então (E, Mas for extra steps).
- Do not describe the design (tables, classes, screens) in the story.

## Full example

```
Título: Realizar check-in com cartão magnético

Como rececionista,
Eu quero efetuar o check-in de um cliente com reserva e atribuir-lhe um cartão magnético,
Para que o cliente aceda ao seu quarto e aos locais incluídos na reserva.

Critérios de aceitação:

Cenário: Check-in com reserva válida
  Dado que existe uma reserva ativa para o quarto 202 sem check-in efetuado
  E existe um cartão magnético livre com o número 12345
  Quando o rececionista efetua o check-in e associa o cartão 12345 à reserva
  Então o estado da reserva passa a "check-in efetuado"
  E o cartão 12345 fica associado ao quarto 202
  E o cartão 12345 dá acesso ao restaurante e à piscina apenas se estes constarem da reserva

Cenário: Check-in sem reserva
  Dado que não existe reserva ativa para o cliente
  Quando o rececionista tenta efetuar o check-in
  Então o sistema recusa o check-in
  E mostra uma mensagem a indicar que não existe reserva

Cenário: Cartão já em uso
  Dado que o cartão 12345 está associado a outra reserva
  Quando o rececionista tenta associar o cartão 12345
  Então o sistema recusa a associação
  E o estado da reserva não muda

Notas: Depende das histórias "Registar cliente" e "Criar reserva". Enunciado, secção 2 (cartão magnético).
Pontos: 5
```
