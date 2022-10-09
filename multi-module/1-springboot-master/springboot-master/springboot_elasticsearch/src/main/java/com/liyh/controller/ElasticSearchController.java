package com.liyh.controller;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Author: liyh
 * @Date: 2020/9/16 17:12
 */
@RestController
public class ElasticSearchController {

    private static int port = 9300;//通过http请求的端口号是9200，通过客户端请求的端口号是9300
    // 使用本地的es,每个测试中的 settings  要换成  Settings.EMPTY
    //private static String host = "localhost";     //elasticsearch的服务器地址
    private static String host = "192.168.2.37";    //elasticsearch的服务器地址

    @RequestMapping("connect")
    public String connect() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，docker-cluster是ES集群的名称
        Settings settings = Settings.builder()
                //设置es集群名称
                .put("cluster.name", "docker-cluster")
                //增加嗅探机制，找到es集群
                .put("client.transport.sniff", true)
                .build();
        /*
         * 创建客户端，所有的操作都由客户端开始，这个就好像是JDBC的Connection对象
         * 用完记得要关闭
         */
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddresses(
                new TransportAddress(InetAddress.getByName(host), port));
        System.out.println("client :  " + client);
        return client.toString();
    }

    //从es中查询数据
    @RequestMapping("testGet/{id}")
    public String testGet(@PathVariable("id") String id) throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，docker-cluster是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "docker-cluster").build();
        //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddresses(
                new TransportAddress(InetAddress.getByName(host), port));
        //实现数据查询(指定_id查询) 参数分别是 索引名，类型名  id
        GetResponse getResponse = client.prepareGet("estest", "student",id).get();
        String sourceAsString = getResponse.getSourceAsString();
        System.out.println(sourceAsString);
        client.close();//关闭客户端
        return sourceAsString;
    }

    //从es中查询所有数据
    @RequestMapping("testGetAll")
    public String testGetAll() throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，docker-cluster是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "docker-cluster").build();
        //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddresses(
                new TransportAddress(InetAddress.getByName(host), port));
        QueryBuilder qBuilder = QueryBuilders.matchAllQuery();
        SearchResponse sResponse = client.prepareSearch("estest")
                .setQuery(qBuilder)
                .get();
        SearchHits hits = sResponse.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            //将获取的值转换成map的形式
            Map<String, Object> map = hit.getSourceAsMap();
            System.out.println("url = : " + map.get("url"));

            for (String key : map.keySet()) {
                System.out.println(key +" ：" +map.get(key));
            }
            System.out.println("==================");
        }
        return "success";
    }

    //插入数据
    @RequestMapping("testInsert")
    public String testInsert() throws IOException {
        //1、指定es集群  cluster.name 是固定的key值，docker-cluster是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "docker-cluster").build();
        //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddresses(
                new TransportAddress(InetAddress.getByName(host), port));
        //将数据转换成文档的格式（后期可以使用java对象，将数据转换成json对象就可以了）
        XContentBuilder doContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .field("id", "1") //字段名 ： 值
                .field("name", "网民")
                .field("address", "上海虹桥")
                .field("age", "20")
                .field("date", "2018-05-20")
                .field("url", "www.wangmin.com")
                .endObject();
        //添加文档  index1:索引名 blog:类型 10:id
        //.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE) 代表插入成功后立即刷新，因为ES中插入数据默认分片要1秒钟后再刷新

        IndexResponse response = client.prepareIndex("estest", "student", "1")
                .setSource(doContentBuilder).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).get();
        //打印出CREATED 表示添加成功
        System.out.println(response.status());
        return response.status().toString();
    }

    //删除数据
    @RequestMapping("testDelete/{id}")
    public String testDelete(@PathVariable("id") String id) throws UnknownHostException {
        //1、指定es集群  cluster.name 是固定的key值，docker-cluster是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "docker-cluster").build();
        //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddresses(
                new TransportAddress(InetAddress.getByName(host), port));
        //将数据转换成文档的格式（后期可以使用java对象，将数据转换成json对象就可以了）
        DeleteResponse response = client.prepareDelete("estest", "student", id).get();
        //控制台打印出OK代表删除成功
        System.out.println(response.status());
        return response.status().toString();
    }

    //修改数据（指定字段进行修改)
    @RequestMapping("testUpdate/{id}")
    public String testUpdate(@PathVariable("id") String id) throws IOException, InterruptedException, ExecutionException {
        //1、指定es集群  cluster.name 是固定的key值，docker-cluster是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "docker-cluster").build();
        //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddresses(
                new TransportAddress(InetAddress.getByName(host), port));
        UpdateRequest request = new UpdateRequest();
        request.index("estest") //索引名
                .type("student") //类型
                .id(id)//id
                .doc(
                        XContentFactory.jsonBuilder()
                                .startObject()
                                .field("age", "33")//要修改的字段 及字段值
                                .endObject()
                );
        UpdateResponse response = client.update(request).get();
        //控制台出现OK 代表更新成功
        System.out.println(response.status());
        return response.status().toString();
    }

    //upsert 修改用法：修改数据存在，执行修改，不存在则执行插入
    @RequestMapping("testUpsert/{id}")
    public String testUpsert(@PathVariable("id") String id) throws IOException, InterruptedException, ExecutionException {
        //1、指定es集群  cluster.name 是固定的key值，docker-cluster是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "docker-cluster").build();
        //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddresses(
                new TransportAddress(InetAddress.getByName(host), port));
        IndexRequest request1 = new IndexRequest("estest", "student",id).source(
                XContentFactory.jsonBuilder()
                        .startObject()
                        .field("id", id) //字段名 ： 值
                        .field("name", "刘希")
                        .field("address", "北京天安门")
                        .field("age", "33")
                        .field("date", "1990-11-11")
                        .field("url", "www.liuxi.com")
                        .endObject()
        );
        UpdateRequest request2 = new UpdateRequest("estest", "student",id).doc(
                XContentFactory.jsonBuilder().startObject()
                        .field("age", "44")
                        .endObject()
        ).upsert(request1);
        UpdateResponse response = client.update(request2).get();
        //控制台出现OK 代表更新成功, 控制台出现CREATED 代表插入成功
        System.out.println(response.status());
        return response.status().toString();
    }

}
