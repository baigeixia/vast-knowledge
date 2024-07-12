package com.vk.user.domain.dto;


import lombok.Data;

/**
 * @version 1.0
 * @description 说明
 * @package com.vk.user.dto
 */
@Data
public class ApUserRealnameDto  {
    private Integer status;

    private Long size;

    private Long page;

    public void checkParam() {
        if (this.page == null || this.page < 0) {
            setPage(1L);
        }
        if (this.size == null || this.size < 0 || this.size > 100) {
            setSize(10L);
        }
    }
}
