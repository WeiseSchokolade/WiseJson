package de.schoko.json.tests;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import de.schoko.json.Json;
import de.schoko.json.JsonObject;

class DeviationCompressionTest {
	
	public void assertEqualsCompressed(JsonObject json, JsonObject standard) {
		JsonObject compressed = json.remove(standard);
		JsonObject merged = standard.merge(compressed);
		assertEquals(merged, json);
	}

	@Test
	void oneValueUnequalsMergeTest() {
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
	void oneValueEqualsMergeTest() {
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
	void multipleValueEqualsMergeTest() {
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
