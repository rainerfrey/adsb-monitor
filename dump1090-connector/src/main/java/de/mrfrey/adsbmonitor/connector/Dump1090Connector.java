package de.mrfrey.adsbmonitor.connector;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mrfrey.adsbmonitor.connector.aggregate.LatestOnlyAggregator;
import de.mrfrey.adsbmonitor.connector.config.AdsbConfiguration;
import de.mrfrey.adsbmonitor.connector.config.AmazonSNSConfiguration;
import de.mrfrey.adsbmonitor.connector.data.AwsData;
import de.mrfrey.adsbmonitor.connector.data.FlightData;
import de.mrfrey.adsbmonitor.connector.flow.AwsFlightTransformer;
import de.mrfrey.adsbmonitor.connector.flow.DuplicateFilter;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.aggregator.AggregatingMessageHandler;
import org.springframework.integration.aggregator.ExpressionEvaluatingCorrelationStrategy;
import org.springframework.integration.aggregator.MessageCountReleaseStrategy;
import org.springframework.integration.aws.outbound.SnsMessageHandler;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.expression.ValueExpression;
import org.springframework.integration.mongodb.outbound.MongoDbStoringMessageHandler;
import org.springframework.integration.mongodb.store.MongoDbMessageStore;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.store.MessageGroupStore;
import org.springframework.integration.store.MessageGroupStoreReaper;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.util.Map;

import static org.springframework.integration.dsl.Transformers.fromJson;
import static org.springframework.integration.dsl.Transformers.toJson;

@SpringBootApplication
@Import( AmazonSNSConfiguration.class )
public class Dump1090Connector {
    @Value( "${mqtt.url}" )
    private String mqttUrl;

    @Value( "${mqtt.publish.clientid}" )
    private String mqttPublisherClientId;

    @Value( "${mqtt.subscribe.clientid}" )
    private String mqttSubscriberClientId;

    private final AdsbConfiguration configuration;

    private final MongoDbFactory mongoDbFactory;

    private final ObjectMapper mapper;

    private final SpelExpressionParser spelExpressionParser;

    public Dump1090Connector( AdsbConfiguration configuration, MongoDbFactory mongoDbFactory, ObjectMapper mapper ) {
        this.configuration = configuration;
        this.mongoDbFactory = mongoDbFactory;
        this.mapper = mapper;
        this.spelExpressionParser = new SpelExpressionParser();
    }

    @Bean
    public MessageHandler mongoPersistence() {
        MongoDbStoringMessageHandler messageHandler = new MongoDbStoringMessageHandler( mongoDbFactory );
        messageHandler.setCollectionNameExpression( new LiteralExpression( configuration.getMongodb().getCollection() ) );
        return messageHandler;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs( new String[]{ mqttUrl } );
        DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
        clientFactory.setConnectionOptions( options );
        return clientFactory;
    }

    @Bean
    public MessageProducerSupport inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter( mqttSubscriberClientId, mqttClientFactory(), configuration.getMqtt().getInboundTopic() );
        adapter.setCompletionTimeout( 5000 );
        adapter.setConverter( new DefaultPahoMessageConverter() );
        adapter.setQos( 1 );
        adapter.setOutputChannel( new DirectChannel() );
        return adapter;
    }

    @Bean
    public MessageHandler outbound() {
        final String defaultTopic = configuration.getMqtt().getOutboundTopic();
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler( mqttPublisherClientId, mqttClientFactory() ) {
            @Override
            protected void handleMessageInternal( Message<?> message ) {
                Object mqttMessage = this.getConverter().fromMessage( message, Object.class );
                this.publish( defaultTopic, mqttMessage, message );

            }
        };
        handler.setDefaultTopic( defaultTopic );
        return handler;
    }

    @Bean
    MessageGroupStore awsAggregatorStore() {
        return new MongoDbMessageStore( mongoDbFactory, configuration.getMongodb().getMessageStoreCollection() );
    }

    MessageGroupStoreReaper messageGroupStoreReaper() {
        MessageGroupStoreReaper reaper = new MessageGroupStoreReaper( awsAggregatorStore() );
        reaper.setAutoStartup( true );
        reaper.setExpireOnDestroy( true );
        reaper.setTimeout( 600000l );
        reaper.setPhase( Ordered.LOWEST_PRECEDENCE );
        return reaper;
    }

    @Bean
    public AggregatingMessageHandler awsAggregator() {
        AggregatingMessageHandler aggregator = new AggregatingMessageHandler( new LatestOnlyAggregator(), awsAggregatorStore(), new ExpressionEvaluatingCorrelationStrategy( "payload.flightId" ), new MessageCountReleaseStrategy( 10 ) );
        aggregator.setExpireGroupsUponCompletion( true );
        aggregator.setExpireGroupsUponTimeout( true );
        aggregator.setGroupTimeoutExpression( new ValueExpression<>( 15000l ) );
        aggregator.setSendPartialResultOnExpiry( true );
        return aggregator;
    }

    @Bean
    public DuplicateFilter duplicateFilter() {
        return new DuplicateFilter();
    }


    @Bean
    public SnsMessageHandler snsMessageHandler( AmazonSNSAsync amazonSNS ) {
        SnsMessageHandler handler = new SnsMessageHandler( amazonSNS );
        handler.setTopicArn( configuration.getAws().getTopic() );
        String bodyExpression = "T(org.springframework.integration.aws.support.SnsBodyBuilder).withDefault(payload)";
        handler.setBodyExpression( spelExpressionParser.parseExpression( bodyExpression ) );
        return handler;
    }

    @Bean
    public IntegrationFlow flightdata( DuplicateFilter duplicateFilter, AwsFlightTransformer awsFlightTransformer, SnsMessageHandler snsMessageHandler ) {
        Jackson2JsonObjectMapper om = new Jackson2JsonObjectMapper( mapper );
        return IntegrationFlows.from( inbound() )
                               .transform( fromJson( Map.class, om ) )
                               .split( Map.class,
                                       m -> m.get( "aircraft" )
                               )
                               .transform( FlightData::fromJSON )
                               .filter( FlightData::isComplete )
                               .filter( ( FlightData fd ) -> !duplicateFilter.duplicate( fd ) )
                               .enrich( e -> e
                                   .requestPayload( Message::getPayload )
                                   .propertyExpression( "timestamp", "headers.timestamp" )
                                   .header( MqttHeaders.TOPIC, null, true )
                               )
                               .publishSubscribeChannel( s -> {
                                                             s
                                                                 .subscribe( f -> f
                                                                     .handle( mongoPersistence() ) )
                                                                 .subscribe( f -> f
                                                                     .transform( FlightData::toMap )
                                                                     .transform( toJson() )
                                                                     .log()
                                                                     .handle( outbound() )
                                                                 )
                                                                 .subscribe( f -> f.handle( System.out::println ) );

                                                             if ( configuration.getAws().isEnabled() ) {
                                                                 s.subscribe( f -> f
                                                                     .handle( awsAggregator() )
                                                                     .transform( awsFlightTransformer::transform )
                                                                     .filter( ( AwsData flight ) -> flight.getRegistration() != null )
                                                                     .transform( toJson() )
                                                                     .log()
                                                                     .handle( snsMessageHandler )
                                                                 );
                                                             }
                                                         }

                               )
                               .get();
    }


    public static void main( String[] args ) {
        SpringApplication.run( Dump1090Connector.class, args );
    }
}
