package com.yrg.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单详情表
 *
 * @Author yrg
 * @Date 2024/8/13 16:30
 */

@Data
@TableName("order_detail")
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private String detailId;       //订单详情id
    private String orderNum;        //订单号
    private String itemId;       //商品id
    private float unitPrice;        //商品单价
    private Integer quantity;       //购买数量

    @TableField(exist = false)
    private Item item;

    @TableField(exist = false)
    private boolean evaluated;
    @TableField(exist = false)
    private String userId;
    @TableField(exist = false)
    private String itemName;
    @TableField(exist = false)
    private String picture;

}
