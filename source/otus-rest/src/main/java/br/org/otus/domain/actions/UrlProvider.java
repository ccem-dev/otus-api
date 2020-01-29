package br.org.otus.domain.actions;

public class UrlProvider {
  public static final String DEFAULT_CONTEXT = "/otus-domain-rest";
  public static final String DEFAULT_VERSION = "/v01";

  private String url;
  private String context;
  private String version;

  public UrlProvider setUrl(String url) {
    this.url = url;

    return this;
  }

  public UrlProvider setContext(String context) {
    this.context = context;

    return this;
  }

  public UrlProvider setVersion(String version) {
    this.version = version;

    return this;
  }

  public String getFullAddress() {
    return url + context + version;
  }
}
