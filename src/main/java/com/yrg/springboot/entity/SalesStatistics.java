package com.yrg.springboot.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 鲜花销售统计(月份和每月销售金额)
 *
 * @Author yrg
 * @Date 2024/8/13 16:33
 */
@Data
public class SalesStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer month;
    private Float price;
}
