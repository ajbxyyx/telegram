package com.ajbxyyx.utils;


import com.ajbxyyx.entity.dto.BaseCursorQueryDTO;
import com.ajbxyyx.entity.vo.CursorQueryVO.BaseCursorQueryVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class CursorQueryUtil<QueryInfo extends BaseCursorQueryDTO,BaseResult,Result> {


    private static final Logger log = LoggerFactory.getLogger(CursorQueryUtil.class);

    public BaseCursorQueryVO<Result> CursorQuery(QueryInfo queryInfo, LambdaQueryWrapper<BaseResult> lambdaQueryWrapper){

        Integer pageSize = queryInfo.getPageSize();
        Integer queryPageSize = pageSize+1;
        //查询包装 + page -> 查询 ->  转page包装
                                                //查目标页
        Page<BaseResult> page = getPage(new Page(1, queryPageSize, true), lambdaQueryWrapper);

        //通过page的结果 封装VO对象
        BaseCursorQueryVO<Result> resp = new BaseCursorQueryVO<>();
        resp.setIsLast(checkIsLast(page.getRecords().size(),queryPageSize));//设置isLast -> 已实现
        resp = getCursorQueryRespData(resp,removeLast(page.getRecords(),resp.getIsLast()));//设置data  ->  抽象实现
        resp.setCursor(getDataLastCursor(resp.getResult()));//设置cursor  -> 抽象实现


        return resp;
    }

    public abstract Page<BaseResult> getPage(Page page, LambdaQueryWrapper<BaseResult> lambdaQueryWrapper);

    private List<BaseResult> removeLast(List<BaseResult> records,Boolean isLast) {
        if (records.size() == 0 || isLast) {
            return records;
        }
        records.remove(records.size() - 1);//除去最后一个元素
        return records;
    }

    private Boolean checkIsLast(Integer size, Integer queryPageSize) {
        if(size.equals(queryPageSize)){
            return false;//不是最后一页
        }
        return true;//是最后一页
    }


    /**
     * 封装最终结果VO 的data
     * @param resp
     * @param baseResultList
     * @return
     */
    public abstract BaseCursorQueryVO<Result> getCursorQueryRespData(BaseCursorQueryVO<Result> resp, List<BaseResult> baseResultList);

    /**
     * 获取最终结果VO 的data 的最后一个元素游标
     * @param data
     * @return
     */
    public abstract Long getDataLastCursor(List<Result> data);
}
