package com.yrg.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yrg.springboot.entity.Evaluate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author yrg
 * @Date 2024/8/7 22:17
 * 评价信息管理相关操作
 */
@Repository
@Mapper
public interface EvaluateDao extends BaseMapper<Evaluate> {
    /**
     * @Author yrg
     * @Date 2024/8/7 22:20
     * 多表连接查询
     */
    Page<Evaluate> getAll(IPage<Evaluate> iPage, Evaluate evaluate);

    /**
     * 判断用户是否已经评价了该订单的该商品
     *
     * @Author yrg
     * @Date 2024/8/7 22:20
     */
    @Select("SELECT count(*) FROM evaluate where itemId = #{itemId} and userId = #{userId} and orderNum = #{orderNum}")
    Integer isEvaluated(String itemId, String userId, String orderNum);

    /**
     * 查看用户该订单的该商品的评价信息
     *
     * @Author yrg
     * @Date 2024/8/7 22:20
     */
    @Select("SELECT * FROM evaluate where itemId = #{itemId} and userId = #{userId} and orderNum = #{orderNum}")
    Evaluate lookEvaluate(String itemId, String userId, String orderNum);

    /**
     * 根据商品ID获取商品的平均分
     *
     * @Author yrg
     * @Date 2024/8/7 22:20
     */
    @Select("select avg(score) from evaluate where itemId = #{itemId}")
    Float getItemScoreAvg(String itemId);
}