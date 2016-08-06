public class LinkPair {
  private String url1;
  private String url2;
  private String pair;

  public LinkPair(String url1, String url2) {
    this.url1 = url1;
    this.url2 = url2;
    this.pair = url1+url2;
  }

  public String getSrc() {
    return url1;
  }

  public String getDest() {
    return url2;
  }

  public String getPair() {
    return pair;
  }
}
