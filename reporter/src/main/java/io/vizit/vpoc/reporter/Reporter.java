package io.vizit.vpoc.reporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Reporter {
    private final Logger logger = LoggerFactory.getLogger(Reporter.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final ReporterConfig reporterConfig;

    public Reporter(ReporterConfig reporterConfig) {
        this.reporterConfig = reporterConfig;
    }

    public void report(ZkChange zkChange) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reporterConfig.getVpocUrl());
        try {
            String json = OBJECT_MAPPER.writeValueAsString(zkChange);
            StringEntity requestEntity = new StringEntity(
                    json,
                    ContentType.APPLICATION_JSON);
            httpPost.setEntity(requestEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            logger.info(response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
