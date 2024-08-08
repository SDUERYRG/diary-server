package com.yrg.springboot.service.imp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import  com.yrg.springboot.dao.ItemDao;
import com.yrg.springboot.entity.*;
import com.yrg.springboot.service.EvaluateService;
import com.yrg.springboot.service.ItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrg.springboot.service.ItemStateService;
import com.yrg.springboot.service.UserService;
import com.yrg.springboot.utils.MyException;
import com.yrg.springboot.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemDao, Item> implements ItemService {
    @Autowired
    private ItemDao itemDao;
    @Resource
    private ItemStateService itemStateService;
    @Resource
    private EvaluateService evaluateService;
    @Resource
    private UserService userService;
    @Value("${page.itemImg}")
    private String imgPath;    //鲜花图片存放路径

    @Override
    public IPage<Item> getPage(int current, int pageSize, String type, String itemName) {
        IPage page = new Page(current, pageSize);
        LambdaQueryWrapper<Item> lqw = new LambdaQueryWrapper<Item>();
        lqw.inSql((type != null) && (type != ""), Item::getItemId, "select itemId from item_state where " + type + " = 1");
        lqw.like((itemName != null) && (itemName != ""), Item::getItemName, itemName);
        itemDao.selectPage(page, lqw);
        return page;
    }

    /**
     * @Author liuzhi
     * @Date 2022/10/3 20:23
     * 第一个参数：只有该参数是true时，才将like条件拼接到sql中；本例中，当houseHoldNum字段不为空时，则拼接name字段的like查询条件；
     * 第二个参数：该参数是数据库中的字段名；
     * 第三个参数：该参数值字段值；
     * 需要说明的是，这里的like查询是使用的默认方式，也就是说在查询条件的左右两边都有%：name= ‘%lz%'；
     * 如果只需要在左边或者右边拼接%，使用likeLeft或者likeRight方法。
     */
    @Override
    public IPage<Item> getPage(int current, int pageSize, Item item) {
        IPage page = new Page(current, pageSize);
        LambdaQueryWrapper<Item> lqw = new LambdaQueryWrapper<Item>();
        //condition为false时查询全部
        lqw.like(item.getItemName() != null, Item::getItemName, item.getItemName());
        lqw.like(item.getDescription() != null, Item::getDescription, item.getDescription());
        itemDao.selectPage(page, lqw);
        return page;
    }

    /**
     * 根据花名查询鲜花id
     *
     * @Author liuzhi
     * @Date 2022/10/14 20:36
     */
    @Override
    public String selectByItemName(String itemName) {
        return itemDao.selectByItemName(itemName);
    }

    @Override
    public Boolean addItem(MultipartFile file, Item item) {
        try {
            String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            item.setPicture(fileName);
            file.transferTo(new File(imgPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyException(ResultCode.FILE_UPLOAD_FAIL.code(), ResultCode.FILE_UPLOAD_FAIL.message());
        }
        String itemId = itemDao.insert(item) > 0 ? item.getItemId() : "";
        if (itemId.equals(""))
            return false;
        else {
            ItemState itemState = new ItemState();
            itemState.setReXiao(item.isRxFlag() ? 1 : 0);
            itemState.setTuiJian(item.isTjFlag() ? 1 : 0);
            itemState.setXinPin(item.isXpFlag() ? 1 : 0);
            itemState.setItemId(item.getItemId());
            itemState.setItemId(itemId);
            if (itemStateService.save(itemState)) {
                return true;
            } else {
                throw new MyException(ResultCode.ERROR.code(), "鲜花状态添加异常");
            }
        }
    }

    @Override
    public Boolean editItem(MultipartFile file, Item item) {
        String fileName = "";
        if (file != null) {
            try {
                fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                File imgFile = new File(imgPath + item.getPicture());
                if (imgFile.exists()) {
                    imgFile.delete();
                }
                file.transferTo(new File(imgPath + fileName));
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultCode.FILE_UPLOAD_FAIL.code(), ResultCode.FILE_UPLOAD_FAIL.message());
            }
            item.setPicture(fileName);
        }
        if (itemDao.updateById(item) > 0) {
            //更改鲜花状态信息
            ItemState itemState = new ItemState();
            itemState.setReXiao(item.isRxFlag() ? 1 : 0);
            itemState.setTuiJian(item.isTjFlag() ? 1 : 0);
            itemState.setXinPin(item.isXpFlag() ? 1 : 0);
            itemState.setItemId(item.getItemId());
            if (itemStateService.updateByItemId(itemState))
                return true;
            else
                return false;
        }
        return true;
    }

    @Override
    public IPage<Item> getNewProduct(int current, int pageSize, String itemName) {
        return getPage(current, pageSize, "xinPin", itemName);
    }

    @Override
    public IPage<Item> getDiscount(int current, int pageSize, String itemName) {
        IPage page = new Page(current, pageSize);
        LambdaQueryWrapper<Item> lqw = new LambdaQueryWrapper<Item>();
        lqw.like(itemName != null, Item::getItemName, itemName);
        lqw.gt(true, Item::getDiscount, 0);
        itemDao.selectPage(page, lqw);
        return page;
    }

    @Override
    public IPage<Item> getHotSale(int current, int pageSize, String itemName) {
        return getPage(current, pageSize, "reXiao", itemName);
    }

    @Override
    public IPage<Item> getRecommend(int current, int pageSize, String itemName) {
        return getPage(current, pageSize, "tuiJian", itemName);
    }

    @Override
    public IPage<Item> itemSort(int current, int pageSize, Sort sort) {
        IPage page = new Page(current, pageSize);
        LambdaQueryWrapper<Item> lqw = new LambdaQueryWrapper<>();
        lqw.inSql(sort.getItemName() != null && sort.getItemName() != "", Item::getItemId, "select itemId from item_table where itemName like '%" + sort.getItemName() + "%'");
        lqw.inSql(sort.getPurpose() != null && sort.getPurpose() != "", Item::getItemId, "select itemId from item_sort where purpose like '%" + sort.getPurpose() + "%'");
        lqw.inSql(sort.getMaterial() != null && sort.getMaterial() != "", Item::getItemId, "select itemId from item_sort where material like '%" + sort.getMaterial() + "%'");
        lqw.inSql(sort.getColor() != null && sort.getColor() != "", Item::getItemId, "select itemId from item_sort where color like '%" + sort.getColor() + "%'");
        itemDao.selectPage(page, lqw);
        return page;
    }

    @Override
    public Item itemDetail(String itemId) {
        Item item = itemDao.selectById(itemId);
        LambdaQueryWrapper<Evaluate> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Evaluate::getItemId, itemId);
        if (evaluateService.count(lqw) != 0) {
            lqw.inSql(Evaluate::getItemId, "select itemId from evaluate where itemId = " + itemId);
            List<Evaluate> evaluateList = evaluateService.list(lqw);
            for (Evaluate evaluate : evaluateList) {
                User user = userService.getById(evaluate.getUserId());
                evaluate.setUserName(user.getUserName());
                evaluate.setUserTx(user.getTxPicture());
            }
            item.setEvaluateList(evaluateList);
        }
        return item;
    }

    @Override
    public boolean updateItemSell(String itemId, Integer quantity) {
        return itemDao.updateItemSell(itemId, quantity);
    }

    @Override
    public boolean updateItemScore(String itemId, Float score) {
        return itemDao.updateItemScore(itemId, score);
    }

    @Override
    public boolean updateItemStock(String itemId, Integer quantity) {
        return itemDao.updateItemStock(itemId, quantity);
    }
}
