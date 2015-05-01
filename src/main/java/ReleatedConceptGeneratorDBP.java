

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.pearson.util.QueryUtil;


public class ReleatedConceptGeneratorDBP {

	public static Set<String> getConcepts(int limit, int offset) {
//		input = WordCustomizeUtil.addUnderscore(input);
		String URL = "http://dbpedia.org";

		String resultString = "Concept";
		
		String query = "SELECT DISTINCT ?Concept "+
				"WHERE { "+
				" ?Concept a [] "+
				"} "+
				"LIMIT "+limit+" OFFSET "+ offset;

		Collection<String> output = QueryUtil.jenaQuery(query, resultString, URL);
		Set<String> result = new HashSet<String>(output);
		return result;

	}

//	public static void main(String[] args) {
//		final int limit = 100;
//		int offset = 1;
//		for (String string : getConcepts(limit,offset)) {
//			System.out.println(string);
//		}
//	}
}
