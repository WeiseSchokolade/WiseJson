package de.schoko.json;

import java.util.ArrayList;
import java.util.List;

public class JsonReader {
	public static void main(String[] args) {
		String s/* = """
				{"text": 50, "list": ["HI", "Hello", "G'day", "How are \\"you\\"?"], "boolean": true}
				"""*/;
		s = """
			{
			  "gates": [
			    {
			      "outputs": [
			        {
			          "name": "INPUT0",
			          "gate": "cf2116e8-99be-42ed-bfac-5087f340a567"
			        }
			      ],
			      "inputs": [],
			      "name": "INPUT",
			      "type": "INPUT"
			    },
			    {
			      "outputs": [],
			      "inputs": [
			        {
			          "name": "OUTPUT1",
			          "gate": "4f18c55e-44ae-4c82-8e7b-8e26b3384a98",
			          "network": "90aa19d6-f5be-403b-b945-857c8826fee5"
			        }
			      ],
			      "name": "OUTPUT",
			      "type": "OUTPUT"
			    }
			  ],
			  "cables": [
			    {
			      "output": {
			        "name": "OUTPUT1",
			        "gate": "4f18c55e-44ae-4c82-8e7b-8e26b3384a98",
			        "network": "90aa19d6-f5be-403b-b945-857c8826fee5"
			      },
			      "input": {
			        "name": "INPUT0",
			        "gate": "cf2116e8-99be-42ed-bfac-5087f340a567"
			      },
			      "uuid": "90aa19d6-f5be-403b-b945-857c8826fee5",
			      "value": false
			    }
			  ]
			}
			""";
		JsonReader jsonReader = new JsonReader();
		List<Token> tokens = jsonReader.tokenize(s);
		System.out.println(tokens);
		JsonObject object = jsonReader.parseTokens(tokens);
		System.out.println("Object: " + object);
	}
	
	public JsonObject parseJson(String json) {
		return parseTokens(tokenize(json));
	}
	
