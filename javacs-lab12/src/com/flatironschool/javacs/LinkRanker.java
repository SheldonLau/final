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
  private Map<String, Integer> map;
  
  public LinkRanker() {
    this.map = new HashMap<String, Integer>();
  }

//  public String getPair() {
//    return pair;
//  }

//  public Set<LinkPair> keySet() {
//    return map.keySet();
//  }

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
  public Map<String, Integer>  processLinks(Set<String> links) throws IOException{
    for(String dest : links) {
      for(String src : links) {
        processPair(src, dest);
      }
    }

    return map;
  }

  public void incrementLinkRank(String url) {
    put(url, get(url) + 1);
  }

  public void put(String url, int lc) {
    map.put(url, lc);
  }

  /**
   * Takes a link source and target and determines if they link to eachother
   *
   * @param src
   * @param dest
   */
  public void processPair(String src, String dest) throws IOException {
    Set<String> links = getLinks(src);
//    LinkPair pair = new LinkPair(src, dest);
    if(links.contains(dest)) {
      incrementLinkRank(dest);
    }
  }

  /**
   * Returns the boolean value associated with link pair
   * 
   * @param pair
   * @return
   */
  public Integer get(String url) {
    Integer linkCount = map.get(url);
    return linkCount == null ? 0 : linkCount;
  }
}
