package com.yrg.springboot.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrg.springboot.dao.ItemStateDao;
import com.yrg.springboot.entity.Item;
import com.yrg.springboot.entity.ItemState;
import com.yrg.springboot.service.ItemStateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class ItemStateServiceImp extends ServiceImpl<ItemStateDao, ItemState> implements ItemStateService {
    @Resource
    private ItemStateDao itemStateDao;

    /**
     * 鲜花状态获取
     *
     * @Author yrg
     * @Date 2022/10/19 16:17
     */
    @Override
    public ItemState getItemStateByItemId(String itemId) {
        return itemStateDao.getItemStateByItemId(itemId);
    }

    /**
     * 鲜花状态修改
     *
     * @Author yrg
     * @Date 2022/10/19 15:56
     */
    @Override
    public boolean updateByItemId(ItemState itemState) {
        return itemStateDao.updateByItemId(itemState);
    }

    /**
     * 删除鲜花状态信息
     *
     * @Author yrg
     * @Date 2022/10/19 16:11
     */
    @Override
    public boolean delItemStateByItemId(String itemId) {
        return itemStateDao.delItemStateByItemId(itemId);
    }
}
