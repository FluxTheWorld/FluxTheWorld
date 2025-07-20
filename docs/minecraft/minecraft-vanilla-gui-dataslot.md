# Minecraft Vanilla GUI and DataSlot Systems

This document provides comprehensive documentation for Minecraft's default, unmodded Graphical User Interface (GUI) and DataSlot synchronization systems.

## 1. GUI Core Components

Minecraft's GUI system is built upon a hierarchy of classes that manage rendering, input, and component layout.

### 1.1. `Screen` (`net/minecraft/client/gui/screens/Screen.java`)

The foundational class for all GUI screens in Minecraft. It serves as the base for main menus, in-game overlays, and container screens.

*   **Purpose**: Manages the overall screen rendering, input handling, and the lifecycle of GUI elements.
*   **Lifecycle**:
    *   **Creation**: A `Screen` instance is created when `Minecraft.setScreen()` is called. The constructor takes a `Component` title.
    *   **Initialization (`init(Minecraft minecraft, int width, int height)`)**: Called once when the screen is set. It initializes `minecraft` instance, `font` renderer, screen `width` and `height`. It also calls the protected `init()` method (which subclasses override) and `setInitialFocus()`.
    *   **Rendering (`render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)`)**: Called every frame. It handles rendering the background (panorama or blurred background), and then iterates through its `renderables` list to draw all registered GUI components.
    *   **Tick (`tick()`)**: Called every game tick. Subclasses can override this for per-tick logic.
    *   **Input Handling**: Overrides methods from `AbstractContainerEventHandler` to process mouse clicks (`mouseClicked`), mouse releases (`mouseReleased`), mouse drags (`mouseDragged`), and keyboard presses (`keyPressed`, `charTyped`). These events are typically passed down to child `GuiEventListener`s.
    *   **Resizing (`resize(Minecraft minecraft, int width, int height)`)**: Called when the window size changes. It updates the screen dimensions and calls `repositionElements()` (which in turn calls `rebuildWidgets()`) to re-layout and re-initialize widgets.
    *   **Destruction (`removed()`, `onClose()`)**: `removed()` is called when the screen is removed from the GUI stack. `onClose()` typically calls `minecraft.popGuiLayer()` to close the current screen.
*   **Key Features**:
    *   Manages `children` (list of `GuiEventListener`s) and `renderables` (list of `Renderable`s).
    *   Provides utility methods for handling keyboard modifiers (`hasShiftDown()`, `hasControlDown()`, `hasAltDown()`) and clipboard operations (`isCut()`, `isCopy()`, `isPaste()`, `isSelectAll()`).
    *   Supports accessibility features through `NarratableEntry` and `ScreenNarrationCollector`.

### 1.2. `AbstractContainerScreen` (`net/minecraft/client/gui/screens/inventory/AbstractContainerScreen.java`)

A specialized `Screen` subclass designed for GUIs that interact with `AbstractContainerMenu` (i.e., inventory-based screens like chests, furnaces, crafting tables).

*   **Purpose**: Provides the common framework for rendering inventory backgrounds, item slots, and handling complex inventory manipulation logic on the client side.
*   **Inheritance**: Extends `Screen` and implements `MenuAccess<T>`.
*   **Key Properties**:
    *   `imageWidth`, `imageHeight`: Dimensions of the GUI background texture.
    *   `leftPos`, `topPos`: Calculated top-left corner of the GUI background on the screen.
    *   `menu`: A reference to the `AbstractContainerMenu` instance that this screen represents.
    *   `hoveredSlot`: The `Slot` currently under the mouse cursor.
*   **Core Functionality**:
    *   **Rendering**: Overrides `render()` to draw the inventory background (`renderBg()`), then iterates through `menu.slots` to render each `Slot` and its contents (`renderSlot()`, `renderSlotContents()`). It also handles rendering the item stack currently "carried" by the mouse cursor.
    *   **Inventory Interaction**: Implements complex logic for various click types (`ClickType.PICKUP`, `QUICK_MOVE`, `THROW`, `SWAP`, `CLONE`, `QUICK_CRAFT`, `PICKUP_ALL`).
    *   `slotClicked(Slot slot, int slotId, int mouseButton, ClickType type)`: The central method that translates client-side mouse actions into calls to `minecraft.gameMode.handleInventoryMouseClick()`, sending the interaction to the server.
    *   Handles "quick crafting" (dragging items across multiple slots to distribute them) and "quick move" (shift-clicking to move items between inventories).
    *   `removed()`: Ensures that the `menu.removed()` method is called on the server when the screen is closed, handling any items left on the cursor.

### 1.3. `AbstractWidget` (`net/minecraft/client/gui/components/AbstractWidget.java`)

The base class for all interactive GUI components (buttons, text fields, sliders, etc.).

*   **Purpose**: Provides common properties and behaviors for interactive elements, including position, size, visibility, active state, focus, and basic input handling.
*   **Inheritance**: Implements `Renderable`, `GuiEventListener`, `LayoutElement`, and `NarratableEntry`.
*   **Key Properties**:
    *   `x`, `y`, `width`, `height`: Position and dimensions.
    *   `message`: The `Component` displayed on the widget.
    *   `active`: If `true`, the widget can be interacted with.
    *   `visible`: If `true`, the widget is rendered.
    *   `isHovered`, `focused`: State flags for rendering and input.
