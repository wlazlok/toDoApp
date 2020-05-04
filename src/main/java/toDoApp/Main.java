package toDoApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import toDoApp.Utils.FxmlUtils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Karol Wlazło
 * toDoApp
 */

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Locale.setDefault(new Locale("en")); // Uncomment for english version of app

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/mainBoard.fxml"));
        loader.setResources(FxmlUtils.getResource());
        BorderPane mainPane = loader.load();
        Scene scene = new Scene(mainPane);
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/icons/app_icon.png")));
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle(FxmlUtils.getResource().getString("app.title"));
        primaryStage.show();
    }
}
