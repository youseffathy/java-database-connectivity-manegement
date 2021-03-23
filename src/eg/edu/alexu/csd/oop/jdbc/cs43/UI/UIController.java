package eg.edu.alexu.csd.oop.jdbc.cs43.UI;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class UIController {
	@FXML
	private TextField input;
	@FXML
	private TextArea output;
	@FXML
	private Button path;
	@FXML
	private TableView table;
	@FXML
	private Button addBatch;
	@FXML
	private Button executeBatch;
	@FXML
	private Button clearBatch;
	
	private Stage stage;
	private DriverInstance driverInstance;
	private boolean connected = false;

	public UIController() {
		driverInstance = new DriverInstance();

	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void enterCommand(KeyEvent e) throws Exception {
		if (e.getCode() == KeyCode.ENTER && connected) {
			driverInstance.handleRequest(input.getText());
		}

	}

	@FXML
	public void addBatch() {
		if (connected) {
			driverInstance.addBatch(input.getText());
		}
	}
	@FXML
	public void clearBatch() {
		if (connected) {
			driverInstance.clearBatch();
		}
	}
	@FXML
	public void executeBatch() {
		if (connected) {
			driverInstance.executeBatch();
		}

	}

	@FXML
	public void fileChooseOpen() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Open Resource File");

		File file = directoryChooser.showDialog(stage);

		if (file == null) {
			connected = false;
		} else {
			connected = true;
			driverInstance.StartConnection(file.getAbsolutePath(), output, table);
		}

	}

	public void Save() {
		driverInstance.closeConnections();
	}

}
