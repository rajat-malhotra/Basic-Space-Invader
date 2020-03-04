import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;


public class SpaceInvadersApp extends Application {

    private Pane root = new Pane();

    private double time = 0;

    private Objects_space ship = new Objects_space(300, 550, 30, 30, "ship", Color.BLUE);

    private Parent createContent() {
        root.setPrefSize(600, 600);
//
        root.getChildren().add(ship);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();

        Alliens();
        return root;
    }

    private void Alliens() {
        for (int j = 0; j < 10; ++j) {
            Objects_space s = new Objects_space(55 + j * 50, 50, 30, 30, "Alien", Color.RED);
            root.getChildren().add(s);
        }
    }

    private List<Objects_space> Objects_space() {
        return root.getChildren().stream().map(n -> (Objects_space)n).collect(Collectors.toList());
    }

    private void update() {
        time += 0.010;

        for(Objects_space objec : Objects_space()){
            switch (objec.type) {
                case "shipbullet":
                    objec.moveUp();
                    Objects_space().stream().filter(e -> e.type.equals("Alien")).forEach(Alien -> {
                        if (objec.getBoundsInParent().intersects(Alien.getBoundsInParent())) {
                            Alien.dead = true;
                            objec.dead = true;
                        }
                    });
                    break;

                case "Alienbullet":
                    objec.moveDown();

                    if (objec.getBoundsInParent().intersects(ship.getBoundsInParent())) {
                        ship.dead = true;
                        objec.dead = true;
                    }
                    break;

                case "Alien":

                    if (time > 2) {
                        if (Math.random() < 0.2) {
                            shoot(objec);
                        }
                    }

                    break;
            }
        };

        root.getChildren().removeIf(n -> {
            Objects_space objec = (Objects_space) n;
            return objec.dead;
        });

        if (time > 2) {
            time = 0;
        }
    }

    private void shoot(Objects_space who) {
        Objects_space s = new Objects_space((int) who.getTranslateX() + 5, (int) who.getTranslateY(), 5, 20, who.type + "bullet", Color.BLACK);

        root.getChildren().add(s);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(key -> {
            switch (key.getCode()) {
                case A:
                    ship.moveLeft();
                    break;
                case D:
                    ship.moveRight();
                    break;
                case SPACE:
                    shoot(ship);
                    break;
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
