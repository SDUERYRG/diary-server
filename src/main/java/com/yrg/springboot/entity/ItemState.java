package com.yrg.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@TableName("item_state")
@Data
public class ItemState implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "stateId")
    private Integer stateId;    //状态id
    private Integer tuiJian;    //是否推荐
    private Integer xinPin;     //是否为新品
    private Integer reXiao;     //是否为热销
    private String itemId;   //鲜花ID

    @TableField(exist = false)
    private List<Item> itemList;
}
