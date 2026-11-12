# Shared screen style (H4Y-83)

Requirement: RNF-04. All 11 FXML screens use `style.css`, which `ScreenLoader` already loads.

## Rules

- `app-view` sets the System font, the base font size and the focus colour on each screen.
- Labels use dark blue text. Existing action colours stay: blue for primary actions, green for save, red for remove and grey for clear.
- `primary-button`, `next-button`, `action-button`, `secondary-button` and `navigation-button` give buttons a common style.
- `data-table` and `data-list` set common headers, rows and selection colours.
- `message` gives errors the same red text, System font and 12 px size. The existing `message-success` class keeps success messages green.
- The reservation tabs and summary use the same colours and font as the other screens. The existing FXML classes now have CSS rules.

Only `styleClass` attributes changed in FXML. The hierarchy, fields, text, event handlers, bindings, sizes and spacing attributes stay the same. No business rules, SQL, database data or library changed.

On 2026-11-12, Pedro also approved a small Java window change outside the original CSS/FXML scope: the login window can resize and starts maximized. After login, the main window stays maximized. This changes `HotelApp` and `LoginController` only for window sizing.

## Checks run on 2026-11-12

1. Wrote `ScreenStyleTest` before changing the resources. `mvnw.cmd -B -Dtest=ScreenStyleTest test` failed on an unstyled table and the missing `next-button` CSS rule.
2. Ran `mvnw.cmd -B verify javadoc:javadoc`: 647 tests passed, with no failures or errors. Javadoc built. The build used the installed JDK 26 with the existing Java 21 release target. Existing compiler and Javadoc warnings remain.
3. Compared every FXML file with the branch base after removing `styleClass` attributes from both XML documents: all 11 structures matched.
4. Rendered all 11 FXML screens and the three reservation tabs with JavaFX. A temporary preview in `target/` removed controllers and event handlers from an in-memory copy, so it did not connect to the database or run business actions. Checked the resulting images and error labels. The computed error colour was `#c0392b`, with a 12 px font. JavaFX reported no CSS parse errors.

The isolated previews check appearance only. On 2026-11-12, Pedro tested the running application and reported that it looked correct. Peer review is still required.

## Manual review

Open login, the main menu, staff, floors, rooms, prices, stock, the reservation list and all three steps of a new reservation. Compare button colours, table headers and fonts. Check an existing validation error. Check selected rows and disabled buttons. Confirm that controls remain in their existing places.

Created by: pedrocostaisep. Updated by: pedrocostaisep. AI use: screen-style-editor, OpenAI Codex (gpt-6.1-sol), 2026-11-12. Reviewed by: pending.
