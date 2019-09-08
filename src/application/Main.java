package application;
	


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			;
			Parent root= FXMLLoader.load(getClass().getResource("Controller.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("CSP Resolver");
	    Image icon= new Image(getClass().getResourceAsStream("icon.png"));	
		primaryStage.getIcons().add(icon);
		primaryStage.show();
	
	
	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		
		launch(args);

		
	}

}

