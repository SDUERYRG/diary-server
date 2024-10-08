package com.yrg.springboot.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrg.springboot.entity.OrderDetail;
import com.yrg.springboot.entity.OrderHistory;
import com.yrg.springboot.entity.SalesStatistics;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import com.yrg.springboot.entity.Order;
import java.util.Date;
import java.util.List;

/**
 * @Author yrg
 * @Date 2024/8/13 16:31
 * 订单管理相关操作
 */

@Repository
@Mapper
public interface OrderDao extends BaseMapper<Order> {

    /**
     * 发货
     *
     * @Author yrg
     * @Date 2022/10/7 14:38
     */
    @Update("update order_table set state = '已发货',deliver_time = #{date} where orderId = #{orderId}")
    boolean deliverGoods(String orderId, Date date);

    /**
     * 获取订单详情及历史信息
     *
     * @Author yrg
     * @Date 2022/10/15 15:57
     */
    @Select("select * from order_history where orderId = #{orderId}")
    OrderHistory getOrderHistory(String orderId);

    @Select("select * from order_detail where orderNum = #{orderNum}")
    List<OrderDetail> getOrderDetail(String orderNum);

    /**
     * 删除订单详情表中的信息
     *
     * @Author yrg
     * @Date 2022/10/22 12:29
     */
    @Delete("delete from order_detail where orderNum = #{orderNum}")
    boolean deleteOrderDetailByOrderNum(String orderNum);

    /**
     * 删除订单历史信息表中的信息
     *
     * @Author yrg
     * @Date 2022/10/22 12:29
     */
    @Delete("delete from order_history where orderId = #{orderId}")
    boolean deleteOrderHistoryByOrderId(String orderId);

    /**
     * 用户订单签收
     *
     * @Author yrg
     * @Date 2022/10/22 22:14
     */
    @Update("update order_table set receiptTime = #{receiptTime},state = '已签收' where orderId = #{orderId}")
    Integer orderReceipt(Date receiptTime, String orderId);

    /**
     * 保存数据到订单详情表
     *
     * @Author yrg
     * @Date 2022/10/23 21:14
     */
    @Insert("insert into order_detail(detailId,orderNum,itemId,unitPrice,quantity) values(#{detailId},#{orderNum},#{itemId},#{unitPrice},#{quantity})")
    Integer saveOrderDetail(OrderDetail orderDetail);

    /**
     * 保存数据到订单历史信息表
     *
     * @Author yrg
     * @Date 2022/10/23 21:18
     */
    @Insert("insert into order_history(orderId,userName,tel,address,beiZhu) values(#{orderId},#{userName},#{tel},#{address},#{beiZhu})")
    Integer saveOrderHistory(OrderHistory orderHistory);

    /**
     * 鲜花销售统计(月份和每月销售金额)
     *
     * @Author yrg
     * @Date 2022/11/1 15:24
     */
    @Select("select MONTH(orderTime) as month,sum(price) as price from order_table where YEAR(orderTime) = #{year} GROUP BY MONTH(orderTime)")
    List<SalesStatistics> itemSalesStatistics(String year);

    /**
     * 根据订单id获取订单信息
     *
     * @Author yrg
     * @Date 2022/11/1 15:35
     */
    @Select("select * from order_table where orderId = #{orderId}")
    Order getOrderById(String orderId);
}
