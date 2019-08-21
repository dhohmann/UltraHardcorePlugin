package io.github.dhohmann.hardcore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Holds player data.
 * 
 * @author Daniel Hohmann
 * @since 0.1.0
 */
public class PlayerData {

	private Object id;
	private Map<String, Object> properties = new HashMap<>();

	/**
	 * Constructs a new player data object using a unique id as the identifier.
	 * 
	 * @param id Unique id of the player
	 */
	public PlayerData(UUID id) {
		this.id = id;
	}

	/**
	 * Constructs a new player data object using a player name as the identifier.
	 * 
	 * @param name Name of the player
	 */
	public PlayerData(String name) {
		this.id = name;
	}

	/**
	 * Sets the property with the name <code>key</code> to the provided object.
	 * 
	 * @param key    Name of the property
	 * @param object Value for the property
	 */
	public void setProperty(String key, Object object) {
		properties.put(key, object);
	}

	/**
	 * Retrieves the value associated to the property.
	 * 
	 * @param key Name of the property
	 * @return <code>null</code>, if the property has no value attached, the value
	 *         otherwise
	 */
	public Object getProperty(String key) {
		return properties.get(key);
	}

	/**
	 * Retrieves the property associated with the player.
	 * 
	 * @return Map with all properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * Retrieves the identifier of the player data.
	 * 
	 * @return Player id
	 */
	public Object getId() {
		return id;
	}

}
