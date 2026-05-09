package de.schoko.json.tests;

import de.schoko.json.Json;
import de.schoko.json.JsonReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EscapingTests {
	@Test
	public void doesEscapedStringConvertToString() {
		assertEquals(
				"{\"stringContainingEscapeCharacter\": \"\\n\"}",
				Json.of(Json.valueOf("stringContainingEscapeCharacter", "\n")).toString());
	}
	
	@Test
	public void doEscapedBackslashesEncodeProperly() {
		assertEquals(
				"\"\\\\ \"", 
				Json.valueOf(null, "\\ ").toString());
		assertEquals(
				"\"\\\\\\\\ \"", 
				Json.valueOf(null, "\\\\ ").toString());
		assertEquals(
				"\"\\\\\\\" \"", 
				Json.valueOf(null, "\\\" ").toString());
	}
	
	@Test
	public void doEscapedBackslashesDecodeProperly() {
		JsonReader reader = new JsonReader();
		assertEquals(
				Json.of(Json.valueOf("value", "\\ ")), // value: \, encoded: \\
				reader.parseJson("{\"value\":\"\\\\ \"}"));
		assertEquals(
				Json.of(Json.valueOf("value", "\\\\ ")), // value: \\, encoded: \\\\
				reader.parseJson("{\"value\":\"\\\\\\\\ \"}")
				);
		assertEquals(
				Json.of(Json.valueOf("value", "\" ")), // value: ", encoded: \"
				reader.parseJson("{\"value\":\"\\\" \"}")
				);
		assertEquals(
				Json.of(Json.valueOf("value", "\\\" ")), // value: \", encoded: \\\"
				reader.parseJson("{\"value\":\"\\\\\\\" \"}")
				);
		assertEquals(
				Json.of(Json.valueOf("value", "\n ")), // value: newline, encoded: \n
				reader.parseJson("{\"value\":\"\\n \"}")
				);
		assertEquals(
				Json.of(Json.valueOf("value", "\\\n ")), // value: \newline, encoded: \\\n
				reader.parseJson("{\"value\":\"\\\\\\n \"}")
				);
	}
}
