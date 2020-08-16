package io.vizit.vpoc.vzookeeper;

import io.vizit.vpoc.reporter.Reporter;
import io.vizit.vpoc.reporter.ZkChange;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@Component
public class ZnodeManager {

    private final Reporter reporter;
    private final ZkConfig zkConfig;

    public ZnodeManager(Reporter reporter, ZkConfig zkConfig) {
        this.reporter = reporter;
        this.zkConfig = zkConfig;
        File file = new File(zkConfig.getDataDir());
        file.mkdirs();
    }

    public void create(ZnodeRequest request) {
        File file = new File(zkConfig.getDataDir() + request.getPath());
        try {
            FileUtils.write(file, "noop", Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ZkChange zkChange = new ZkChange(request.getPath(), ZkChange.Type.CREATED);
        reporter.report(zkChange);
    }
}
