package io.github.dhohmann.hardcore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Calculates punishments for light mode.
 * 
 * @author Daniel Hohmann
 * @since 0.1.0
 */
public class Punishments {

	/**
	 * Minimum number of ticks a punishment will last
	 */
	public static int MIN_DURATION_TICKS = 1000;

	/**
	 * Returns a list of punishments the user has to receive.
	 * 
	 * @param level Punishment level of the player (Number of deaths)
	 * @return Punishments
	 */
	public static List<PotionEffect> getPunishments(int level) {
		List<PotionEffect> effects = new ArrayList<>();
		int duration = (int) Math.pow(level, 2) * MIN_DURATION_TICKS;
		switch (level) {
		case 6:
			effects.add(new PotionEffect(PotionEffectType.GLOWING, duration, level));
		case 5:
			effects.add(new PotionEffect(PotionEffectType.SLOW, duration, level));
		case 4:
			effects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, level));
		case 3:
			effects.add(new PotionEffect(PotionEffectType.WEAKNESS, duration, level));
		case 2:
			effects.add(new PotionEffect(PotionEffectType.UNLUCK, duration, level));
		case 1:
			effects.add(new PotionEffect(PotionEffectType.HUNGER, duration, level));
			break;
		default:
			if (level > 10) {
				duration = Integer.MAX_VALUE;
			}
			effects.add(new PotionEffect(PotionEffectType.CONFUSION, duration, level));
			break;
		}
		return effects;
	}

}
