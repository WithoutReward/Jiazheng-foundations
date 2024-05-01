package com.jzo2o.foundations.service.impl;


import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-01
 */
@Service
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {


    @Override
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO) {
                return PageHelperUtils.selectPage(servePageQueryReqDTO,
                        () -> baseMapper.queryServeListByRegionId(servePageQueryReqDTO.getRegionId()));

    }
}
