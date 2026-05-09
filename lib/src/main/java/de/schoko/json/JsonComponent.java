package de.schoko.json;

public abstract class JsonComponent {
	private String key;
	
	public JsonComponent(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	abstract JsonComponent copy();	
	abstract String getStringValue();
	public abstract void overwriteValue(JsonComponent component);
	abstract JsonComponent findDifference(JsonComponent component);
	abstract JsonComponent merge(JsonComponent component);
	
	@Override
	public String toString() {
		if (key == null) return getStringValue();
		return "\"" + key + "\": " + getStringValue();
	}
	
	public void setKey(String key) {
		this.key = key;
	}
}
