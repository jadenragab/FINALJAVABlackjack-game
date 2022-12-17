package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Card.Rank;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class Controller implements Commons, Initializable {

	// These are the Game Players Objects 
	private Deck deck;
	private Player player, dealer;

	@FXML
	private Pane root;
	
	@FXML
	private Region background;
	
	@FXML
	private BorderPane leftSidePanel, rightSidePanel;
	
	@FXML
	private HBox  playerCardsContainer, dealerCardsContainer,  containerBtnHitAndStand, containerLeftAndRightPane;
	
	@FXML
	private VBox playerHandsContainer, containerGameButtons, containerInitButtons;
	
	@FXML
	private Rectangle rightSideRectangleBG, leftSideRectangleBG;

	@FXML
	private Slider betSlider;
	
	@FXML
	private Label  gameMessageLabel, betSliderLabel, dealerScoreLabel, moneyLabel,  playerScoreLabel;
	
	@FXML
	private Button playButton, resetButton, hitButton, standButton, doubleDownButton;
	
	private SimpleBooleanProperty isGameOver, isGame, isAnimation, isTwoCardsHands; 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		isGame = new SimpleBooleanProperty(false);
		isGameOver = new SimpleBooleanProperty(false);
		isTwoCardsHands = new SimpleBooleanProperty(false);
		isAnimation = new SimpleBooleanProperty(false);
		
		deck = new Deck();
		player = new Player(System.getProperty("user.name"), 150);
		dealer = new Player("Dealer", Integer.MAX_VALUE);

		player.setHand(new Hand(playerCardsContainer.getChildren()));
		dealer.setHand(new Hand(dealerCardsContainer.getChildren()));

		root.setPrefSize(APP_WIDTH, APP_HEIGHT);
		background.setPrefSize(APP_WIDTH, APP_HEIGHT);
		leftSideRectangleBG.setWidth(APP_WIDTH * 0.7 - 1.5 * APP_SPACING);
		leftSideRectangleBG.setHeight(APP_HEIGHT - 2 * APP_SPACING);
		rightSideRectangleBG.setWidth(APP_WIDTH * 0.3 - 1.5 * APP_SPACING);
		rightSideRectangleBG.setHeight(APP_HEIGHT - 2 * APP_SPACING);


		moneyLabel.setText("CASH: " + player.money());
		gameMessageLabel.setText("PRESS PLAY TO PLAY");
		betSliderLabel.setText("CURRENT BET: " + (int) betSlider.getValue());
		playerUpdateScoreMessage(dealer, dealerScoreLabel);
		playerUpdateScoreMessage(player, playerScoreLabel);
		betSlider.setMax(player.money());


		BooleanBinding notInGameInAnimationInGameOver = Bindings.or(Bindings.or(isGame.not(), isAnimation), isGameOver);
		BooleanBinding inGameInGameOver = Bindings.or(isGame, isGameOver);

		playButton.disableProperty().bind(inGameInGameOver);
		doubleDownButton.disableProperty().bind(Bindings.or(isTwoCardsHands.not(), notInGameInAnimationInGameOver));
		hitButton.disableProperty().bind(notInGameInAnimationInGameOver);
		standButton.disableProperty().bind(notInGameInAnimationInGameOver);
		betSliderLabel.disableProperty().bind(inGameInGameOver);
		betSlider.disableProperty().bind(inGameInGameOver);

		player.hand().valueProperty().addListener((obs, old, newValue) -> {
			playerUpdateScoreMessage(player, playerScoreLabel);
		});

		player.hand().sizeProperty().addListener((obs, old, newValue) -> {
			isTwoCardsHands.set(newValue.intValue() == 2 ? true : false);
			if (newValue.intValue() == PLAYER_HAND_SIZE_BLACK_JACK && player.hand().value() == PLAYER_HAND_MAX_VALUE) {
				endGame();
			}
		});

		player.currentBetProperty().addListener((obs, old, newValue) -> {
			betSliderLabel.setText("CURRENT BET: " + player.currentBet());
		});

		dealer.hand().valueProperty().addListener((obs, old, newValue) -> {
			playerUpdateScoreMessage(dealer, dealerScoreLabel);
			if (newValue.intValue() >= PLAYER_HAND_MAX_VALUE) {
				endGame();
			}
		});

		player.moneyProperty().addListener((obs, old, newValue) -> {
			betSlider.setMax(newValue.intValue());
			moneyLabel.setText("CASH: " + newValue.intValue());
		});

		betSlider.valueProperty().addListener((obs, old, newValue) -> {
			player.setCurrentBet((int) betSlider.getValue());
		});
	}

	
