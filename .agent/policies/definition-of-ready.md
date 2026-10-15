# Definition of Ready (DoR)

## Ready for estimation (DoR)

A story is ready for estimation when all items are true. No estimate is needed yet.

- [ ] The story uses the format: "Como <papel>, Eu quero <ação>, Para que <benefício>" (see `story-format.md`).
- [ ] The story has one actor.
- [ ] The story follows INVEST:
  - [ ] Independent: it can be built without a hidden dependency.
  - [ ] Negotiable: it states the need, not the design.
  - [ ] Valuable: the benefit is clear.
  - [ ] Estimable: the team understands it enough to estimate.
  - [ ] Small: it looks like 8 points or less.
  - [ ] Testable: a tester can check it.
- [ ] Acceptance criteria are in Given/When/Then (Dado/Quando/Então), numbered AC-1, AC-2, and so on.
- [ ] The story links to a statement requirement (RF-NN or RNF-NN). It adds no new requirement.
- [ ] Dependencies are listed and linked (stories, data, diagrams).
- [ ] No open question. Open questions are answered or sent to the teacher.
- [ ] The story has a parent Epic.

The refiner sets `status: ready` when all items pass.

## Ready to commit

A story can enter a sprint only when:

- [ ] It meets the DoR above.
- [ ] The team set `points` (Fibonacci, 8 or less). `points_suggested` alone is not enough.
- [ ] The human team approved the draft (`status: approved`).
