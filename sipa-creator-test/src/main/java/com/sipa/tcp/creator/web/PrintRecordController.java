package com.sipa.tcp.creator.web;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sipa.boot.core.allinone.SipaRequest;
import com.sipa.boot.mvc.allinone.SipaRest;
import com.sipa.tcp.creator.api.PrintRecordApi;
import com.sipa.tcp.creator.entity.PrintRecord;
import com.sipa.tcp.creator.service.PrintRecordService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author caszhou
 * @date 2024-07-23
 */
@Slf4j
@SipaRest(tag = "PrintRecord控制器", path = PrintRecordApi.MappingConstant.PATH)
public class PrintRecordController implements PrintRecordApi {
    @Resource
    private PrintRecordService printRecordService;

    @Override
    @SipaRequest.GetMapping(summary = "分页查询", path = MappingConstant.PAGE)
    public Page<PrintRecord> list(@RequestParam(required = false) Integer current,
        @RequestParam(required = false) Integer pageSize) {
        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        return printRecordService.page(new Page<>(current, pageSize));
    }

    @Override
    @SipaRequest.GetMapping(summary = "详情", path = MappingConstant.DETAIL)
    public PrintRecord getById(@PathVariable("id") String id) {
        return printRecordService.getById(id);
    }

    @Override
    @SipaRequest.PostMapping(summary = "新增", path = MappingConstant.CREATE)
    public Object create(@RequestBody PrintRecord payload) {
        printRecordService.save(payload);
        return "created successfully";
    }

    @Override
    @SipaRequest.DeleteMapping(summary = "删除", path = MappingConstant.DELETE)
    public Object delete(@PathVariable("id") String id) {
        printRecordService.removeById(id);
        return "deleted successfully";
    }

    @Override
    @SipaRequest.PutMapping(summary = "更新", path = MappingConstant.UPDATE)
    public Object update(@RequestBody PrintRecord payload) {
        printRecordService.updateById(payload);
        return "updated successfully";
    }
}
