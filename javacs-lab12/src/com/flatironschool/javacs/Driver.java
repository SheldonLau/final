//package com.flatironschool.javacs;                                              

import static org.junit.Assert.assertThat;                                      
import static org.hamcrest.CoreMatchers.*;                                      
                                                                                
import java.io.IOException;                                                     
import java.util.Scanner;
import java.util.Map;                                                           
import java.util.HashMap;                                                       
import java.util.List;                                                          
import java.util.Map.Entry;                                                     
                                                                                
import org.jsoup.select.Elements;                                               
import org.junit.After;                                                         
import org.junit.Before;                                                        
import org.junit.Test;                                                          
                                                                                
import redis.clients.jedis.Jedis;  
                                                                                
public class Driver {
  public static void main(String[] args) {
    Map<String, Integer> map = new HashMap<String, Integer>();
    Scanner input = new Scanner(System.in);
    WikiSearch search = new WikiSearch(map);
  }
}