	/**
	 * @return The object based on the tokens or null if it couldn't be parsed
	 */
	public JsonObject parseTokens(List<Token> tokens) {
		JsonObject object = new JsonObject();
		if (tokens.get(0).type != TokenType.OBJECT_START) throw new JsonParseException("JSON missing opening bracket");
		if (tokens.get(tokens.size() - 1).type != TokenType.OBJECT_END) throw new JsonParseException("JSON missing closing bracket: " + tokens.get(tokens.size() - 1));
		tokens = tokens.subList(1, tokens.size() - 1);
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.type() != TokenType.STRING) throw new JsonParseException("Key of invalid type: " + token);
			Token key = token;
			i++;
			token = tokens.get(i);
			if (token.type() != TokenType.COLON) throw new JsonParseException("Key and Value not seperated by colon: " + token);
			i++;
			token = tokens.get(i);
			switch (token.type()) {
			case LIST_START:
				int[] index = new int[] {i};
				List<Token> bracketContent = extractBrackets(tokens, index, TokenType.LIST_START, TokenType.LIST_END, false);
				i = index[0];
				JsonList valueList = parseTokenList(key.value(), bracketContent);
				object.put(valueList);
				break;
			case OBJECT_START:
				index = new int[] {i};
				bracketContent = extractBrackets(tokens, index, TokenType.OBJECT_START, TokenType.OBJECT_END, true);
				i = index[0];
				JsonObject valueObject = parseTokens(bracketContent);
				valueObject.setKey(key.value());
				object.put(valueObject);
				break;
			case STRING:
				object.put(Json.valueOf(key.value(), token.value()));
				break;
			case NULL:
				object.put(new NullComponent(key.value()));
				break;
			case NUMBER:
				String value = token.value();
				object.put(parseNumber(key.value(), value));
				break;
			case BOOLEAN:
				object.put(Json.valueOf(key.value(), Boolean.valueOf(token.value())));
				break;
			default:
				throw new JsonParseException("Invalid token type: " + token);
			}
			i++;
			if (i == tokens.size()) break;
			token = tokens.get(i);
			if (token.type() != TokenType.COMMA && token.type() != TokenType.OBJECT_END && token.type() != TokenType.LIST_END) {
				System.err.println(tokens.get(i - 5));
				System.err.println(tokens.get(i - 4));
				System.err.println(tokens.get(i - 3));
				System.err.println(tokens.get(i - 2));
				System.err.println(tokens.get(i - 1));
				throw new JsonParseException("Pair invalidly continued: " + token);
			}
		}
		return object;
	}
	
	public JsonList parseTokenList(String key, List<Token> tokens) {
		List<JsonComponent> components = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			String tokenKey = Integer.toString(i);
			switch (token.type()) {
			case LIST_START:
				int[] index = new int[] {i};
				List<Token> bracketContent = extractBrackets(tokens, index, TokenType.LIST_START, TokenType.LIST_END, false);
				i = index[0];
				JsonList valueList = parseTokenList(tokenKey, bracketContent);
				components.add(valueList);
				break;
			case OBJECT_START:
				index = new int[] {i};
				bracketContent = extractBrackets(tokens, index, TokenType.OBJECT_START, TokenType.OBJECT_END, true);
				i = index[0];
				JsonObject valueObject = parseTokens(bracketContent);
				valueObject.setKey(tokenKey);
				components.add(valueObject);
				break;
			case STRING:
				components.add(Json.valueOf(tokenKey, token.value()));
				break;
			case NULL:
				components.add(new NullComponent(tokenKey));
				break;
			case NUMBER:
				String value = token.value();
				components.add(parseNumber(tokenKey, value));
				break;
			case BOOLEAN:
				components.add(Json.valueOf(tokenKey, Boolean.valueOf(token.value())));
				break;
			default:
				return null;
			}
			i++;
			if (i == tokens.size()) break;
			token = tokens.get(i);
			if (token.type() != TokenType.COMMA && token.type() != TokenType.OBJECT_END && token.type() != TokenType.LIST_END) return null;
		}
		return new JsonList(key, components.toArray(new JsonComponent[components.size()]));
	}
	
	public List<Token> extractBrackets(List<Token> tokens, int[] index, TokenType openingBracket, TokenType closingBracket, boolean includeEnclosingBrackets) {
		int openBrackets = 1;
		List<Token> subTokens = new ArrayList<>();
		if (includeEnclosingBrackets) {
			subTokens.add(tokens.get(index[0]));
		}
		while (openBrackets > 0) {
			index[0]++;
			Token token = tokens.get(index[0]);
			if (token.type() == openingBracket) {
				openBrackets++;
			} else if (token.type() == closingBracket) {
				openBrackets--;
			}
			if (openBrackets != 0) {
				subTokens.add(token);
			} else if (includeEnclosingBrackets) {
				subTokens.add(token);
			}
		}
		return subTokens;
	}
	
	public List<Token> tokenize(String json) {
		List<Token> tokens = new ArrayList<>();
		char[] chars = json.toCharArray();
		String token = "";
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			token += chars[i];
			token = token.trim();
			switch (token) {
			case "\"":
				int[] newIndex = new int[1];
				String data = readUntilUnescaped(chars, '"', i + 1, newIndex);
				i = newIndex[0];
				tokens.add(new Token(data, TokenType.STRING));
				token = "";
				break;
			case "null":
				tokens.add(new Token(token, TokenType.NULL));
				token = "";
				break;
			case "true":
			case "false":
				tokens.add(new Token(token, TokenType.BOOLEAN));
				token = "";
				break;
			case "{":
				tokens.add(new Token(token, TokenType.OBJECT_START));
				token = "";
				break;
			case "}":
				tokens.add(new Token(token, TokenType.OBJECT_END));
				token = "";
				break;
			case "[":
				tokens.add(new Token(token, TokenType.LIST_START));
				token = "";
				break;
			case "]":
				tokens.add(new Token(token, TokenType.LIST_END));
				token = "";
				break;
			case ":":
				tokens.add(new Token(token, TokenType.COLON));
				token = "";
				break;
			case ",":
				tokens.add(new Token(token, TokenType.COMMA));
				token = "";
				break;
			case "":
				break;
			default:
				if (isNumber(c)) {
					String number = readNumber(chars, i);
					i += number.length() - 1;
					tokens.add(new Token(number, TokenType.NUMBER));
					token = "";
				}
				break;
			}
		}
		return tokens;
	}
	
	public JsonValue<?> parseNumber(String tokenKey, String number) {
		if (number.contains("e")) {
			String[] split = number.split("e", 1);
			if (split.length > 1) {
				int exponent = Integer.parseInt(split[1]);
				if (exponent > Double.MAX_EXPONENT) {
					if (number.charAt(0) == '-') {
						return Json.valueOf(tokenKey, Double.MIN_VALUE);
					} else {
						return Json.valueOf(tokenKey, Double.MAX_VALUE);
					}
				}
				if (exponent < Double.MIN_EXPONENT) {
					return Json.valueOf(tokenKey, 0);
				}
			}
			return Json.valueOf(tokenKey, Double.parseDouble(number));
		} else if (number.contains(".")) {
			return Json.valueOf(tokenKey, Double.parseDouble(number));
		} else if (number.length() > 10 || (number.length() > 9 && !number.startsWith("-"))) {
			return Json.valueOf(tokenKey, Long.parseLong(number));
		} else {
			return Json.valueOf(tokenKey, Integer.parseInt(number));
		}
	}
	
	public String readNumber(char[] chars, int index) {
		String string = "";
		for (int i = index; i < chars.length; i++) {
			char c = chars[i];
			if (!isNumber(c)) break;
			string += c;
		}
		return string;
	}
	
	public String readUntilUnescaped(char[] chars, char character, int index, int[] newIndex) {
		String string = "";
		boolean escaped = false;
		int i;
		for (i = index; i < chars.length; i++) {
			char c = chars[i];
			if (!escaped && c == character) {
				break;
			} else if (c == '\\' && !escaped) {
				escaped = true;
			} else {
				if (escaped && c == 'n') {
					string += "\n";
				} else {
					string += c;
				}
				escaped = false;
			}
		}
		newIndex[0] = i;
		return string;
	}
	
	public boolean isNumber(char c) {
		return "0123456789.-Ee".contains("" + c);
	}
	
	record Token(String value, TokenType type) {
		
	}
	
	enum TokenType {
		STRING,
		NUMBER,
		BOOLEAN,
		COMMA,
		COLON,
		OBJECT_START,
		OBJECT_END,
		LIST_START,
		LIST_END,
		NULL;
	}
}
