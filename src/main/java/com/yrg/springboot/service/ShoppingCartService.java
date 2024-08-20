package com.yrg.springboot.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrg.springboot.dto.CartItem;
import com.yrg.springboot.entity.Address;
import com.yrg.springboot.entity.Item;
import com.yrg.springboot.entity.ShoppingCart;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 查看当前用户的购物车中有哪些东西
     *
     * @Author yrg
     * @Date 2022/10/23 13:43
     */
    Page<CartItem> getPage(IPage<CartItem> iPage, String userId);

    /**
     * 查询对应商品的加购信息
     *
     * @Author yrg
     * @Date 2024/8/15 17:13
     */
    Page<CartItem> getCartItems(IPage<CartItem> iPage, String itemId);

    /**
     * 查看所有购物车
     *
     * @Author yrg
     * @Date 2022/10/23 13:43
     */
    Page<CartItem> getPage(IPage<CartItem> iPage);

    /**
     * 购买
     *
     * @Author yrg
     * @Date 2023/4/10 13:42
     */
    boolean pay(List<Item> itemList, Address address, Float price);

    /**
     * 判断当前用户购物车中是否已有该鲜花
     *
     * @Author yrg
     * @Date 2022/10/23 14:23
     */
    boolean judgeExistence(String itemId, String userId);

    /**
     * 删除购物车中的商品
     *
     * @Author yrg
     * @Date 2024/8/15 17:47
     */
    boolean deleteItem(String itemId, String userId);
    /**
     * 将商品加入购物车
     *
     * @Author yrg
     * @Date 2024/8/19 20:22
     */

    boolean addItem(String itemId, String userId,String cartId,double price);
    /**
     * 根据userId获取购物车Id
     *
     * @Author yrg
     * @Date 2024/8/19 20:22
     */
    String getCartId(String userId);
}
