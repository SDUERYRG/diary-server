package com.yrg.springboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yrg.springboot.entity.Evaluate;
import com.yrg.springboot.service.EvaluateService;
import com.yrg.springboot.service.ItemService;
import com.yrg.springboot.utils.Result;
import com.yrg.springboot.utils.ResultCode;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Author yrg
 * @Date 2022/10/8 10:18
 * 评价信息管理相关操作
 */
@RestController
@RequestMapping("/evaluate")
public class EvaluateController {

    @Resource
    private EvaluateService evaluateService;
    @Resource
    private ItemService itemService;

    @GetMapping("/{current}/{pageSize}")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result getAll(@PathVariable int current, @PathVariable int pageSize, Evaluate evaluate) {
        Page<Evaluate> allEvaluate = evaluateService.getAll(new Page<>(current, pageSize), evaluate);
        return new Result(true, allEvaluate);
    }

    /**
     * @Author yrg
     * @Date 2022/10/8 19:55
     * 删除评价信息
     */
    @DeleteMapping("/{evaluateId}")
    @Transactional
    public Result delEvaluateById(@PathVariable String evaluateId) {
        if (evaluateService.removeById(evaluateId))
            return Result.success(ResultCode.SUCCESS.code(), "删除成功(●'◡'●)");
        else
            return Result.error(ResultCode.ERROR.code(), "删除失败,可能不存在该数据(＞﹏＜)");
    }

    /**
     * 批量删除
     *
     * @Author yrg
     * @Date 2023/3/27 10:45
     */
    @DeleteMapping("/delByIdList")
    @Transactional
    public Result delByIdList(@RequestBody JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("idArr");
        ArrayList<String> idList = new ArrayList<>();
        for (Object id : jsonArray) {
            idList.add((String) id);
        }
        if (evaluateService.removeByIds(idList))
            return Result.success(ResultCode.SUCCESS.code(), "删除成功（＾∀＾●）ﾉｼ");
        else
            return Result.error(ResultCode.ERROR.code(), "删除失败(* ￣︿￣)");
    }

    /**
     * 用户订单页面添加评价信息
     *
     * @Author yrg
     * @Date 2023/4/11 16:37
     */
    @PostMapping
    @Transactional
    public Result addEvaluate(@RequestBody Evaluate evaluate) {
        evaluate.setEvaluateTime(new Date());
        if (evaluateService.save(evaluate)) {
            //更改鲜花评分
            Float itemScoreAvg = evaluateService.getItemScoreAvg(evaluate.getItemId());
            itemService.updateItemScore(evaluate.getItemId(), itemScoreAvg);
            return Result.success(ResultCode.SUCCESS.code(), "评价成功");
        } else {
            return Result.error(ResultCode.ERROR.code(), "评价失败");
        }
    }

    /**
     * 查看用户该订单的该鲜花的评价信息
     * @Author yrg
     * @Date 2023/4/11 17:32
     */
    @PostMapping("/lookEvaluate")
    public Result lookEvaluate(@RequestBody Evaluate evaluate) {
        Evaluate evaluate1 = evaluateService.lookEvaluate(evaluate.getItemId(), evaluate.getUserId(), evaluate.getOrderNum());
        if (evaluate1 != null) {
            return Result.success(evaluate1);
        } else {
            return Result.error();
        }
    }

}
