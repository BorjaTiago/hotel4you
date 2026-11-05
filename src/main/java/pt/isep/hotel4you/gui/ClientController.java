package pt.isep.hotel4you.gui;

import java.text.Collator;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.DocumentType;
import pt.isep.hotel4you.service.HotelFacade;

/**
 * Controller of the client screen. The receptionist or the manager searches, registers, edits and
 * removes clients. All rules and messages come from the service.
 */
public class ClientController {

    private static final String FIELD_ERROR = "field-error";
    private static final Locale PORTUGUESE = Locale.forLanguageTag("pt-PT");
    static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private static final long TYPING_PAUSE_MILLIS = 1000;

    private final HotelFacade facade;
    private String typedText = "";
    private long lastKeyMillis;

    @FXML
    private Label searchLabel;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Client> clientTable;

    @FXML
    private TableColumn<Client, String> nameColumn;

    @FXML
    private TableColumn<Client, String> documentColumn;

    @FXML
    private TableColumn<Client, String> phoneColumn;

    @FXML
    private TableColumn<Client, String> nationalityColumn;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField nameField;

    @FXML
    private Label documentTypeLabel;

    @FXML
    private ComboBox<DocumentType> documentTypeBox;

    @FXML
    private Label documentLabel;

    @FXML
    private TextField documentField;

    @FXML
    private Label birthDateLabel;

    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private Label phoneLabel;

    @FXML
    private TextField phoneField;

    @FXML
    private Label nationalityLabel;

    @FXML
    private ComboBox<String> nationalityBox;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField emailField;

    @FXML
    private Label taxNumberLabel;

    @FXML
    private TextField taxNumberField;

