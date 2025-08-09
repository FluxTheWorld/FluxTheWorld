# Unified Storage System Implementation

This document provides the complete implementation for the unified storage system that handles both Items and Fluids using the adapter pattern.

## File Structure

```
src/main/java/com/fluxtheworld/core/storage/unified/
├── UnifiedStorage.java
├── StackAdapter.java
├── StorageFactory.java
├── adapters/
│   ├── ItemStackAdapter.java
│   └── FluidStackAdapter.java
├── wrappers/
│   ├── ItemHandlerWrapper.java
│   ├── FluidHandlerWrapper.java
│   ├── PipeItemHandlerWrapper.java
│   ├── PipeFluidHandlerWrapper.java
│   ├── MenuItemHandlerWrapper.java
│   └── MenuFluidHandlerWrapper.java
└── package-info.java
```

## 1. Enhanced StackAdapter Interface

**File: `src/main/java/com/fluxtheworld/core/storage/unified/StackAdapter.java`**

```java
package com.fluxtheworld.core.storage.unified;

import java.util.Optional;
import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.side_access.SideAccessConfig;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

/**
 * Adapter interface for handling different stack types (Items, Fluids, etc.)
 * All type-specific operations are delegated to implementations of this interface.
 */
public interface StackAdapter<T> {
    
    // Basic stack operations
    T getEmpty();
    boolean isEmpty(T stack);
    int getAmount(T stack);
    T copy(T stack);
    T copyWithAmount(T stack, int amount);
    void grow(T stack, int amount);
    void shrink(T stack, int amount);
    boolean isSameContent(T stack1, T stack2);
    
    // Handler creation - returns appropriate NeoForge handler
    Object createHandler(UnifiedStorage<T> storage);
    Object createPipeHandler(UnifiedStorage<T> storage, SideAccessConfig sideAccess, @Nullable Direction side);
    Object createMenuHandler(UnifiedStorage<T> storage);
    
    // Serialization support
    CompoundTag serializeStack(T stack, HolderLookup.Provider provider);
    Optional<T> deserializeStack(CompoundTag tag, HolderLookup.Provider provider);
    
    // Stack limits and validation
    int getMaxStackSize(T stack);
    boolean canStackWith(T stack1, T stack2);
}
```

## 2. UnifiedStorage Class

**File: `src/main/java/com/fluxtheworld/core/storage/unified/UnifiedStorage.java`**

