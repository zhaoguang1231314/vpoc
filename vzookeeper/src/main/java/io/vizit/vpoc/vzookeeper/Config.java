package io.vizit.vpoc.vzookeeper;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class Config {
    @Value("${data.dir}")
    private String dataDir;

}
