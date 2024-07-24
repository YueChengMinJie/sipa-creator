package com.sipa.tcp.creator.api;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sipa.boot.core.allinone.SipaRequest;
import com.sipa.boot.core.pojo.form.IdsForm;
import com.sipa.boot.feign.allinone.SipaApi;
import com.sipa.tcp.creator.form.PrintTemplateForm;
import com.sipa.tcp.creator.form.PrintTemplatePageForm;
import com.sipa.tcp.creator.vo.PrintTemplateVo;

/**
 * @author caszhou
 * @date 2024-07-24
 */
@SipaApi(tag = PrintTemplateApi.MappingConstant.TAG, name = "ams-service", contextId = "printTemplateApi",
    path = PrintTemplateApi.MappingConstant.PATH)
public interface PrintTemplateApi {
    interface MappingConstant {
        String PATH = "/print-template";

        String TAG = "打印模版";

        String PAGE = "page";

        String DETAIL = "/{id}";

        String CREATE = "";

        String DELETE = "";

        String UPDATE = "";
    }

    @SipaRequest.PostMapping(summary = "分页查询", path = MappingConstant.PAGE)
    Page<PrintTemplateVo> list(@RequestBody @Valid PrintTemplatePageForm form);

    @SipaRequest.GetMapping(summary = "详情", path = MappingConstant.DETAIL)
    PrintTemplateVo getById(@PathVariable("id") String id);

    @SipaRequest.PostMapping(summary = "新增", path = MappingConstant.CREATE)
    Object create(@RequestBody @Valid PrintTemplateForm form);

    @SipaRequest.DeleteMapping(summary = "删除", path = MappingConstant.DELETE)
    Object delete(@RequestBody @Valid IdsForm form);

    @SipaRequest.PutMapping(summary = "更新", path = MappingConstant.UPDATE)
    Object update(@RequestBody @Valid PrintTemplateForm form);
}
