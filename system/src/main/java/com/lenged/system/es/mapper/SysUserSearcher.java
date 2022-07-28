package com.lenged.system.es.mapper;

import com.lenged.system.es.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: SysUserSearcher
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/5/8 21:46
 */

@Component
public class SysUserSearcher {

    @Autowired
    private ElasticsearchOperations eo;

    public List<SysUser> findByPassword(String password){
        Criteria criteria = new Criteria("password").is(password);
        PageRequest pageRequest = PageRequest.of(0,10);
        List<SysUser> search = search(criteria, pageRequest);
        return search;
    }

    private List<SysUser> search(Criteria criteria, PageRequest pageRequest){
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        if(pageRequest !=null){
            criteriaQuery.setPageable(pageRequest);
        }
        //执行搜索
        SearchHits<SysUser> hits = eo.search(criteriaQuery,SysUser.class);
        List<SysUser> collect = hits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        return collect;

    }
}
