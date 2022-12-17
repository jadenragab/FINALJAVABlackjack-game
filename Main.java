package application;



import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application implements Commons {
	
	private Scene scene;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		

		GridPane formGrid = new GridPane();
		formGrid.setAlignment(Pos.CENTER);
		formGrid.setHgap(10);
		formGrid.setVgap(10);
		formGrid.setPadding(new Insets(25, 25, 25, 25));
		formGrid.setBackground(new Background(new BackgroundFill(Color.BLACK, null, Insets.EMPTY)));
		scene = new Scene(formGrid, 300, 275);
		
		primaryStage.setScene(scene);
		
		Text welcomeText = new Text("Welcome");
		welcomeText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		welcomeText.setFill(Color.WHITE);
		formGrid.add(welcomeText, 0, 0, 2, 1);

		Label userName = new Label("Player Name:");
		userName.setTextFill(Color.WHITE);
		formGrid.add(userName, 0, 1);

		TextField userTextField = new TextField();
		formGrid.add(userTextField, 1, 1);

		Label amountLabel = new Label("Amount");
		amountLabel.setTextFill(Color.WHITE);
		formGrid.add(amountLabel, 0, 2);
		
		TextField amount = new TextField();
		formGrid.add(amount, 1, 2);
		
		
		Button enterButton = new Button("Enter");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.CENTER);
		hbBtn.getChildren().add(enterButton);
		formGrid.add(hbBtn, 1, 3);
		
		final Text actiontarget = new Text();
		formGrid.add(actiontarget, 1, 6);
		
		enterButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
			 
		    @Override
		    public void handle(ActionEvent e) {
		    	String playerName = userTextField.getText();
		    	String amountToBet = amount.getText();
		    	
		    	
		    	if(playerName != null && !playerName.isEmpty() && !amountToBet.isEmpty()) {
		    		if(amountToBet.matches("\\d*")) {
		    			if( Integer.parseInt(amountToBet) > 90) {
		    				GamePane gamePane = new GamePane(playerName, amountToBet);
					    	scene = new Scene(gamePane);
					    	primaryStage.setScene(scene);
					    	primaryStage.centerOnScreen();
		    			}else {
				    		 actiontarget.setFill(Color.WHITE);
				    	        actiontarget.setText("Amount should be \ngreater than 90.");
				    	}
		    		}else {
			    		 actiontarget.setFill(Color.WHITE);
			    	        actiontarget.setText("Enter valid number \nin amount.");
			    	}
		    	}else {
		    		 actiontarget.setFill(Color.WHITE);
		    	        actiontarget.setText("Player name and amount \ncan not be empty.");
		    	}
		    }
		});
		
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.setResizable(false);
		primaryStage.setTitle("Black Jack");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}