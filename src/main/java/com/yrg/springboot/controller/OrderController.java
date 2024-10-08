package com.yrg.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrg.springboot.dto.OrderItem;
import com.yrg.springboot.entity.Order;
import com.yrg.springboot.entity.OrderDetail;
import com.yrg.springboot.entity.OrderHistory;
import com.yrg.springboot.entity.SalesStatistics;
import com.yrg.springboot.service.ItemService;
import com.yrg.springboot.service.OrderService;
import com.yrg.springboot.utils.Result;
import com.yrg.springboot.utils.ResultCode;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author yrg
 * @Date 2024/8/13 17:41
 * 订单管理相关操作
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private ItemService itemService;

    /**
     * 查询出所有订单数据
     * @Author yrg
     * @Date 20248/13 17:41
     */
    @GetMapping("/{current}/{pageSize}")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result getAll(@PathVariable int current, @PathVariable int pageSize, Order order) {
        IPage<Order> allOrder = orderService.getPage(current, pageSize, order);
        return new Result(true, allOrder);
    }

    /**
     * 查询出对应用户的订单数据
     * @Author yrg
     * @Date 20248/13 17:41
     */
    @GetMapping("/userOrder/{userId}/{current}/{pageSize}")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result getUserOrder(@PathVariable String userId, @PathVariable int current, @PathVariable int pageSize) {
        IPage<Order> allOrder = orderService.getUserOrder(userId, current, pageSize);
        return new Result(true, allOrder);
    }




    /**
     * 添加订单
     * @Author yrg
     * @Date 2024/8/14 14:41
     */
//    @GetMapping("/addOrder")
//    @Transactional
//    public Result addOrder(@RequestBody JSONObject jsonObject) {
//
//    }


    /**
     * 查看详情操作,订单和商品信息,一个订单对应多个商品
     * @Author yrg
     * @Date 20248/13 17:41
     */
    @GetMapping("/show/{orderId}/{orderNum}")
    @Transactional
    public Result editShow(@PathVariable String orderId,@PathVariable String orderNum) {
        List<OrderDetail> orderDetail = orderService.getOrderDetail(orderNum);
        OrderHistory orderHistory = orderService.getOrderHistory(orderId);
        Order order = orderService.getOrderById(orderId);

        order.setDetailList(orderDetail);
        order.setOrderHistory(orderHistory);
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderNum(order.getOrderNum());
        orderItem.setOrderTime(order.getOrderTime());

        List<String> itemId = new ArrayList<>();
        List<String> itemName = new ArrayList<>();
        for (OrderDetail detail : orderDetail) {
            itemId.add(detail.getItemId());
            itemName.add(itemService.getItemNameById(detail.getItemId()));
        }
        orderItem.setItemId(itemId);
        orderItem.setItemName(itemName);
        orderItem.setUserId(order.getUserId());
        orderItem.setUserName(orderHistory.getUserName());
        orderItem.setAddress(orderHistory.getAddress());
        orderItem.setPrice(order.getPrice());
        return Result.success(orderItem);
    }

    /**
     * 发货操作
     * @Author yrg
     * @Date 2024/8/13 17:42
     */
    @GetMapping("/deliverGoods/{orderId}")
    @Transactional
    public Result deliverGoods(@PathVariable String orderId) {
        Date date = new Date();
        if (orderService.deliverGoods(orderId, date)) {
            return Result.success(ResultCode.SUCCESS.code(), "发货成功(●'◡'●)");
        } else {
            return Result.error(ResultCode.ERROR.code(), "发货失败(＞﹏＜)");
        }
    }

    /**
     * 删除订单信息
     * @Author yrg
     * @Date 2024/8/13 17:43
     */
    @DeleteMapping("/{orderId}/{orderNum}")
    @Transactional
    public Result delOrderById(@PathVariable String orderId, @PathVariable String orderNum) {
        if (orderService.removeById(orderId)) {
            orderService.deleteOrderDetailByOrderNum(orderNum);
            orderService.deleteOrderHistoryByOrderId(orderId);
            return Result.success(ResultCode.SUCCESS.code(), "删除成功(●'◡'●)");
        } else
            return Result.error(ResultCode.ERROR.code(), "删除失败(＞﹏＜)");
    }

    /**
     * 鲜花销售金额统计数据
     *
     * @Author yrg
     * @Date 2024/8/13 17:43
     */
    @GetMapping("/itemSalesStatistics")
    @ResponseBody
    public Result itemSalesStatistics(String year) {
        List<SalesStatistics> salesStatistics = orderService.itemSalesStatistics(year);
        Integer[] month = new Integer[12];
        Float[] price = new Float[12];
        for (int i = 0; i < 12; i++) {
            month[i] = i + 1;
            price[i] = Float.valueOf(0);
            for (SalesStatistics salesStatistic : salesStatistics) {
                if (salesStatistic.getMonth() == month[i]) {
                    price[i] = salesStatistic.getPrice();
                    break;
                }
            }
        }
        return new Result(true, price);
    }
}
