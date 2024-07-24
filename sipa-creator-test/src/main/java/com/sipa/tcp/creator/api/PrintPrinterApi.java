package com.sipa.tcp.creator.api;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sipa.boot.core.allinone.SipaRequest;
import com.sipa.boot.core.pojo.form.IdsForm;
import com.sipa.boot.feign.allinone.SipaApi;
import com.sipa.tcp.creator.form.PrintPrinterForm;
import com.sipa.tcp.creator.form.PrintPrinterPageForm;
import com.sipa.tcp.creator.vo.PrintPrinterVo;

/**
 * @author caszhou
 * @date 2024-07-24
 */
@SipaApi(tag = PrintPrinterApi.MappingConstant.TAG, name = "ams-service", contextId = "printPrinterApi",
    path = PrintPrinterApi.MappingConstant.PATH)
public interface PrintPrinterApi {
    interface MappingConstant {
        String PATH = "/print-printer";

        String TAG = "打印机";

        String PAGE = "page";

        String DETAIL = "/{id}";

        String CREATE = "";

        String DELETE = "";

        String UPDATE = "";
    }

    @SipaRequest.PostMapping(summary = "分页查询", path = MappingConstant.PAGE)
    Page<PrintPrinterVo> list(@RequestBody @Valid PrintPrinterPageForm form);

    @SipaRequest.GetMapping(summary = "详情", path = MappingConstant.DETAIL)
    PrintPrinterVo getById(@PathVariable("id") String id);

    @SipaRequest.PostMapping(summary = "新增", path = MappingConstant.CREATE)
    Object create(@RequestBody @Valid PrintPrinterForm form);

    @SipaRequest.DeleteMapping(summary = "删除", path = MappingConstant.DELETE)
    Object delete(@RequestBody @Valid IdsForm form);

    @SipaRequest.PutMapping(summary = "更新", path = MappingConstant.UPDATE)
    Object update(@RequestBody @Valid PrintPrinterForm form);
}
