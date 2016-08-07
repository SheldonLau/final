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
    String prompt = "Options - s: search a term ; i: index a topic ; " +
                    "r: remove all indexed topics ; q: quit : ";
    Jedis jedis = JedisMaker.make();
    JedisIndex index = new JedisIndex(jedis);
    Map<String, Integer> map = new HashMap<String, Integer>();
    Scanner input = new Scanner(System.in);
    WikiSearch search = new WikiSearch(map);
    Set<String> urls = index.URLs();
//    index.links("https://en.wikipedia.org/wiki/Mathematics");
    for(String url : urls) {
      System.out.println(url);
    }
//    System.out.println(index.termCounterKeys());

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
        System.out.println(index.termCounterKeys());
        index.deleteURLSets();
        index.deleteAllKeys();
        System.out.print(prompt);
        option = input.nextLine();
        break;

        case "s":
        System.out.print("Enter a term to search for: ");
        String term = input.nextLine();
        WikiSearch search1 = WikiSearch.search(term, index);
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
