package my.apps;

import java.util.HashMap;
import java.util.Map;

public class HttpParameters {
    public String url;
    public Map<String,String> formParameters;
    public Map<String,String> headers;
    public Map<String, String> cookies;

    public HttpParameters(String url) {
        this.url = url;
        this.cookies = new HashMap<String, String>();
        this.formParameters = new HashMap<String, String>();
        this.headers = new HashMap<String, String>();
        this.cookies = new HashMap<String, String>();
    }

    public static HttpParameters with(String url) {
        return new HttpParameters(url);
    }

    public HttpParameters cookie(String name, String value) {
        cookies.put(name, value);
        return this;
    }

    public HttpParameters formParameter(String name, String value) {
        formParameters.put(name, value);
        return this;
    }

    public HttpParameters header(String name, String value) {
        headers.put(name, value);
        return this;
    }
}
