package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * A very simple viewer for piece placements in the twist game.
 *
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 60;
    private static final int VIEWER_WIDTH = 800;
    private static final int VIEWER_HEIGHT = 500;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField textField;

    private final Group imageRoot = new Group();


    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement  A valid placement string
     */

    /*
    Showing rotated and flipped placement on screen Originally
    Removing all previous images from screen and add a background of dots
     */

    private void makePlacement(String placement) {

        int size = imageRoot.getChildren().size();

        for(int i = size-1; i < size && i != 0; i--){
            imageRoot.getChildren().remove(i);
        }

        for(int i = 0; i < placement.length(); i += 4){

            ImageView imageView = new ImageView();
            Image image = new Image(Viewer.class.getResourceAsStream(URI_BASE  + placement.charAt(i) +".png"));
            imageView.setImage(image);

            //
            switch (placement.charAt(i+3)){
                case '1': {
                    if(placement.charAt(i)=='a' ||placement.charAt(i)=='b'||placement.charAt(i)=='d'||placement.charAt(i)=='f' ){
                        imageView.setTranslateX(-50);
                        imageView.setTranslateY(50);
                    }else if(placement.charAt(i)=='c'){
                        imageView.setTranslateX(-150);
                        imageView.setTranslateY(150);
                    }else if(placement.charAt(i)=='h'){
                        imageView.setTranslateX(-100);
                        imageView.setTranslateY(100);
                    }
                    imageView.setRotate(90);
                    break;
                }
                case '2': {
                    imageView.setRotate(180);
                    break;
                }
                case '3': {
                    if(placement.charAt(i)=='a' ||placement.charAt(i)=='b'||placement.charAt(i)=='d'||placement.charAt(i)=='f' ){
                        imageView.setTranslateX(-50);
                        imageView.setTranslateY(50);
                    }else if(placement.charAt(i)=='c'){
                        imageView.setTranslateX(-150);
                        imageView.setTranslateY(150);
                    }else if(placement.charAt(i)=='h'){
                        imageView.setTranslateX(-100);
                        imageView.setTranslateY(100);
                    }
                    imageView.setRotate(270);
                    break;
                }
                case '4': {
                    imageView.setScaleY(-1);
                    break;
                }
                case '5': {
                    if(placement.charAt(i)=='a' ||placement.charAt(i)=='b'||placement.charAt(i)=='d'||placement.charAt(i)=='f' ){
                        imageView.setTranslateX(-50);
                        imageView.setTranslateY(50);
                    }else if(placement.charAt(i)=='c'){
                        imageView.setTranslateX(-150);
                        imageView.setTranslateY(150);
                    }else if(placement.charAt(i)=='h'){
                        imageView.setTranslateX(-100);
                        imageView.setTranslateY(100);
                    }
                    imageView.setRotate(90);
                    imageView.setScaleY(-1);
                    break;
                }
                case '6': {
                    imageView.setScaleX(-1);
                    break;
                }
                case '7': {
                    if(placement.charAt(i)=='a' ||placement.charAt(i)=='b'||placement.charAt(i)=='d'||placement.charAt(i)=='f' ){
                        imageView.setTranslateX(-50);
                        imageView.setTranslateY(50);
                    }else if(placement.charAt(i)=='c'){
                        imageView.setTranslateX(-150);
                        imageView.setTranslateY(150);
                    }else if(placement.charAt(i)=='h'){
                        imageView.setTranslateX(-100);
                        imageView.setTranslateY(100);
                    }
                    imageView.setRotate(270);
                    imageView.setScaleY(-1);
                    break;
                }
            }

            double column = Double.parseDouble(placement.substring(i+1,i+2));
            imageView.setX((column-1)*100);

            switch(placement.charAt(i+2)){
                case 'A': {
                    imageView.setY(0);
                    break;
                }
                case 'B': {
                    imageView.setY(100);
                    break;
                }
                case 'C': {
                    imageView.setY(200);
                    break;
                }case 'D': {
                    imageView.setY(300);
                    break;
                }
            }

            imageRoot.getChildren().add(imageView);
        }

        // FIXME Task 4: implement the simple placement viewer
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField ();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makePlacement(textField.getText());
                textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
        for(int n = 0; n < 32; n++){
            Circle c = new Circle();
            c.setCenterX(50+100*(n%8));
            c.setCenterY(50+100*(Math.floor(n/8)));
            c.setRadius(15);
            c.setFill(Color.STEELBLUE);
            root.getChildren().add(c);
        }
        controls.getChildren().add(root);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TwistGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
        Scene imageScene = new Scene(imageRoot, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);
        imageRoot.getChildren().add(controls);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.setScene(imageScene);
        primaryStage.show();
    }
}