package com.vk.behaviour.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data

public class AckDataMsg implements Serializable {
    private  Integer code = 500;
    private  String  msg ;

    public AckDataMsg(Integer code,String msg){
        this.msg=msg;
        this.code=code;
    }

    public AckDataMsg(String msg){
        this.msg=msg;
    }

    public AckDataMsg(){
    }

    public static AckDataMsg setAckDataMsg(String msg){
        return  new AckDataMsg(msg);
    }

    public static AckDataMsg setAckDataMsg(String msg,Integer code ){
        return  new AckDataMsg(code,msg);
    }

}