```java
package com.fluxtheworld.core.storage.unified;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessTag;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Unified storage implementation that can handle any stack type through adapters.
 * This single class replaces separate ItemStorage and FluidStorage implementations.
 */
public class UnifiedStorage<T> implements INBTSerializable<CompoundTag> {
    
    private final List<T> stacks;
    private final StackAdapter<T> adapter;
    private final SlotAccessConfig<T> slotAccess;
    private final @Nullable ChangeListener changeListener;
    
    public UnifiedStorage(StackAdapter<T> adapter, SlotAccessConfig<T> slotAccess) {
        this(adapter, slotAccess, null);
    }
    
    public UnifiedStorage(StackAdapter<T> adapter, SlotAccessConfig<T> slotAccess, 
                         @Nullable ChangeListener changeListener) {
        this.adapter = adapter;
        this.slotAccess = slotAccess;
        this.changeListener = changeListener;
        this.stacks = new ArrayList<>(Collections.nCopies(slotAccess.getSlotCount(), adapter.getEmpty()));
    }
    
    // Core storage operations
    
    public int getSlotCount() {
        return slotAccess.getSlotCount();
    }
    
    public T getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return adapter.copy(stacks.get(slot));
    }
    
    public T insert(int slot, T stack, boolean simulate) {
        if (adapter.isEmpty(stack)) {
            return adapter.getEmpty();
        }
        
        if (!isValid(slot, stack) || !canInsert(slot)) {
            return stack;
        }
        
        validateSlotIndex(slot);
        T existing = stacks.get(slot);
        int limit = getStackLimit(slot, stack);
        
        if (!adapter.isEmpty(existing)) {
            if (!adapter.isSameContent(stack, existing)) {
                return stack;
            }
            limit -= adapter.getAmount(existing);
        }
        
        if (limit <= 0) {
            return stack;
        }
        
        boolean reachedLimit = adapter.getAmount(stack) > limit;
        
        if (!simulate) {
            if (adapter.isEmpty(existing)) {
                stacks.set(slot, reachedLimit ? 
                    adapter.copyWithAmount(stack, limit) : 
                    adapter.copy(stack));
            } else {
                adapter.grow(existing, reachedLimit ? limit : adapter.getAmount(stack));
            }
            onContentsChanged(slot);
        }
        
        return reachedLimit ? 
            adapter.copyWithAmount(stack, adapter.getAmount(stack) - limit) : 
            adapter.getEmpty();
    }
    
    public T extract(int slot, int amount, boolean simulate) {
        if (amount == 0 || !canExtract(slot)) {
            return adapter.getEmpty();
        }
        
        validateSlotIndex(slot);
        T existing = stacks.get(slot);
        
        if (adapter.isEmpty(existing)) {
            return adapter.getEmpty();
        }
        
        int toExtract = Math.min(amount, adapter.getAmount(existing));
        
        if (adapter.getAmount(existing) <= toExtract) {
            if (!simulate) {
                stacks.set(slot, adapter.getEmpty());
                onContentsChanged(slot);
                return existing;
            } else {
                return adapter.copy(existing);
            }
        } else {
            if (!simulate) {
                adapter.shrink(existing, toExtract);
                onContentsChanged(slot);
            }
            return adapter.copyWithAmount(existing, toExtract);
        }
    }
    
    protected void setStackInSlot(int slot, T stack) {
        validateSlotIndex(slot);
        stacks.set(slot, stack);
        onContentsChanged(slot);
    }
    
    // Handler creation - delegates to adapter
    
    public Object getHandler() {
        return adapter.createHandler(this);
    }
    
    public Object getForPipe(SideAccessConfig sideAccess, @Nullable Direction side) {
        return adapter.createPipeHandler(this, sideAccess, side);
    }
    
    public Object getForMenu() {
        return adapter.createMenuHandler(this);
    }
    
    // Utility methods for tagged operations
    
    public T insert(SlotAccessTag tag, T stack, boolean simulate) {
        T remaining = stack;
        
        for (int slot : slotAccess.getTaggedSlots(tag)) {
            if (adapter.isEmpty(remaining)) {
                break;
            }
            remaining = insert(slot, remaining, simulate);
        }
        
        return remaining;
    }
    
    public T extract(SlotAccessTag tag, T stack, boolean simulate) {
        T extracted = adapter.copyWithAmount(stack, 0);
        
        for (int slot : slotAccess.getTaggedSlots(tag)) {
            if (adapter.getAmount(extracted) == adapter.getAmount(stack)) {
                break;
            }
            
            if (!adapter.isSameContent(getStackInSlot(slot), stack)) {
                continue;
            }
            
            T found = extract(slot, adapter.getAmount(stack) - adapter.getAmount(extracted), simulate);
            adapter.grow(extracted, adapter.getAmount(found));
        }
        
        return extracted;
    }
    
    // Named slot operations
    
    public int getSlotIndex(String name) {
        return slotAccess.getSlotIndex(name);
    }
    
    public T getStackInSlot(String name) {
        return getStackInSlot(getSlotIndex(name));
    }
    
    public T insert(String name, T stack, boolean simulate) {
        return insert(getSlotIndex(name), stack, simulate);
    }
    
    public T extract(String name, int amount, boolean simulate) {
        return extract(getSlotIndex(name), amount, simulate);
    }
    
    // Access control
    
    public boolean canInsert(int slot) {
        return slotAccess.canPipeInsert(slot); // Default to pipe access
    }
    
    public boolean canExtract(int slot) {
        return slotAccess.canPipeExtract(slot); // Default to pipe access
    }
    
    public boolean isValid(int slot, T stack) {
        return slotAccess.isValid(slot, stack);
    }
    
    public int getSlotCapacity(int slot) {
        return slotAccess.getSlotCapacity(slot);
    }
    
    private int getStackLimit(int slot, T stack) {
        return Math.min(getSlotCapacity(slot), adapter.getMaxStackSize(stack));
    }
    
    // Utility methods
    
    private void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= getSlotCount()) {
            throw new IllegalArgumentException("Slot " + slot + " not in valid range - [0," + getSlotCount() + ")");
        }
    }
    
    protected void onContentsChanged(int slot) {
        if (changeListener != null) {
            changeListener.onStorageChanged(slot);
        }
    }
    
    // Serialization - delegates to adapter
    
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        ListTag items = new ListTag();
        for (int i = 0; i < stacks.size(); i++) {
            T stack = stacks.get(i);
            if (!adapter.isEmpty(stack)) {
                CompoundTag itemTag = adapter.serializeStack(stack, provider);
                itemTag.putInt("Slot", i);
                items.add(itemTag);
            }
        }
        
        CompoundTag tag = new CompoundTag();
        tag.put("Items", items);
        return tag;
    }
    
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < items.size(); i++) {
            CompoundTag item = items.getCompound(i);
            int slot = item.getInt("Slot");
            validateSlotIndex(slot);
            
            adapter.deserializeStack(item, provider).ifPresent(stack -> 
                stacks.set(slot, stack));
        }
    }
    
    // Getters for internal access
    
    public StackAdapter<T> getAdapter() {
        return adapter;
    }
    
    public SlotAccessConfig<T> getSlotAccess() {
        return slotAccess;
    }
    
    // Change listener interface
    
    public interface ChangeListener {
        void onStorageChanged(int slot);
    }
}
```

