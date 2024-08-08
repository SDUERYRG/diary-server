package com.yrg.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrg.springboot.entity.Evaluate;

public interface EvaluateService extends IService<Evaluate> {
    /**
     * 多表查询，根据评价表里的用户id和鲜花id查出用户和鲜花信息
     *
     * @Author liuzhi
     * @Date 2022/10/7 20:31
     */
    Page<Evaluate> getAll(IPage<Evaluate> iPage, Evaluate evaluate);

    /**
     * 判断是否已经登录
     *
     * @Author liuzhi
     * @Date 2022/10/22 17:49
     */
    Integer isEvaluated(String itemId, String userId, String orderNum);

    /**
     * 查看用户该订单的该鲜花的评价信息
     *
     * @Author liuzhi
     * @Date 2022/10/27 8:43
     */
    Evaluate lookEvaluate(String itemId, String userId, String orderNum);

    /**
     * 获取鲜花的平均分
     *
     * @Author liuzhi
     * @Date 2022/10/28 12:54
     */
    Float getItemScoreAvg(String itemId);
}
