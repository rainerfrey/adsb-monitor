package de.mrfrey.adsbmonitor.connector.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNSAsyncClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.config.AmazonWebserviceClientFactoryBean;
import org.springframework.cloud.aws.core.region.RegionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonSNSConfiguration {
    private final AWSCredentialsProvider awsCredentialsProvider;

    private final RegionProvider regionProvider;

    public AmazonSNSConfiguration( @Autowired(required = false) AWSCredentialsProvider awsCredentialsProvider, @Autowired(required = false) RegionProvider regionProvider ) {
        this.awsCredentialsProvider = awsCredentialsProvider;
        this.regionProvider = regionProvider;
    }

    @Bean
    public AmazonWebserviceClientFactoryBean<AmazonSNSAsyncClient> amazonSNS() {
        return new AmazonWebserviceClientFactoryBean<>( AmazonSNSAsyncClient.class,
                                                        this.awsCredentialsProvider, this.regionProvider );
    }
}