*   **Core Functionality**:
    *   **Rendering (`render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)`)**: Calls `renderWidget()` (abstract, implemented by subclasses) to draw the widget. Also handles hover state and tooltip refreshing.
    *   **Input Handling**: Provides default implementations for `mouseClicked`, `mouseReleased`, `mouseDragged`, `keyPressed`, `charTyped`. These methods check `active` and `visible` states and delegate to more specific `onClick()`, `onRelease()`, `onDrag()` methods.
    *   **Focus Navigation**: Implements `nextFocusPath()` for keyboard navigation between widgets.
    *   **Sound**: `playDownSound()` plays a click sound.
    *   **Accessibility**: Integrates with Minecraft's narration system via `NarratableEntry`.

### 1.4. Common Widget Implementations

*   **`Button` (`net/minecraft/client/gui/components/Button.java`)**: A basic clickable button. It uses an `OnPress` functional interface to define its action.
*   **`EditBox` (`net/minecraft/client/gui/components/EditBox.java`)**: A single-line text input field. Manages text content, cursor position, selection, and handles text manipulation (typing, deleting, copying, pasting). It uses a `Consumer<String>` `responder` to notify when its value changes.

## 2. DataSlot System (Container Data Synchronization)

Minecraft's vanilla system for synchronizing simple integer-based data (like furnace progress, energy levels, or fluid amounts in vanilla blocks) within `AbstractContainerMenu`s.

### 2.1. `ContainerData` (`net/minecraft/world/inventory/ContainerData.java`)

An interface that defines a contract for an array-like structure of integer values that can be synchronized.

*   **Purpose**: To provide a simple, indexed way to expose integer data from a server-side container to its client-side menu counterpart.
*   **Methods**:
    *   `int get(int index)`: Retrieves the integer value at a given index.
    *   `void set(int index, int value)`: Sets the integer value at a given index.
    *   `int getCount()`: Returns the total number of integer values in the data array.

### 2.2. `DataSlot` (`net/minecraft/world/inventory/DataSlot.java`)

An abstract class that wraps a single integer value and provides a mechanism to track if its value has changed.

*   **Purpose**: Used by `AbstractContainerMenu` to monitor individual integer values for changes and determine if they need to be synchronized.
*   **Key Method**:
    *   `boolean checkAndClearUpdateFlag()`: This is crucial for synchronization. It compares the current value (`get()`) with its `prevValue`. If they differ, it updates `prevValue` to the current value and returns `true`, indicating a change. Otherwise, it returns `false`.
*   **Factory Methods**:
    *   `static DataSlot forContainer(final ContainerData data, final int idx)`: Creates a `DataSlot` that reads from and writes to a specific index within a `ContainerData` instance.
    *   `static DataSlot shared(final int[] data, final int idx)`: Creates a `DataSlot` that reads from and writes to a specific index within a shared `int[]` array.
    *   `static DataSlot standalone()`: Creates a `DataSlot` that manages its own internal integer value, useful for temporary or non-backed data.

### 2.3. `SimpleContainerData` (`net/minecraft/world/inventory/SimpleContainerData.java`)

A basic concrete implementation of the `ContainerData` interface.

*   **Purpose**: Provides a straightforward `int[]` array to back the `ContainerData` interface, making it easy to create fixed-size integer data arrays for synchronization.
*   **Implementation**: Stores data in a `private final int[] ints;`.

## 3. DataSlot Synchronization Flow

The synchronization of `DataSlot`s occurs within the `AbstractContainerMenu` and is managed by the server.

1.  **Registration**: In the constructor of an `AbstractContainerMenu` subclass, `DataSlot` instances are added using `addDataSlot()` or `addDataSlots(ContainerData array)`. Each `DataSlot` is assigned an index.
2.  **Server-Side Update (`AbstractContainerMenu.broadcastChanges()`)**:
    *   Periodically (e.g., every game tick), the server calls `broadcastChanges()` on the `AbstractContainerMenu` instance.
    *   This method iterates through all registered `DataSlot`s.
    *   For each `DataSlot`, it calls `checkAndClearUpdateFlag()`. If this returns `true` (meaning the value has changed), the new value is sent to all `ContainerListener`s (which includes the client-side player's `ServerGamePacketListenerImpl`).
    *   The `ContainerSynchronizer` (if set) is also used to send these changes to the remote client via network packets (e.g., `ClientboundContainerSetDataPacket`).
3.  **Client-Side Reception**:
    *   On the client, the `ClientPacketListener` receives packets related to container data changes.
    *   The `AbstractContainerMenu.setData(int id, int data)` method is called on the client-side menu instance, which updates the corresponding `DataSlot`'s value.
    *   Client-side GUI elements (e.g., `AbstractContainerScreen`s) can then read these updated `DataSlot` values to refresh their display (e.g., update a progress bar).

## 4. Client-Side vs. Server-Side Responsibilities

*   **Client-Side**:
    *   **GUI Rendering**: `Screen` and `AbstractContainerScreen` are responsible for drawing all visual elements.
    *   **User Input**: `AbstractWidget`s and `Screen`s capture and process player interactions.
    *   **Local Data Mirroring**: Maintains a local copy of `DataSlot` values and inventory contents, updated by server packets.
    *   **Sending Actions**: Translates user interactions (e.g., slot clicks, button presses) into network packets sent to the server.
*   **Server-Side**:
    *   **Authoritative Game Logic**: Manages the true state of inventories, block entities, and all game mechanics.
    *   **Data Synchronization**: Tracks changes in `DataSlot`s and inventory contents, and dispatches update packets to clients.
    *   **Processing Client Actions**: Receives client packets, validates actions, and applies changes to the authoritative game state.

## 5. Reference

For more detailed information on Minecraft's vanilla GUI and DataSlot systems, refer to the decompiled Minecraft source code located at `@/refs/minecraft-sources`, specifically within the `net.minecraft.client.gui` and `net.minecraft.world.inventory` packages.
