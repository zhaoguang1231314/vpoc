package io.vizit.vpoc.reporter;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ReporterConfig {
    //    @Value("${vpoc.url}")
    private String vpocUrl = "http://localhost:8080/visualize/kafka/zk";
}
