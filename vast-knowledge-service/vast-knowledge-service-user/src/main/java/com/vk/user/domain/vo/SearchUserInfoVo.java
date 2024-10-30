package com.vk.user.domain.vo;

import com.vk.common.es.domain.UserInfoDocument;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchUserInfoVo extends UserInfoDocument {
    /**
     * 是否关注
     */
    private Integer concerned;
}