    @FXML
    private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade that registers, finds, edits and removes the clients
     */
    public ClientController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        searchLabel.setLabelFor(searchField);
        nameLabel.setLabelFor(nameField);
        documentTypeLabel.setLabelFor(documentTypeBox);
        documentLabel.setLabelFor(documentField);
        birthDateLabel.setLabelFor(birthDatePicker);
        phoneLabel.setLabelFor(phoneField);
        nationalityLabel.setLabelFor(nationalityBox);
        emailLabel.setLabelFor(emailField);
        taxNumberLabel.setLabelFor(taxNumberField);

        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        documentColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDocumentType().displayName() + " " + data.getValue().getDocument()));
        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
        nationalityColumn.setCellValueFactory(
                data -> new SimpleStringProperty(countryName(data.getValue().getNationality())));

        documentTypeBox.getItems().setAll(DocumentType.values());
        documentTypeBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(DocumentType type) {
                return type == null ? "" : type.displayName();
            }

            @Override
            public DocumentType fromString(String text) {
                return null;
            }
        });
        nationalityBox.getItems().setAll(countryCodes());
        nationalityBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String code) {
                return code == null ? "" : countryName(code);
            }

            @Override
            public String fromString(String text) {
                return null;
            }
        });
        nationalityBox.addEventFilter(KeyEvent.KEY_TYPED, this::onNationalityKeyTyped);
        birthDatePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date == null ? "" : DATE_FORMAT.format(date);
            }

            @Override
            public LocalDate fromString(String text) {
                return text == null || text.isBlank() ? null : LocalDate.parse(text.trim(), DATE_FORMAT);
            }
        });
        clientTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldClient, newClient) -> fillForm(newClient));
        loadClients();
    }

    @FXML
    private void onSearchClientClicked() {
        showMessage("", false);
        loadClients();
    }

    @FXML
    private void onRegisterClientClicked() {
        clearErrors();
        try {
            facade.registerClient(readClient(null), HotelApp.currentUser);
            clearForm();
            loadClients();
            showMessage("Cliente registado.", true);
        } catch (IllegalArgumentException e) {
            showFieldError(e.getMessage());
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onSaveClientChangesClicked() {
        clearErrors();
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Escolha um cliente na tabela.", false);
            return;
        }
        try {
            facade.editClient(readClient(selected.getId()), HotelApp.currentUser);
            clearForm();
            loadClients();
            showMessage("Cliente guardado.", true);
        } catch (IllegalArgumentException e) {
            showFieldError(e.getMessage());
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onRemoveClientClicked() {
        clearErrors();
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Escolha um cliente na tabela.", false);
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Remover o cliente " + selected.getName() + "?", ButtonType.YES, ButtonType.NO);
        if (confirmation.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }
        try {
            facade.removeClient(selected.getId(), HotelApp.currentUser);
            clearForm();
            loadClients();
            showMessage("Cliente removido.", true);
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onClearClicked() {
        clearErrors();
        clearForm();
        showMessage("", false);
    }

    private void loadClients() {
        try {
            clientTable.getItems().setAll(facade.searchClients(searchField.getText(), HotelApp.currentUser));
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    private void fillForm(Client client) {
        if (client == null) {
            return;
        }
        clearErrors();
        nameField.setText(client.getName());
        documentTypeBox.setValue(client.getDocumentType());
        documentField.setText(client.getDocument());
        birthDatePicker.setValue(client.getBirthDate());
        phoneField.setText(client.getPhone());
        nationalityBox.setValue(client.getNationality());
        emailField.setText(client.getEmail());
        taxNumberField.setText(client.getTaxNumber());
    }

    private Client readClient(Long id) {
        return new Client(id, nameField.getText(), documentTypeBox.getValue(), documentField.getText(),
                readBirthDate(), phoneField.getText(), nationalityBox.getValue(), emailField.getText(),
                taxNumberField.getText());
    }

    /** Reads the typed date too, because the date picker keeps it only after Enter. */
    private LocalDate readBirthDate() {
        String text = birthDatePicker.getEditor().getText();
        try {
            return birthDatePicker.getConverter().fromString(text);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Escreva a data de nascimento como dd/mm/aaaa.");
        }
    }

    /** Marks in red the field that the message names, and shows the message. */
    private void showFieldError(String message) {
        Control field = fieldFor(message);
        if (field != null) {
            field.getStyleClass().add(FIELD_ERROR);
        }
        showMessage(message, false);
    }

    private Control fieldFor(String message) {
        if (message.contains("tipo de documento")) {
            return documentTypeBox;
        }
        if (message.contains("documento")) {
            return documentField;
        }
        if (message.contains("nome")) {
            return nameField;
        }
        if (message.contains("data de nascimento")) {
            return birthDatePicker;
        }
        if (message.contains("telefone")) {
            return phoneField;
        }
        if (message.contains("nacionalidade")) {
            return nationalityBox;
        }
        if (message.contains("email")) {
            return emailField;
        }
        if (message.contains("NIF")) {
            return taxNumberField;
        }
        return null;
    }

    private void clearErrors() {
        for (Control field : List.of(nameField, documentTypeBox, documentField, birthDatePicker,
                phoneField, nationalityBox, emailField, taxNumberField)) {
            field.getStyleClass().remove(FIELD_ERROR);
        }
    }

    private void clearForm() {
        clientTable.getSelectionModel().clearSelection();
        nameField.clear();
        documentTypeBox.setValue(null);
        documentField.clear();
        birthDatePicker.setValue(null);
        birthDatePicker.getEditor().clear();
        phoneField.clear();
        nationalityBox.setValue(null);
        emailField.clear();
        taxNumberField.clear();
    }

    private void showMessage(String message, boolean success) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().remove("message-success");
        if (success) {
            messageLabel.getStyleClass().add("message-success");
        }
    }

    static List<String> countryCodes() {
        Collator collator = Collator.getInstance(PORTUGUESE);
        return Arrays.stream(Locale.getISOCountries())
                .sorted(Comparator.comparing(ClientController::countryName, collator))
                .toList();
    }

    private static String countryName(String code) {
        return Locale.of("", code).getDisplayCountry(PORTUGUESE);
    }

    private void onNationalityKeyTyped(KeyEvent event) {
        String character = event.getCharacter();
        if (character.isBlank() || Character.isISOControl(character.charAt(0))) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastKeyMillis > TYPING_PAUSE_MILLIS) {
            typedText = "";
        }
        lastKeyMillis = now;
        typedText += character;
        String code = findCountry(nationalityBox.getItems(), typedText);
        if (code != null) {
            nationalityBox.setValue(code);
        }
    }

    static String findCountry(List<String> codes, String typed) {
        String start = withoutAccents(typed);
        return codes.stream()
                .filter(code -> withoutAccents(countryName(code)).startsWith(start))
                .findFirst()
                .orElse(null);
    }

    private static String withoutAccents(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(PORTUGUESE);
    }
}
