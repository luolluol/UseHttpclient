package com.luol.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by ruanlisi on 17/6/3.
 */
public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);


    public static  <T> T post(String url,
                              CloseableHttpClient httpClient,
                              Header[] headers,
                              HttpEntity httpEntity,
                              EntityConsumer<T> entityConsumer){
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeaders(headers);
        httpPost.setEntity(httpEntity);
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            if(httpResponse != null && HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()){
                HttpEntity responseEntity = httpResponse.getEntity();
                return entityConsumer.consume(responseEntity);
            }else{
                LOGGER.error("Execute http request not ok!");
            }
        } catch (IOException e) {
            LOGGER.error("Execute http request error!", e);
        } finally {
            if(httpResponse != null){
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    LOGGER.error("Close http response error!", e);
                }
            }
        }
        return null;
    }

    public static String postForString(String url,
                                       CloseableHttpClient httpClient,
                                       Header[] headers,
                                       HttpEntity httpEntity,
                                       final String encoding){
        return HttpUtil.post(url, httpClient, headers, httpEntity, new EntityConsumer<String>() {
            public String consume(HttpEntity httpEntity) {
                try {
                    return EntityUtils.toString(httpEntity, encoding);
                } catch (IOException e) {
                    LOGGER.error("Consumer http entity error!", e);
                }
                return null;
            }
        });
    }

    public interface EntityConsumer<T>{
        T consume(HttpEntity httpEntity);
    }
}
