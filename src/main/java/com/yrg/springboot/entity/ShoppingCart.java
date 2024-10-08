package com.yrg.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车表
 *
 * @Author yrg
 * @Date 2022/10/23 13:32
 */
@Data
@TableName("shoppingCart")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "cartId", type = IdType.ASSIGN_UUID)
    private String cartId;    //购物车id
    private String itemId;    //商品id
    private String userId;    //用户id
    private Integer quantity;    //加入的数量
    private Double price;    //商品价格

    @TableField(exist = false)
    private List<Item> itemList;
}
