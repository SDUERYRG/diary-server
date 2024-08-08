package com.yrg.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yrg.springboot.entity.ItemState;



public interface ItemStateService extends IService<ItemState> {
    /**
     * 鲜花状态获取
     *
     * @Author liuzhi
     * @Date 2022/10/19 16:17
     */
    ItemState getItemStateByItemId(String itemId);

    /**
     * 鲜花状态修改
     *
     * @Author liuzhi
     * @Date 2022/10/19 15:56
     */
    boolean updateByItemId(ItemState itemState);

    /**
     * 删除鲜花状态信息
     *
     * @Author liuzhi
     * @Date 2022/10/19 16:11
     */
    boolean delItemStateByItemId(String itemId);
}
