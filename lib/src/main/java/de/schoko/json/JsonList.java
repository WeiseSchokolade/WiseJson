package de.schoko.json;

public class JsonList extends JsonComponent {
	private JsonComponent[] components;

	public JsonList(String key, JsonComponent... components) {
		super(key);
		this.components = components;
	}
	
	public JsonList(String key, int size) {
		super(key);
		this.components = new JsonComponent[size];
	}
	
	@Override
	JsonList copy() {
		JsonList copy = new JsonList(getKey(), components.length);
		for (int i = 0; i < components.length; i++) {
			copy.components[i] = components[i].copy();
		}
		return copy;
	}
	
	@Override
	public void overwriteValue(JsonComponent component) {
		components = ((JsonList) component).components;
	}
	
	@Override
	JsonComponent findDifference(JsonComponent component) {
		if (component == null || !(component instanceof JsonList)) return copy();
		JsonList list = (JsonList) component;
		if (list.components.length != components.length) return copy();
		JsonList copy = new JsonList(getKey(), components.length);
		boolean notNull = false;
		for (int i = 0; i < components.length; i++) {
			JsonComponent valueCopy = list.components[i].findDifference(components[i]);
			if (valueCopy != null) {
				notNull = true;
				copy.components[i] = valueCopy;
			}
		}
		if (notNull) return copy;
		return null;
	}
	
	@Override
	JsonComponent merge(JsonComponent component) {
		if (component instanceof JsonList list) {
			return merge(list);
		}
		return this.copy();
	}
	
	JsonList merge(JsonList list) {
		JsonList copy = copy();
		int i;
		for (i = 0; i < list.components.length && i < components.length; i++) {
			copy.components[i] = copy.components[i].merge(list.components[i]);
		}
		if (i == components.length && list.components.length > i) {
			JsonComponent[] components = new JsonComponent[list.components.length];
			for (int j = 0; j < components.length; j++) {
				if (j < copy.components.length) {
					components[j] = copy.components[j];
				} else {
					components[j] = list.components[j];
				}
			}
			copy.components = components;
		}
		return copy;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof JsonList)) return false;
		JsonList list = (JsonList) obj;
		if (list.length() != this.length()) return false;
		for (int i = 0; i < components.length; i++) {
			if (!components[i].equals(list.components[i])) return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(int i, Class<T> clazz) {
		if (i >= components.length) return null;
		return ((JsonValue<T>) components[i]).getValue();
	}
	
	public JsonObject getObject(int i) {
		return (JsonObject) components[i];
	}

	@Override
	String getStringValue() {
		String result = "[";
		for (int i = 0; i < components.length; i++) {
			result += components[i].getStringValue();
			if (i < components.length - 1) {
				result += ", ";
			}
		}
		return result + "]";
	}
	
	public JsonComponent[] getComponents() {
		return components;
	}
	
	public int length() {
		return components.length;
	}
}
