package com.sipa.tcp.creator.web;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sipa.boot.core.pojo.form.IdsForm;
import com.sipa.boot.mvc.allinone.SipaRest;
import com.sipa.tcp.creator.api.PrintPrinterApi;
import com.sipa.tcp.creator.form.PrintPrinterForm;
import com.sipa.tcp.creator.form.PrintPrinterPageForm;
import com.sipa.tcp.creator.service.PrintPrinterService;
import com.sipa.tcp.creator.vo.PrintPrinterVo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author caszhou
 * @date 2024-07-24
 */
@Slf4j
@SipaRest(tag = PrintPrinterApi.MappingConstant.TAG, path = PrintPrinterApi.MappingConstant.PATH)
public class PrintPrinterController implements PrintPrinterApi {
    @Resource
    private PrintPrinterService printPrinterService;

    @Override
    public Page<PrintPrinterVo> list(PrintPrinterPageForm form) {
        return null;
    }

    @Override
    public PrintPrinterVo getById(String id) {
        return null;
    }

    @Override
    public Object create(PrintPrinterForm form) {
        return null;
    }

    @Override
    public Object delete(IdsForm form) {
        return null;
    }

    @Override
    public Object update(PrintPrinterForm form) {
        return null;
    }
}
