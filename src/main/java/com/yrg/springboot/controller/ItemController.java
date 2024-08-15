package com.yrg.springboot.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrg.springboot.entity.Item;
import com.yrg.springboot.entity.ItemState;
import com.yrg.springboot.service.ItemService;
import com.yrg.springboot.service.ItemStateService;
import com.yrg.springboot.service.SortService;
import com.yrg.springboot.utils.Result;
import com.yrg.springboot.utils.ResultCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Resource
    private ItemService itemService;
    @Resource
    private ItemStateService itemStateService;
    @Resource
    private SortService sortService;
    @Value("${page.itemImg}")
    private String imgPath;    //鲜花图片存放路径


    /**
     * 添加鲜花数据
     * @Author yrg
     * @Date 2022/10/16 14:27
     */
    @PostMapping
    @Transactional
    public Result addItem(MultipartFile file, Item item) {
        if (!(itemService.selectByItemName(item.getItemName()) == null)) {
            return Result.error(ResultCode.ITEM_ALREADY_EXISTS.code(), ResultCode.ITEM_ALREADY_EXISTS.message());
        }
        if (itemService.addItem(file, item)) {
            return Result.success(ResultCode.SUCCESS.code(), "添加成功（＾▽＾）");
        } else {
            return Result.error(ResultCode.ERROR.code(), "添加失败(。>︿<)_θ");
        }
    }

    @GetMapping("/{current}/{pageSize}")
    @Transactional
    public Result getAllItem(@PathVariable Integer current, @PathVariable Integer pageSize, Item item) {
        IPage<Item> page = itemService.getPage(current, pageSize, item);
        if (current > page.getPages()) //当前页大于总页数
            page = itemService.getPage((int) page.getPages(), pageSize, item);
        System.out.println("查询商品成功");
        return new Result(true, page);
    }

    @GetMapping("/search/{current}/{pageSize}")
    @ResponseBody
    public Result searchItem(@PathVariable int current, @PathVariable int pageSize, @RequestParam String keyword) {
        System.out.println("搜索关键字："+keyword);
        if (keyword == null || keyword.equals(""))
            return getAllItem(current, pageSize, null);
        IPage<Item> items = itemService.searchItem(current, pageSize, keyword);
        return new Result(true, items);
    }

    /**
     * 获取item状态
     *
     * @Author yrg
     * @Date 2023/3/27 16:30
     */
    @GetMapping("/itemStatus/{itemId}")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result getItemStatusById(@PathVariable String itemId) {
        //获取鲜花状态信息
        ItemState itemState = itemStateService.getItemStateByItemId(itemId);
        if (itemState != null) {
            Item item = new Item();
            item.setRxFlag(itemState.getReXiao() == 1);
            item.setTjFlag(itemState.getTuiJian() == 1);
            item.setXpFlag(itemState.getXinPin() == 1);
            return Result.success(item);
        } else {
            return Result.error(ResultCode.ERROR.code(), "获取鲜花信息失败");
        }
    }

    @PutMapping("/updateItem")
    @Transactional
    public Result updateItem(MultipartFile file, Item item) {
        System.out.println("修改商品");
        if (itemService.editItem(file, item))
            return Result.success(ResultCode.SUCCESS.code(), "修改成功ʕ•ᴥ•ʔ");
        else
            return Result.error(ResultCode.ERROR.code(), "修改失败(๑＞ ＜)☆");
    }

    @DeleteMapping("/{itemId}/{picture}")
    @Transactional
    public Result delItemById(@PathVariable String itemId, @PathVariable String picture) {

        if (picture != null && !picture.equals("")) {
            if (itemService.removeById(itemId)) {
                System.out.println("删除商品");
                System.out.println(imgPath + picture);
                File file = new File(imgPath + picture);
                if (file.exists())
                    file.delete();
                sortService.delSortByItemId(itemId);
                itemStateService.delItemStateByItemId(itemId);
                return Result.success(ResultCode.SUCCESS.code(), "删除成功(。・・)ノ");
            }
        }
        return Result.error(ResultCode.ERROR.code(), "删除失败ಠಿ_ಠ");
    }

    /**
     * 批量删除
     * @Author yrg
     * @Date 2023/3/28 9:16
     */
    @DeleteMapping("/delByIdList")
    @Transactional
    public Result delItemByIdList(@RequestBody JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("idArr");
        ArrayList<String> idList = new ArrayList<>();
        for (Object id : jsonArray) {
            idList.add((String) id);
        }
        if (itemService.removeByIds(idList))
            return Result.success(ResultCode.SUCCESS.code(), "删除成功（＾∀＾●）ﾉｼ");
        else
            return Result.error(ResultCode.ERROR.code(), "删除失败(* ￣︿￣)");
    }


}
