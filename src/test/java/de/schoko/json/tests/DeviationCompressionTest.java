package de.schoko.json.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.schoko.json.Json;
import de.schoko.json.JsonObject;
import org.junit.jupiter.api.Test;

public class DeviationCompressionTest {
	
	public void assertEqualsCompressed(JsonObject json, JsonObject standard) {
		JsonObject compressed = json.remove(standard);
		JsonObject merged = standard.merge(compressed);
		assertEquals(merged, json);
	}

	@Test
	public void oneValueUnequalsMergeTest() {
		assertEqualsCompressed(
				Json.of(
						Json.valueOf("name", "foo")
						),
				Json.of(
						Json.valueOf("name", "bar")
						)
				);
	}

	@Test
	public void oneValueEqualsMergeTest() {
		assertEqualsCompressed(
				Json.of(
						Json.valueOf("name", "foo")
						),
				Json.of(
						Json.valueOf("name", "foo")
						)
				);
	}

	@Test
	public void multipleValueEqualsMergeTest() {
		assertEqualsCompressed(
				Json.of(
						Json.valueOf("name", "foo"),
						Json.valueOf("bar", "baz"),
						Json.valueOf("qux", "quux")
						),
				Json.of(
						Json.valueOf("name", "foo")
						)
				);
	}

}
