package com.yrg.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrg.springboot.entity.ItemState;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ItemStateDao extends BaseMapper<ItemState> {
    /**
     * 鲜花状态获取
     *
     * @Author liuzhi
     * @Date 2022/10/19 16:16
     */
    @Select("select tuiJian,xinPin,reXiao from item_state where itemId = #{itemId}")
    ItemState getItemStateByItemId(String itemId);

    /**
     * 鲜花状态修改
     *
     * @Author liuzhi
     * @Date 2022/10/19 15:55
     */
    @Update("update item_state set tuiJian = #{tuiJian},reXiao = #{reXiao},xinPin = #{xinPin} where itemId = #{itemId}")
    boolean updateByItemId(ItemState itemState);

    /**
     * 鲜花状态删除
     *
     * @Author liuzhi
     * @Date 2022/10/19 16:09
     */
    @Delete("delete from item_state where itemId = #{itemId}")
    boolean delItemStateByItemId(String itemId);
}
