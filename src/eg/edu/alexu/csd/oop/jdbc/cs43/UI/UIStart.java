package eg.edu.alexu.csd.oop.jdbc.cs43.UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UIStart extends Application {
	private Stage primaryStage;
	private UIController controller;

	@Override
	public void start(Stage arg0) throws Exception {
		this.primaryStage = arg0;
		this.primaryStage.setTitle("SQLServer");
		this.primaryStage.setResizable(false);
		initiate();

	}

	@Override
	public void stop() {
		controller.Save();
	}

	private void initiate() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(UIStart.class.getResource("CommandUI.fxml"));

		try {
			BorderPane borderPane = (BorderPane) loader.load();
			Scene scene = new Scene(borderPane);
			primaryStage.setScene(scene);
			primaryStage.show();
			controller = loader.getController();
			controller.setStage(primaryStage);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
