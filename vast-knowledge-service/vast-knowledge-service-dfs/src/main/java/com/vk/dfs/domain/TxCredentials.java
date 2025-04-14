package com.vk.dfs.domain;

import com.tencent.cloud.Credentials;
import lombok.Data;

@Data
public class TxCredentials {
    public Credentials credentials;
    public String expiration;
    public long startTime;
    public long expiredTime;
}
