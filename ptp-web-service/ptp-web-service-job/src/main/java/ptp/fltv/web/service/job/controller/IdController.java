package ptp.fltv.web.service.job.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pfp.fltv.common.exceptions.PtpException;
import pfp.fltv.common.model.po.response.Result;
import ptp.fltv.web.service.job.worker.IdWorker;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/19 PM 9:27:11
 * @description ID生成与管理的控制器
 * @filename IdController.java
 */

@Tag(name = "ID生成接口")
@RestController
@RequestMapping("/service/job/id")
public class IdController {


    @Autowired
    private IdWorker idWorker;


    @SentinelResource("service-job-id-controller")
    @Operation(description = "根据ID查询单条公告数据")
    @GetMapping("/goods/kill")
    public Result<Long> generateGoodsKillId(

            @Parameter(name = "infix", required = true) @RequestParam(name = "infix") String infix

    ) throws PtpException {

        if (!StringUtils.hasLength(infix)) {

            throw new PtpException(804);

        }

        return Result.success(idWorker.generateGoodsKillId(infix));

    }


}