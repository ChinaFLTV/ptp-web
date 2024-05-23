package ptp.fltv.web.service.elasticsearch.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.web.bind.annotation.*;
import pfp.fltv.common.model.po.finance.Commodity;
import pfp.fltv.common.response.Result;
import pfp.fltv.common.utils.ReflectUtils;
import ptp.fltv.web.service.elasticsearch.service.EsSearchService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/5/23 PM 10:17:02
 * @description 商品控制器
 * @filename CommodityController.java
 */

@Tag(name = "商品操作接口(ES)")
@RestController
@RequestMapping("/finance/commodity")
public class CommodityController {


    @Resource
    private EsSearchService esSearchService;


    @SentinelResource("service-elasticsearch-finance-commodity-controller")
    @Operation(description = "根据给定的关键词分页查询符合个件的商品数据")
    @PostMapping("/fuzzy_query/page/{offset}/{limit}")
    public Result<List<Commodity>> fuzzyQueryCommodityPage(

            @Parameter(name = "keywords", description = "查询商品数据用到的关键词", in = ParameterIn.DEFAULT) @RequestParam("keywords") List<String> keywords,
            @Parameter(name = "offset", description = "查询的一页商品数据的起始偏移量", in = ParameterIn.PATH) @PathVariable("offset") Long offset,
            @Parameter(name = "limit", description = "查询的这一页商品数据的数量", in = ParameterIn.PATH) @PathVariable("limit") Long limit

    ) {

        return Result.success(esSearchService.pagingQueryByKeywords(keywords, "title", offset, limit, Commodity.class));

    }


    @SentinelResource("service-elasticsearch-finance-commodity-controller")
    @Operation(description = "修改单个商品数据(ES)")
    @PutMapping("/update/single")
    public Result<Map<String, Object>> updateSingleCommodity(

            @Parameter(name = "commodity", description = "待修改的单个商品数据") @RequestBody Commodity commodity

    ) {

        UpdateResponse response = esSearchService.updateEntity(commodity, null);
        return Result.neutral(ReflectUtils.toJSONObjectForcibly(response, null));

    }


    @SentinelResource("service-elasticsearch-finance-commodity-controller")
    @Operation(description = "添加单个商品数据(ES)")
    @PostMapping("/insert/single")
    public Result<Map<String, Object>> insertSingleCommodity(

            @Parameter(name = "commodity", description = "待添加的单个数据") @RequestBody Commodity commodity

    ) {

        Commodity savedCommodity = esSearchService.insertEntity(commodity, null);

        Map<String, Object> map = new HashMap<>();
        map.put("savedEntity", savedCommodity);
        return Result.neutral(map);

    }


    @SentinelResource("service-elasticsearch-finance-commodity-controller")
    @Operation(description = "删除单个商品数据(ES)")
    @DeleteMapping("/delete/single/{id}")
    public Result<?> deleteSingleCommodity(

            @Parameter(name = "id", description = "待删除的单个商品ID", in = ParameterIn.PATH) @PathVariable("id") Long id

    ) {

        ByQueryResponse response = esSearchService.deleteEntityById(id, Commodity.class, null);
        return Result.neutral(ReflectUtils.toJSONObjectForcibly(response, null));

    }


}