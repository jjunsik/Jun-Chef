package main.java.util.http.request;

public abstract class CommonRequest {
    abstract public String toPostRequestString();
    abstract public String toGetRequestString();
    abstract public String getKey();
    abstract public String getUrl();
}
