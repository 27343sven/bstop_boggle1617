import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by sven on 15-May-17.
 */
public class MainProgram extends Application {

    public void start(Stage PrimaryStage){
        BoggleButton test = new BoggleButton('t', new Point2D(1, 2));

        HBox hb = new HBox(test);
        Scene scene = new Scene(hb, 500, 500);
        PrimaryStage.setScene(scene);
        PrimaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
