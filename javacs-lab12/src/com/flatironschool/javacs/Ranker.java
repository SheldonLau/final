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
import java.lang.Math;                                                          
import org.jsoup.nodes.Element;                                                 
import org.jsoup.nodes.Node;                                                    
import org.jsoup.nodes.TextNode;                                                
                                                                                
import org.jsoup.select.Elements;                                                                                  
import redis.clients.jedis.Jedis;     

/**
 * Represents the link ranking of a url
 *
 */

public class Ranker {

  final static WikiFetcher wf = new WikiFetcher();

  // map from URLs that contain the link to rank
//  private Map<String, Integer> map;
//
//  /**
//   * Constructor.
//   * 
//   * @param map
//   */
//  public Ranker(Map<String, Integer> map) {
//    this.map = map;
//  }

//  public Integer getRank(String url) {
//    Integer ranking = map.get(url);
//    return ranking==null ? 0: ranking;
//  }
  /**
   * Determines if url is linked from another page
   * 
   * @param src
   * @param dest
   * @return 
   */
  public boolean isLinked(String src, String dest) throws IOException{
    Elements paragraphs = wf.fetchWikipedia(src);                               
//    for(Element paragraph : paragraphs) {
//      Iterable<Node> iter = new WikiNodeIterable(paragraph);
//      for(Node node : iter) {
//        if(node instanceof Element) {
//          Element element = (Element) node;
//          Elements links = element.select("a[href]");
//          for(Element link : links) {
//            String absHref = link.attr("abs:href");
//            if(absHref.equals(dest)) {
//              return true;
//            }
//          }
//          
//        }
//      }
//    }
    Elements links = paragraphs.select("a[href]");
    for(Element link : links) {
      String absHref = link.attr("abs:href");
      if(absHref.equals(dest)) {
        return true;
      }
    }
    return false;                                                                       
  } 
}
