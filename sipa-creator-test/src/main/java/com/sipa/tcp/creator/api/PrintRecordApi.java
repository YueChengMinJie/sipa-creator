package com.sipa.tcp.creator.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sipa.boot.core.allinone.SipaRequest;
import com.sipa.boot.feign.allinone.SipaApi;
import com.sipa.tcp.creator.entity.PrintRecord;

/**
 * @author caszhou
 * @date 2024-07-23
 */
@SipaApi(tag = "PrintRecord控制器", name = "ams-service", contextId = "printRecordApi",
    path = PrintRecordApi.MappingConstant.PATH)
public interface PrintRecordApi {
    interface MappingConstant {
        String PATH = "/print-record";

        String PAGE = "";

        String DETAIL = "/{id}";

        String CREATE = "";

        String DELETE = "/{id}";

        String UPDATE = "";
    }

    @SipaRequest.GetMapping(summary = "分页查询", path = MappingConstant.PAGE)
    Page<PrintRecord> list(@RequestParam(required = false) Integer current,
        @RequestParam(required = false) Integer pageSize);

    @SipaRequest.GetMapping(summary = "详情", path = MappingConstant.DETAIL)
    PrintRecord getById(@PathVariable("id") String id);

    @SipaRequest.PostMapping(summary = "新增", path = MappingConstant.CREATE)
    Object create(@RequestBody PrintRecord payload);

    @SipaRequest.DeleteMapping(summary = "删除", path = MappingConstant.DELETE)
    Object delete(@PathVariable("id") String id);

    @SipaRequest.PutMapping(summary = "更新", path = MappingConstant.UPDATE)
    Object update(@RequestBody PrintRecord payload);
}
