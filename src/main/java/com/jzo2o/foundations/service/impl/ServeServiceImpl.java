package com.jzo2o.foundations.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.jzo2o.common.expcetions.CommonException;
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
import java.math.BigDecimal;
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

    final private Integer Is_Hot = 1;

    final private Integer Is_Not_Hot = 0;


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

    /**
     * 服务价格修改
     * @param id    服务id
     * @param price 价格
     * @return
     */
    @Override
    public Serve update(Long id, BigDecimal price) {
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getPrice, price)
                .update();
        if(!update)
            throw new CommonException("修改服务价格失败");
        Serve serve = baseMapper.selectById(id);
        return serve;
    }

    /**
     * 上架区域服务
     * @param id    服务id
     * @return
     */
    @Override
    public Serve onSale(Long id) {
        //根据id查询serve信息
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve))
            throw new ForbiddenOperationException("区域服务信息不存在");
        //如果serve的sale_status是0或1可以上架
        Integer saleStatus = serve.getSaleStatus();
        if(saleStatus==FoundationStatusEnum.DISABLE.getStatus() || saleStatus==FoundationStatusEnum.INIT.getStatus()){
            //如果服务项没有启用不能上架
            Long serveItemId = serve.getServeItemId();
            ServeItem serveItem = serveItemMapper.selectById(serveItemId);
            if(serveItem.getActiveStatus()!=FoundationStatusEnum.ENABLE.getStatus())
                throw new ForbiddenOperationException("服务项状态未启用，不能上架");
            //更新sale_status的状态
            boolean update = lambdaUpdate()
                    .eq(Serve::getId, id)
                    .set(Serve::getSaleStatus, FoundationStatusEnum.ENABLE.getStatus())
                    .update();
            if(!update)
                throw new CommonException("服务上架失败");
        }else {
            throw new ForbiddenOperationException("区域服务状态为草稿或下架时方可上架");
        }
        return baseMapper.selectById(id);

    }


    /**
     * 根据id删除区域服务
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        //根据id查询serve信息
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve))
            throw new ForbiddenOperationException("区域服务信息不存在");
        //如果serve的sale_status是0可以删除
        Integer saleStatus = serve.getSaleStatus();
        if(saleStatus!=FoundationStatusEnum.INIT.getStatus())
            throw new ForbiddenOperationException("当前服务状态不为草稿");
        //删除区域服务
        int res = baseMapper.deleteById(id);
        if(res==0)
            throw new CommonException("区域服务删除失败");
    }


    /**
     * 服务下架
     * @param id
     * @return
     */
    @Override
    public Serve offSale(Long id) {
        //根据id查询serve信息
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve))
            throw new ForbiddenOperationException("区域服务信息不存在");
        //如果serve的sale_status是2可以下架
        Integer saleStatus = serve.getSaleStatus();
        if(saleStatus == FoundationStatusEnum.ENABLE.getStatus()){
            //更新sale_status的状态
            boolean update = lambdaUpdate()
                    .eq(Serve::getId, id)
                    .set(Serve::getSaleStatus, FoundationStatusEnum.DISABLE.getStatus())
                    .update();
            if(!update)
                throw new CommonException("服务下架失败");
        }else {
            throw new ForbiddenOperationException("区域服务状态为上架时方可下架");
        }
        return baseMapper.selectById(id);
    }


    /**
     * 设置热门
     * @param id
     * @return
     */
    @Override
    public Serve onHot(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve))
            throw new ForbiddenOperationException("区域服务信息不存在");
        Integer isHot = serve.getIsHot();
        if(isHot == 1)
            throw new ForbiddenOperationException("当前服务已经是热门");
        else {
            boolean update = lambdaUpdate()
                    .eq(Serve::getId, id)
                    .set(Serve::getIsHot, Is_Hot)
                    .update();
            if(!update)
                throw new CommonException("服务设置热门失败");
        }
        return baseMapper.selectById(id);
    }

    /**
     * 取消热门
     * @param id
     * @return
     */
    @Override
    public Serve offHot(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve))
            throw new ForbiddenOperationException("区域服务信息不存在");
        Integer isHot = serve.getIsHot();
        if(isHot == 0)
            throw new ForbiddenOperationException("当前服务已经是非热门");
        else {
            boolean update = lambdaUpdate()
                    .eq(Serve::getId, id)
                    .set(Serve::getIsHot, Is_Not_Hot)
                    .update();
            if(!update)
                throw new CommonException("服务取消热门失败");
        }
        return baseMapper.selectById(id);
    }
}