## 3. StorageFactory

**File: `src/main/java/com/fluxtheworld/core/storage/unified/StorageFactory.java`**

```java
package com.fluxtheworld.core.storage.unified;

import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;
import com.fluxtheworld.core.storage.slot_access.FluidSlotAccessConfig;
import com.fluxtheworld.core.storage.unified.adapters.ItemStackAdapter;
import com.fluxtheworld.core.storage.unified.adapters.FluidStackAdapter;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Factory class for creating type-safe unified storage instances.
 */
public class StorageFactory {
    
    // Item storage creation
    
    public static UnifiedStorage<ItemStack> createItemStorage(ItemSlotAccessConfig slotAccess) {
        return new UnifiedStorage<>(ItemStackAdapter.INSTANCE, slotAccess);
    }
    
    public static UnifiedStorage<ItemStack> createItemStorage(ItemSlotAccessConfig slotAccess, 
                                                             UnifiedStorage.ChangeListener listener) {
        return new UnifiedStorage<>(ItemStackAdapter.INSTANCE, slotAccess, listener);
    }
    
    // Fluid storage creation
    
    public static UnifiedStorage<FluidStack> createFluidStorage(FluidSlotAccessConfig slotAccess) {
        return new UnifiedStorage<>(FluidStackAdapter.INSTANCE, slotAccess);
    }
    
    public static UnifiedStorage<FluidStack> createFluidStorage(FluidSlotAccessConfig slotAccess, 
                                                               UnifiedStorage.ChangeListener listener) {
        return new UnifiedStorage<>(FluidStackAdapter.INSTANCE, slotAccess, listener);
    }
}
```

## 4. ItemStackAdapter Implementation

**File: `src/main/java/com/fluxtheworld/core/storage/unified/adapters/ItemStackAdapter.java`**

