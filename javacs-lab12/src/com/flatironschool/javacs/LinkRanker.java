//package com.flatironschool.javacs;                                            
                                                                                
import java.io.IOException;                                                     
import java.util.HashMap;                                                       
import java.util.Map;                                                           
import java.util.Set;   
import java.util.HashSet;
                                                                                
import org.jsoup.nodes.Node;                                                    
import org.jsoup.nodes.TextNode;                                                
import org.jsoup.nodes.Element;                                                
import org.jsoup.select.Elements; 

/**
 * Encapsulates map from url to link status
 *
 */

public class LinkRanker {
  private Map<LinkPair, Boolean> map;
  private String pair;
  
  public LinkRanker(String pair) {
    this.map = new HashMap<LinkPair, Boolean>();
    this.pair = pair;
  }

  public String getPair() {
    return pair;
  }

  public Set<LinkPair> keySet() {
    return map.keySet();
  }

  /**
   * Returns set of links contained in page
   *
   * @return
   */
  public Set<String> getLinks(String src) throws IOException {
    Set<String> linkSet = new HashSet<String>();
    WikiFetcher wf = new WikiFetcher();
    Elements paragraphs = wf.fetchWikipedia(src);
    Elements links = paragraphs.select("a[href]");
    for(Element link : links) {
      String absHref = link.attr("abs:href");
      linkSet.add(absHref);
    }

    return linkSet;
  }
  
  /**
   * Takes a set of links and determines which pages link to eachother
   * 
   * @param links
   */
  public void processLinks(Set<String> links) throws IOException{
    for(String dest : links) {
      for(String src : links) {
        processPair(src, dest);
      }
    }
  }

  /**
   * Takes a link source and target and determines if they link to eachother
   *
   * @param src
   * @param dest
   */
  public void processPair(String src, String dest) throws IOException {
    Set<String> links = getLinks(src);
    LinkPair pair = new LinkPair(src, dest);
    if(links.contains(dest)) {
      map.put(pair, true);
    }
    else {
      map.put(pair, false);
    }
  }

  /**
   * Returns the boolean value associated with link pair
   * 
   * @param pair
   * @return
   */
  public boolean get(LinkPair pair) {
    boolean isLinked = map.get(pair);
    return isLinked;
  }
}
