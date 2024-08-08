package com.yrg.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrg.springboot.entity.Item;
import com.yrg.springboot.entity.Sort;
import org.springframework.web.multipart.MultipartFile;

public interface ItemService extends IService<Item> {
    /**
     * 分页获取
     *
     * @Author liuzhi
     * @Date 2022/10/3 20:23
     */
    IPage<Item> getPage(int current, int pageSize, String type, String itemName);

    IPage<Item> getPage(int current, int pageSize, Item item);   //方法重载

    /**
     * 根据花名查询鲜花id
     *
     * @Author liuzhi
     * @Date 2022/10/14 20:36
     */
    String selectByItemName(String itemName);

    /**
     * 添加鲜花
     *
     * @Author liuzhi
     * @Date 2022/10/27 11:23
     */
    Boolean addItem(MultipartFile file, Item item);

    /**
     * 修改鲜花
     *
     * @Author liuzhi
     * @Date 2022/10/27 11:25
     */
    Boolean editItem(MultipartFile file, Item item);

    /**
     * 前台主页
     *
     * @Author liuzhi
     */
    //新品鲜花
    IPage<Item> getNewProduct(int current, int pageSize, String itemName);

    //特惠鲜花
    IPage<Item> getDiscount(int current, int pageSize, String itemName);

    //热销鲜花
    IPage<Item> getHotSale(int current, int pageSize, String itemName);

    //推荐鲜花
    IPage<Item> getRecommend(int current, int pageSize, String itemName);

    //所有鲜花/分类
    IPage<Item> itemSort(int current, int pageSize, Sort sort);
    /**
     * 鲜花详情页数据
     *
     * @Author liuzhi
     * @Date 2023/4/11 11:50
     */
    Item itemDetail(String itemId);

    /**
     * 更改鲜花售出的数量
     *
     * @Author liuzhi
     * @Date 2022/10/26 13:13
     */
    boolean updateItemSell(String itemId, Integer quantity);

    /**
     * 更新鲜花评分
     *
     * @Author liuzhi
     * @Date 2022/10/28 12:55
     */
    boolean updateItemScore(String itemId, Float score);

    /**
     * 更新库存数量
     *
     * @Author liuzhi
     * @Date 2022/10/28 13:40
     */
    boolean updateItemStock(String itemId, Integer quantity);
}