```java
package com.fluxtheworld.core.storage.unified.adapters;

import java.util.Optional;
import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.unified.StackAdapter;
import com.fluxtheworld.core.storage.unified.UnifiedStorage;
import com.fluxtheworld.core.storage.unified.wrappers.ItemHandlerWrapper;
import com.fluxtheworld.core.storage.unified.wrappers.PipeItemHandlerWrapper;
import com.fluxtheworld.core.storage.unified.wrappers.MenuItemHandlerWrapper;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemStackAdapter implements StackAdapter<ItemStack> {
    
    public static final ItemStackAdapter INSTANCE = new ItemStackAdapter();
    
    private ItemStackAdapter() {
    }
    
    @Override
    public ItemStack getEmpty() {
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean isEmpty(ItemStack stack) {
        return stack.isEmpty();
    }
    
    @Override
    public int getAmount(ItemStack stack) {
        return stack.getCount();
    }
    
    @Override
    public ItemStack copy(ItemStack stack) {
        return stack.copy();
    }
    
    @Override
    public ItemStack copyWithAmount(ItemStack stack, int amount) {
        return stack.copyWithCount(amount);
    }
    
    @Override
    public void grow(ItemStack stack, int amount) {
        stack.grow(amount);
    }
    
    @Override
    public void shrink(ItemStack stack, int amount) {
        stack.shrink(amount);
    }
    
    @Override
    public boolean isSameContent(ItemStack stack1, ItemStack stack2) {
        return ItemStack.isSameItemSameComponents(stack1, stack2);
    }
    
    @Override
    public Object createHandler(UnifiedStorage<ItemStack> storage) {
        return new ItemHandlerWrapper(storage);
    }
    
    @Override
    public Object createPipeHandler(UnifiedStorage<ItemStack> storage, SideAccessConfig sideAccess, @Nullable Direction side) {
        return new PipeItemHandlerWrapper(storage, sideAccess, side);
    }
    
    @Override
    public Object createMenuHandler(UnifiedStorage<ItemStack> storage) {
        return new MenuItemHandlerWrapper(storage);
    }
    
    @Override
    public CompoundTag serializeStack(ItemStack stack, HolderLookup.Provider provider) {
        return stack.save(provider, new CompoundTag());
    }
    
    @Override
    public Optional<ItemStack> deserializeStack(CompoundTag tag, HolderLookup.Provider provider) {
        return ItemStack.parse(provider, tag);
    }
    
    @Override
    public int getMaxStackSize(ItemStack stack) {
        return stack.getMaxStackSize();
    }
    
    @Override
    public boolean canStackWith(ItemStack stack1, ItemStack stack2) {
        return ItemStack.isSameItemSameComponents(stack1, stack2);
    }
}
```

## 5. FluidStackAdapter Implementation

**File: `src/main/java/com/fluxtheworld/core/storage/unified/adapters/FluidStackAdapter.java`**

```java
package com.fluxtheworld.core.storage.unified.adapters;

import java.util.Optional;
import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.unified.StackAdapter;
import com.fluxtheworld.core.storage.unified.UnifiedStorage;
import com.fluxtheworld.core.storage.unified.wrappers.FluidHandlerWrapper;
import com.fluxtheworld.core.storage.unified.wrappers.PipeFluidHandlerWrapper;
import com.fluxtheworld.core.storage.unified.wrappers.MenuFluidHandlerWrapper;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidStackAdapter implements StackAdapter<FluidStack> {
    
    public static final FluidStackAdapter INSTANCE = new FluidStackAdapter();
    
    private FluidStackAdapter() {
    }
    
    @Override
    public FluidStack getEmpty() {
        return FluidStack.EMPTY;
    }
    
    @Override
    public boolean isEmpty(FluidStack stack) {
        return stack.isEmpty();
    }
    
    @Override
    public int getAmount(FluidStack stack) {
        return stack.getAmount();
    }
    
    @Override
    public FluidStack copy(FluidStack stack) {
        return stack.copy();
    }
    
    @Override
    public FluidStack copyWithAmount(FluidStack stack, int amount) {
        return stack.copyWithAmount(amount);
    }
    
    @Override
    public void grow(FluidStack stack, int amount) {
        stack.grow(amount);
    }
    
    @Override
    public void shrink(FluidStack stack, int amount) {
        stack.shrink(amount);
    }
    
    @Override
    public boolean isSameContent(FluidStack stack1, FluidStack stack2) {
        return FluidStack.isSameFluidSameComponents(stack1, stack2);
    }
    
    @Override
    public Object createHandler(UnifiedStorage<FluidStack> storage) {
        return new FluidHandlerWrapper(storage);
    }
    
    @Override
    public Object createPipeHandler(UnifiedStorage<FluidStack> storage, SideAccessConfig sideAccess, @Nullable Direction side) {
        return new PipeFluidHandlerWrapper(storage, sideAccess, side);
    }
    
    @Override
    public Object createMenuHandler(UnifiedStorage<FluidStack> storage) {
        return new MenuFluidHandlerWrapper(storage);
    }
    
    @Override
    public CompoundTag serializeStack(FluidStack stack, HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        return stack.save(provider, tag);
    }
    
    @Override
    public Optional<FluidStack> deserializeStack(CompoundTag tag, HolderLookup.Provider provider) {
        return FluidStack.parse(provider, tag);
    }
    
    @Override
    public int getMaxStackSize(FluidStack stack) {
        return Integer.MAX_VALUE; // Fluids don't have inherent stack limits
    }
    
    @Override
    public boolean canStackWith(FluidStack stack1, FluidStack stack2) {
        return FluidStack.isSameFluidSameComponents(stack1, stack2);
    }
}
```

