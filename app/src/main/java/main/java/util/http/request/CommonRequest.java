package main.java.util.http.request;

public abstract class CommonRequest {
    abstract public String toRequestString();
    abstract public String getKey();
    abstract public String getUrl();
}