// Called whenever a new game is started. Reset game and deal two new cards to both players.
	 
	private void startNewGame() {
		isGame.set(true);
		isAnimation.set(true);
		gameMessageLabel.setText("");
		
		deck.reset();
		deck.shuffle();
		dealer.resetHand();
		player.resetHand();
		
		ArrayList<Card> playerDrawn = new ArrayList<Card>();
		ArrayList<Card> dealerDrawn = new ArrayList<Card>();

		SequentialTransition revealOneAtATime = new SequentialTransition();
		for (int i = 0; i < 4; i++) {
			Card drawn = deck.drawCard();

			Player giveCardTo = i % 2 == 0 ? dealer : player;
			String drawSound = i % 2 == 0 ? DEALER_DRAW_SOUND : PLAYER_DRAW_SOUND;
			if (i % 2 == 0)
				dealerDrawn.add(drawn);
			else
				playerDrawn.add(drawn);

			addCardToView(drawn, revealOneAtATime, giveCardTo, drawSound);
		}

		dealer.hand().hideFirstCard();
		revealOneAtATime.setOnFinished(event -> {
			player.hand().addAllCards(playerDrawn.toArray(new Card[playerDrawn.size()]));
			dealer.hand().addAllCards(dealerDrawn.toArray(new Card[dealerDrawn.size()]));
			isAnimation.set(false);
		});
		revealOneAtATime.play();
	}

	
	 //Called whenever a end game event is triggered. Reveal dealers hand, check who won and update players money.
	 
	private void endGame() {
		isGame.set(false);

		dealer.hand().showFirstCard();
		int dealerValue = dealer.hand().valueProperty().get();
		int playerValue = player.hand().valueProperty().get();
		Player winner = null;

		if (dealerValue == PLAYER_HAND_MAX_VALUE || playerValue > PLAYER_HAND_MAX_VALUE || dealerValue == playerValue
				|| (dealerValue < PLAYER_HAND_MAX_VALUE && dealerValue > playerValue)) {
			winner = dealer;
			player.setMoney(player.moneyProperty().get() - player.currentBetProperty().get());
		} else {
			winner = player;
			player.setMoney(player.moneyProperty().get() + player.currentBetProperty().get());
		}

		gameMessageLabel.setText(winner.name() + " WON");

		if (player.moneyProperty().get() <= 0) {
			gameOver();
		}
	}

	
	 // Called when game over. Displays reset button and show game over message.
	 
	private void gameOver() {
		isGameOver.set(true);
		gameMessageLabel.setText("NO MONEY. NO PLAY. GAME OVER");
		playButton.setVisible(false);
		resetButton.setVisible(true);
	}

	
	 // Called when reset button is pressed. Reset game stats.
	 
	private void resetGame() {
		player.setMoney(PLAYER_START_MONEY);
		gameMessageLabel.setText("PRESS PLAY TO PLAY");
		resetButton.setVisible(false);
		playButton.setVisible(true);
		isGameOver.set(false);
	}

	private void playerUpdateScoreMessage(Player player, Label scoreLabel) {
		scoreLabel.setText(player.name() + ": " + player.hand().visibleValue());
	}

	private void addCardToView(Card drawn, SequentialTransition revealOneAtATime, Player player, String drawSound) {
		FadeTransition ft = new FadeTransition(Duration.millis(200), drawn);
		ft.setFromValue(0);
		ft.setToValue(1.0);
		PauseTransition pt = new PauseTransition(Duration.millis(300));
		player.hand().addCardToView(drawn);
	}


	private void playerStand() {
		ArrayList<Card> dealerDrawn = new ArrayList<Card>();
		SequentialTransition revealOneAtATime = new SequentialTransition();
		isAnimation.set(true);
		PauseTransition showFirstCard = new PauseTransition(Duration.millis(100));
		showFirstCard.setOnFinished((ptEvent -> {
			dealer.hand().showFirstCard();
			playerUpdateScoreMessage(dealer, dealerScoreLabel);
		}));
		revealOneAtATime.getChildren().addAll(showFirstCard, new PauseTransition(Duration.millis(300)));
		int current = dealer.hand().valueProperty().get();
		int numAces = dealer.hand().aces();

		while (current < DEALER_HAND_STOP) {
			Card drawn = deck.drawCard();
			dealerDrawn.add(drawn);
			addCardToView(drawn, revealOneAtATime, dealer, DEALER_DRAW_SOUND);

			current += drawn.value();
			if (drawn.rank() == Rank.ACE)
				numAces++;

			if (current > PLAYER_HAND_MAX_VALUE && numAces > 0) {
				current -= 10;
				numAces--;
			}
		}
		final int sum = current;
		revealOneAtATime.setOnFinished(revealOneAtATimeEvent -> {
			dealer.hand().addAllCards(dealerDrawn.toArray(new Card[dealerDrawn.size()]));
			isAnimation.set(false);

			if (sum < PLAYER_HAND_MAX_VALUE) {
				endGame();
			}
		});

		revealOneAtATime.play();
	}

	
	 //Function will let player draw one card.
	private void playerHit() {
		isAnimation.set(true);
		System.out.println("playerHit Function clicked");
		Card drawn = deck.drawCard();
		SequentialTransition revealOneAtATime = new SequentialTransition();
		addCardToView(drawn, revealOneAtATime, player, PLAYER_DRAW_SOUND);
		revealOneAtATime.setOnFinished(revealOneAtATimeEvent -> {
			player.hand().addAllCards(drawn);
			isAnimation.set(false);
			if (player.hand().valueProperty().get() > PLAYER_HAND_MAX_VALUE) {
				endGame();
			} else if (player.hand().valueProperty().get() == PLAYER_HAND_MAX_VALUE) {
				playerStand();
			}
		});
		revealOneAtATime.play();
	}
	
	private void playerDoubleDown() {
		isAnimation.set(true);
		int newBet = Math.min(player.currentBetProperty().get() * 2, player.money());
		player.setCurrentBet(newBet);
		betSlider.setValue(newBet);
		Card drawn = deck.drawCard();
		SequentialTransition revealOneAtATime = new SequentialTransition();
		addCardToView(drawn, revealOneAtATime, player, PLAYER_DRAW_SOUND);
		revealOneAtATime.setOnFinished(revealOneAtATimeEvent -> {
			player.hand().addAllCards(drawn);
			isAnimation.set(false);
			if (player.hand().valueProperty().get() > PLAYER_HAND_MAX_VALUE) {
				endGame();
			} else {
				playerStand();
			}
		});
		revealOneAtATime.play();
	}

	@FXML
	private void doubleDownButtonEvent(ActionEvent click) {
		playerDoubleDown();
	}

	@FXML
	private void hitButtonEvent(ActionEvent click) {
		playerHit();
	}

	@FXML
	private void standButtonEvent(ActionEvent click) {
		playerStand();
	}

	@FXML
	private void playButtonEvent(ActionEvent click) {
		startNewGame();
	}

	@FXML
	private void resetButtonEvent(ActionEvent click) {
		resetGame();
	}
}