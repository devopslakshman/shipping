package com.instana.robotshop.shipping;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
public class CartHelper {
    private static final Logger logger = LoggerFactory.getLogger(CartHelper.class);

    private String baseUrl;
    public CartHelper(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    public String addToCart(String id, String data) {
        logger.info("add shipping to cart {}", id);
        StringBuilder buffer = new StringBuilder();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build()) {
            HttpPost postRequest = new HttpPost(baseUrl + id);
            StringEntity payload = new StringEntity(data, StandardCharsets.UTF_8);
            payload.setContentType("application/json");
            postRequest.setEntity(payload);
            try (CloseableHttpResponse res = httpClient.execute(postRequest)) {
                if (res.getStatusLine().getStatusCode() == 200) {
                    try (BufferedReader in = new BufferedReader(
                            new InputStreamReader(res.getEntity().getContent(), StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = in.readLine()) != null) {
                            buffer.append(line);
                        }
                    }
                } else {
                    logger.warn("Failed with code {}", res.getStatusLine().getStatusCode());
                }
            }
        } catch (IOException e) {
            logger.warn("http client exception", e);
        }
        // this will be empty on error
        return buffer.toString();
    }
}
