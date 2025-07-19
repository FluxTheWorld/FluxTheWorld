package com.fluxtheworld.core.common.storage.side_access;

import net.minecraft.core.Direction;

public interface MutableSideAccessConfigurable extends SideAccessConfigurable {
    void setMode(Direction side, SideAccessMode mode);
    void setNextMode(Direction side);
}