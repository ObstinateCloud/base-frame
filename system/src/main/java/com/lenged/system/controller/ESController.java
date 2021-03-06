package com.lenged.system.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
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
@Api(tags = "es ????????????")
public class ESController {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserSearcher sysUserSearcher;

    @Autowired
    RestHighLevelClient restHighLevelClient;


    @ApiOperation(value = "??????ElasticsearchRepository ????????????????????????",position = 1)
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
                    .username("AI??????" + i)
                    .level(i)
                    .roles(list.subList(0, toIndex))
                    .build();
            sysUserDao.save(build);
        }
        System.out.printf("??????");
        return "success";
    }
    @ApiOperation(value = "??????ElasticsearchRepository ????????????????????????",position = 2)
    @GetMapping("findAll")
    public void testFindAll() {
        Iterable<SysUser> all = sysUserDao.findAll();
        all.forEach((sysUser) -> {
            System.out.println(sysUser.getId());
        });
    }

    @ApiOperation("??????ElasticsearchRepository ??????????????????")
    @GetMapping("testDel")
    public void testDel(String id) {
        sysUserDao.deleteById(id);
        System.out.println(sysUserDao.existsById(id));
    }
    @ApiOperation("??????ElasticsearchRepository ????????????????????????")
    @GetMapping("pageList")
    public void pageList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SysUser> all = sysUserDao.findAll(pageable);
        all.getContent().forEach(p -> {
            System.out.println(p.getId());
        });
    }

    @ApiOperation("??????ElasticsearchRepository ??????????????????")
    @GetMapping("findById")
    public JSONObject findById(String id) {
        JSONObject jsonObject = new JSONObject();
        Optional<SysUser> sysUser = sysUserDao.findById(id);
        jsonObject = JSONObject.parseObject(JSON.toJSONString(sysUser));

        return jsonObject;
    }

    @ApiOperation("??????ElasticsearchRepository ?????????jpa????????????")
    @GetMapping("findByUsername")
    public JSONArray findByUsername(@ApiParam(value = "?????????", required =true)String userName) {
        List<SysUser> sysUsers = sysUserDao.findSysUsersByUsername(userName);//????????????
//        List<SysUser> sysUsers = sysUserDao.findByUsername(userName);//????????????
        JSONArray objects = JSON.parseArray(JSON.toJSONString(sysUsers));
        return objects;
    }

    @ApiOperation("??????ElasticsearchRepository ?????????jpa????????????")
    @GetMapping("findByLevel")
    public JSONArray findByLevel(@ApiParam(value = "??????", required =true)int level) {
        List<SysUser> sysUsers = sysUserDao.findSysUsersByLevelGreaterThanEqual(level);
//        List<SysUser> sysUsers = sysUserDao.findByLevelGreaterThanEqual(level);
        JSONArray objects = JSON.parseArray(JSON.toJSONString(sysUsers));
        return objects;
    }

    @ApiOperation("Criteria ????????????")
    @GetMapping(value = "findByPass")
    public JSONArray findByPass(@ApiParam(value = "??????", required =true)String password) {
        List<SysUser> sysUsers = sysUserSearcher.findByPassword(password);
        JSONArray objects = JSON.parseArray(JSON.toJSONString(sysUsers));
        return objects;
    }
    @ApiOperation("RestHighLevelClient ????????????????????????")
    @GetMapping(value = "createIndex")
    public CreateIndexResponse createIndex(String index) {
        //??????????????????
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        //client ????????????
        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createIndexResponse;
    }
    @ApiOperation("RestHighLevelClient ????????????")
    @GetMapping(value = "createMapping")
    public CreateIndexResponse createMapping(@ApiParam(value = "??????", required =true)String index) {
        //??????????????????
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        //client ????????????
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
//            createIndexRequest.mapping(mapping, XContentType.JSON);
            createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createIndexResponse;
    }

    @ApiOperation("RestHighLevelClient ????????????????????????")
    @GetMapping(value = "indexExists")
    public boolean indexExists(@ApiParam(value = "??????", required =true)String index) {
        //??????????????????
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        //client ????????????
        boolean exists = false;
        try {
            exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return exists;
    }
    @ApiOperation("RestHighLevelClient ????????????")
    @GetMapping(value = "delIndex")
    public AcknowledgedResponse delIndex(@ApiParam(value = "??????", required =true)String index) {
        //??????????????????
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        //client ????????????
        AcknowledgedResponse delete = null;
        try {
            delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return delete;
    }

    @GetMapping(value = "createDoc")
    @ApiOperation("RestHighLevelClient ????????????")
    public IndexResponse createDoc(String index) {
        List<String> list = new ArrayList<>();
        list.add("teacher");
        list.add("student");
        list.add("admin");
        list.add("leader");
        //1.????????????
        SysUser build = SysUser.builder()
                .password("zjynb")
                .username("lengedyun")
                .level(5)
                .roles(list)
                .build();
        //2 ????????????
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id("1");
        indexRequest.timeout(TimeValue.timeValueSeconds(5));
        indexRequest.source(JSON.toJSONString(build), XContentType.JSON);
        //????????????
        IndexResponse indexResponse = null;
        try {
            indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return indexResponse;
    }

    @GetMapping(value = "updateDocFull")
    @ApiOperation("RestHighLevelClient ????????????-????????????")
    public IndexResponse updateDocFull(String index) {
        //1.????????????
        SysUser build = SysUser.builder()
                .username("lengedyun??????update")
                .level(8)
                .build();
        //2 ????????????
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id("2");
        indexRequest.source(JSON.toJSONString(build), XContentType.JSON);
        //????????????
        IndexResponse indexResponse = null;
        try {
            indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return indexResponse;
    }

    @GetMapping(value = "updateDocPart")
    @ApiOperation("RestHighLevelClient ????????????-????????????")
    public UpdateResponse updateDocPart(String index) {
        //1.????????????
        SysUser build = SysUser.builder()
                .username("lengedyun??????update")
                .level(8)
                .build();
        //2 ????????????
        UpdateRequest updateRequest = new UpdateRequest(index,"1");
        updateRequest.doc(JSON.toJSONString(build),XContentType.JSON);
        //????????????
        UpdateResponse updateResponse = null;
        try {
            updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return updateResponse;
    }

    @GetMapping(value = "deleteDoc")
    @ApiOperation("RestHighLevelClient ????????????")
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
    @ApiOperation("RestHighLevelClient ??????????????????")
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
    @ApiOperation("RestHighLevelClient ??????????????????")
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
                    .username("?????????" + i+"xx")
                    .level(i)
                    .roles(list.subList(0, i%3+1))
                    .build();
            IndexRequest indexRequest = new IndexRequest(index);
            indexRequest.source(JSON.toJSONString(build),XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        //????????????????????????
        BulkResponse bulk = null;
        try {
            bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bulk;
    }

    @GetMapping(value = "getDocList")
    @ApiOperation("RestHighLevelClient ?????????????????????")
    public SearchHits getDocList(String index, SysUser sysUser) {
        SearchRequest searchRequest =new SearchRequest(index);
        //????????????builder
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //????????????
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("username",sysUser.getUsername());
        //????????????
        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("username",sysUser.getUsername());
        //???????????? ??? term ????????????????????????????????? ?????????
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("username", sysUser.getUsername());


        builder.query(matchPhraseQueryBuilder);
        //??????????????????
        builder.from(0);
        builder.size(14);
        //??????
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
    @ApiOperation("RestHighLevelClient ????????????")
    public SearchHits filterDocList(String index) {
        SearchRequest searchRequest =new SearchRequest(index);
        //????????????builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //????????????
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(matchAllQueryBuilder);

        String[] excludeds = {};
        String[] includes = {"username"};
        //??????????????????
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
    @ApiOperation("RestHighLevelClient ?????????????????????")
    public SearchHits boolQueryDocList(String index, SysUser sysUser) {
        SearchRequest searchRequest =new SearchRequest(index);
        //????????????builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //???????????????builder
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //must ????????????
//        boolQueryBuilder.must(QueryBuilders.termQuery("username",sysUser.getUsername()));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("username",sysUser.getUsername()));
        // must not ???????????????
        //should ?????? ?????????mysql ???or
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
    @ApiOperation("RestHighLevelClient ????????????")
    public SearchHits rangeQueryDocList(String index, SysUser sysUser) {
        SearchRequest searchRequest =new SearchRequest(index);
        //????????????builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //????????????builder
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
    @ApiOperation("RestHighLevelClient ????????????")
    public SearchHits fuzzyQueryDocList(String index, SysUser sysUser) {
        SearchRequest searchRequest =new SearchRequest(index);
        //????????????builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //????????????builder  ??????????????????
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
    @ApiOperation("RestHighLevelClient ????????????")
    public SearchHits highLightDocList(String index, SysUser sysUser) {
        SearchRequest searchRequest =new SearchRequest(index);
        //????????????builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //????????????builder
        HighlightBuilder highlightBuilder = new HighlightBuilder();

        //??????????????????
        highlightBuilder.preTags("<font color='red'>");//??????
        highlightBuilder.postTags("</font>");//??????
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
    @ApiOperation("RestHighLevelClient ???????????????????????????????????????")
    public Aggregations aggregationDoc(String index, String field) {
        SearchRequest searchRequest =new SearchRequest(index);
        //????????????builder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //???????????????builder
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
    @ApiOperation("RestHighLevelClient ????????????????????????")
    public Aggregations aggregationGroupDoc(String index, String field) {
        SearchRequest searchRequest =new SearchRequest(index);
        //????????????builder
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
