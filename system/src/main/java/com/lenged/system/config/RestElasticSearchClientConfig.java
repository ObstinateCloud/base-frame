package com.lenged.system.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @title: RestElasticSearchClientConfig
 * @description: 使用RestHighLevelClient
 * @auther: zhangjianyun
 * @date: 2022/7/11 17:03
 */
//@Configuration
public class RestElasticSearchClientConfig extends AbstractElasticsearchConfiguration {


    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("192.168.20.216:9200")
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}
