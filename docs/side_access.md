# Side Access Package Documentation

This package (`com.fluxtheworld.core.storage.side_access`) provides a flexible and extensible system for managing and configuring side-based access for machines within the FluxTheWorld project. It allows machines to define how their various sides (e.g., top, bottom, front) interact with the environment, specifically regarding the flow of items, fluids, or energy.

## Core Components:

### `SideAccessMode` (Enum)
Defines the different modes of interaction for a machine's side. These modes dictate whether a side can input, output, push, pull, or be disabled.
*   **`NONE`**: No specific configuration; allows external input and output but doesn't pull or push itself.
*   **`PUSH`**: Only pushes outputs; allows both external pulling and the machine pushes itself.
*   **`PULL`**: Only pulls inputs; allows both external pushing and the machine pulling itself.
*   **`DISABLED`**: Disallows any side access for all resources.

### `SideAccessConfigurable` (Interface)
Provides an immutable view of a machine's side access configuration. It defines methods to query the current mode of a side and its capabilities without allowing modification.
*   `SideAccessMode getMode(Direction side)`: Retrieves the current access mode for a given side.
*   `boolean shouldRenderOverlay()`: Suggests whether an overlay should be rendered for this side in the UI.
*   `boolean supportsMode(Direction side, SideAccessMode state)`: Checks if a given mode is supported for a specific side.

### `MutableSideAccessConfigurable` (Interface)
Extends `SideAccessConfigurableView` and adds methods for modifying the side access configuration.
*   `void setMode(Direction side, SideAccessMode mode)`: Sets the access mode for a given side.
*   `void setNextMode(Direction side)`: Cycles to the next available access mode for a given side.

### `SideAccessConfiguration` (Record)
A record that holds the actual mapping of `Direction` to `SideAccessMode`. It provides utility methods for creating copies, empty configurations, and configurations with a default mode.
*   `Map<Direction, SideAccessMode> modes`: The internal map storing the configuration.
*   `static SideAccessConfiguration copyOf(SideAccessConfiguration other)`: Creates a copy of an existing configuration.
*   `static SideAccessConfiguration empty()`: Creates an empty configuration.
*   `static SideAccessConfiguration of(SideAccessMode mode)`: Creates a configuration where all sides have the same default mode.
*   `SideAccessMode getMode(Direction side)`: Retrieves the mode for a specific side, defaulting to `NONE` if not set.
*   `SideAccessConfiguration withMode(Direction side, SideAccessMode mode)`: Returns a new configuration with the specified side's mode updated.

### `MachineSideAccessController` (Class)
An implementation of `MutableSideAccessConfigurable` that manages the side access configuration for a machine. It uses `SideAccessConfiguration` internally and provides the logic for setting and cycling through modes.
*   `private SideAccessConfiguration config`: The internal configuration.
*   `MachineSideAccessController()`: Constructor, initializes with all sides set to `NONE`.
*   Implements all methods from `MutableSideAccessConfigurable`, including the cycling logic for `NONE`, `PULL`, `PUSH`, and `DISABLED` modes.

## Usage:
This package is intended to be used by machine block entities or similar components that require configurable side interactions. By implementing `MutableSideAccessConfigurable` and using `SideAccessConfiguration`, machines can expose their side access settings to users and other systems.
