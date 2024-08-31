package com.yrg.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrg.springboot.entity.Item;
import com.yrg.springboot.entity.Sort;

import java.util.List;

public interface SortService extends IService<Sort> {

    /**
     * 鲜花表、鲜花分类表，两表查询
     *
     * @Author yrg
     * @Date 2022/10/14 14:20
     */
    Page<Sort> getAll(IPage<Sort> iPage, Sort Sort);

    /**
     * 查询还没有分类的鲜花id
     *
     * @Author yrg
     * @Date 2022/10/14 17:06
     */
    String[] notSortItemId();

    /**
     * 可以分类的鲜花名称
     *
     * @Author yrg
     * @Date 2022/10/14 18:28
     */
    String[] canSortItemName(String[] integers);

    /**
     * 根据itemID删除分类
     *
     * @Author yrg
     * @Date 2023/3/27 17:26
     */
    boolean delSortByItemId(String itemId);

    /**
     * 根据sortId查询Item
     *
     * @Author yrg
     * @Date 2024/8/31 19:51
     */
    List<Item> selectItemBySortId(Integer sortId);
}
