package com.sipa.tcp.creator.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sipa.tcp.creator.entity.Message;
import com.sipa.tcp.creator.mapper.MessageMapper;
import com.sipa.tcp.creator.service.MessageService;

/**
 * @author caszhou
 * @date 2023-06-14
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
