package com.jzo2o.foundations.controller.operation;



import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 区域服务管理相关的接口
 */

@RestController("operationServeController")
@RequestMapping("/operation/serve")
@Api(tags = "运营端 - 区域服务相关接口")
public class ServeController {

    @Resource
    private IServeService serveService;


    /**
     * 区域服务分页查询
     * @param servePageQueryReqDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("区域服务分页查询")
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO) {
        return serveService.page(servePageQueryReqDTO);
    }

    /**
     * 批量添加区域服务
     * @param serveUpsertReqDTOList
     */
    @PostMapping("/batch")
    @ApiOperation("区域服务批量新增")
    public void add(@RequestBody List<ServeUpsertReqDTO> serveUpsertReqDTOList){
        serveService.batchAdd(serveUpsertReqDTOList);
    }


    /**
     * 修改区域服务价格
     * @param id
     * @param price
     */
    @PutMapping("/{id}")
    @ApiOperation("修改区域服务价格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataTypeClass = BigDecimal.class)
    })
    public void update(@PathVariable Long id, BigDecimal price){
        serveService.update(id,price);
    }

    /**
     * 区域服务上架
     * @param id
     */
    @PutMapping("/onSale/{id}")
    @ApiOperation("区域服务上架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void onSale(@PathVariable Long id){
        serveService.onSale(id);
    }

    /**
     * 删除区域服务
     * @param id
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除区域服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void delete(@PathVariable Long id){
        serveService.deleteById(id);
    }


    /**
     * 服务下架
     * @param id
     */
    @PutMapping("/offSale/{id}")
    @ApiOperation("服务下架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void offSale(@PathVariable Long id){
        serveService.offSale(id);
    }

    /**
     * 设置热门
     * @param id
     */
    @PutMapping("/onHot/{id}")
    @ApiOperation("设置热门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void onHot(@PathVariable Long id){
        serveService.onHot(id);
    }


    @PutMapping("/offHot/{id}")
    @ApiOperation("取消热门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void offHot(@PathVariable Long id){
        serveService.offHot(id);
    }



}
