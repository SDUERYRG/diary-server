package com.yrg.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yrg.springboot.entity.Sort;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SortDao extends BaseMapper<Sort> {

    /**
     * 鲜花表、鲜花分类表，两表查询
     *
     * @Author yrg
     * @Date 2022/10/14 14:21
     */
    Page<Sort> getAll(IPage<Sort> iPage, Sort sort);

    /**
     * 查询还没有分类的鲜花id
     *
     * @Author yrg
     * @Date 2022/10/14 17:05
     */
    String[] notSortItemId();

    /**
     * 可以分类的鲜花名称
     *
     * @Author yrg
     * @Date 2022/10/14 18:27
     */
    String[] canSortItemName(String[] integers);

    /**
     * 删除(removeById用不了？)
     *
     * @Author yrg
     * @Date 2022/10/14 21:05
     */
    @Delete("delete from item_sort where sortId = #{sortId}")
    boolean delSortById(Integer sortId);

    @Delete("delete from item_sort where itemId = #{itemId}")
    boolean delSortByItemId(String itemId);


}
