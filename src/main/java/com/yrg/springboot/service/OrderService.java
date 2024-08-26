package com.yrg.springboot.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrg.springboot.entity.OrderDetail;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yrg.springboot.entity.Order;
import com.yrg.springboot.entity.OrderHistory;
import com.yrg.springboot.entity.SalesStatistics;

import java.util.Date;
import java.util.List;

public interface OrderService extends IService<Order> {
    /**
     * 分页查询
     *
     * @Author yrg
     * @Date 2022/10/5 17:32
     */
    IPage<Order> getPage(int current, int pageSize);

    IPage<Order> getPage(int current, int pageSize, Order order);   //方法重载

    /**
     * 查询出对应用户的订单数据
     *
     * @Author yrg
     * @Date 2022/10/5 17:32
     */
    IPage<Order> getUserOrder(String userId, int current, int pageSize);

    /**
     * 获取订单历史信息
     *
     * @Author yrg
     * @Date 2022/10/15 15:58
     */
    OrderHistory getOrderHistory(String orderId);

    /**
     * 获取订单详情
     *
     * @Author yrg
     * @Date 2023/4/3 11:46
     */
    List<OrderDetail> getOrderDetail(String orderNum);

    /**
     * 发货
     *
     * @Author yrg
     * @Date 2022/10/7 14:38
     */
    boolean deliverGoods(String orderId, Date date);

    /**
     * 删除订单及关联表信息
     *
     * @Author yrg
     * @Date 2022/10/22 12:29
     */
    boolean deleteOrderDetailByOrderNum(String orderNum);

    boolean deleteOrderHistoryByOrderId(String orderId);

    /**
     * 鲜花销售统计(月份和每月销售金额)
     *
     * @Author yrg
     * @Date 2022/11/1 15:35
     */
    List<SalesStatistics> itemSalesStatistics(String year);

    /**
     * 根据订单id获取订单信息
     *
     * @Author yrg
     * @Date 2022/11/1 15:35
     */
    Order getOrderById(String orderId);

}
