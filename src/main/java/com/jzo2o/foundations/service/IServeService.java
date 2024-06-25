package com.jzo2o.foundations.service;


import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;

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

}
