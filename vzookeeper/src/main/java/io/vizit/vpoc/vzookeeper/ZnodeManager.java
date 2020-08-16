package io.vizit.vpoc.vzookeeper;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@Component
public class ZnodeManager {

    private final Config config;

    public ZnodeManager(Config config) {
        this.config = config;
        File file = new File(config.getDataDir());
        file.mkdirs();
    }

    public void create(ZnodeRequest request) {
        File file = new File(config.getDataDir() + request.getPath());
        try {
            FileUtils.write(file, "noop", Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
