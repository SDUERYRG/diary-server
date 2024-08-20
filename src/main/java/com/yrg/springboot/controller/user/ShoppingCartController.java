package com.yrg.springboot.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.yrg.springboot.dto.CartItem;
import com.yrg.springboot.entity.Address;
import com.yrg.springboot.entity.Item;
import com.yrg.springboot.entity.ShoppingCart;
import com.yrg.springboot.service.ItemService;
import com.yrg.springboot.service.ShoppingCartService;
import com.yrg.springboot.service.UserService;
import com.yrg.springboot.utils.Result;
import com.yrg.springboot.utils.ResultCode;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车相关操作
 *
 * @Author yrg
 * @Date 2022/10/23 13:35
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;
    @Resource
    private UserService userService;
    @Resource
    private ItemService itemService;


    /**
     * 获取所有数据
     *
     * @Author yrg
     * @Date 2023/4/5 13:48
     */
    @GetMapping("/{current}/{pageSize}/{userId}")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result getItemsByUserId(@PathVariable Integer current, @PathVariable Integer pageSize, @PathVariable String userId) {
        try {
            IPage<CartItem> page = shoppingCartService.getPage(new Page<>(current, pageSize), userId);

            return new Result(true, page);
        } catch (Exception e) {
            System.out.println(e);
            return new Result(false, "Error fetching items");
        }
    }

    /**
     * 搜索指定用户的购物车
     *
     * @Author yrg
     * @Date 2024/8/14 17:05
     */
    @GetMapping("/searchByUser/{current}/{pageSize}")
    @ResponseBody
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result searchByUser(@PathVariable int current, @PathVariable int pageSize, @RequestParam String userName) {
        String searchUserId = userService.getUserIdByUserName(userName);
        System.out.println(searchUserId);
        if (searchUserId == null) {
            return new Result(false, "未找到该用户");
        } else {
            IPage<CartItem> page = shoppingCartService.getPage(new Page<>(current, pageSize), searchUserId);
            return new Result(true, page);
        }
    }

    /**
     * 搜索指定商品的购物车
     *
     * @Author yrg
     * @Date 2024/8/14 17:05
     */
    @GetMapping("/searchByItem/{current}/{pageSize}")
    @ResponseBody
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result searchByItem(@PathVariable int current, @PathVariable int pageSize, @RequestParam String itemName) {
        String searchItemId = itemService.selectByItemName(itemName);
        System.out.println(searchItemId);
        if (searchItemId == null) {
            return new Result(false, "未找到该商品的加购信息");
        } else {
            IPage<CartItem> page = shoppingCartService.getCartItems(new Page<>(current, pageSize), searchItemId);
            System.out.println(page);
            return new Result(true, page);
        }
    }

    /**
     * 获取所有数据
     *
     * @Author yrg
     * @Date 2024/8/14 15:12
     */
    @GetMapping("/{current}/{pageSize}")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result getAllCart(@PathVariable Integer current, @PathVariable Integer pageSize) {
        System.out.println("查询全部购物车");
        IPage<CartItem> page = shoppingCartService.getPage(new Page<>(current, pageSize));
        System.out.println(page);
        return new Result(true, page);
    }

    /**
     * 更改购物车鲜花数量
     * @Author yrg
     * @Date 2023/4/5 13:55
     */
    @PostMapping("/changeItemNum")
    @Transactional
    public Result changeItemNum(@RequestBody ShoppingCart shoppingCart) {
        if (shoppingCartService.updateById(shoppingCart))
            return Result.success();
        else
            return Result.error();
    }

    /**
     * 移除购物车鲜花
     * @Author yrg
     * @Date 2023/4/5 14:18
     */
    @DeleteMapping("/delShoppingCartItem/{shoppingCarId}")
    @Transactional
    public Result delShoppingCartItem(@PathVariable String shoppingCarId) {
        if (shoppingCartService.removeById(shoppingCarId))
            return Result.success(ResultCode.SUCCESS.code(), "移除成功");
        else
            return Result.error();
    }

    /**
     * 删除购物车中的商品
     *
     * @Author yrg
     * @Date 2024/8/15 17:50
     */
    @DeleteMapping("/deleteItem/{itemId}/{userId}")
    @Transactional
    public Result deleteItem(@PathVariable String itemId, @PathVariable String userId) {
        if (shoppingCartService.deleteItem(itemId, userId)) {
            return Result.success(ResultCode.SUCCESS.code(), "删除成功");
        } else {
            return Result.error(ResultCode.ERROR.code(), "删除失败");
        }
    }

    /**
     * 购买
     * @Author yrg
     * @Date 2023/4/10 11:24
     */
//    @PostMapping("/pay")
//    @Transactional
//    public Result pay(@RequestBody JSONObject jsonObject) {
//        //JSONObject转实体类
//        JSONObject addressJo = jsonObject.getJSONObject("address");
//        Address address = JSONObject.toJavaObject(addressJo, Address.class);
//        //JSONObject转List
//        JSONArray itemJa = jsonObject.getJSONArray("itemList");
//        List<Item> itemList = JSONObject.parseArray(itemJa.toJSONString(), Item.class);
//        String price = jsonObject.get("price").toString();
//        if (shoppingCartService.pay(itemList, address, Float.valueOf(price))) {
//            return Result.success("购买成功");
//        } else
//            return Result.error();
//    }
    @PostMapping("/pay")
    @Transactional
    public Result pay(@RequestBody JSONObject jsonObject) {
        // JSONObject转实体类
        JSONObject addressJo = jsonObject.getJSONObject("address");
        Address address = JSONObject.toJavaObject(addressJo, Address.class);

        // JSONObject转List
        JSONArray itemJa = jsonObject.getJSONArray("itemIdList");
        List<Integer> itemIdList = JSONObject.parseArray(itemJa.toJSONString(), Integer.class);

        // 初始化 itemList
        List<Item> itemList = new ArrayList<>();
        for (Integer itemId : itemIdList) {
            System.out.println(itemId);
            Item item = itemService.getById(itemId);
            System.out.println(item);
            itemList.add(item);
        }
        System.out.println(itemList);

        String price = jsonObject.get("price").toString();
        if (shoppingCartService.pay(itemList, address, Float.valueOf(price))) {
            return Result.success("购买成功");
        } else {
            return Result.error();
        }
    }


    /**
     * 添加到购物车
     * @Author yrg
     * @Date 2023/4/11 14:50
     */
    @PostMapping("/addToShoppingCart")
    @Transactional
    public Result addToShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        String cartId = shoppingCartService.getCartId( shoppingCart.getUserId());
        if (shoppingCartService.judgeExistence(shoppingCart.getItemId(), shoppingCart.getUserId())) {
            return Result.error(ResultCode.ITEM_IS_IN_SHOPPINGCART.code(), ResultCode.ITEM_IS_IN_SHOPPINGCART.message());
        }
        if (shoppingCartService.addItem(shoppingCart.getItemId(),shoppingCart.getUserId(),cartId,shoppingCart.getPrice())) {
            return Result.success(ResultCode.SUCCESS.code(), "已成功加入购物车✪ ω ✪");
        } else {
            return Result.error(ResultCode.ERROR.code(), "加入购物车失败╥﹏╥");
        }
    }
}
