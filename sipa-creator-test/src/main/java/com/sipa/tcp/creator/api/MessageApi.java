package com.sipa.tcp.creator.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sipa.boot.core.allinone.SipaRequest;
import com.sipa.boot.feign.allinone.SipaApi;
import com.sipa.tcp.creator.entity.Message;

/**
 * @author caszhou
 * @date 2023-06-14
 */
@SipaApi(tag = "Message控制器", name = "message-service-server", contextId = "messageApi",
    path = MessageApi.MappingConstant.PATH)
public interface MessageApi {
    interface MappingConstant {
        String PATH = "/message";

        String PAGE = "";

        String DETAIL = "/{id}";

        String CREATE = "";

        String DELETE = "/{id}";

        String UPDATE = "";
    }

    @SipaRequest.GetMapping(summary = "分页查询", path = MappingConstant.PAGE)
    Page<Message> list(@RequestParam(required = false) Integer current,
        @RequestParam(required = false) Integer pageSize);

    @SipaRequest.GetMapping(summary = "详情", path = MappingConstant.DETAIL)
    Message getById(@PathVariable("id") String id);

    @SipaRequest.PostMapping(summary = "新增", path = MappingConstant.CREATE)
    Object create(@RequestBody Message payload);

    @SipaRequest.DeleteMapping(summary = "删除", path = MappingConstant.DELETE)
    Object delete(@PathVariable("id") String id);

    @SipaRequest.PutMapping(summary = "更新", path = MappingConstant.UPDATE)
    Object update(@RequestBody Message payload);
}
