package com.by.dao;

import com.by.entity.TPhone;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (TPhone)表数据库访问层
 *
 * @author makejava
 * @since 2019-11-22 15:30:24
 */
@Mapper
public interface TPhoneDao {

    /**
     * 通过ID查询单条数据
     *
     * @param phoneId 主键
     * @return 实例对象
     */
    TPhone queryById(Integer phoneId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<TPhone> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tPhone 实例对象
     * @return 对象列表
     */
    List<TPhone> queryAll(TPhone tPhone);

    /**
     * 新增数据
     *
     * @param tPhone 实例对象
     * @return 影响行数
     */
    int insert(TPhone tPhone);


    void delete(Integer id);

    List<TPhone> find();
}