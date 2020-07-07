package io.vizit.vpoc.kafka;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaApi {
    private final Producer producer;
    private final Consumer consumer;
    private final Cluster cluster;

    public KafkaApi(Producer producer, Consumer consumer, Cluster cluster) {
        this.producer = producer;
        this.consumer = consumer;
        this.cluster = cluster;
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public @ResponseBody
    KafkaResponse send(@RequestBody KafkaRequest request) {
        return cluster.send(request);
    }

    @RequestMapping(value = "/poll", method = RequestMethod.POST)
    public @ResponseBody
    KafkaResponse poll(@RequestBody KafkaRequest request) {
        return new KafkaResponse();
    }

    @RequestMapping(value = "/replicate", method = RequestMethod.POST)
    public @ResponseBody
    KafkaResponse replicate(@RequestBody KafkaRequest request) {
        return new KafkaResponse();
    }
}
