package de.schoko.json;

public class NullComponent extends JsonComponent {

	public NullComponent(String key) {
		super(key);
	}
	
	@Override
	JsonComponent copy() {
		return new NullComponent(getKey());
	}
	
	@Override
	JsonComponent findDifference(JsonComponent component) {
		if (component instanceof NullComponent) return null;
		return new NullComponent(getKey());
	}
	
	@Override
	JsonComponent merge(JsonComponent component) {
		return component.copy();
	}

	@Override
	String getStringValue() {
		return "null";
	}

	@Override
	public void overwriteValue(JsonComponent component) {
		
	}

}
