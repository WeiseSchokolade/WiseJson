package de.schoko.json.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.schoko.json.Json;
import de.schoko.json.JsonList;
import de.schoko.json.JsonObject;

class RemovalTest {
	@Test
	void subtractionOfOneEqualValue() {
		JsonObject jsonA = Json.of(
				Json.valueOf("foo", "bar")
				);
		JsonObject jsonB = Json.of(
				Json.valueOf("foo", "bar")
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(result, Json.of());
	}

	@Test
	void subtractionOfOneUnequalValue() {
		JsonObject jsonA = Json.of(
				Json.valueOf("foo", "bar")
				);
		JsonObject jsonB = Json.of(
				Json.valueOf("foo", "baz")
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(result, Json.of(
				Json.valueOf("foo", "bar")
				));
	}
	
	@Test
	void succesfulSubtractionOfOneFromTwoLeavesOneUnaffected() {
		JsonObject jsonA = Json.of(
				Json.valueOf("foo", "bar"),
				Json.valueOf("qux", "quux")
				);
		JsonObject jsonB = Json.of(
				Json.valueOf("foo", "bar")
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(result, Json.of(
				Json.valueOf("qux", "quux")
				));
	}

	@Test
	void succesfulSubtractionOfTwoFromOne() {
		JsonObject jsonA = Json.of(
				Json.valueOf("foo", "bar")
				);
		JsonObject jsonB = Json.of(
				Json.valueOf("foo", "bar"),
				Json.valueOf("qux", "quux")
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(result, Json.of(
				));
	}

	@Test
	void unequalSubtractionOfTwoFromOne() {
		JsonObject jsonA = Json.of(
				Json.valueOf("foo", "baz")
				);
		JsonObject jsonB = Json.of(
				Json.valueOf("foo", "bar"),
				Json.valueOf("qux", "quux")
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(result, Json.of(
				Json.valueOf("foo", "baz")
				));
	}
	
	@Test
	void depthRemoveEqualsTest() {
		JsonObject jsonA = Json.of(
				Json.valueOf("foo", "bar"),
				Json.of("baz", 
						Json.valueOf("qux", "quux")
						)
				);
		JsonObject jsonB = Json.of(
				Json.valueOf("foo", "bar"),
				Json.of("baz", 
						Json.valueOf("qux", "quux")
						)
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(result, Json.of());
	}
	
	@Test
	void depthRemoveButOneOfTheValuesInDepthIsUnequalTest() {
		JsonObject jsonA = Json.of(
				Json.valueOf("foo", "bar"),
				Json.of("baz", 
						Json.valueOf("qux", "quax")
						)
				);
		JsonObject jsonB = Json.of(
				Json.valueOf("foo", "bar"),
				Json.of("baz", 
						Json.valueOf("qux", "quux")
						)
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(result, Json.of(
				Json.of("baz", 
						Json.valueOf("qux", "quax")
						)
				));
	}
	
	@Test
	void depthRemovalButOneOfTheValuesIsNotAnObject() {
		JsonObject jsonA = Json.of(
				Json.valueOf("foo", "bar"),
				Json.of("baz", 
						Json.valueOf("qux", "quax")
						)
				);
		JsonObject jsonB = Json.of(
				Json.valueOf("foo", "bar"),
				Json.valueOf("baz", "qux")
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(result, Json.of(
				Json.of("baz", 
						Json.valueOf("qux", "quax")
						)
				));
	}
	
	@Test
	void cleanRemovalOfListTest() {
		JsonObject jsonA = Json.of(
				Json.listOf("foo",
						Json.valueOf("bar", "baz")
						)
				);
		JsonObject jsonB = Json.of(
				Json.listOf("foo",
						Json.valueOf("bar", "baz")
						)
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(result, Json.of());
	}
	
	@Test
	void noRemovalOfListBecauseTheValuesAreDifferentTest() {
		JsonObject jsonA = Json.of(
				Json.listOf("foo",
						Json.valueOf("bar", "baz")
						)
				);
		JsonObject jsonB = Json.of(
				new JsonList("foo")
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(result, Json.of(
				Json.listOf("foo",
						Json.valueOf("bar", "baz")
						)
				));
	}
	
	@Test
	void noRemovalOfListBecauseThereAreMoreValuesTest() {
		JsonObject jsonA = Json.of(
				Json.listOf("foo",
						Json.valueOf("bar", "baz"),
						Json.valueOf("qux", "quux"),
						Json.valueOf("garply", "gaarply")
						)
				);
		JsonObject jsonB = Json.of(
				Json.listOf("foo", 
						Json.valueOf("bar", "baz")
						)
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(result, jsonA);
	}
	
	@Test
	void noRemovalOfListBecauseTooManyValuesWereRemovedTest() {
		JsonObject jsonA = Json.of(
				Json.listOf("foo",
						Json.valueOf("bar", "baz")
						)
				);
		JsonObject jsonB = Json.of(
				Json.listOf("foo", 
						Json.valueOf("bar", "baz"),
						Json.valueOf("qux", "quux"),
						Json.valueOf("garply", "gaarply")
						)
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(result, jsonA);
	}
	
	@Test
	void objectListRemovedCleanlyTest() {
		JsonObject jsonA = Json.of(
				Json.listOf("foo",
						Json.of(
								Json.valueOf("foo", "bar"),
								Json.valueOf("baz", "qux")
								)
						),
						Json.of(
								Json.valueOf("foo", "bar"),
								Json.valueOf("baz", "qux")
						)
				);
		JsonObject jsonB = Json.of(
				Json.listOf("foo",
						Json.of(
								Json.valueOf("foo", "bar"),
								Json.valueOf("baz", "qux")
								)
						),
						Json.of(
								Json.valueOf("foo", "bar"),
								Json.valueOf("baz", "qux")
						)
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(result, Json.of());
	}
}
