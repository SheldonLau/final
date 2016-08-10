// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\HelloServlet.java"
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

//import static org.hamcrest.CoreMatchers.*;
//import java.io.IOException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.select.Elements;
import redis.clients.jedis.Jedis;

public class SearchServlet extends HttpServlet {
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws IOException, ServletException {

      // set up vars for search (from driver)
        Ranker test = new Ranker();
        Jedis jedis = JedisMaker.make();
        JedisIndex index = new JedisIndex(jedis);
        Map<String, Integer> map = new HashMap<String, Integer>();
        WikiSearch search = new WikiSearch(map);
        Set<String> urls = index.termCounterKeys();

        // String prompt = "Options - s: search a term ; i: index a topic ; " +
        //         "r: remove all indexed topics ; q: quit : ";
        // System.out.print("Enter a term to search for: ");
        // String term = input.nextLine();
        WikiSearch search1;
        String searchString = request.getParameter("searchString");
        //operators/second term
        if (searchString.contains("+")) {
            ///find int start and end of and
            int[] arr = getStartandEndIndexes("+");
            List<String> searchterms = getStrings(searchString, arr[0], arr[1]);
            search1 = WikiSearch.search(searchterms.get(0), index).add(WikiSearch.search(searchterms.get(1), index))
        } else if (searchString.contains("-")) {
            int[] arr = getStartandEndIndexes("-");
            List<String> searchterms = getStrings(searchString, arr[0], arr[1]);
            search1 = WikiSearch.search(searchterms.get(0), index).add(WikiSearch.search(searchterms.get(1), index))
        } else if (searchString.contains("or")) {
            int[] arr = getStartandEndIndexes("or");
            List<String> searchterms = getStrings(searchString, arr[0], arr[1]);
            search1 = WikiSearch.search(searchterms.get(0), index).add(WikiSearch.search(searchterms.get(1), index))
        } else {//1 term
            WikiSearch search1 = WikiSearch.search(searchString, index);
        }
      // Set the response MIME type of the response message
      response.setContentType("text/html");
      // Allocate a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();

      // Write the response message, in an HTML page
      try {
         out.println("<html>");
         out.println("<head><title>Search</title></head>");
         out.println("<script src='https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js'></script>
         <link rel='stylesheet' type='text/css' href='style.css'/>
         <link href='https://fonts.googleapis.com/css?family=Pacifico|Arimo:400,400italic' rel='stylesheet' type='text/css'>")
         out.println("<body>");
         out.println("<div class='mainContent rectangle'");
         out.println("<div class='textContent'");
         out.println("<h1>Search Results</h1>");
         if (searchString.equals(""))
            out.println("<p> You can't have an empty search!</p>")
         out.println("<p>" + search1 + "</p>");
         out.println("<a href='search.jsp'>new search</a>");
         out.println("</div></div>");
         out.println("</body></html>");
      } finally {
         out.close();
      }
   }
   private List<String> getStrings(String searchString, int opbeg, int opend) {
       List<String> list = new List<String>();
       list.add(searchString.substring(0,opbeg).trim());
       list.add(searchString.substring(opend+1).trim());
       return list;
   }
   private int[] getStartandEndIndexes(String pattern, String text) {
       //brute force for now
       int[] arr = new int[2];
       int patlength = pattern.length();
       int textlength = text.length();

       for (int i = 0; i <= textlength - patlength; i++) {
           int j;
           for (j = 0; j < patlength; j++) {
               if (text.charAt(i + j) != pattern.charAt(j))
                break;
           }
           if (j == patlength) {
               arr[0] = i;
               arr[1] = i + textlength;
               return arr;
           }
       }
       return arr;
   }
}