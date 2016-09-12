package de.mrfrey.adsbmonitor.connector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("adsb")
public class AdsbConfiguration {
    private Mqtt mqtt;
    private Mongodb mongodb;

    public Mqtt getMqtt() {
        return mqtt;
    }

    public void setMqtt(Mqtt mqtt) {
        this.mqtt = mqtt;
    }

    public Mongodb getMongodb() {
        return mongodb;
    }

    public void setMongodb(Mongodb mongodb) {
        this.mongodb = mongodb;
    }

    public static class Mqtt {
        private String inboundTopic;

        private String outboundTopic;

        public String getOutboundTopic() {
            return outboundTopic;
        }

        public void setOutboundTopic(String outboundTopic) {
            this.outboundTopic = outboundTopic;
        }

        public String getInboundTopic() {
            return inboundTopic;
        }

        public void setInboundTopic(String inboundTopic) {
            this.inboundTopic = inboundTopic;
        }
    }

    public static class Mongodb {
        private String collection;

        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }
    }
}
