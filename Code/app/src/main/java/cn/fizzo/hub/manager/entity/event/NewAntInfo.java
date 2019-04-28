package cn.fizzo.hub.manager.entity.event;

import java.util.List;

import cn.fizzo.hub.manager.entity.model.AntPlusInfo;

public class NewAntInfo {

    public List<AntPlusInfo> ants;

    public NewAntInfo() {
    }

    public NewAntInfo(List<AntPlusInfo> ants) {
        this.ants = ants;
    }
}
