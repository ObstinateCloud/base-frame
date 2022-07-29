package com.lenged.system.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title: JavaApiEsClient
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/7/29 14:52
 */

@Configuration
public class JavaApiEsClientConfig {

    @Bean
    public ElasticsearchClient esNewClient(){
        RestClient restClient = RestClient.builder(new HttpHost("192.168.20.216",9200)).build();
        ElasticsearchTransport elasticsearchTransport = new RestClientTransport(restClient,new JacksonJsonpMapper());
        ElasticsearchClient elasticsearchClient = new ElasticsearchClient(elasticsearchTransport);
        return elasticsearchClient;
    }



}
