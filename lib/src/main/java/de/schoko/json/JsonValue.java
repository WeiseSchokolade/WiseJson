package de.schoko.json;

public class JsonValue<T> extends JsonComponent {
	private T value;
	
	public JsonValue(String key, T value) {
		super(key);
		this.value = value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	@Override
	JsonComponent copy() {
		return new JsonValue<T>(getKey(), value);
	}
	
	@Override
	JsonComponent findDifference(JsonComponent component) {
		if (this.equals(component)) return null;
		return copy();
	}
	
	@Override
	JsonComponent merge(JsonComponent component) {
		if (component instanceof JsonValue value) {
			return value.copy();
		}
		return copy();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof JsonValue<?>)) return false;
		return ((JsonValue<?>) obj).value.equals(value);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void overwriteValue(JsonComponent component) {
		this.value = (T) ((JsonValue<?>) component).value;
	}
	
	private static String escapeString(String string) {
		string = string.replace("\\", "\\\\");
		string = string.replace("\"", "\\\"");
		string = string.replace("\n", "\\n");
		return string;
	}
	
	@Override
	String getStringValue() {
		if (value instanceof String) {
			return "\"" + escapeString(value.toString()) + "\"";
		} else if (value instanceof JsonSerializable serializable) {
			return serializable.toJson().toString();
		} else {
			return value.toString();
		}
	}
}
