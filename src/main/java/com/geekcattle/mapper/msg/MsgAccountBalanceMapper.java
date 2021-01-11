package com.geekcattle.mapper.msg;

import com.geekcattle.core.CustomerMapper;
import com.geekcattle.model.msg.MsgAccountBalance;
import org.springframework.stereotype.Service;

@Service
public interface MsgAccountBalanceMapper extends CustomerMapper<MsgAccountBalance> {
}