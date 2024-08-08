package com.yrg.springboot.controller;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ArrayUtils;
import com.yrg.springboot.entity.Sort;
import com.yrg.springboot.service.ItemService;
import com.yrg.springboot.service.SortService;
import com.yrg.springboot.utils.Result;
import com.yrg.springboot.utils.ResultCode;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;
import javax.annotation.Resource;
import java.util.ArrayList;

@RestController
@RequestMapping("/sort")
public class SortController {

    @Resource
    private SortService sortService;
    @Resource
    private ItemService itemService;

    @GetMapping("/{current}/{pageSize}")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result getAll(@PathVariable int current, @PathVariable int pageSize, Sort sort) {
        Page<Sort> allSort = sortService.getAll(new Page<>(current, pageSize), sort);
        return new Result(true, allSort);
    }

    @PostMapping
    @Transactional
    public Result addSort(@RequestBody Sort sort) {
        //根据花名查id
        String itemId = itemService.selectByItemName(sort.getItemName());
        String purposeStr = ArrayUtils.toString(sort.getPurposeArray());
        purposeStr = purposeStr.substring(1, purposeStr.length() - 1);
        sort.setItemId(itemId);
        sort.setPurpose(purposeStr);
        if (sortService.save(sort))
            return Result.success(ResultCode.SUCCESS.code(), "添加成功（￣︶￣）↗　");
        else
            return Result.error(ResultCode.ERROR.code(), "添加失败〒▽〒");
    }

    @PutMapping
    @Transactional
    public Result updateSort(@RequestBody Sort sort) {
        String purposeStr = ArrayUtils.toString(sort.getPurposeArray());
        purposeStr = purposeStr.substring(1, purposeStr.length() - 1);
        sort.setPurpose(purposeStr);
        if (sortService.updateById(sort))
            return Result.success(ResultCode.SUCCESS.code(), "修改成功(╹ڡ╹ )");
        else
            return Result.error(ResultCode.ERROR.code(), "修改失败＞︿＜");
    }

    @DeleteMapping("/{sortId}")
    @Transactional
    public Result delSortById(@PathVariable Integer sortId) {
        if (sortService.removeById(sortId))
            return Result.success(ResultCode.SUCCESS.code(), "删除成功(❁´◡`❁)");
        else
            return Result.error(ResultCode.ERROR.code(), "删除失败≡(▔﹏▔)≡");
    }

    /**
     * 批量删除
     * @Author liuzhi
     * @Date 2023/3/28 17:00
     */
    @DeleteMapping("/delByIdList")
    @Transactional
    public Result delItemSortByIds(@RequestBody JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("idArr");
        ArrayList<Integer> idList = new ArrayList<>();
        for (Object id : jsonArray) {
            idList.add((Integer) id);
        }
        if (sortService.removeByIds(idList))
            return Result.success(ResultCode.SUCCESS.code(), "删除成功(❁´◡`❁)");
        else
            return Result.error(ResultCode.ERROR.code(), "删除失败≡(▔﹏▔)≡");
    }

    /**
     * 可被分类的鲜花名
     * @Author liuzhi
     * @Date 2023/3/28 10:47
     */
    @GetMapping("/canSortItemName")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result canSortItemName() {
        String[] itemIds = sortService.notSortItemId();
        //可分类的鲜花名称，如果查询出来的id数组为空则会获取全部的鲜花名称，所以这里限制一下
        if (itemIds.length != 0) {
            String[] itemNames = sortService.canSortItemName(itemIds);
            return Result.success(itemNames);
        } else
            return Result.error("", "无数据");
    }
}
