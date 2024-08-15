package com.yrg.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yrg.springboot.dto.CartItem;
import com.yrg.springboot.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ShoppingCartDao extends BaseMapper<ShoppingCart> {

    /**
     * 查看当前用户的购物车中有哪些东西
     *
     * @Author yrg
     * @Date 2022/10/23 13:43
     */
    @Select("SELECT sc.cartId, sc.itemId, i.item_name, sc.userId, u.user_name,sc.quantity " +
            "FROM shoppingCart sc " +
            "JOIN item_table i ON sc.itemId = i.itemId " +
            "JOIN user_table u ON sc.userId = u.userId " +
            "WHERE sc.userId = #{userId}")
    Page<CartItem> getPage(IPage<CartItem> iPage, @Param("userId") String userId);

    /**
     * 查看所有购物车
     *
     * @Author yrg
     * @Date 2022/10/23 13:43
     */
    @Select("SELECT sc.cartId, sc.itemId, i.item_name, sc.userId, u.user_name,sc.quantity " +
            "FROM shoppingCart sc " +
            "JOIN item_table i ON sc.itemId = i.itemId " +
            "JOIN user_table u ON sc.userId = u.userId ")
    Page<CartItem> getAllPage(IPage<CartItem> iPage);

    /**
     * 判断当前用户购物车中是否已有该鲜花
     *
     * @Author yrg
     * @Date 2022/10/23 14:20
     */
    @Select("SELECT * from shoppingCart where itemId = #{itemId} and userId = #{userId}")
    ShoppingCart judgeExistence(String itemId, String userId);

    /**
     * 移除购物车鲜花
     *
     * @Author yrg
     * @Date 2023/4/10 15:22
     */
    @Delete("delete from shoppingcart where cartId = #{cartId}")
    boolean removeById(String cartId);

    /**
     * 查询对应商品的加购信息
     *
     * @Author yrg
     * @Date 2024/8/15 17:14
     */
    @Select("SELECT sc.cartId, sc.itemId, i.item_name, sc.userId, u.user_name,sc.quantity " +
            "FROM shoppingCart sc " +
            "JOIN item_table i ON sc.itemId = i.itemId " +
            "JOIN user_table u ON sc.userId = u.userId " +
            "WHERE sc.itemId = #{itemId}")
    Page<CartItem> getCartItems(IPage<CartItem> iPage, String itemId);

    /**
     * 删除购物车中的商品
     *
     * @Author yrg
     * @Date 2024/8/15 17:48
     */
    @Delete("delete from shoppingcart where itemId = #{itemId} and userId = #{userId}")
    boolean deleteItem(String itemId, String userId);
}
