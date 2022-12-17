package application;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


public class Card extends Parent implements Commons {

	
	// The background image to be used by all cards.
	
	private final static String BACKGROUN_PATH = "/img/backside.png";
	private final static ImagePattern BACKGROUND_PATTERN = new ImagePattern(new Image(Card.class.getResource(BACKGROUN_PATH).toExternalForm()));

	enum Suit {
		HEARTS, DIAMONDS, CLUBS, SPADES
	};

	enum Rank {
		TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10), ACE(11);

		final int value;

		private Rank(int value) {
			this.value = value;
		}
	};

	
	// The image for this card.
	 
	private final String IMAGE_PATH;
	private final ImagePattern IMAGE_PATTERN;

	// Rank of card.
	
	private final Rank rank;
	
	// Suit of card.
	
	private final Suit suit;

	// View of card.
	
	private Rectangle card;

	
	private SimpleBooleanProperty showCard = new SimpleBooleanProperty(true);

	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
		IMAGE_PATH = "/img/" + rank.toString().toLowerCase() + "_of_" + suit.toString().toLowerCase() + ".png";
		IMAGE_PATTERN = new ImagePattern(new Image(Card.class.getResource(IMAGE_PATH).toExternalForm()));

		card = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
		card.setArcWidth(15);
		card.setArcHeight(15);
		card.setFill(IMAGE_PATTERN);
		getChildren().add(card);

		// === Show card or back of card based on showCard's value ===
		showCard.addListener((obs, old, newValue) -> {
			if (newValue.booleanValue()) {
				card.setFill(IMAGE_PATTERN);
			} else {
				card.setFill(BACKGROUND_PATTERN);
			}
		});
	}

	public void showCard() {
		showCard.set(true);
	}

	public void hideCard() {
		showCard.set(false);
	}

	public boolean isHidden() {
		return !showCard.get();
	}

	public Suit suit() {
		return suit;
	}

	public Rank rank() {
		return rank;
	}

	public int value() {
		return rank.value;
	}
}