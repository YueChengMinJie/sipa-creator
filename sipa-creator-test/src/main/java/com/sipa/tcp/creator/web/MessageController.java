package com.sipa.tcp.creator.web;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sipa.boot.core.allinone.SipaRequest;
import com.sipa.boot.mvc.allinone.SipaRest;
import com.sipa.tcp.creator.api.MessageApi;
import com.sipa.tcp.creator.entity.Message;
import com.sipa.tcp.creator.service.MessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author caszhou
 * @date 2023-06-14
 */
@Slf4j
@SipaRest(tag = "Message控制器", path = MessageApi.MappingConstant.PATH)
public class MessageController implements MessageApi {
    @Resource
    private MessageService messageService;

    @Override
    @SipaRequest.GetMapping(summary = "分页查询", path = MappingConstant.PAGE)
    public Page<Message> list(@RequestParam(required = false) Integer current,
        @RequestParam(required = false) Integer pageSize) {
        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        return messageService.page(new Page<>(current, pageSize));
    }

    @Override
    @SipaRequest.GetMapping(summary = "详情", path = MappingConstant.DETAIL)
    public Message getById(@PathVariable("id") String id) {
        return messageService.getById(id);
    }

    @Override
    @SipaRequest.PostMapping(summary = "新增", path = MappingConstant.CREATE)
    public Object create(@RequestBody Message payload) {
        messageService.save(payload);
        return "created successfully";
    }

    @Override
    @SipaRequest.DeleteMapping(summary = "删除", path = MappingConstant.DELETE)
    public Object delete(@PathVariable("id") String id) {
        messageService.removeById(id);
        return "deleted successfully";
    }

    @Override
    @SipaRequest.PutMapping(summary = "更新", path = MappingConstant.UPDATE)
    public Object update(@RequestBody Message payload) {
        messageService.updateById(payload);
        return "updated successfully";
    }
}
