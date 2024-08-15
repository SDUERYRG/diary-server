package com.yrg.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author yrg
 * @Date 2024/8/7 22:02
 * 商品信息表
 */
@Data
@TableName("item_table")
public class Item implements Serializable {   //一个类只有实现了Serializable接口，它的对象才能被序列化

    private static final long serialVersionUID = 1L;

    //映射数据库表中的主键，该字段与表中字段名不一样且不是驼峰式，则指定value
    @TableId(value = "itemId", type = IdType.ASSIGN_UUID)
    private String itemId;           //id
    private String itemName;          //名称
    private String picture;         //图片(地址)
    private Integer stock;          //库存
    private String description;     //描述
    private Integer sell;           //售出数量
    private float price;            //单价
    private float discount;         //优惠价
    private float score;            //评分

    @TableField(exist = false)
    private String orderNum;

    @TableField(exist = false)
    private boolean rxFlag;
    @TableField(exist = false)
    private boolean xpFlag;
    @TableField(exist = false)
    private boolean tjFlag;//推荐

    @TableField(exist = false)
    private String shoppingCartId;
    @TableField(exist = false)
    private List<Evaluate> evaluateList;

}
