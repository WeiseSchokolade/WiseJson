package de.schoko.json.tests;

import de.schoko.json.Json;
import de.schoko.json.JsonObject;
import de.schoko.json.JsonReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UnitTest {
	@Test
	public void emptyObjectGetsSerializedTest() {
		String data = "{}";
		JsonReader reader = new JsonReader();
		JsonObject json = reader.parseJson(data);
		assertEquals(data, json.toString());
	}

	@Test
	public void jsonArrayReadingTest() {
		String list = "{\"list\":[1, 2, 3]}";
		JsonReader reader = new JsonReader();
		JsonObject json = reader.parseJson(list);
		assertEquals(1, (int) json.getList("list").getValue(0, Integer.class));
	}
	
	@Test
	public void readNegativeValueTest() {
		String jsonString = "{\"value\":-5}";
		JsonReader reader = new JsonReader();
		JsonObject json = reader.parseJson(jsonString);
		assertEquals(-5, (int) json.getValue("value", Integer.class));
	}
	
	@Test
	public void escapeStringInObjectWhileWritingTest() {
		JsonObject object = Json.of(
				Json.valueOf("double_quote", "\"")
				);
		String value = object.toString();
		assertEquals("{\"double_quote\": \"\\\"\"}", value);
	}
	
	@Test
	public void readObjectWithEscapedQuoteSignInStringSuccesfullyOutputsQuoteTest() {
		String data = "{\"double_quote\": \"\\\"\"}";
		JsonReader reader = new JsonReader();
		JsonObject json = reader.parseJson(data);
		assertEquals("\"", json.getValue("double_quote", String.class));
	}

	@Test
	public void jsonObjectDoesNotEqualAnotherJsonObjectWithAMissingValue1() {
		String data1 = "{\"myValue\": 5}";
		String data2 = "{\"myValue\": 5, \"greeting\":\"Hi\"}";
		JsonReader reader = new JsonReader();
		JsonObject json1 = reader.parseJson(data1);
		JsonObject json2 = reader.parseJson(data2);
		assertNotEquals(json1, json2);
	}

	@Test
	public void jsonObjectDoesNotEqualAnotherJsonObjectWithAMissingValue2() {
		String data1 = "{\"myValue\": 5, \"greeting\":\"Hi\"}";
		String data2 = "{\"myValue\": 5}";
		JsonReader reader = new JsonReader();
		JsonObject json1 = reader.parseJson(data1);
		JsonObject json2 = reader.parseJson(data2);
		assertNotEquals(json1, json2);
	}

	@Test
	public void jsonObjectEqualsAnotherJsonObjectWithASingleNumberTest() {
		String data1 = "{\"myValue\": 5}";
		String data2 = "{\"myValue\": 5}";
		JsonReader reader = new JsonReader();
		JsonObject json1 = reader.parseJson(data1);
		JsonObject json2 = reader.parseJson(data2);
		assertEquals(json1, json2);
	}

	@Test
	public void jsonObjectDoesNotEqualAnotherJsonObjectWithASingleNumberTest() {
		String data1 = "{\"myValue\": 5}";
		String data2 = "{\"myValue\": -5}";
		JsonReader reader = new JsonReader();
		JsonObject json1 = reader.parseJson(data1);
		JsonObject json2 = reader.parseJson(data2);
		assertNotEquals(json1, json2);
	}
	
	@Test
	public void jsonObjectEqualsAnotherJsonObjectWithAStringTest() {
		String data1 = "{\"myValue\": \"Hi\"}";
		String data2 = "{\"myValue\": \"Hi\"}";
		JsonReader reader = new JsonReader();
		JsonObject json1 = reader.parseJson(data1);
		JsonObject json2 = reader.parseJson(data2);
		assertEquals(json1, json2);
	}
	
	@Test
	public void jsonObjectDoesNotEqualAnotherJsonObjectWithAStringTest() {
		String data1 = "{\"myValue\": \"Hi\"}";
		String data2 = "{\"myValue\": \"hi\"}";
		JsonReader reader = new JsonReader();
		JsonObject json1 = reader.parseJson(data1);
		JsonObject json2 = reader.parseJson(data2);
		assertNotEquals(json1, json2);
	}
}
