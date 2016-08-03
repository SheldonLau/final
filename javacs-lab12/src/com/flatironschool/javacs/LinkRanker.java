//package com.flatironschool.javacs;                                            
                                                                                
import java.io.IOException;                                                     
import java.util.HashMap;                                                       
import java.util.Map;                                                           
import java.util.Set;                                                           
                                                                                
import org.jsoup.nodes.Node;                                                    
import org.jsoup.nodes.TextNode;                                                
import org.jsoup.select.Elements; 

/**
 * Encapsulates map from url to link ranking
 *
 */

public class LinkRanker {
  private Map<String, Integer> map;
  private String link;
  
  public LinkRanker(String link) {
    this.link = link;
    this.map = new HashMap<String, Integer>();
  }

  
}
