package com.example.ticket.api;

import com.example.ticket.exception.Code;
import com.example.ticket.exception.GeneralException;
import org.springframework.stereotype.Component;

@Component
public class ErrorTest {
    
    public void intervalError(){
        Boolean condition=true;
        if (condition){
            throw new GeneralException(Code.INTERNAL_ERROR,"Interval Error PingPong");
        }
    }
}
