package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.css.CssParser;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

/** Checks that all FXML screens use valid shared styles without starting JavaFX or a database. */
class ScreenStyleTest {

    @Test
    @Tag("H4Y-83-AC-1")
    void onLoadScreens_useSharedStyles_whenControlsPresent() throws Exception {
        Set<String> buttons = Set.of("primary-button", "action-button", "next-button",
                "secondary-button", "navigation-button");
        for (Path file : screens()) {
            var nodes = document(file).getElementsByTagName("*");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                if (element.hasAttribute("fx:constant")) {
                    continue;
                }
                Set<String> classes = classes(element);
                switch (element.getTagName()) {
                    case "Button" -> assertTrue(classes.stream().anyMatch(buttons::contains),
                            file.getFileName() + ": unstyled button " + element.getAttribute("text"));
                    case "TableView" -> assertTrue(classes.contains("data-table"),
                            file.getFileName() + ": unstyled table");
                    case "ListView" -> assertTrue(classes.contains("data-list"),
                            file.getFileName() + ": unstyled list");
                    default -> { }
                }
                if (element.getAttribute("fx:id").equals("messageLabel")) {
                    assertTrue(classes.contains("message"), file.getFileName() + ": unstyled message");
                }
            }
        }
    }

    @Test
    @Tag("H4Y-83-AC-1")
    @Tag("H4Y-83-AC-2")
    @Tag("H4Y-83-AC-3")
    void onLoadStyles_defineEveryClass_whenScreensUseCustomStyles() throws Exception {
        var stylesheet = new CssParser().parse(getClass().getResource("style.css"));
        String selectors = stylesheet.getRules().stream()
                .flatMap(rule -> rule.getSelectors().stream())
                .map(Object::toString).collect(Collectors.joining(" "));
        for (Path file : screens()) {
            var nodes = document(file).getElementsByTagName("*");
            for (int i = 0; i < nodes.getLength(); i++) {
                for (String styleClass : classes((Element) nodes.item(i))) {
                    assertTrue(java.util.regex.Pattern.compile("\\." + styleClass + "(?![\\w-])")
                                    .matcher(selectors).find(),
                            file.getFileName() + ": missing CSS class " + styleClass);
                }
            }
        }
    }

    @Test
    @Tag("H4Y-83-AC-2")
    @Tag("H4Y-83-AC-3")
    void onParseStyles_reportNoErrors_whenSharedStylesLoaded() throws Exception {
        CssParser.errorsProperty().clear();
        var stylesheet = new CssParser().parse(getClass().getResource("style.css"));
        assertFalse(stylesheet.getRules().isEmpty());
        assertTrue(CssParser.errorsProperty().isEmpty(), () -> CssParser.errorsProperty().toString());
    }

    private java.util.List<Path> screens() throws Exception {
        Path directory = Path.of(getClass().getResource("login.fxml").toURI()).getParent();
        try (var files = Files.list(directory)) {
            return files.filter(path -> path.toString().endsWith(".fxml")).sorted().toList();
        }
    }

    private org.w3c.dom.Document document(Path file) throws Exception {
        var factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return factory.newDocumentBuilder().parse(file.toFile());
    }

    private Set<String> classes(Element element) {
        return Arrays.stream(element.getAttribute("styleClass").split("[,\\s]+"))
                .filter(value -> !value.isBlank()).collect(Collectors.toSet());
    }
}
