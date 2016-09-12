package de.mrfrey.adsbmonitor.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.mrfrey.adsbmonitor.connector.config.AdsbConfiguration;
import de.mrfrey.adsbmonitor.connector.data.FlightData;
import de.mrfrey.adsbmonitor.connector.flow.DuplicateFilter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mongodb.outbound.MongoDbStoringMessageHandler;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.net.UnknownHostException;
import java.util.Map;

import static org.springframework.integration.dsl.support.Transformers.fromJson;

@SpringBootApplication
public class Dump1090Connector {
    @Value("${mqtt.url}")
    private String mqttUrl;

    @Value("${mqtt.publish.clientid}")
    private String mqttPublisherClientId;

    @Value("${mqtt.subscribe.clientid}")
    private String mqttSubscriberClientId;

    @Autowired
    private AdsbConfiguration configuration;

    @Autowired
    private MongoDbFactory mongoDbFactory;

    @Autowired
    private ObjectMapper mapper;

    @Bean
    public MessageHandler mongoPersistence() {
        MongoDbStoringMessageHandler messageHandler = new MongoDbStoringMessageHandler(mongoDbFactory);
        messageHandler.setCollectionNameExpression(new LiteralExpression(configuration.getMongodb().getCollection()));
        return messageHandler;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
        clientFactory.setServerURIs(mqttUrl);
        clientFactory.setConnectionTimeout(5000);
        return clientFactory;
    }

    @Bean
    public MessageProducerSupport inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(mqttSubscriberClientId, mqttClientFactory(), configuration.getMqtt().getInboundTopic());
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(new DirectChannel());
        return adapter;
    }

    @Bean
    public MessageHandler outbound() {
        final String defaultTopic = configuration.getMqtt().getOutboundTopic();
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(mqttPublisherClientId, mqttClientFactory()) {
            @Override
            protected void handleMessageInternal(Message<?> message) throws Exception {
                Object mqttMessage = this.getConverter().fromMessage(message, Object.class);
                this.publish(defaultTopic, mqttMessage, message);

            }
        };
        handler.setDefaultTopic(defaultTopic);
        return handler;
    }

    @Bean
    public DuplicateFilter duplicateFilter() {
        return new DuplicateFilter();
    }

    @Bean
    public IntegrationFlow flightdata(DuplicateFilter duplicateFilter) {
        Jackson2JsonObjectMapper om = new Jackson2JsonObjectMapper(mapper);
        return IntegrationFlows.from(inbound())
                .transform(fromJson(Map.class, om))
                .split(Map.class,
                        m -> m.get("aircraft")
                )
                .transform(FlightData::fromJSON)
                .filter(FlightData::isComplete)
                .filter((FlightData fd) -> !duplicateFilter.duplicate(fd))
                .enrich(e -> e.requestPayload(Message::getPayload)
                        .propertyExpression("timestamp", "headers.timestamp")
                        .header(MqttHeaders.TOPIC, null, true)
                )
                .publishSubscribeChannel(s -> s
                        .subscribe(f -> f
//                                .transform(FlightData::toMap)
                                .handle(mongoPersistence()))
                        .subscribe(f -> f
                                .transform(FlightData::toMap)
                                .transform(Transformers.toJson())
                                .handle(outbound())
                        )
                        .subscribe(f -> f.handle(System.out::println))
                )
                .get();
    }


    public static void main(String[] args) {
        SpringApplication.run(Dump1090Connector.class, args);
    }
}
