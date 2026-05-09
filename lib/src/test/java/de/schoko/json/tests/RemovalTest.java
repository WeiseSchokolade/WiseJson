package de.schoko.json.tests;

import de.schoko.json.Json;
import de.schoko.json.JsonList;
import de.schoko.json.JsonObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RemovalTest {
	@Test
	public void subtractionOfOneEqualValue() {
		JsonObject jsonA = Json.of(
				Json.valueOf("foo", "bar")
				);
		JsonObject jsonB = Json.of(
				Json.valueOf("foo", "bar")
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(Json.of(), result);
	}

	@Test
	public void subtractionOfOneUnequalValue() {
		JsonObject jsonA = Json.of(
				Json.valueOf("foo", "bar")
				);
		JsonObject jsonB = Json.of(
				Json.valueOf("foo", "baz")
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(Json.of(Json.valueOf("foo", "bar")),
			result);
	}
	
	@Test
	public void succesfulSubtractionOfOneFromTwoLeavesOneUnaffected() {
		JsonObject jsonA = Json.of(
				Json.valueOf("foo", "bar"),
				Json.valueOf("qux", "quux")
				);
		JsonObject jsonB = Json.of(
				Json.valueOf("foo", "bar")
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(Json.of(
				Json.valueOf("qux", "quux")
				), result);
	}

	@Test
	public void succesfulSubtractionOfTwoFromOne() {
		JsonObject jsonA = Json.of(
				Json.valueOf("foo", "bar")
				);
		JsonObject jsonB = Json.of(
				Json.valueOf("foo", "bar"),
				Json.valueOf("qux", "quux")
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(Json.of(
				), result);
	}

	@Test
	public void unequalSubtractionOfTwoFromOne() {
		JsonObject jsonA = Json.of(
				Json.valueOf("foo", "baz")
				);
		JsonObject jsonB = Json.of(
				Json.valueOf("foo", "bar"),
				Json.valueOf("qux", "quux")
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(Json.of(
				Json.valueOf("foo", "baz")
				), result);
	}
	
	@Test
	public void depthRemoveEqualsTest() {
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
		assertEquals(Json.of(), result);
	}
	
	@Test
	public void depthRemoveButOneOfTheValuesInDepthIsUnequalTest() {
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
		assertEquals(Json.of(
				Json.of("baz", 
						Json.valueOf("qux", "quax")
						)
				), result);
	}
	
	@Test
	public void depthRemovalButOneOfTheValuesIsNotAnObject() {
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
		assertEquals(Json.of(
				Json.of("baz", 
						Json.valueOf("qux", "quax")
						)
				), result);
	}
	
	@Test
	public void cleanRemovalOfListTest() {
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
		assertEquals(Json.of(), result);
	}
	
	@Test
	public void noRemovalOfListBecauseTheValuesAreDifferentTest() {
		JsonObject jsonA = Json.of(
				Json.listOf("foo",
						Json.valueOf("bar", "baz")
						)
				);
		JsonObject jsonB = Json.of(
				new JsonList("foo")
				);
		JsonObject result = jsonA.remove(jsonB);
		assertEquals(Json.of(
				Json.listOf("foo",
						Json.valueOf("bar", "baz")
						)
				), result);
	}
	
	@Test
	public void noRemovalOfListBecauseThereAreMoreValuesTest() {
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
		assertEquals(jsonA, result);
	}
	
	@Test
	public void noRemovalOfListBecauseTooManyValuesWereRemovedTest() {
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
		assertEquals(jsonA, result);
	}
	
	@Test
	public void objectListRemovedCleanlyTest() {
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
		assertEquals(Json.of(), result);
	}
}
