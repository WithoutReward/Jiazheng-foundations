package com.jzo2o.foundations.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private ServeItemMapper serveItemMapper;

    @Resource
    private RegionMapper regionMapper;


    /**
     * 区域服务分页查询
     * @param servePageQueryReqDTO
     * @return
     */
    @Override
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO) {
                return PageHelperUtils.selectPage(servePageQueryReqDTO,
                        () -> baseMapper.queryServeListByRegionId(servePageQueryReqDTO.getRegionId()));

    }

    /**
     * 批量添加区域服务
     * @param serveUpsertReqDTOList
     */
    @Override
    public void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        for (ServeUpsertReqDTO serveUpsertReqDTO : serveUpsertReqDTOList) {
            //1.校验服务项是否为启用状态，不是启用状态不能新增
            ServeItem serveItem = serveItemMapper.selectById(serveUpsertReqDTO.getServeItemId());
            if(serveItem.getActiveStatus()!= FoundationStatusEnum.ENABLE.getStatus() || ObjectUtil.isNull(serveItem)){
                throw new ForbiddenOperationException("该服务不存在或未启用，无法添加到区域下使用");
            }

            //2.校验是否重复新增
            Integer count = lambdaQuery()
                    .eq(Serve::getServeItemId, serveUpsertReqDTO.getServeItemId())
                    .eq(Serve::getRegionId, serveUpsertReqDTO.getRegionId())
                    .count();
            if(count>0)
                throw new ForbiddenOperationException(serveItem.getName()+"服务已存在");

            //3.新增服务
            Serve serve = BeanUtils.toBean(serveUpsertReqDTO, Serve.class);
            Region region = regionMapper.selectById(serveUpsertReqDTO.getRegionId());
            serve.setCityCode(region.getCityCode());
            baseMapper.insert(serve);

        }
    }
}
