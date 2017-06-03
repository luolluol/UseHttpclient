package com.luol.httpclient;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by ruanlisi on 17/6/3.
 */
public class HttpclientTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpclientTest.class);

    @Test
    public void test() throws InterruptedException {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(20);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(2);

        //poolingHttpClientConnectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("localhost", 8081)), 3);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000).build();

        final CloseableHttpClient closeableHttpClient = HttpClients.custom().
                setConnectionManager(poolingHttpClientConnectionManager).
                setDefaultRequestConfig(requestConfig).
                build();

        final long timeout = 4000;
        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    String uuid = UUID.randomUUID().toString().replace("-", "");
                    LOGGER.info("Http request uuid:{}, wating for response!", uuid);
                    String responseStr = HttpUtil.postForString("http://localhost:8081/hangup/" + uuid + "/" + timeout,
                            closeableHttpClient, null, null, "UTF-8");
                    LOGGER.info("Http request uuid:{}, response:{}", uuid, responseStr);
                }
            });
            thread.start();
        }

        TimeUnit.MILLISECONDS.sleep(timeout + 3000);
    }
}
