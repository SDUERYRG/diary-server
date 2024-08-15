package com.yrg.springboot.service.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrg.springboot.dao.SortDao;
import com.yrg.springboot.entity.Sort;
import com.yrg.springboot.service.SortService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SortServiceImpl extends ServiceImpl<SortDao, Sort> implements SortService {

    @Resource
    private SortDao sortDao;

    /**
     * 鲜花表、鲜花分类表，两表查询
     *
     * @Author yrg
     * @Date 2022/10/14 14:20
     */
    @Override
    public Page<Sort> getAll(IPage<Sort> iPage, Sort Sort) {
        return sortDao.getAll(iPage, Sort);
    }


    /**
     * 查询还没有分类的鲜花id
     *
     * @Author yrg
     * @Date 2022/10/14 17:06
     */
    @Override
    public String[] notSortItemId() {
        return sortDao.notSortItemId();
    }

    /**
     * 可以分类的鲜花名称
     *
     * @Author yrg
     * @Date 2022/10/14 18:28
     */
    @Override
    public String[] canSortItemName(String[] idArr) {
        return sortDao.canSortItemName(idArr);
    }

    /**
     * 根据itemID删除分类
     *
     * @Author yrg
     * @Date 2023/3/27 17:26
     */
    @Override
    public boolean delSortByItemId(String itemId) {
        return sortDao.delSortByItemId(itemId);
    }

}
