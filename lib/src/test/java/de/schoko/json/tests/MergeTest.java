package de.schoko.json.tests;

import static org.junit.Assert.assertEquals;

import de.schoko.json.Json;
import de.schoko.json.JsonObject;
import org.junit.Test;

public class MergeTest {
	
	public void assertEqualsMerged(JsonObject jsonA, JsonObject jsonB, JsonObject result) {
		JsonObject merged = jsonA.merge(jsonB);
		assertEquals(merged, result);
	}

	@Test
	public void oneValueMergeTest() {
		assertEqualsMerged(
				Json.of(
						),
				Json.of(
						Json.valueOf("foo", "bar")
						),
				Json.of(
						Json.valueOf("foo", "bar")
						)
				);
	}

	@Test
	public void oneValueUnchangedMergeTest() {
		assertEqualsMerged(
				Json.of(
						Json.valueOf("foo", "bar")
						),
				Json.of(
						),
				Json.of(
						Json.valueOf("foo", "bar")
						)
				);
	}

	@Test
	public void oneValueChangedMergeTest() {
		assertEqualsMerged(
				Json.of(
						Json.valueOf("foo", "bar")
						),
				Json.of(
						Json.valueOf("foo", "baz")
						),
				Json.of(
						Json.valueOf("foo", "baz")
						)
				);
	}

	@Test
	public void oneValueAddedMergeTest() {
		assertEqualsMerged(
				Json.of(
						Json.valueOf("foo", "bar")
						),
				Json.of(
						Json.valueOf("quz", "quux")
						),
				Json.of(
						Json.valueOf("foo", "bar"),
						Json.valueOf("quz", "quux")
						)
				);
	}
	
	@Test
	public void valueInListOverwrittenMergeTest() {
		assertEqualsMerged(
				Json.of(
						Json.listOf("foo", 
								Json.valueOf("bar", "baz")
								)
						),
				Json.of(
						Json.listOf("foo", 
								Json.valueOf("bar", "qux")
								)
						),
				Json.of(
						Json.listOf("foo", 
								Json.valueOf("bar", "qux")
								)
						)
				);
	}
	
	@Test
	public void valueInListAppendedMergeTest() {
		assertEqualsMerged(
				Json.of(
						Json.listOf("foo", 
								Json.valueOf("bar", "baz")
								)
						),
				Json.of(
						Json.listOf("foo", 
								Json.valueOf("bar", "qux"),
								Json.valueOf("quux", "garply")
								)
						),
				Json.of(
						Json.listOf("foo", 
								Json.valueOf("bar", "qux"),
								Json.valueOf("quux", "garply")
								)
						)
				);
	}
}
