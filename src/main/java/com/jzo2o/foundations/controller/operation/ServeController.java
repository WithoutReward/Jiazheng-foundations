package com.jzo2o.foundations.controller.operation;



import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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


}
