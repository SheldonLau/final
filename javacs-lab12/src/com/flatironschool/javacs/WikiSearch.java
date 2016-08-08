//package com.flatironschool.javacs;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.lang.Math;

import redis.clients.jedis.Jedis;


/**
 * Represents the results of a search query.
 *
 */
public class WikiSearch {
	
	// map from URLs that contain the term(s) to relevance score
	private Map<String, Integer> map;

	/**
	 * Constructor.
	 * 
	 * @param map
	 */
	public WikiSearch(Map<String, Integer> map) {
		this.map = map;
	}

	/**
	 * Looks up the relevance of a given URL.
	 * 
	 * @param url
	 * @return
	 */
	public Integer getRelevance(String url) {
		Integer relevance = map.get(url);
		return relevance==null ? 0: relevance;
	}
	
	/**
	 * Prints the contents in order of term frequency.
	 * 
	 * @param map
	 */
	public void print() {
		List<Entry<String, Integer>> entries = sort();
		for (Entry<String, Integer> entry: entries) {
			System.out.println(entry);
		}
	}
	
	/**
	 * Computes the union of two search results.
	 * 
	 * @param that
	 * @return New WikiSearch object.
	 */
	public WikiSearch or(WikiSearch that) {

    // initialize union map with map containing individual results
		Map<String, Integer> union = new HashMap<String, Integer>(map);

    for(String url : that.map.keySet()) {

      // relevance of each result
      int thisResult = this.getRelevance(url);
      int thatResult = that.getRelevance(url);

      // sum of relevances
      int relevance = totalRelevance(thisResult, thatResult);

      // add mapping to union results
      union.put(url, relevance);
    }

    WikiSearch unionSearch = new WikiSearch(union);
    return unionSearch;
	}
	
	/**
	 * Computes the intersection of two search results.
	 * 
	 * @param that
	 * @return New WikiSearch object.
	 */
	public WikiSearch and(WikiSearch that) {
    // initialize empty intersection map
		Map<String, Integer> intersection = new HashMap<String, Integer>();

    for(String url : this.map.keySet()) {

      // determine whether there is an intersection
      boolean intersected = that.map.containsKey(url);

      if(intersected) {
        int thisResult = this.getRelevance(url);
        int thatResult = that.getRelevance(url);
        int relevance = Math.min(thisResult, thatResult);
//        int relevance = totalRelevance(thisResult, thatResult);
        intersection.put(url, relevance);
      }
      
    }
    WikiSearch intersectionSearch = new WikiSearch(intersection);
    return intersectionSearch;
	}
	
	/**
	 * Computes the difference of two search results.
	 * 
	 * @param that
	 * @return New WikiSearch object.
	 */
	public WikiSearch minus(WikiSearch that) {
	  Map<String, Integer> difference = new HashMap<String, Integer>(map);

    for(String url : that.map.keySet()) {
      difference.remove(url);  
    }
    WikiSearch differenceSearch = new WikiSearch(difference);
    return differenceSearch;

  }
	
	/**
	 * Computes the relevance of a search with multiple terms.
	 * 
	 * @param rel1: relevance score for the first search
	 * @param rel2: relevance score for the second search
	 * @return
	 */
	protected int totalRelevance(Integer rel1, Integer rel2) {
		// simple starting place: relevance is the sum of the term frequencies.
		return rel1 + rel2;
	}

	/**
	 * Sort the results by relevance.
	 * 
	 * @return List of entries with URL and relevance.
	 */
	public List<Entry<String, Integer>> sort() {
    // list of entries with URL and relevance
    List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(map.entrySet());

    Comparator<Entry<String, Integer>> comparator = new Comparator<Entry<String, Integer>>() {
      @Override
      public int compare(Entry<String, Integer> entry1, Entry<String, Integer> entry2) {
        int entry1Val = entry1.getValue();
        int entry2Val = entry2.getValue();

        if(entry1Val > entry2Val) {
          return 1;
        }
        else if(entry1Val < entry2Val) {
          return -1;
        }
        else {
          return 0;
        }
      }
    };
    
    Collections.sort(entries, comparator);
    return entries;
	}

	/**
	 * Performs a search and makes a WikiSearch object.
	 * 
	 * @param term
	 * @param index
	 * @return
	 */
	public static WikiSearch search(String term, JedisIndex index)throws IOException {
		Map<String, Integer> map = index.getRank(term);
		return new WikiSearch(map);
	}

	public static void main(String[] args) throws IOException {
		
		// make a JedisIndex
		Jedis jedis = JedisMaker.make();
		JedisIndex index = new JedisIndex(jedis); 
		
		// search for the first term
		String term1 = "java";
		System.out.println("Query: " + term1);
		WikiSearch search1 = search(term1, index);
		search1.print();
		
		// search for the second term
		String term2 = "programming";
		System.out.println("Query: " + term2);
		WikiSearch search2 = search(term2, index);
		search2.print();
		
		// compute the intersection of the searches
		System.out.println("Query: " + term1 + " AND " + term2);
		WikiSearch intersection = search1.and(search2);
		intersection.print();
	}
}
