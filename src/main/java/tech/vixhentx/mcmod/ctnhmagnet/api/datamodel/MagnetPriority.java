package tech.vixhentx.mcmod.ctnhmagnet.api.datamodel;

import org.jetbrains.annotations.Range;

public enum MagnetPriority {
    HIGHEST,    //保留
    STATIC,     //激励源
    DYNAMIC,    //受控源
    PASSIVE;    //被动

    public int priority(@Range(from = 0, to = 15) int subPriority) {
        return this.ordinal() * 16 + subPriority;
    }
    public int priority() {
        return priority(0);
    }
}
