package com.yrg.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrg.springboot.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ItemDao extends BaseMapper<Item> {

    /**
     * @Author yrg
     * @Date 2024/8/7 22:13
     * 一对一查询，根据订单表的商品id查询商品数据，一个订单对应一个商品
     */
    @Select("select * from item_table where itemId=#{itemId}")
    List<Item> selectByItemId(Integer itemId);

    /**
     * 根据商品名查询商品id,查询出可以分类的商品 判断商品是否重复
     *
     * @Author yrg
     * @Date 2024/8/7 22:13
     */
    @Select("select itemId from item_table where item_name = #{itemName}")

    String selectByItemName(String itemName);


    /**
     * 按用途分类获取鲜花id
     *
     * @Author yrg
     * @Date 2024/8/7 22:15
     */
    List<Integer> getSortIds(String sortType, String sortName);

    /**
     * 更改商品售出的数量
     *
     * @Author yrg
     * @Date 2024/8/7 22:15
     */
    @Update("update item_table set sell = sell + #{quantity} where itemId = #{itemId}")
    boolean updateItemSell(String itemId, Integer quantity);

    /**
     * 更新商品的评分
     *
     * @Author yrg
     * @Date 2024/8/7 22:16
     */
    @Update("update item_table set score = #{score} where itemId = #{itemId}")
    boolean updateItemScore(String itemId, Float score);

    /**
     * 更新库存数量
     *
     * @Author yrg
     * @Date 2024/8/7 22:17
     */
    @Update("update item_table set stock = stock - #{quantity} where itemId = #{itemId}")
    boolean updateItemStock(String itemId, Integer quantity);

}