## 6. Handler Wrappers

### ItemHandlerWrapper

**File: `src/main/java/com/fluxtheworld/core/storage/unified/wrappers/ItemHandlerWrapper.java`**

```java
package com.fluxtheworld.core.storage.unified.wrappers;

import com.fluxtheworld.core.storage.unified.UnifiedStorage;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * Basic IItemHandler wrapper for UnifiedStorage<ItemStack>
 */
public class ItemHandlerWrapper implements IItemHandler {
    
    protected final UnifiedStorage<ItemStack> storage;
    
    public ItemHandlerWrapper(UnifiedStorage<ItemStack> storage) {
        this.storage = storage;
    }
    
    @Override
    public int getSlots() {
        return storage.getSlotCount();
    }
    
    @Override
    public ItemStack getStackInSlot(int slot) {
        return storage.getStackInSlot(slot);
    }
    
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return storage.insert(slot, stack, simulate);
    }
    
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return storage.extract(slot, amount, simulate);
    }
    
    @Override
    public int getSlotLimit(int slot) {
        return storage.getSlotCapacity(slot);
    }
    
    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return storage.isValid(slot, stack);
    }
}
```

### PipeItemHandlerWrapper

**File: `src/main/java/com/fluxtheworld/core/storage/unified/wrappers/PipeItemHandlerWrapper.java`**

```java
package com.fluxtheworld.core.storage.unified.wrappers;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.unified.UnifiedStorage;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

/**
 * Pipe-specific IItemHandler wrapper with side access control
 */
public class PipeItemHandlerWrapper extends ItemHandlerWrapper {
    
    private final SideAccessConfig sideAccess;
    private final @Nullable Direction side;
    
    public PipeItemHandlerWrapper(UnifiedStorage<ItemStack> storage, SideAccessConfig sideAccess, @Nullable Direction side) {
        super(storage);
        this.sideAccess = sideAccess;
        this.side = side;
    }
    
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!canInsert(slot)) {
            return stack;
        }
        return super.insertItem(slot, stack, simulate);
    }
    
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!canExtract(slot)) {
            return ItemStack.EMPTY;
        }
        return super.extractItem(slot, amount, simulate);
    }
    
    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return canInsert(slot) && super.isItemValid(slot, stack);
    }
    
    private boolean canInsert(int slot) {
        if (!storage.getSlotAccess().canPipeInsert(slot)) {
            return false;
        }
        
        if (side != null && !sideAccess.getMode(side).canInput()) {
            return false;
        }
        
        return true;
    }
    
    private boolean canExtract(int slot) {
        if (!storage.getSlotAccess().canPipeExtract(slot)) {
            return false;
        }
        
        if (side != null && !sideAccess.getMode(side).canOutput()) {
            return false;
        }
        
        return true;
    }
}
```

### MenuItemHandlerWrapper

**File: `src/main/java/com/fluxtheworld/core/storage/unified/wrappers/MenuItemHandlerWrapper.java`**

