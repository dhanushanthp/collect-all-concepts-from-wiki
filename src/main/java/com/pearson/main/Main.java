package com.pearson.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.WritableByteChannel;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {
	private static String sendGet(int limit, int offset) throws Exception {

		String url = "http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=SELECT+DISTINCT+%3FConcept+WHERE+%7B++%3FConcept+a+%5B%5D+.+%7D+LIMIT+"
				+ limit + "+OFFSET+" + offset + "&format=application%2Fsparql-results%2Bjson&timeout=30000000";

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		// add request header
		// request.addHeader("User-Agent", USER_AGENT);

		HttpResponse response = client.execute(request);

		// System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		return result.toString();
	}

	public static String getConcepts(String input) {
		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(input).getAsJsonObject();
		JsonObject results = o.get("results").getAsJsonObject();
		JsonElement bindings = results.get("bindings");
		JsonArray resultArray = bindings.getAsJsonArray();
		StringBuffer sb = new StringBuffer();
		for (JsonElement jsonElement : resultArray) {
			String line = jsonElement.getAsJsonObject().get("Concept").getAsJsonObject().get("value").getAsString();
			if (line.contains("Category:")) {
				line = line.replace("http://dbpedia.org/resource/Category:", "");
			} else if(line.contains("/resource/")){
				line = line.replace("http://dbpedia.org/resource/", "");
			} else{
				line = line.replace("http://en.wikipedia.org/wiki/", "");
			}

			sb.append(line + "\n");
		}
		return sb.toString();
	}

	private static void writeInToFile(String input, String fileName) {
		try {
			File file = new File("/opt/data-extractor/Concepts/" + fileName);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(input);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		Properties prop = new Properties();
		InputStream input = new FileInputStream("/opt/data-extractor/config/config.properties");
		prop.load(input);
		int numberOfResults = Integer.parseInt(prop.getProperty("num_of_result"));
		int startingFrom = Integer.parseInt(prop.getProperty("starting_count"));
		int count = 0;
		while (true) {
			System.out.println("Starting from : " + startingFrom);
			String response = sendGet(numberOfResults, startingFrom);
			if (response.equals("")) {
				break;
			}
			String concepts = getConcepts(response);
			writeInToFile(concepts, count + "_" + String.valueOf(startingFrom));
			startingFrom = startingFrom + numberOfResults;
			count++;
		}
	}
}
