package com.yrg.springboot.service.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yrg.springboot.dao.ShoppingCartDao;
import com.yrg.springboot.dto.CartItem;
import com.yrg.springboot.entity.*;
import com.yrg.springboot.service.ItemService;
import com.yrg.springboot.service.ShoppingCartService;
import com.yrg.springboot.service.UserOrderService;
import com.yrg.springboot.utils.MyException;
import com.yrg.springboot.utils.ResultCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements ShoppingCartService {

    @Resource
    private ShoppingCartDao shoppingCartDao;
    @Resource
    private UserOrderService orderService;
    @Resource
    private ItemService itemService;

    @Override
    public Page<CartItem> getPage(IPage<CartItem> iPage, String userId) {
        System.out.println("调用serimp.getPage");
        return shoppingCartDao.getPage(iPage, userId);
    }


    @Override
    public Page<CartItem> getPage(IPage<CartItem> iPage) {
        return shoppingCartDao.getAllPage(iPage);
    }

    @Override
    public boolean pay(List<Item> itemList, Address address, Float price) {
        Order order = new Order();
//        String orderNum = UUID.randomUUID().toString();
        String orderNum = String.valueOf(System.currentTimeMillis());
        order.setOrderNum(orderNum);
        order.setOrderTime(new Date());
        order.setState("未发货");
        order.setPrice(price);
        order.setUserId(address.getUserId());
        if (orderService.save(order)) {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrderId(order.getOrderId());
            orderHistory.setUserName(address.getUserName());
            orderHistory.setTel(address.getTel());
            orderHistory.setAddress(address.getAddress());
            orderService.saveOrderHistory(orderHistory);
            for (Item item : itemList) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setDetailId(UUID.randomUUID().toString());
                orderDetail.setOrderNum(orderNum);
                orderDetail.setUnitPrice(item.getDiscount() == 0 ? item.getPrice() : item.getDiscount());
                orderDetail.setQuantity(item.getSell());
                orderDetail.setItemId(item.getItemId());
                orderService.saveOrderDetail(orderDetail);
                shoppingCartDao.removeById(item.getShoppingCartId());
                //更新鲜花售出数
                itemService.updateItemSell(item.getItemId(), item.getSell());
                //更新鲜花库存数量
                itemService.updateItemStock(item.getItemId(), item.getSell());
            }
        } else {
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new MyException(ResultCode.ADD_ORDER_ERROR.code(), ResultCode.ADD_ORDER_ERROR.message());
        }
        return true;
    }

    @Override
    public boolean judgeExistence(String itemId, String userId) {
        return shoppingCartDao.judgeExistence(itemId, userId) != null;
    }

    @Override
    public Page<CartItem> getCartItems(IPage<CartItem> iPage, String itemId) {
        return shoppingCartDao.getCartItems(iPage, itemId);
    }

    @Override
    public boolean deleteItem(String itemId, String userId) {
        return shoppingCartDao.deleteItem(itemId, userId);
    }
}
