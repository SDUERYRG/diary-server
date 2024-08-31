package com.yrg.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品分类表
 *
 * @Author yrg
 * @Date 2024/8/14 14:01
 */
@Data
@TableName("item_sort")    //指定数据库表名
public class Sort implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sortId")
    private Integer sortId;         //id
    private String itemId;       //物品id
    private String sortName;       //用途

    @TableField(exist = false)
    private String[] purposeArray;
    @TableField(exist = false)
    private String itemName;
}
