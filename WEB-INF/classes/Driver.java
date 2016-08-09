//package com.flatironschool.javacs;                                              

import static org.junit.Assert.assertThat;                                      
import static org.hamcrest.CoreMatchers.*;                                     
                                                                                
import java.io.IOException;                                                     
import java.util.Scanner;
import java.util.Map;                                                           
import java.util.HashMap;                                                       
import java.util.List;                                                          
import java.util.Map.Entry;                                                     
import java.util.Set;
                                                                                
import org.jsoup.select.Elements;                                               
import org.junit.After;                                                         
import org.junit.Before;                                                        
import org.junit.Test;                                                          
                                                                                
import redis.clients.jedis.Jedis;  
                                                                                
public class Driver {
  public static void main(String[] args) throws IOException {
    LinkRanker test = new LinkRanker();
    TermCounter testing = new TermCounter("testing");
    String prompt = "Options - s: search a term ; i: index a topic ; " +
                    "r: remove all indexed topics ; b: perform boolean search ; " + 
                    "q: quit : ";
    Jedis jedis = JedisMaker.make();
    JedisIndex index = new JedisIndex(jedis);
    Map<String, Integer> map = new HashMap<String, Integer>();
    Scanner input = new Scanner(System.in);
    WikiSearch search = new WikiSearch(map);
    Set<String> urls = index.URLs();
    System.out.println("Indexed URLs");
    for(String url : urls) {
      System.out.println(url);
    }

    System.out.print(prompt);
    String option = input.nextLine();
    while(!option.equals("q")) {
      switch(option) {
        case "i": 
        System.out.print("Enter a topic to insert: ");
        String topic = input.nextLine();
        String url = "https://en.wikipedia.org/wiki/" + topic;
        WikiCrawler wc = new WikiCrawler(url, index);
        String res = wc.crawl(false);
        System.out.print(prompt);
        option = input.nextLine();
        break;
    
        case "r":
        System.out.println("Indexed topics removed:");
        System.out.println(index.URLs());
        index.deleteURLSets();
        index.deleteAllKeys();
        System.out.print(prompt);
        option = input.nextLine();
        break;

        case "b":
        System.out.print("Options - a: and query ;  o: or query ; m: minus query : ");
        String subOption = input.nextLine();
        System.out.print("Enter two words separated by a space: ");
        String words = input.nextLine();

        // pull out words from input and store in array
        String[] splitWords = words.split(" ");

        // perform search on each word
        WikiSearch search1 = WikiSearch.search(splitWords[0], index);
        WikiSearch search2 = WikiSearch.search(splitWords[1], index);

        if(subOption.equals("a")) {
          WikiSearch intersection = search1.and(search2);
          intersection.print();
        }
        else if(subOption.equals("o")) {
          WikiSearch union = search1.or(search2);
          union.print();
        }
        else if(subOption.equals("m")) {
          WikiSearch difference = search1.minus(search2);
          difference.print();
        }
        else {
          System.out.println("Invalid query");
        }
        System.out.print(prompt);
        option = input.nextLine();
        break;

        case "s":
        System.out.print("Enter a term to search for: ");
        String term = input.nextLine();
        search1 = WikiSearch.search(term, index);
        search1.print();
        System.out.print(prompt);
        option = input.nextLine();
        break;
        
        default: 
        System.out.println("Not a valid option");
        System.out.print(prompt);
        option = input.nextLine();
        break;
      }
    }
      
  }                                     

}
