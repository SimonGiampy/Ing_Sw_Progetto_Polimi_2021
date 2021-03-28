package it.polimi.ingsw;

/**
 * Strategy Pattern for the activation of the special ability of a leader card
 */
public interface AbilityEffectActivation {
	
	/**
	 *  This method activates the special ability for a Leader Card. Once the special ability is activated, the other classes in the game model
	 *  behave accordingly to the ability. Every class that implements this method, communicates to the corresponding management class that the
	 *  specific ability has been enabled. This way those classes change their behavior and apply the effects of the leader ability.
	 */
	void activateAbility();

}
