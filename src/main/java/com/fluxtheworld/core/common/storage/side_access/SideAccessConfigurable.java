package com.fluxtheworld.core.common.storage.side_access;

import net.minecraft.core.Direction;

public interface SideAccessConfigurable {
    SideAccessMode getMode(Direction side);
    boolean isModeSupported(Direction side, SideAccessMode state);
}