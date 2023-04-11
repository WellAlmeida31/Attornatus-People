package com.attornatus.people.infrastructure.configuration;

import com.attornatus.people.infrastructure.ConnectionRemote;
import jakarta.ws.rs.core.UriBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient43Engine;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResteasyConfig {
    private final UriBuilder BASE_PATH = UriBuilder.fromPath("https://viacep.com.br");

    @Bean
    public ResteasyClient resteasyClient(){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(300);
        connectionManager.setDefaultMaxPerRoute(20);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        ApacheHttpClient43Engine engine = new ApacheHttpClient43Engine(httpClient);
        return new ResteasyClientBuilderImpl()
                .httpEngine(engine)
                .register(new ResteasyJackson2Provider()).build();
    }

    @Bean
    public ResteasyWebTarget target(){
        return resteasyClient().target(BASE_PATH);
    }

    @Bean
    public ConnectionRemote conectionRemote(){
        return new ResteasyConfig().target().proxy(ConnectionRemote.class);
    }

}
