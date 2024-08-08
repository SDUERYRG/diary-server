package com.yrg.springboot.service.imp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrg.springboot.dao.EvaluateDao;
import com.yrg.springboot.entity.Evaluate;
import com.yrg.springboot.service.EvaluateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author liuzhi
 * @Date 2022/10/7 20:25
 * 评价信息管理相关操作
 */
@Service
public class EvaluateServiceImpl extends ServiceImpl<EvaluateDao, Evaluate> implements EvaluateService {

    @Resource
    private EvaluateDao evaluateDao;

    @Override
//    @Cached(name = "evaluateCache_",expire = 10,timeUnit = TimeUnit.MINUTES)   //数据添加到缓存
    @Transactional(propagation = Propagation.SUPPORTS)
    public Page<Evaluate> getAll(IPage<Evaluate> iPage, Evaluate evaluate) {
        return evaluateDao.getAll(iPage, evaluate);
    }


    /**
     * 判断用户是否已经评价了该订单的该鲜花
     * @Author liuzhi
     * @Date 2022/10/26 15:56
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer isEvaluated(String itemId, String userId, String orderNum) {
        return evaluateDao.isEvaluated(itemId, userId, orderNum);
    }

    /**
     * 查看用户该订单的该鲜花的评价信息
     * @Author liuzhi
     * @Date 2022/10/27 8:44
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public Evaluate lookEvaluate(String itemId, String userId, String orderNum) {
        return evaluateDao.lookEvaluate(itemId, userId, orderNum);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Float getItemScoreAvg(String itemId) {
        return evaluateDao.getItemScoreAvg(itemId);
    }
}