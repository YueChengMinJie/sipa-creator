package com.sipa.tcp.creator.web;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sipa.boot.core.pojo.form.IdsForm;
import com.sipa.boot.mvc.allinone.SipaRest;
import com.sipa.tcp.creator.api.PrintTemplateApi;
import com.sipa.tcp.creator.form.PrintTemplateForm;
import com.sipa.tcp.creator.form.PrintTemplatePageForm;
import com.sipa.tcp.creator.service.PrintTemplateService;
import com.sipa.tcp.creator.vo.PrintTemplateVo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author caszhou
 * @date 2024-07-24
 */
@Slf4j
@SipaRest(tag = "打印模版", path = PrintTemplateApi.MappingConstant.PATH)
public class PrintTemplateController implements PrintTemplateApi {
    @Resource
    private PrintTemplateService printTemplateService;

    @Override
    public Page<PrintTemplateVo> list(PrintTemplatePageForm form) {
        return null;
    }

    @Override
    public PrintTemplateVo getById(String id) {
        return null;
    }

    @Override
    public Object create(PrintTemplateForm form) {
        return null;
    }

    @Override
    public Object delete(IdsForm form) {
        return null;
    }

    @Override
    public Object update(PrintTemplateForm form) {
        return null;
    }
}
