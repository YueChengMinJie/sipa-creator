package com.sipa.tcp.creator.web;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sipa.boot.core.allinone.SipaRequest;
import com.sipa.boot.mvc.allinone.SipaRest;
import com.sipa.tcp.creator.api.PrintPrinterApi;
import com.sipa.tcp.creator.entity.PrintPrinter;
import com.sipa.tcp.creator.service.PrintPrinterService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author caszhou
 * @date 2024-07-23
 */
@Slf4j
@SipaRest(tag = "PrintPrinter控制器", path = PrintPrinterApi.MappingConstant.PATH)
public class PrintPrinterController implements PrintPrinterApi {
    @Resource
    private PrintPrinterService printPrinterService;

    @Override
    @SipaRequest.GetMapping(summary = "分页查询", path = MappingConstant.PAGE)
    public Page<PrintPrinter> list(@RequestParam(required = false) Integer current,
        @RequestParam(required = false) Integer pageSize) {
        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        return printPrinterService.page(new Page<>(current, pageSize));
    }

    @Override
    @SipaRequest.GetMapping(summary = "详情", path = MappingConstant.DETAIL)
    public PrintPrinter getById(@PathVariable("id") String id) {
        return printPrinterService.getById(id);
    }

    @Override
    @SipaRequest.PostMapping(summary = "新增", path = MappingConstant.CREATE)
    public Object create(@RequestBody PrintPrinter payload) {
        printPrinterService.save(payload);
        return "created successfully";
    }

    @Override
    @SipaRequest.DeleteMapping(summary = "删除", path = MappingConstant.DELETE)
    public Object delete(@PathVariable("id") String id) {
        printPrinterService.removeById(id);
        return "deleted successfully";
    }

    @Override
    @SipaRequest.PutMapping(summary = "更新", path = MappingConstant.UPDATE)
    public Object update(@RequestBody PrintPrinter payload) {
        printPrinterService.updateById(payload);
        return "updated successfully";
    }
}
