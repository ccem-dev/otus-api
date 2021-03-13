package br.org.otus.response.exception;

public class HttpResponseException extends RuntimeException {
  private ResponseInfo responseInfo;

  public HttpResponseException(ResponseInfo responseInfo) {
    this.responseInfo = responseInfo;
  }

  public String getMessage() {
    return this.responseInfo.MESSAGE;
  }

  public ResponseInfo getResponseInfo() {
    return responseInfo;
  }
}
