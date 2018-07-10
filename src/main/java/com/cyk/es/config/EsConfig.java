package com.cyk.es.config;/**
 * Created by zhangshipeng on 2017/9/19.
 */

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * EsConfig
 *
 * @author zhangshipeng
 * @ClassName: EsConfig
 * @date 2017-09-19 10:50
 **/
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.cyk.es.service")
public class EsConfig {

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(Client client) {
        return new ElasticsearchTemplate(client);
    }


}
