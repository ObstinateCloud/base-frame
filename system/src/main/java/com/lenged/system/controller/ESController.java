package com.lenged.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lenged.system.es.entity.SysUser;
import com.lenged.system.es.mapper.SysUserDao;
import com.lenged.system.es.mapper.SysUserSearcher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * @title: ESController
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/5/7 11:49
 */

@RestController
@RequestMapping("es")
@Api(tags = "es 测试接口")
public class ESController {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserSearcher sysUserSearcher;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @ApiOperation(value = "继承ElasticsearchRepository 父类默认新增方法",position = 1)
    @GetMapping("put")
    public String esPut() {
        List<String> list = new ArrayList<>();
        list.add("teacher");
        list.add("student");
        list.add("admin");
        list.add("leader");
        for (int i = 0; i < 10; i++) {
            int toIndex = new Random(1).nextInt(4);
            SysUser build = SysUser.builder()
                    .password("123456" + i)
                    .username("AI码师" + i)
                    .level(i)
                    .roles(list.subList(0, toIndex))
                    .build();
            sysUserDao.save(build);
        }
        System.out.printf("结束");
        return "success";
    }
    @ApiOperation(value = "继承ElasticsearchRepository 父类默认查询全部",position = 2)
    @GetMapping("findAll")
    public void testFindAll() {
        Iterable<SysUser> all = sysUserDao.findAll();
        all.forEach((sysUser) -> {
            System.out.println(sysUser.getId());
        });
    }