```java
package com.fluxtheworld.core.storage.unified.wrappers;

import com.fluxtheworld.core.storage.unified.UnifiedStorage;

import net.minecraft.world.item.ItemStack;

/**
 * Menu-specific IItemHandler wrapper with menu access control
 */
public class MenuItemHandlerWrapper extends ItemHandlerWrapper {
    
    public MenuItemHandlerWrapper(UnifiedStorage<ItemStack> storage) {
        super(storage);
    }
    
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!canInsert(slot)) {
            return stack;
        }
        return super.insertItem(slot, stack, simulate);
    }
    
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!canExtract(slot)) {
            return ItemStack.EMPTY;
        }
        return super.extractItem(slot, amount, simulate);
    }
    
    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return canInsert(slot) && super.isItemValid(slot, stack);
    }
    
    private boolean canInsert(int slot) {
        return storage.getSlotAccess().canMenuInsert(slot);
    }
    
    private boolean canExtract(int slot) {
        return storage.getSlotAccess().canMenuExtract(slot);
    }
}
```

## 7. Fluid Handler Wrappers

### FluidHandlerWrapper

**File: `src/main/java/com/fluxtheworld/core/storage/unified/wrappers/FluidHandlerWrapper.java`**

```java
package com.fluxtheworld.core.storage.unified.wrappers;

import com.fluxtheworld.core.storage.unified.UnifiedStorage;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

/**
 * Basic IFluidHandler wrapper for UnifiedStorage<FluidStack>
 */
public class FluidHandlerWrapper implements IFluidHandler {
    
    protected final UnifiedStorage<FluidStack> storage;
    
    public FluidHandlerWrapper(UnifiedStorage<FluidStack> storage) {
        this.storage = storage;
    }
    
    @Override
    public int getTanks() {
        return storage.getSlotCount();
    }
    
    @Override
    public FluidStack getFluidInTank(int tank) {
        return storage.getStackInSlot(tank);
    }
    
    @Override
    public int getTankCapacity(int tank) {
        return storage.getSlotCapacity(tank);
    }
    
    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return storage.isValid(tank, stack);
    }
    
    @Override
    public int fill(FluidStack resource, FluidAction action) {
        boolean simulate = action == FluidAction.SIMULATE;
        
        for (int i = 0; i < getTanks(); i++) {
            if (storage.isValid(i, resource)) {
                FluidStack remaining = storage.insert(i, resource, simulate);
                return resource.getAmount() - remaining.getAmount();
            }
        }
        
        return 0;
    }
    
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        boolean simulate = action == FluidAction.SIMULATE;
        
        for (int i = 0; i < getTanks(); i++) {
            FluidStack tankFluid = getFluidInTank(i);
            if (tankFluid.isFluidEqual(resource)) {
                return storage.extract(i, resource.getAmount(), simulate);
            }
        }
        
        return FluidStack.EMPTY;
    }
    
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        boolean simulate = action == FluidAction.SIMULATE;
        
        for (int i = 0; i < getTanks(); i++) {
            FluidStack tankFluid = getFluidInTank(i);
            if (!tankFluid.isEmpty()) {
                return storage.extract(i, maxDrain, simulate);
            }
        }
        
        return FluidStack.EMPTY;
    }
}
```

### PipeFluidHandlerWrapper

**File: `src/main/java/com/fluxtheworld/core/storage/unified/wrappers/PipeFluidHandlerWrapper.java`**

```java
package com.fluxtheworld.core.storage.unified.wrappers;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.unified.UnifiedStorage;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Pipe-specific IFluidHandler wrapper with side access control
 */
public class PipeFluidHandlerWrapper extends FluidHandlerWrapper {
    
    private final SideAccessConfig sideAccess;
    private final @Nullable Direction side;
    
    public PipeFluidHandlerWrapper(UnifiedStorage<FluidStack> storage, SideAccessConfig sideAccess, @Nullable Direction side) {
        super(storage);
        this.sideAccess = sideAccess;
        this.side = side;
    }
    
    @Override
    public int fill(FluidStack resource, FluidAction action) {
        boolean simulate = action == FluidAction.SIMULATE;
        
        for (int i = 0; i < getTanks(); i++) {
            if (!canInsert(i)) {
                continue;
            }
            
            if (storage.isValid(i, resource)) {
                FluidStack remaining = storage.insert(i, resource, simulate);
                return resource.getAmount() - remaining.getAmount();
            }
        }
        
        return 0;
    }
    
    @Override