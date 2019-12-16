package com.by.service;

import com.by.entity.TPhone;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;

/**
 * (TPhone)表服务接口
 *
 * @author makejava
 * @since 2019-11-22 15:30:24
 */
public interface TPhoneService {

    /**
     * 通过ID查询单条数据
     *
     * @param phoneId 主键
     * @return 实例对象
     */

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<TPhone> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tPhone 实例对象
     * @return 实例对象
     */
    TPhone insert(TPhone tPhone);

    /**
     * 修改数据
     *
     * @param tPhone 实例对象
     * @return 实例对象
     */

    /**
     * 通过主键删除数据
     *
     * @param
     * @param i
     * @param limit
     * @return 是否成功
     */

    List<TPhone> sl(String mmm, int i, Integer limit) throws IOException, SolrServerException;

    void delete(Integer id);

    List<TPhone> find();
}