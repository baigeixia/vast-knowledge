package com.vk.dfs.template.impl;

import com.vk.dfs.enums.DFSType;
import com.vk.dfs.model.BaseFileModel;
import com.vk.dfs.template.AbstractDfsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class TxDfsTemplate extends AbstractDfsTemplate {
    @Override
    public DFSType support() {
        return DFSType.TX;
    }

    @Override
    public String uploadFile(BaseFileModel fileModel) {
        return null;
    }

    @Override
    public boolean delete(String fullPath) {
        return false;
    }

    @Override
    public List<byte[]> download(Collection<String> fullPath) {
        return null;
    }

    @Override
    public String getAccessServerAddr() {
        return null;
    }
}
