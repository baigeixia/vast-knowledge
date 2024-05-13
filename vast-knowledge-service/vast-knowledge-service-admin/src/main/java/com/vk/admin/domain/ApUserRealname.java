package com.vk.admin.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description <p>APP实名认证信息 </p>
 *
 * @version 1.0
 * @package
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table("ap_user_realname")
public class ApUserRealname implements Serializable {


    @Id(keyType = KeyType.Auto)
    private Long id;

    private Long userId;


    private String name;


    private String idno;


    private String fontImage;


    private String backImage;


    private String holdImage;


    private String liveImage;


    private Integer status;


    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitedTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;


}
