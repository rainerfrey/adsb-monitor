package de.mrfrey.adsbmonitor.connector.config;

import de.mrfrey.adsbmonitor.connector.data.Position;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("adsb")
public class AdsbConfiguration {
    private Mqtt mqtt;
    private Mongodb mongodb;
    private Aws aws;
    private Position home;


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

    public Aws getAws() {
        return aws;
    }

    public void setAws( Aws aws ) {
        this.aws = aws;
    }

    public Position getHome() {
        return home;
    }

    public void setHome( Position home ) {
        this.home = home;
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
        private String messageStoreCollection;
        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }

        public String getMessageStoreCollection() {
            return messageStoreCollection;
        }

        public void setMessageStoreCollection(String messageStoreCollection) {
            this.messageStoreCollection = messageStoreCollection;
        }
    }

    public static class Aws {
        private boolean enabled;
        private String topic;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled( boolean enabled ) {
            this.enabled = enabled;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic( String topic ) {
            this.topic = topic;
        }
    }
}
