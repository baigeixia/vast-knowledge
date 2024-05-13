package com.vk.admin.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.util.Date;

/**
 * @version 1.0
 * @description 频道实体类
 * @package com.itheima.admin.pojo
 */
@Data
@Table("ad_channel")
public class AdChannel {

    @Id(keyType = KeyType.Auto)
    private Integer id;

    // 使用Hibernate Validator校验参数
    // Size规则：最小、最长多少位，不满足时则报错，错误信息为message的内容
    @Size(min = 2,max = 10,message = "频道名称的长度必须在2到10之间!")
    private String name;

    private String description;

    private Boolean isDefault;

    private Boolean status;

    private Integer ord;

    private Date createdTime;
}
