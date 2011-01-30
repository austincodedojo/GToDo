package my.apps;

import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class HttpService {
    private DefaultHttpClient httpClient;
    private HttpContext context;
    private BasicCookieStore cookieStore;

    public HttpService() {
        httpClient = new DefaultHttpClient();
        cookieStore = new BasicCookieStore();
        context = new BasicHttpContext();
        context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    public String get(HttpParameters parameters) throws IOException {
        HttpGet httpGet = new HttpGet(parameters.url);
        populateMessageFromParameters(httpGet, parameters);
        HttpResponse httpResponse = httpClient.execute(httpGet, context);
        return responseToString(httpResponse);
    }

    public String post(HttpParameters parameters) throws IOException {
        HttpPost httpPost = new HttpPost(parameters.url);
        populateMessageFromParameters(httpPost, parameters);
        populatePostBodyFromParameters(httpPost, parameters);
        HttpResponse httpResponse = httpClient.execute(httpPost, context);
        return responseToString(httpResponse);
    }

    private void populatePostBodyFromParameters(HttpPost httpPost, HttpParameters parameters) throws UnsupportedEncodingException {
        List<NameValuePair> args = new ArrayList<NameValuePair>(parameters.formParameters.size());
        for(Map.Entry<String, String> formParam : parameters.formParameters.entrySet()) {
            args.add(new BasicNameValuePair(formParam.getKey(), formParam.getValue()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(args));
    }

    private String responseToString(HttpResponse httpResponse) throws IOException {
        StringBuffer response = new StringBuffer();
        byte[] contentBuffer = new byte[1024];
        InputStream content = httpResponse.getEntity().getContent();
        int l;
        while((l = content.read(contentBuffer)) != -1) {
            response.append(new String(contentBuffer, 0, l, "US-ASCII"));
        }

        return response.toString();
    }

    private void populateMessageFromParameters(HttpMessage httpMessage, HttpParameters parameters) {
        for(Map.Entry<String, String> header : parameters.headers.entrySet()) {
            httpMessage.addHeader(header.getKey(), header.getValue());
        }

        for(Map.Entry<String, String> cookie : parameters.cookies.entrySet()) {
            BasicClientCookie clientCookie = new BasicClientCookie(cookie.getKey(), cookie.getValue());
            clientCookie.setDomain("mail.google.com");
            clientCookie.setPath("/tasks");

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 3);
            clientCookie.setExpiryDate(cal.getTime());
            clientCookie.setSecure(true);
            cookieStore.addCookie(clientCookie);
        }
    }
}
