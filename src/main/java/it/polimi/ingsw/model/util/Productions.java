package it.polimi.ingsw.model.util;

public enum Productions {

	STACK_1_CARD_PRODUCTION("Development Card Slot 1"),
	STACK_2_CARD_PRODUCTION("Development Card Slot 2"),
	STACK_3_CARD_PRODUCTION("Development Card Slot 3"),
	BASE_PRODUCTION("Base Production"),
	LEADER_CARD_1_PRODUCTION("Leader 1's Extra Production"),
	LEADER_CARD_2_PRODUCTION("Leader 2's Extra Production");
	
	private final String text;
	
	Productions(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}
}

