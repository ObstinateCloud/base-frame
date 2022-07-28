package com.lenged.system.es.mapper;

/**
 * @title: SysUserDao
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/5/7 11:07
 */

import com.lenged.system.es.entity.SysUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 第一种方式，类似于JPA，编写一个ElasticsearchRepository
 *  第一个泛型为Bean的类型
 *  第二个泛型为Bean的主键类型
 */

@Repository
public interface SysUserDao extends ElasticsearchRepository<SysUser,String> {

    List<SysUser> findSysUsersByUsername(String username);

    List<SysUser> findByUsername(String username);

    List<SysUser> findByLevelGreaterThanEqual(int level);

    List<SysUser> findSysUsersByLevelGreaterThanEqual(int level);

}
