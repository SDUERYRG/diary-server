package com.yrg.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrg.springboot.entity.Order;
import com.yrg.springboot.entity.OrderDetail;
import com.yrg.springboot.entity.OrderHistory;


/**
 * 用户订单相关操作
 *
 * @Author yrg
 * @Date 2023/4/3 11:48
 */
public interface UserOrderService extends IService<Order> {

    /**
     * 分页获取
     *
     * @Author yrg
     * @Date 2022/10/22 22:15
     */
    IPage<Order> getPage(int current, int pageSize, String userId);   //方法重载

    /**
     * 用户订单签收
     *
     * @Author yrg
     * @Date 2022/10/22 22:18
     */
    Integer orderReceipt(String orderId);

    /**
     * 保存数据到订单详情表
     *
     * @Author yrg
     * @Date 2022/10/23 21:17
     */
    Integer saveOrderDetail(OrderDetail orderDetail);

    /**
     * 保存数据到订单历史信息表
     *
     * @Author yrg
     * @Date 2022/10/23 21:19
     */
    Integer saveOrderHistory(OrderHistory orderHistory);
}
