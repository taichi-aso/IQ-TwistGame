package comp1110.ass2.gui;

import comp1110.ass2.TwistGame;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Board extends Application {
    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;
    private static final double LOWER_PANEL_WIDTH = 250;
    private static final double LOWER_PANEL_HEIGHT = 440;
    private static Stage alertBox = new Stage();

    private static boolean isBoardEmpty = true;

    private static final String URI_BASE = "assets/";

    private static final String BASEBOARD_URI = Board.class.getResource(URI_BASE + "bg.jpeg").toString();

    /* Groups */
    public static Group pieces = new Group();
    private static Group boardSet = new Group();
    private static Group startMenu = new Group();
    private static Group pegs = new Group();
    private static Group hintPiece = new Group();
    private static Group instructionGroup = new Group();

    /* Two Scenes */
    private static Scene window = new Scene(boardSet, BOARD_WIDTH, BOARD_HEIGHT); // main board of game
    private static Scene menu = new Scene(startMenu, BOARD_WIDTH, BOARD_HEIGHT); // Start Menu

    /* the difficulty slider */
    private static final Slider difficulty = new Slider(0,3, 0);
    private static Line line1 = new Line(LOWER_PANEL_WIDTH, 440, LOWER_PANEL_WIDTH, 700);
    private static Line line2 = new Line(0,LOWER_PANEL_HEIGHT,933,LOWER_PANEL_HEIGHT);
    private static Line line3 = new Line(250, 570, 933, 570);

    /* different buttons */
    private static Button restart = new Button("Restart");
    private static Button hint = new Button("Hint");
    private static Button toMenu = new Button("Menu");
    private static Button NewGame = new Button("New Game");
    private static Button Resume = new Button("Resume");
    private static Button quit = new Button("Quit");
    private static Button mute = new Button("Mute");
    private static Button instruction = new Button("?");

    /* texts shown */
    private static Text easy = new Text("Starter");
    private static Text medium = new Text("Junior");
    private static Text hard = new Text("Expert");
    private static Text legendary = new Text("Master");
    private static Text Title = new Text("IQ TWIST");

    static String placementString = "";

    /* BGM */
    private final String BGM_URL = getClass().getResource(URI_BASE + "OMFG - Hello.mp3").toString();
    private Media BGM = new Media(BGM_URL);
    private MediaPlayer mp = new MediaPlayer(BGM);
    private boolean loopPlaying = true;

    /* effect of pieces */
    private static DropShadow dropShadow;

    static {
        dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, .4));
    }

    public static String PiecePlacement = "";
    private static int rotation;

    /* return value of popup boxes */
    private static boolean answer;

    private boolean makeResumeBox() {
        Group root = new Group();
        Scene scene = new Scene(root,300,160);
        alertBox.setWidth(300);
        alertBox.setHeight(160);
        Label msg = new Label("No game in progress. Start a new game?");
        msg.setFont(Font.font("Silom",12));
        msg.setLayoutX(20);
        msg.setLayoutY(30);

        Button yes = new Button("Yes");
        yes.setStyle("-fx-base: black;");
        yes.setLayoutX(50);
        yes.setLayoutY(75);
        Button no = new Button("No");
        no.setStyle("-fx-base: black;");
        no.setLayoutX(100);
        no.setLayoutY(75);
        no.setOnAction(event -> {
            answer = false;
            alertBox.close();
        });
        yes.setOnAction(event -> {
            answer = true;
            pieces.getChildren().clear();
            makePieces();
            alertBox.close();
        });

        root.getChildren().add(yes);
        root.getChildren().add(no);
        root.getChildren().add(msg);
        alertBox.setScene(scene);

        alertBox.showAndWait();
        return answer;
    }

    private boolean makeNewGameBox() {
        Group root = new Group();
        Scene scene = new Scene(root,300,160);
        alertBox.setWidth(300);
        alertBox.setHeight(160);
        Label msg = new Label("Game in progress. Quit and start a new game?");
        msg.setFont(Font.font("Silom",12));
        msg.setLayoutY(30);

        Button yes = new Button("Yes");
        yes.setStyle("-fx-base: black;");
        yes.setLayoutX(50);
        yes.setLayoutY(75);
        Button no = new Button("No");
        no.setStyle("-fx-base: black;");
        no.setLayoutX(100);
        no.setLayoutY(75);
        no.setOnAction(event -> {
            answer = false;
            alertBox.close();
        });
        yes.setOnAction(event -> {
            answer = true;
            pieces.getChildren().clear();
            makePieces();
            alertBox.close();
        });

        root.getChildren().add(yes);
        root.getChildren().add(no);
        root.getChildren().add(msg);
        alertBox.setScene(scene);

        alertBox.showAndWait();
        return answer;
    }

    private boolean makeQuitBox() {
        Group root = new Group();
        Scene scene = new Scene(root,300,160);
        alertBox.setWidth(300);
        alertBox.setHeight(160);
        Label msg = new Label("You sure you want to quit this LOVELY GAME?");
        msg.setFont(Font.font("Silom",12));
        msg.setLayoutY(30);
        msg.setLayoutX(5);

        Button yes = new Button("Yes");
        yes.setStyle("-fx-base: black;");
        yes.setLayoutX(50);
        yes.setLayoutY(75);
        Button no = new Button("No");
        no.setStyle("-fx-base: black;");
        no.setLayoutX(100);
        no.setLayoutY(75);
        no.setOnAction(event -> {
            answer = false;
            alertBox.close();
        });
        yes.setOnAction(event -> {
            answer = true;
            pieces.getChildren().clear();
            makePieces();
            alertBox.close();
        });

        root.getChildren().add(yes);
        root.getChildren().add(no);
        root.getChildren().add(msg);
        alertBox.setScene(scene);

        alertBox.showAndWait();
        return answer;
    }

    private void setUpBGM() {
        mp.setAutoPlay(true);
        mp.setCycleCount(999);
        mp.play();
    }

    public static String initialPegs = "";
    public static String solution = "";
    public static int difficultyLevel = 0;

    private void makePegs() {
        difficultyLevel=(int)difficulty.getValue();
        InitialPegs.generateStartingPoint(InitialPegs.randomReadFromtxt());
        System.out.println("Difficulty level is "+difficultyLevel);
        System.out.println("InitialPeg is "+initialPegs);
        setPegs(initialPegs);
    }

    public void setPegs(String placement) {
        String pegPlacement = "";
        for(int i = 0; i < placement.length(); i+=4) {
            if (placement.charAt(i)>='i'){
                pegPlacement=placement.substring(i,placement.length());
                break;
            }
        }
        for(int n = 0; n < pegPlacement.length(); n+=4) {
            String singlePeg = pegPlacement.substring(n,n+4);
            char pegName = singlePeg.charAt(n%4);
            double row = getRow(singlePeg);
            double column = getColumn(singlePeg);
            Image image = new Image(Board.class.getResource(URI_BASE+pegName+".png").toString());
            ImageView im = new ImageView(image);
            im.setY(row);
            im.setX(column);
            im.setScaleY(1.1);
            im.setScaleX(1.1);
            im.setImage(image);
            pegs.getChildren().add(im);
        }
    }

    private double getRow(String pegPlacement) {
        return (pegPlacement.charAt(2)-'A')*110 + 5;
    }

    private double getColumn(String pegPlacement) {
        return (pegPlacement.charAt(1)-'1')*110 + 31.5 ;
    }

    private void toggleSoundLoop() {
        if (loopPlaying)
            mp.setVolume(0);
        else
            mp.setVolume(1);
        loopPlaying = !loopPlaying;
    }

    static class Piece extends ImageView{
        char pieceName;

        Piece(char pieceName) {
            if (pieceName >= 'i') {
                throw new IllegalArgumentException("Bad tile: \"" + pieceName + "\"");
            }

            setImage(new Image(Board.class.getResource(URI_BASE + pieceName + ".png").toString()));
            this.pieceName = pieceName;
            setScaleX(0.3);
            setScaleY(0.3);
            setEffect(dropShadow);
        }
    }

    static class DraggablePiece extends Piece {
        double homeX;              // the position in the window where the mask should be when not on the board
        double homeY;
        double mouseX, mouseY;     // the last known mouse positions (used when dragging)

        private void snapToHome() {
            setScaleX(0.3);
            setScaleY(0.3);
            setLayoutX(homeX);
            setLayoutY(homeY);
        }

        private void snapToGrid() {
            double imageHeight = getImage().getHeight();
            double imageWidth =  getImage().getWidth();

            double rotateAddition = 0;
            if(getRotate()%180!=0){
                rotateAddition = 1.1*(imageWidth-imageHeight)/2;
            }

            double positionX = getLayoutX() - imageWidth*0.05 - 26.5+rotateAddition;
            double positionY = getLayoutY() - imageHeight*0.05-rotateAddition;

            int placenementX;
            int placenementY;

            if(positionX%110<=55){
                placenementX = ((int)(positionX/110)*110);
            }else{
                placenementX = ((int)(positionX/110+1)*110);
            }
            if(positionY%110<=55){
                placenementY = ((int)(positionY/110)*110);
            }else{
                placenementY = ((int)(positionY/110+1)*110);
            }

            PiecePlacement = "" + pieceName+(placenementX/110+1)+(char)('A'+placenementY/110)+rotation;

            if(TwistGame.isPlacementStringValid(comp1110.ass2.gui.Piece.makeWellFormedString(placementString,PiecePlacement)+initialPegs)){
                setLayoutX(placenementX+26.5+imageWidth*0.05-rotateAddition);
                setLayoutY(placenementY+imageHeight*0.05+rotateAddition);
                placementString = InitialPegs.getNewPlacement(placementString,PiecePlacement);
                System.out.println("Placement string is "+placementString);
                if(TwistGame.isSolutionValid(placementString+initialPegs)){

                    Label congrats = new Label("Congratulations!");
                    congrats.setFont(Font.font("Silom",100));
                    congrats.setTextFill(Color.BLUEVIOLET);
                    congrats.setLayoutX(10);
                    congrats.setLayoutY(480);
                    congrats.toFront();
                    Glow glow = new Glow();
                    glow.setLevel(1.5);

                    congrats.setEffect(glow);
                    pieces.getChildren().add(congrats);

//                    finished = true;
//                    System.out.println(finished);
                    // need to add some popping up message
                    System.out.println("Congratulations!!");
                }
            }else{
                snapToHome();
            }
        }

        DraggablePiece(char pieceName) {
            super(pieceName);
            toFront();
            Image image = new Image(Viewer.class.getResourceAsStream(URI_BASE  + pieceName +".png"));
            homeX = LOWER_PANEL_WIDTH  + (170.75 * ((pieceName -'a')/2))-(image.getWidth()/2-170.75/2);
            setLayoutX(homeX);
            homeY = LOWER_PANEL_HEIGHT +(130 * ((pieceName - 'a')%2))-(image.getHeight()/2-130/2);
            setLayoutY(homeY);

            setOnMousePressed(event -> {      // mouse press indicates begin of drag
                if(placementString.contains(Character.toString(pieceName))){
                    placementString=placementString.substring(0,placementString.indexOf(pieceName))+placementString.substring(placementString.indexOf(pieceName)+4);
                }

                rotation = (int)getRotate()/90;

                toFront();
                setScaleX(1.1);
                if(getScaleY()<0){
                    setScaleY(-1.1);
                }else{
                    setScaleY(1.1);
                }
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            });

            setOnMouseDragged(event -> {      // mouse is being dragged
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
            });

            setOnMouseReleased(event -> {     // drag is complete
                toFront();

                double imageHeight = image.getHeight();

                double rotateAddition = 0;

                double positionY = getLayoutY() - imageHeight*0.05-rotateAddition;

                if(positionY+imageHeight*1.1/2>=440) {
                    snapToHome();
                }else {
                    snapToGrid();
                }
                rotation = (int)getRotate()/90;
                isBoardEmpty = false;

                PiecePlacement = placementString;
                System.out.println(PiecePlacement);

            }); // THIS action could potentially solve putting one piece and hint get confused problem

            /*
            * here we'd have to update every time we add some piece to board or remove some off game board
            * so that we can keep track of our PiecePlacement and give hint accordingly*/

            setOnScroll(event -> {
               if(!isPressed()&&getLayoutY()==homeY&&getLayoutX()==homeX) {
                    if (getRotate() <= 180) {
                        setScaleY(0.3);
                        setScaleX(0.3);
                        setRotate((getRotate() + 90));
                        rotation = (int) getRotate() / 90;
                    }else if(getRotate()<=540) {
                        setScaleY(-0.3);
                        setScaleX(0.3);
                        setRotate((getRotate() + 90));
                        rotation = (int)getRotate()/90;
                   } else {
                        setScaleY(0.3);
                        setScaleX(0.3);
                        setRotate(0);
                        rotation=0;
                   }
                }else{
                    if (getRotate() <= 180) {
                        setScaleY(1.1);
                        setScaleX(1.1);
                        setRotate((getRotate() + 90));
                        rotation = (int)getRotate()/90;
                    }else if (getRotate() <= 540) {
                        setScaleY(-1.1);
                        setScaleX(1.1);
                        setRotate((getRotate() + 90));
                        rotation = (int)getRotate()/90;
                    } else {
                        setScaleX(1.1);
                        setScaleY(1.1);
                        setRotate(0);
                        rotation = 0;
                    }
                }
            });
        }
    }

    private static void makePieces() {
        pieces.getChildren().clear();
        for (char m = 'a'; m <= 'h'; m++) {
            DraggablePiece piece = new DraggablePiece(m);
            pieces.getChildren().add(piece);
        }
    }

    private void makeDifficulty() {
        difficulty.setStyle("-fx-base: black;");
        difficulty.setShowTickLabels(false);
        difficulty.setShowTickMarks(true);
        difficulty.setMajorTickUnit(1);
        difficulty.setMinorTickCount(1);
        difficulty.setSnapToTicks(true);
        difficulty.setOrientation(Orientation.VERTICAL);

        difficulty.setLayoutX(40);
        difficulty.setLayoutY(500);
    }

    private void makeLevels() {
        final Label difficultyLabel = new Label("Can you defeat them?");
        difficultyLabel.setLayoutX(60);
        difficultyLabel.setLayoutY(650);
        difficultyLabel.setTextFill(Color.BLACK);
        difficultyLabel.setFont(Font.font("Silom",15));
        easy.setX(80);
        easy.setY(637);
        easy.setFont(Font.font("Silom",12));
        medium.setX(80);
        medium.setY(596);
        medium.setFont(Font.font("Silom",12));
        hard.setX(80);
        hard.setY(555);
        hard.setFont(Font.font("Silom",12));
        legendary.setX(80);
        legendary.setY(514);
        legendary.setFont(Font.font("Silom",12));
        boardSet.getChildren().add(difficultyLabel);
        Title.setFont(Font.font("Silom",100));
        Title.setLayoutX(250);
        Title.setLayoutY(150);
    }

    private void setHintPiece(){
        String piece = InitialPegs.getHint(placementString,solution);
        if(!piece.isEmpty()){
            Image image = new Image(Board.class.getResource(URI_BASE+piece.charAt(0)+".png").toString());
            ImageView im = new ImageView(image);
            im.setRotate(Character.getNumericValue(piece.charAt(3))*90);
            double rotateAddition = 0;
            if(im.getRotate()%180!=0){
                rotateAddition = 1.1*(image.getWidth()-image.getHeight())/2;
            }
            im.setX((piece.charAt(1)-'1')*110+26.5+image.getWidth()*0.05-rotateAddition);
            im.setY((piece.charAt(2)-'A')*110+image.getHeight()*0.05+rotateAddition);
            im.setScaleX(1.1);
            if(Character.getNumericValue(piece.charAt(3))>=4){
                im.setScaleY(-1.1);
            }else{
                im.setScaleY(1.1);
            }
            im.setOpacity(0.55);
            im.setImage(image);
            hintPiece.getChildren().add(im);
        }
    }

    private void makeButton() {
        restart.setLayoutX(40);
        restart.setLayoutY(460);
        restart.setFont(Font.font("Silom",12));
        restart.setStyle("-fx-base: black;");
        hint.setLayoutX(140);
        hint.setLayoutY(460);
        hint.setFont(Font.font("Silom",12));
        hint.setStyle("-fx-base: black;");
        instruction.setLayoutX(20);
        instruction.setLayoutY(660);
        instruction.setStyle("-fx-base: black;");
        instruction.setOnMousePressed( event -> {
            Image i = new Image(Board.class.getResource(URI_BASE+"instruction.png").toString());
            ImageView im = new ImageView(i);
            im.setImage(i);
            instructionGroup.getChildren().add(im);
        });
        instruction.setOnMouseReleased( event -> {
            instructionGroup.getChildren().clear();
        });
        restart.setOnAction(event -> {
            if(difficultyLevel==(int)Math.floor(difficulty.getValue())) {
                pieces.getChildren().clear();
                makePieces();
                pegs.getChildren().clear();
                makePegs();
                placementString = "";
                PiecePlacement = "";
                isBoardEmpty = true;
                System.out.println(placementString + "No change in Difficulty Level");
            } else  {
                pieces.getChildren().clear();
                makePieces();
                isBoardEmpty = true;
                pegs.getChildren().clear();
                difficultyLevel = (int)Math.floor(difficulty.getValue());
                placementString= "";
                PiecePlacement = "";
                makePegs();
            }
        });
        hint.setOnMousePressed(event ->{
            setHintPiece();
        });
        hint.setOnMouseReleased(event->{
            hintPiece.getChildren().clear();
        });
        mute.setFont(Font.font("Silom",10));
        mute.setLayoutX(140);
        mute.setLayoutY(670);
        mute.setStyle("-fx-base: black;");
        mute.setOnAction(event -> {
            toggleSoundLoop();
        });
    }

    private void addKeyHandler(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            KeyCode keycode = keyEvent.getCode();
            if(TwistGame.isSolutionValid(PiecePlacement+initialPegs)) {

                Label congrats = new Label("Congratulations!");
                congrats.setFont(Font.font("Silom",100));
                congrats.setTextFill(Color.BLUEVIOLET);
                congrats.setLayoutX(10);
                congrats.setLayoutY(480);
                congrats.toFront();
                Glow glow = new Glow();
                glow.setLevel(1.5);

                congrats.setEffect(glow);
                pieces.getChildren().add(congrats);

                System.out.println("Congratulations!");
            }
            else if(keycode.equals(KeyCode.SLASH)) {
                String piece = InitialPegs.getNextStep();
                Image image = new Image(Board.class.getResource(URI_BASE+piece.charAt(0)+".png").toString());
                ImageView im = (DraggablePiece) pieces.getChildren().get(piece.charAt(0)-'a');
                im.setRotate(Character.getNumericValue(piece.charAt(3))*90);
                double rotateAddition = 0;
                if(im.getRotate()%180!=0){
                    rotateAddition = 1.1*(image.getWidth()-image.getHeight())/2;
                }
                im.setLayoutX((piece.charAt(1)-'1')*110+26.5+image.getWidth()*0.05-rotateAddition);
                im.setLayoutY((piece.charAt(2)-'A')*110+image.getHeight()*0.05+rotateAddition);
                im.setScaleX(1.1);
                if(Character.getNumericValue(piece.charAt(3))>=4){
                    im.setScaleY(-1.1);
                }else{
                    im.setScaleY(1.1);
                }
                PiecePlacement = InitialPegs.getNewPlacement(PiecePlacement, piece);
                isBoardEmpty = false;
                System.out.println("Piece Placement is: "+ PiecePlacement);
                pieces.getChildren().set(piece.charAt(0)-'a', im);
                if(TwistGame.isSolutionValid(PiecePlacement+initialPegs)) {

                    Label congrats = new Label("Congratulations!");
                    congrats.setFont(Font.font("Silom",100));
                    congrats.setTextFill(Color.BLUEVIOLET);
                    congrats.setLayoutX(10);
                    congrats.setLayoutY(480);
                    congrats.toFront();
                    Glow glow = new Glow();
                    glow.setLevel(1.5);

                    congrats.setEffect(glow);
                    pieces.getChildren().add(congrats);
                    System.out.println("Congratulations!");
                }
            }
        });
    }

    private void addBoardSetChildren() {
        boardSet.getChildren().addAll(difficulty, line1, line2, line3, restart, hint, easy, medium, hard, legendary, pieces, pegs, hintPiece, toMenu, mute);
        startMenu.getChildren().addAll(Title, NewGame, quit, Resume, instruction, instructionGroup);
    }

    @Override
    public void start(Stage primaryStage){

        setUpBGM();

        ImageView im = new ImageView();
        im.setImage(new Image(BASEBOARD_URI));
        im.setOpacity(0.5);
        startMenu.getChildren().add(im);

        ImageView iim = new ImageView();
        iim.setImage(new Image(BASEBOARD_URI));
        iim.setOpacity(0.3);
        boardSet.getChildren().add(iim);

        NewGame.setFont(Font.font("Silom",15));
        NewGame.setLayoutX(420.5);
        NewGame.setLayoutY(250);
        NewGame.setScaleX(2);
        NewGame.setScaleY(2);
        NewGame.setStyle("-fx-base: black;");
        NewGame.setOnAction(event -> {
            if(isBoardEmpty) {
                primaryStage.setScene(window);
                PiecePlacement="";
            } else {
                boolean result = makeNewGameBox();
                if (result) {
                    pieces.getChildren().clear();
                    makePieces();
                    pegs.getChildren().clear();
                    makePegs();
                    primaryStage.setScene(window);
                } else {
                    primaryStage.setScene(menu);
                }
            }
        });

        Resume.setFont(Font.font("Silom",15));
        Resume.setLayoutX(430);
        Resume.setLayoutY(350);
        Resume.setScaleX(2);
        Resume.setScaleY(2);
        Resume.setStyle("-fx-base: black;");
        Resume.setOnAction(event -> {
            if(isBoardEmpty) {
                boolean result = makeResumeBox();
                if(result) {
                    primaryStage.setScene(window);
                } else {
                    primaryStage.setScene(menu);
                }
            } else {
                primaryStage.setScene(window);
            }
        });

        quit.setFont(Font.font("Silom",15));
        quit.setLayoutX(443.5);
        quit.setLayoutY(450);
        quit.setScaleX(2);
        quit.setScaleY(2);
        quit.setStyle("-fx-base: black;");
        quit.setOnAction(event -> {
            boolean result = makeQuitBox();
            if(result) {
                primaryStage.close();
            } else {
                primaryStage.setScene(menu);
            }
        });

        toMenu.setFont(Font.font("Silom",10));
        toMenu.setLayoutX(40);
        toMenu.setLayoutY(670);
        toMenu.setStyle("-fx-base: black;");
        toMenu.setOnAction(event -> {
            primaryStage.setScene(menu);
        });

        for(int i = 0; i < 32; i++) {
            Circle c = new Circle();
            c.setCenterX(26.5 + 55 + 110*(i%8));
            c.setCenterY(55 + 110*(Math.floor(i/8)));
            c.setRadius(15);
            c.setFill(Color.STEELBLUE);
            boardSet.getChildren().add(c);
        }

        for(int n = 1; n <= 3; n++) {
            Line line = new Line(250+170.75*n, 440, 250+170.75*n, 700);
            boardSet.getChildren().add(line);
        }

        addKeyHandler(window);

        makeDifficulty();
        makeLevels();
        makeButton();
        addBoardSetChildren();
        makePieces();
        makePegs();

        String path = Board.class.getResource(URI_BASE + "OMFG - Hello.mp3").toString();
        Media BGM = new Media(path);
        MediaPlayer mp = new MediaPlayer(BGM);
        mp.setAutoPlay(true);
        mp.setVolume(1);

        primaryStage.setTitle("Twist Game");
        primaryStage.setScene(menu);
        primaryStage.show();
    }
}