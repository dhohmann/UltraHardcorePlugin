package io.github.dhohmann.hardcore;

/**
 * Enumeration of available modes the plugin can run.
 * 
 * @author Daniel Hohmann
 * @since 0.1
 */
public enum HardcoreMode {
	/**
	 * Normal hardcore mode
	 * <p>
	 * <b>Behaviour:</b> Players are moved to spectators when they die.
	 */
	NORMAL,
	/**
	 * Light hardcore mode.
	 * <p>
	 * <b>Behaviour:</b> A little bit confusing, but used to punish player death with de-buffs
	 */
	LIGHT;

}
