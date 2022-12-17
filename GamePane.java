package application;

import java.util.ArrayList;
import application.Card.Rank;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class GamePane extends Pane implements Commons {

	private Pane leftPane;
	private BorderPane leftPaneLayout;
	private StackPane leftPaneStack;

	private Pane rightPane;
	private BorderPane rightPaneLayout;
	private StackPane rightPaneStack;

	private Deck deck;
	private Player player, dealer;

	private HBox playerCardsContainer, dealerCardsContainer;

	private VBox playerHandsContainer, containerGameButtons, containerInitButtons;

	private Slider betSlider;

	private Label gameMessageLabel, betSliderLabel, moneyLabel, playerScoreLabel, dealerScoreLabel;

	private Button playButton, resetButton, hitButton, standButton, doubleDownButton;

	private SimpleBooleanProperty isGameOver, isGame, isAnimation, isTwoCardsHands;

	private void init() {
		//left and Right Pane
		leftPane = new Pane();
		rightPane = new Pane();
		
		//left and right StackPane
		leftPaneStack = new StackPane();
		rightPaneStack = new StackPane();
		
		//left and right BorderPane
		leftPaneLayout = new BorderPane();
		rightPaneLayout = new BorderPane();
		

		// HBox-es
		playerCardsContainer = new HBox();
		dealerCardsContainer = new HBox();
		
		dealerCardsContainer = new HBox();
		dealerCardsContainer.setMinSize(585, CARD_HEIGHT + 40);
//		dealerCardsContainer.setBackground(new Background(new BackgroundFill(Color.RED, null, Insets.EMPTY)));
		dealerCardsContainer.setPadding(new Insets(0, 10, 0, 10));
		dealerCardsContainer.setSpacing(10);

		playerCardsContainer = new HBox();
		playerCardsContainer.setMinSize(585, CARD_HEIGHT + 40);
//		playerCardsContainer.setBackground(new Background(new BackgroundFill(Color.RED, null, Insets.EMPTY)));
		playerCardsContainer.setPadding(new Insets(0, 10, 0, 10));
		playerCardsContainer.setSpacing(10);
		
		

		// VBox-es
		playerHandsContainer = new VBox();
		containerGameButtons = new VBox();
		containerInitButtons = new VBox();

		// Labels

		betSliderLabel = new Label("Current Bet: 0");
		betSliderLabel.setPadding(new Insets(30, 0, 0, 0));
		betSliderLabel.setFont(new Font(20));
		betSliderLabel.setTextFill(Color.WHITE);

		moneyLabel = new Label("Cash: 100");
		moneyLabel.setPadding(new Insets(30, 0, 0, 0));
		moneyLabel.setFont(new Font(20));
		moneyLabel.setTextFill(Color.WHITE);

		gameMessageLabel = new Label("Press Play To Play");
		gameMessageLabel.setPadding(new Insets(10, 0, 10, 220));
		gameMessageLabel.setFont(new Font(20));
		gameMessageLabel.setTextFill(Color.WHITE);

		playerScoreLabel = new Label("Player: 0");
		playerScoreLabel.setPadding(new Insets(10, 0, 10, 260));
		playerScoreLabel.setFont(new Font(20));
		playerScoreLabel.setTextFill(Color.WHITE);

		dealerScoreLabel = new Label("Dealer: 0");
		dealerScoreLabel.setPadding(new Insets(10, 0, 10, 260));
		dealerScoreLabel.setFont(new Font(20));
		dealerScoreLabel.setTextFill(Color.WHITE);

		

		// Slider
		betSlider = new Slider();
		betSlider.setMaxWidth(160);

		// Buttons
		playButton = new Button();
		playButton.setText("Play");
		playButton.setMinWidth(160);
		playButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				startNewGame();
			}
		});

		resetButton = new Button("Reset");
		resetButton.setMinWidth(160);
		resetButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resetGame();
			}
		});
		resetButton.setVisible(false);

		hitButton = new Button("Hit");
		hitButton.setMinWidth(160);
		hitButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playerHit();
			}
		});

		standButton = new Button("Stand");
		standButton.setMinWidth(160);
		standButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playerStand();
			}
		});

		doubleDownButton = new Button("Double Down");
		doubleDownButton.setMinWidth(160);
		doubleDownButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playerDoubleDown();
			}
		});
		
		isGame = new SimpleBooleanProperty(false);
		isGameOver = new SimpleBooleanProperty(false);
		isTwoCardsHands = new SimpleBooleanProperty(false);
		isAnimation = new SimpleBooleanProperty(false);

	}

	private void initialize() {
		init();

		
		//Setting Left Pane Layout
				leftPane.setMinSize(585, 480);
				leftPane.setBackground(new Background(new BackgroundFill(Color.web("#023020", 1), null, Insets.EMPTY)));
				leftPaneStack.getChildren().add(leftPaneLayout);
				leftPane.getChildren().add(leftPaneStack);

				leftPaneLayout.setTop(dealerScoreLabel);

				playerHandsContainer.getChildren().addAll(dealerCardsContainer, gameMessageLabel, playerCardsContainer);

				leftPaneLayout.setCenter(playerHandsContainer);

				leftPaneLayout.setBottom(playerScoreLabel);

				//Setting Right Pane Layout
//				rightPane.setMinSize(185, 480);
				rightPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, Insets.EMPTY)));
				rightPaneStack.getChildren().add(rightPaneLayout);
				rightPane.getChildren().add(rightPaneStack);

				rightPaneLayout.setMinSize(185, 480);
				rightPaneLayout.setPadding(new Insets(10, 10, 10, 10));
				
				// Right Side Pane Top
				rightPaneLayout.setTop(moneyLabel);

				// Right Side Pane center
				containerGameButtons.getChildren().addAll(betSliderLabel, betSlider, playButton,doubleDownButton, hitButton, standButton);
				containerGameButtons.setSpacing(15);

				rightPaneLayout.setCenter(containerGameButtons);

				// Right Side Pane Bottom

				containerInitButtons.getChildren().addAll(playButton, resetButton);
				containerInitButtons.setSpacing(10);

				rightPaneLayout.setBottom(containerInitButtons);

				HBox row = new HBox();
				row.getChildren().addAll(leftPane, rightPane);
				row.setSpacing(10);
				GridPane grid = new GridPane();
				grid.getChildren().add(row);
				grid.setPadding(new Insets(10, 10, 10, 10));
				this.getChildren().add(grid);
				this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, Insets.EMPTY)));
	}

	public GamePane(String playerName, String amount) {
		initialize();
		
		isGame = new SimpleBooleanProperty(false);
		isGameOver = new SimpleBooleanProperty(false);
		isTwoCardsHands = new SimpleBooleanProperty(false);
		isAnimation = new SimpleBooleanProperty(false);
		
		deck = new Deck();
		player = new Player(playerName, Integer.parseInt(amount));
		dealer = new Player("Dealer", Integer.MAX_VALUE);
		
		player.setHand(new Hand(playerCardsContainer.getChildren()));
		dealer.setHand(new Hand(dealerCardsContainer.getChildren()));
		
		moneyLabel.setText("CASH: " + player.money());
		gameMessageLabel.setText("PRESS PLAY TO PLAY");
		betSliderLabel.setText("CURRENT BET: " + (int) betSlider.getValue());
		playerUpdateScoreMessage(dealer, dealerScoreLabel);
		playerUpdateScoreMessage(player, playerScoreLabel);
		betSlider.setMax(player.money());

		
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
		
		BooleanBinding notInGameInAnimationInGameOver = Bindings.or(Bindings.or(isGame.not(), isAnimation), isGameOver);
		BooleanBinding inGameInGameOver = Bindings.or(isGame, isGameOver);
		
		playButton.disableProperty().bind(inGameInGameOver);
		doubleDownButton.disableProperty().bind(Bindings.or(isTwoCardsHands.not(), notInGameInAnimationInGameOver));
		hitButton.disableProperty().bind(notInGameInAnimationInGameOver);
		standButton.disableProperty().bind(notInGameInAnimationInGameOver);
		betSliderLabel.disableProperty().bind(inGameInGameOver);
		betSlider.disableProperty().bind(inGameInGameOver);
	}
	
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
	
	private void addCardToView(Card drawn, SequentialTransition revealOneAtATime, Player player, String drawSound) {
		FadeTransition ft = new FadeTransition(Duration.millis(200), drawn);
		ft.setFromValue(0);
		ft.setToValue(1.0);
		player.hand().addCardToView(drawn);
	}
	
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
	
	
	private void gameOver() {
		isGameOver.set(true);
		gameMessageLabel.setText("NO MONEY. NO PLAY. GAME OVER");
		playButton.setVisible(false);
		resetButton.setVisible(true);
	}

	private void resetGame() {
		player.setMoney(PLAYER_START_MONEY);
		gameMessageLabel.setText("PRESS PLAY TO PLAY");
		resetButton.setVisible(false);
		playButton.setVisible(true);
		isGameOver.set(false);
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

	private void playerHit() {
		isAnimation.set(true);
		Card drawn = deck.drawCard();
		SequentialTransition revealOneAtATime = new SequentialTransition();
		addCardToView(drawn, revealOneAtATime, player, PLAYER_DRAW_SOUND);
		revealOneAtATime.setOnFinished(revealOneAtATimeEvent -> {
			player.hand().addAllCards(drawn);
			isAnimation.set(false);
			if (player.hand().valueProperty().get() > PLAYER_HAND_MAX_VALUE) {
				endGame();
			} else if (player.hand().valueProperty().get() == PLAYER_HAND_MAX_VALUE) {
//				playerStand();
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

	private void playerUpdateScoreMessage(Player player, Label scoreLabel) {
		scoreLabel.setText(player.name() + ": " + player.hand().visibleValue());
	}
}
