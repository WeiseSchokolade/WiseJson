package de.schoko.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonObject extends JsonComponent implements JsonSerializable {
	private Map<String, JsonComponent> map;
	
	public JsonObject(JsonComponent... components) {
		this(null, components);
	}
	
	public JsonObject(String key, JsonComponent... components) {
		super(key);
		map = new HashMap<>();
		put(components);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof JsonObject)) return false;
		JsonObject object = (JsonObject) obj;
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			if (!object.map.containsKey(string)) {
				return false;
			}
			if (!object.map.get(string).equals(map.get(string))) {
				return false;
			}
		}
		iterator = object.map.keySet().iterator();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			if (!map.containsKey(string)) {
				return false;
			}
			if (!map.get(string).equals(object.map.get(string))) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	JsonObject copy() {
		JsonObject copy = new JsonObject(getKey());
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			copy.map.put(string, map.get(string).copy());
		}
		return copy;
	}
	
	@Override
	public void overwriteValue(JsonComponent component) {
		map.putAll(((JsonObject) component).map);
	}
	
	@Override
	JsonComponent findDifference(JsonComponent component) {
		if (component instanceof JsonObject object) {
			return remove(object);
		}
		return this.copy();
	}
	
	/**
	 * Removes all identical values
	 * @return A copy of this object without the removed values
	 */
	public JsonObject remove(JsonObject object) {
		JsonObject copy = new JsonObject(getKey());
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			JsonComponent value = map.get(key);
			if (value == null) continue;
			if (object.contains(key)) {
				if (value.equals(object.get(key))) continue;
				JsonComponent difference = value.findDifference(object.get(key));
				if (difference != null) {
					copy.map.put(key, difference);
				}
			} else {
				copy.map.put(key, value);
			}
		}
		return copy;
	}

	@Override
	JsonComponent merge(JsonComponent component) {
		if (component instanceof JsonObject object) {
			return merge(object);
		}
		return this.copy();
	}
	
	/**
	 * Merge the values of {@code object} into the values of this object and return a copy
	 */
	public JsonObject merge(JsonObject object) {
		JsonObject copy = copy();
		Iterator<String> iterator = object.map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (copy.map.containsKey(key)) {
				copy.map.put(key, copy.map.get(key).merge(object.get(key)));
			} else {
				copy.map.put(key, object.get(key).copy());
			}
		}
		return copy;
	}

	public void put(JsonComponent... components) {
		for (int i = 0; i < components.length; i++) {
			map.put(components[i].getKey(), components[i]);
		}
	}
	
	public JsonComponent remove(String key) {
		return map.remove(key);
	}
	
	public boolean contains(String key) {
		return map.containsKey(key);
	}
	
	/**
	 * @return The value with the specified key or null if it doesn't exist
	 */
	public JsonComponent get(String key) {
		return map.get(key);
	}

	public JsonList getList(String key) {
		return (JsonList) map.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(String key, Class<T> clazz) {
		try {
			return ((JsonValue<T>) map.get(key)).getValue();
		} catch (RuntimeException e) {
			System.err.println("Key: " + key);
			System.err.println(this);
			throw e;
		}
	}
	
	public JsonObject getObject(String key) {
		return (JsonObject) get(key);
	}
	
	@Override
	String getStringValue() {
		String result = "{";
		int i = 0;
		for (String key : map.keySet()) {
			i++;
			result += map.get(key).toString();
			if (i < map.size()) {
				result += ", ";
			}
		}
		return result + "}";
	}
	
	public Map<String, JsonComponent> getContentMap() {
		return map;
	}
	
	@Override
	public JsonObject toJson() {
		return this;
	}
}
