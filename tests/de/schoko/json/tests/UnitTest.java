package de.schoko.json.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.schoko.json.Json;
import de.schoko.json.JsonObject;
import de.schoko.json.JsonReader;

class UnitTest {
	@Test
	void emptyObjectGetsSerializedTest() {
		String data = "{}";
		JsonReader reader = new JsonReader();
		JsonObject json = reader.parseJson(data);
		assertEquals(json.toString(), data);
	}

	@Test
	void jsonArrayReadingTest() {
		String list = "{\"list\":[1, 2, 3]}";
		JsonReader reader = new JsonReader();
		JsonObject json = reader.parseJson(list);
		assertEquals(json.getList("list").getValue(0, Integer.class), 1);
	}
	
	@Test
	void readNegativeValueTest() {
		String jsonString = "{\"value\":-5}";
		JsonReader reader = new JsonReader();
		JsonObject json = reader.parseJson(jsonString);
		assertEquals(json.getValue("value", Integer.class), -5);
	}
	
	@Test
	void escapeStringInObjectWhileWritingTest() {
		JsonObject object = Json.of(
				Json.valueOf("double_quote", "\"")
				);
		String value = object.toString();
		assertEquals(value, "{\"double_quote\": \"\\\"\"}");
	}
	
	@Test
	void readObjectWithEscapedQuoteSignInStringSuccesfullyOutputsQuoteTest() {
		String data = "{\"double_quote\": \"\\\"\"}";
		JsonReader reader = new JsonReader();
		JsonObject json = reader.parseJson(data);
		assertEquals(json.getValue("double_quote", String.class), "\"");
	}

	@Test
	void jsonObjectDoesNotEqualAnotherJsonObjectWithAMissingValue1() {
		String data1 = "{\"myValue\": 5}";
		String data2 = "{\"myValue\": 5, \"greeting\":\"Hi\"}";
		JsonReader reader = new JsonReader();
		JsonObject json1 = reader.parseJson(data1);
		JsonObject json2 = reader.parseJson(data2);
		assertFalse(json1.equals(json2));
	}

	@Test
	void jsonObjectDoesNotEqualAnotherJsonObjectWithAMissingValue2() {
		String data1 = "{\"myValue\": 5, \"greeting\":\"Hi\"}";
		String data2 = "{\"myValue\": 5}";
		JsonReader reader = new JsonReader();
		JsonObject json1 = reader.parseJson(data1);
		JsonObject json2 = reader.parseJson(data2);
		assertFalse(json1.equals(json2));
	}

	@Test
	void jsonObjectEqualsAnotherJsonObjectWithASingleNumberTest() {
		String data1 = "{\"myValue\": 5}";
		String data2 = "{\"myValue\": 5}";
		JsonReader reader = new JsonReader();
		JsonObject json1 = reader.parseJson(data1);
		JsonObject json2 = reader.parseJson(data2);
		assertTrue(json1.equals(json2));
	}

	@Test
	void jsonObjectDoesNotEqualAnotherJsonObjectWithASingleNumberTest() {
		String data1 = "{\"myValue\": 5}";
		String data2 = "{\"myValue\": -5}";
		JsonReader reader = new JsonReader();
		JsonObject json1 = reader.parseJson(data1);
		JsonObject json2 = reader.parseJson(data2);
		assertFalse(json1.equals(json2));
	}
	
	@Test
	void jsonObjectEqualsAnotherJsonObjectWithAStringTest() {
		String data1 = "{\"myValue\": \"Hi\"}";
		String data2 = "{\"myValue\": \"Hi\"}";
		JsonReader reader = new JsonReader();
		JsonObject json1 = reader.parseJson(data1);
		JsonObject json2 = reader.parseJson(data2);
		assertTrue(json1.equals(json2));
	}
	
	@Test
	void jsonObjectDoesNotEqualAnotherJsonObjectWithAStringTest() {
		String data1 = "{\"myValue\": \"Hi\"}";
		String data2 = "{\"myValue\": \"hi\"}";
		JsonReader reader = new JsonReader();
		JsonObject json1 = reader.parseJson(data1);
		JsonObject json2 = reader.parseJson(data2);
		assertFalse(json1.equals(json2));
	}
}
