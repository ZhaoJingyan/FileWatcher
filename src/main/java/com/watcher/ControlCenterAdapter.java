package com.watcher;

import java.util.List;

/**
 * Control Center Adapter.
 * Created by Zhao Jinyan on 2017/1/17.
 */
public interface ControlCenterAdapter {

    void setTableColumn(List<FileInformationColumn> columns);

    void isInitialized();

}