    @ApiOperation("继承ElasticsearchRepository 父类默认删除")
    @GetMapping("testDel")
    public void testDel(String id) {
        sysUserDao.deleteById(id);
        System.out.println(sysUserDao.existsById(id));
    }
    @ApiOperation("继承ElasticsearchRepository 父类默认分页查询")
    @GetMapping("pageList")
    public void pageList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SysUser> all = sysUserDao.findAll(pageable);
        all.getContent().forEach(p -> {
            System.out.println(p.getId());
        });
    }

    @ApiOperation("继承ElasticsearchRepository 父类默认方法")
    @GetMapping("findById")
    public JSONObject findById(String id) {
        JSONObject jsonObject = new JSONObject();
        Optional<SysUser> sysUser = sysUserDao.findById(id);
        jsonObject = JSONObject.parseObject(JSON.toJSONString(sysUser));

        return jsonObject;
    }

    @ApiOperation("继承ElasticsearchRepository 自定义jpa字段查询")
    @GetMapping("findByUsername")
    public JSONArray findByUsername(@ApiParam(value = "用户名", required =true)String userName) {
        List<SysUser> sysUsers = sysUserDao.findSysUsersByUsername(userName);//精确匹配
//        List<SysUser> sysUsers = sysUserDao.findByUsername(userName);//精确匹配
        JSONArray objects = JSON.parseArray(JSON.toJSONString(sysUsers));
        return objects;
    }

    @ApiOperation("继承ElasticsearchRepository 自定义jpa范围查询")
    @GetMapping("findByLevel")
    public JSONArray findByLevel(@ApiParam(value = "层级", required =true)int level) {
        List<SysUser> sysUsers = sysUserDao.findSysUsersByLevelGreaterThanEqual(level);
//        List<SysUser> sysUsers = sysUserDao.findByLevelGreaterThanEqual(level);
        JSONArray objects = JSON.parseArray(JSON.toJSONString(sysUsers));
        return objects;
    }

    @ApiOperation("Criteria 方式查询")
    @GetMapping(value = "findByPass")
    public JSONArray findByPass(@ApiParam(value = "密码", required =true)String password) {
        List<SysUser> sysUsers = sysUserSearcher.findByPassword(password);
        JSONArray objects = JSON.parseArray(JSON.toJSONString(sysUsers));
        return objects;
    }
    @ApiOperation("RestHighLevelClient 创建索引简单方式")
    @GetMapping(value = "createIndex")
    public CreateIndexResponse createIndex(String index) {
        //创建索引请求
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        //client 执行请求
        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createIndexResponse;
    }
    @ApiOperation("RestHighLevelClient 创建索引")
    @GetMapping(value = "createMapping")
    public CreateIndexResponse createMapping(@ApiParam(value = "索引", required =true)String index) {
        //创建索引请求
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        //client 执行请求
        CreateIndexResponse createIndexResponse = null;
        try {
            String mapping = "{\n" +
                    "  \"properties\":{\n" +
                    "    \"name\":{\n" +
                    "      \"type\":\"keyword\"\n" +
                    "    },\n" +
                    "    \"age\":{\n" +
                    "      \"type\":\"integer\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
            createIndexRequest.mapping(mapping, XContentType.JSON);
            createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createIndexResponse;
    }

    @ApiOperation("RestHighLevelClient 判断索引是否存在")
    @GetMapping(value = "indexExists")
    public boolean indexExists(@ApiParam(value = "索引", required =true)String index) {
        //查询索引请求
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        //client 执行请求
        boolean exists = false;
        try {
            exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return exists;
    }
    @ApiOperation("RestHighLevelClient 删除索引")
    @GetMapping(value = "delIndex")
    public AcknowledgedResponse delIndex(@ApiParam(value = "索引", required =true)String index) {
        //删除索引请求
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        //client 执行请求
        AcknowledgedResponse delete = null;
        try {
            delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return delete;
    }

    @GetMapping(value = "createDoc")
    @ApiOperation("RestHighLevelClient 创建文档")
    public IndexResponse createDoc(String index) {
        List<String> list = new ArrayList<>();
        list.add("teacher");
        list.add("student");
        list.add("admin");
        list.add("leader");
        //1.准备对象
        SysUser build = SysUser.builder()
                .password("zjynb")
                .username("lengedyun")
                .level(5)
                .roles(list)
                .build();
        //2 创建索引
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id("1");
        indexRequest.timeout(TimeValue.timeValueSeconds(5));
        indexRequest.source(JSON.toJSONString(build),XContentType.JSON);
        //执行请求
        IndexResponse indexResponse = null;
        try {
            indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return indexResponse;
    }

    @GetMapping(value = "updateDocFull")
    @ApiOperation("RestHighLevelClient 更新文档-全局更新")
    public IndexResponse updateDocFull(String index) {
        //1.准备对象
        SysUser build = SysUser.builder()
                .username("lengedyun——update")
                .level(8)
                .build();
        //2 创建索引
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id("2");
        indexRequest.source(JSON.toJSONString(build), XContentType.JSON);
        //执行请求
        IndexResponse indexResponse = null;
        try {
            indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return indexResponse;
    }

    @GetMapping(value = "updateDocPart")
    @ApiOperation("RestHighLevelClient 更新文档-局部更新")
    public UpdateResponse updateDocPart(String index) {
        //1.准备对象
        SysUser build = SysUser.builder()
                .username("lengedyun——update")
                .level(8)
                .build();
        //2 创建索引
        UpdateRequest updateRequest = new UpdateRequest(index,"1");
        updateRequest.doc(JSON.toJSONString(build),XContentType.JSON);
        //执行请求
        UpdateResponse updateResponse = null;
        try {
            updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return updateResponse;
    }

    @GetMapping(value = "deleteDoc")
    @ApiOperation("RestHighLevelClient 删除文档")
    public DeleteResponse deleteDoc(String index, String id) {

        DeleteRequest deleteRequest = new DeleteRequest(index,id);
        DeleteResponse delete = null;
        try {
            delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return delete;
    }

    @GetMapping(value = "getDocById")
    @ApiOperation("RestHighLevelClient 获取单个文档")
    public GetResponse getDocById(String index, String id) {

        GetRequest getRequest = new GetRequest(index,id);
        GetResponse documentFields = null;
        try {
            documentFields = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentFields;
    }

    @GetMapping(value = "batchAdd")
    @ApiOperation("RestHighLevelClient 批量添加文档")
    public BulkResponse batchAdd(String index) {
        List<String> list = new ArrayList<>();
        list.add("teacher");
        list.add("student");
        list.add("admin");
        list.add("leader");
        BulkRequest bulkRequest =new BulkRequest();
        for (int i = 0; i < 10; i++) {
            SysUser build = SysUser.builder()
                    .password("admin" + i)
                    .username("程序员" + i+"xx")
                    .level(i)
                    .roles(list.subList(0, i%3+1))
                    .build();
            IndexRequest indexRequest = new IndexRequest(index);
            indexRequest.source(JSON.toJSONString(build),XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        //执行批量添加请求
        BulkResponse bulk = null;
        try {
            bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bulk;
    }

    @GetMapping(value = "getDocList")
    @ApiOperation("RestHighLevelClient 带条件查询列表")
    public SearchHits getDocList(String index, SysUser sysUser) {
        SearchRequest searchRequest =new SearchRequest(index);
        //构建搜索builder
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //模糊匹配
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("username",sysUser.getUsername());
        //精准匹配
        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("username",sysUser.getUsername());
        //精准匹配 注 term 遇到单次或者汉字会分词 查不到
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("username", sysUser.getUsername());


        builder.query(matchPhraseQueryBuilder);
        //设置分页查询
        builder.from(0);
        builder.size(14);
        //排序
        builder.sort("level", SortOrder.DESC);

        searchRequest.source(builder);

        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHits hits = search.getHits();
        hits.forEach(p->{
            System.out.println(p.getSourceAsMap().toString());
            System.out.println(p.getSourceAsString());
            System.out.println(p.getHighlightFields().toString());
            System.out.println("------------------");
        });

        return hits;

    }

    @GetMapping(value = "filterDocList")
    @ApiOperation("RestHighLevelClient 字段过滤")
    public SearchHits filterDocList(String index) {
        SearchRequest searchRequest =new SearchRequest(index);
        //构建搜索builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //全部查询
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(matchAllQueryBuilder);

        String[] excludeds = {};
        String[] includes = {"username"};
        //设置过滤字段
        searchSourceBuilder.fetchSource(includes, excludeds);
        searchRequest.source(searchSourceBuilder);

        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHits hits = search.getHits();
        hits.forEach(p->{
            System.out.println(p.getSourceAsString());
        });

        return hits;

    }

    @GetMapping(value = "boolQueryDocList")
    @ApiOperation("RestHighLevelClient 组合多条件查询")
    public SearchHits boolQueryDocList(String index, SysUser sysUser) {
        SearchRequest searchRequest =new SearchRequest(index);
        //构建搜索builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //构建多条件builder
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //must 必须满足
//        boolQueryBuilder.must(QueryBuilders.termQuery("username",sysUser.getUsername()));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("username",sysUser.getUsername()));
        // must not 必须不满足
        //should 或者 相当于mysql 的or
        boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("password",sysUser.getPassword()));
//        boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("username",sysUser.getUsername()));

        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHits hits = search.getHits();
        hits.forEach(p->{
            System.out.println(p.getSourceAsString());
        });

        return hits;

    }

    @GetMapping(value = "rangeQueryDocList")
    @ApiOperation("RestHighLevelClient 范围查询")
    public SearchHits rangeQueryDocList(String index, SysUser sysUser) {
        SearchRequest searchRequest =new SearchRequest(index);
        //构建搜索builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //构建范围builder
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("level");
        rangeQueryBuilder.gte(sysUser.getLevel());

        searchSourceBuilder.query(rangeQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHits hits = search.getHits();
        hits.forEach(p->{
            System.out.println(p.getSourceAsString());
        });

        return hits;

    }

    @GetMapping(value = "fuzzyQueryDocList")
    @ApiOperation("RestHighLevelClient 模糊查询")
    public SearchHits fuzzyQueryDocList(String index, SysUser sysUser) {
        SearchRequest searchRequest =new SearchRequest(index);
        //构建搜索builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //构建模糊builder  需要注意分词
        FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("username.keyword",sysUser.getUsername())
                .fuzziness(Fuzziness.TWO);


        searchSourceBuilder.query(fuzzyQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHits hits = search.getHits();
        hits.forEach(p->{
            System.out.println(p.getSourceAsString());
        });

        return hits;

    }

    @GetMapping(value = "highLightDocList")
    @ApiOperation("RestHighLevelClient 高亮查询")
    public SearchHits highLightDocList(String index, SysUser sysUser) {
        SearchRequest searchRequest =new SearchRequest(index);
        //构建搜索builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //构建高亮builder
        HighlightBuilder highlightBuilder = new HighlightBuilder();

        //设置高亮字段
        highlightBuilder.preTags("<font color='red'>");//前缀
        highlightBuilder.postTags("</font>");//后缀
        highlightBuilder.field("username");

        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("username",sysUser.getUsername()));

        searchRequest.source(searchSourceBuilder);

        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHits hits = search.getHits();


        hits.forEach(p->{
            Text[] usernames = p.getHighlightFields().get("username").fragments();
            String name = "";
            for (Text username : usernames) {
                name+= username;
            }
            System.out.println(name);
        });

        return hits;

    }

    @GetMapping(value = "aggregationDoc")
    @ApiOperation("RestHighLevelClient 聚合函数最大、最下、平均值")
    public Aggregations aggregationDoc(String index, String field) {
        SearchRequest searchRequest =new SearchRequest(index);
        //构建搜索builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //构建最大值builder
        MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max("Max_"+field).field(field);

        searchSourceBuilder.aggregation(maxAggregationBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Aggregations aggregations = search.getAggregations();


        return aggregations;

    }

    @GetMapping(value = "aggregationGroupDoc")
    @ApiOperation("RestHighLevelClient 聚合函数分组查询")
    public Aggregations aggregationGroupDoc(String index, String field) {
        SearchRequest searchRequest =new SearchRequest(index);
        //构建搜索builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(field+"_group").field(field);
        searchSourceBuilder.aggregation(termsAggregationBuilder);


        searchRequest.source(searchSourceBuilder);

        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHits hits = search.getHits();
        hits.forEach(p->{
            System.out.println(p.getSourceAsString());
        });
        Aggregations aggregations = search.getAggregations();
        return aggregations;

    }









}
