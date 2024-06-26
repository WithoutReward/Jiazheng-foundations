package com.jzo2o.foundations.service;


import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-01
 */
public interface IServeService extends IService<Serve> {

    /**
     * 区域服务分页查询
     * @param servePageQueryReqDTO
     * @return
     */
    PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO);

    /**
     * 批量添加区域服务
     * @param serveUpsertReqDTOList
     */
    void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList);

    /**
     * 服务价格修改
     * @param id    服务id
     * @param price 价格
     * @return 服务
     */
    Serve update(Long id, BigDecimal price);


    /**
     * 上架区域服务
     * @param id    服务id
     */
    Serve onSale(Long id);


    /**
     * 根据id删除区域服务
     * @param id
     */
    void deleteById(Long id);

    /**
     * 服务下架
     * @param id
     * @return
     */
    Serve offSale(Long id);

    /**
     * 设置热门
     * @param id
     * @return
     */
    Serve onHot(Long id);

    /**
     * 取消热门
     * @param id
     * @return
     */
    Serve offHot(Long id);
}
