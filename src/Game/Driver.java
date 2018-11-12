package Game;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Driver extends Application {
	
	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	
	private ArrayList<Node> platforms = new ArrayList<Node>();
	private ArrayList<Node> grassGrounds = new ArrayList<Node>();
	private ArrayList<Node> airs = new ArrayList<Node>();
	private ArrayList<Node> waters = new ArrayList<Node>();
	
	private Pane appRoot = new Pane();
	private Pane gameRoot = new Pane();
	private Pane uiRoot = new Pane();
	
	private Node player; 
	
	private int levelWidth;
	
	private boolean running = true;
	private boolean inWater = false;	
	
	private void initContent() {
        Rectangle bg = new Rectangle(1280, 720);

        levelWidth = LevelData.SCENERY1[0].length() * 60;

        for (int i = 0; i < LevelData.SCENERY1.length; i++) {
            String line = LevelData.SCENERY1[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                    	Node grassGround = createEntity(j*60, i*60, 60, 60, Color.LIGHTGREEN);
                        grassGrounds.add(grassGround);
                        break;
                    case '1':
                        Node platform = createEntity(j*60, i*60, 60, 60, Color.BURLYWOOD);
                        platforms.add(platform);
                        break;
                        
                    case '2':
                    	Node air = createEntity(j*60, i*60, 60, 60, Color.LIGHTSKYBLUE);
                    	airs.add(air);
                    	break;
                    	
                    case '3':
                    	Node water = createEntity(j*60, i*60, 60, 60, Color.DODGERBLUE);
                    	waters.add(water);
                    	break;
                }
            }
        }
        
        
        player = createEntity(400, 600, 40, 60, Color.BLUE);

        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 640 && offset < levelWidth - 640) {
                gameRoot.setLayoutX(-(offset - 640));
            }
        });
        
        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
    }
	

	private void update() {
		
		if (isPressed(KeyCode.UP) && player.getTranslateY() >= 5) {
			if (inWater == true) {
				movePlayerY(-3);
			}
			else {
				movePlayerY(-5);
			}
		}
		
		if (isPressed(KeyCode.DOWN) && player.getTranslateY() <= levelWidth) {
			if (inWater == true) {
				movePlayerY(3);
			}
			else {
				movePlayerY(5);
			}
		}
		
		if (isPressed(KeyCode.LEFT) && player.getTranslateX() >= 5) {
			if (inWater == true) {
				movePlayerX(-3);
			}
			else {
				movePlayerX(-5);	
			}
		}
		
		if (isPressed(KeyCode.RIGHT) && player.getTranslateX() <= levelWidth) {
			if (inWater == true) {
				movePlayerX(3);
			}
			else {
				movePlayerX(5);
			}
		}
	}
	
	private void movePlayerX(int value) {
		boolean movingRight = value > 0;
		
		for (int i = 0; i < Math.abs(value); i++) {
			for (Node platform : platforms) {
				if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
					if (movingRight) {
						if (player.getTranslateX() + 40 == platform.getTranslateX()) {
							return;
						
					}
				}
					else {
						if (player.getTranslateX() == platform.getTranslateX() + 60 ) {
							return;
					}
				}
			}
		}
			player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
			
			for (Node air : airs) {
				if (player.getBoundsInParent().intersects(air.getBoundsInParent())) {
					if (movingRight) {
						if (player.getTranslateX() + 40 == air.getTranslateY()) {
							player.setTranslateX(player.getTranslateY() -1);
							return;
						}
					}
					else { 
						if (player.getTranslateX() == air.getTranslateY() + 60) {
							player.setTranslateX(player.getTranslateY() + 1);
							return;
						}
					}
				}
			}
			for (Node water : waters) {
				if (player.getBoundsInParent().intersects(water.getBoundsInParent())) {
						inWater = true;
						for (Node grassGround : grassGrounds) {
							if (player.getBoundsInParent().intersects(grassGround.getBoundsInParent())) {
								inWater = false;
							
						}
					}
				}
			}
		}
	}
	
	private void movePlayerY(int value) {
		boolean movingDown = value > 0;
		
		for (int i = 0; i < Math.abs(value); i++) {
			for (Node platform : platforms) {
				if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
					if (movingDown) {
						if (player.getTranslateY() + 60 == platform.getTranslateY()) {
							player.setTranslateY(player.getTranslateY() -1);
							return;
						}
					}
					else { 
						if (player.getTranslateY() == platform.getTranslateY() + 60) {
							player.setTranslateY(player.getTranslateY() + 1);
							return;
						}
					}
				}
			}
			
			player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
			
			for (Node air : airs) {
				if (player.getBoundsInParent().intersects(air.getBoundsInParent())) {
					if (movingDown) {
						if (player.getTranslateY() + 40 == air.getTranslateY()) {
							player.setTranslateY(player.getTranslateY() -1);
							return;
						}
					}
					else { 
						if (player.getTranslateY() == air.getTranslateY() + 60) {
							player.setTranslateY(player.getTranslateY() + 1);
							return;
						}
					}
				}
			}
			for (Node water : waters) {
				if (player.getBoundsInParent().intersects(water.getBoundsInParent())) {
						inWater = true;
						for (Node grassGround : grassGrounds) {
							if (player.getBoundsInParent().intersects(grassGround.getBoundsInParent())) {
								inWater = false;
							
						}
					}
				}
			}
		}
	}
		
	private Node createEntity(int x, int y, int w, int h, Color color) {
		Rectangle entity = new Rectangle(w, h);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(color);

		gameRoot.getChildren().add(entity);
		return entity;
	
	}
	
	private boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}
		
	@Override
	public void start(Stage primaryStage) throws Exception {	
		initContent();
		
		Scene scene = new Scene(appRoot);
		scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
		scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
		primaryStage.setTitle("Julian's Game"); 
		primaryStage.setScene(scene);
		primaryStage.show();
		
		AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    update();
                }

                
            }
        };
        timer.start();
    }
	
	public static void main (String [] args) {
			launch(args);	
	}

}

