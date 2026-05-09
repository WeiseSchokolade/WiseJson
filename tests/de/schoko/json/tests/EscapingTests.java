package de.schoko.json.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.schoko.json.Json;
import de.schoko.json.JsonReader;

public class EscapingTests {
	@Test
	void doesEscapedStringConvertToString() {
		assertEquals(
				"{\"stringContainingEscapeCharacter\": \"\\n\"}",
				Json.of(Json.valueOf("stringContainingEscapeCharacter", "\n")).toString());
	}
	
	@Test
	void doEscapedBackslashesEncodeProperly() {
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
	void doEscapedBackslashesDecodeProperly() {
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
