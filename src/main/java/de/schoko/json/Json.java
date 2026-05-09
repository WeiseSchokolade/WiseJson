package de.schoko.json;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class Json {
	private Json() {
		
	}
	
	public static JsonObject of(JsonComponent... jsonComponent) {
		return new JsonObject(jsonComponent);
	}

	public static JsonObject of(String key, JsonComponent... jsonComponent) {
		return new JsonObject(key, jsonComponent);
	}
	
	public static <T> JsonValue<T> valueOf(String key, T value) {
		return new JsonValue<>(key, value);
	}
	
	public static JsonObject of(String key, JsonSerializable serializables) {
		JsonObject json = serializables.toJson();
		json.setKey(key);
		return json;
	}

	public static JsonList listOf(String key, JsonComponent... components) {
		return new JsonList(key, components);
	}

	public static JsonList listOf(String key, JsonObject... components) {
		return new JsonList(key, components);
	}

	@SafeVarargs
	public static <T> JsonList listOf(String key, Function<T, JsonComponent> converter, T... objects) {
		JsonList list = new JsonList(key, objects.length);
		for (int i = 0; i < objects.length; i++) {
			list.getComponents()[i] = converter.apply(objects[i]);
		}
		return list;
	}
	
	public static JsonList listOf(String key, JsonSerializable... serializables) {
		JsonList list = new JsonList(key, serializables.length);
		for (int i = 0; i < serializables.length; i++) {
			JsonSerializable jsonSerializable = serializables[i];
			list.getComponents()[i] = jsonSerializable.toJson();
		}
		return list;
	}
	
	public static JsonList listOf(String key, Collection<? extends JsonSerializable> serializables) {
		JsonList list = new JsonList(key, serializables.size());
		Iterator<? extends JsonSerializable> iterator = serializables.iterator();
		int index = 0;
		while (iterator.hasNext()) {
			list.getComponents()[index++] = iterator.next().toJson();
		}
		return list;
	}

	public static <T> JsonList listOf(String key, Function<T, JsonComponent> converter, Collection<T> values) {
		JsonList list = new JsonList(key, values.size());
		Iterator<T> iterator = values.iterator();
		int index = 0;
		while (iterator.hasNext()) {
			list.getComponents()[index++] = converter.apply(iterator.next());
		}
		return list;
	}

	public static JsonList listOf(String key, List<String> values) {
		JsonList list = new JsonList(key, values.size());
		for (int i = 0; i < values.size(); i++) {
			list.getComponents()[i] = Json.valueOf("" + i, values.get(i));
		}
		return list;
	}
	
	public static JsonObject of(String key, Map<String, ?> map) {
		JsonObject object = new JsonObject(key);
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			object.put(Json.valueOf(string, map.get(string)));
		}
		return object;
	}
	
	public static <T> JsonList listOf(String key, T[] array) {
		JsonList list = new JsonList(key, array.length);
		for (int i = 0; i < array.length; i++) {
			list.getComponents()[i] = Json.valueOf("" + i, array[i]);
		}
		return list;
	}

	public static JsonObject of(String key, JsonObject object) {
		object.setKey(key);
		return object;
	}
	
}
