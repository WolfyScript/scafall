# Spigot API
This module includes API parts only available on Spigot.

## Wrappers
This module provides some extension functions that help wrap Spigot types to use in the API.

### ItemStacks
```kotlin
// Wrap the spigot item stack
val scafallStack = spigotStack.wrap()
val scafallSnapshot = spigotStack.snapshot()

// Unwrap the stack again
// Either to a Spigot Stack
val unwrappedSpigot = scafallStack.unwrapSpigot()
// or to a Minecraft stack
val unwrappedMinecraft = scafallStack.unwrap()
```

> [!warning]
> 
> Important to note is that in some cases the Spigot ItemStack is not actually bound to a Minecraft stack.  
> When such a stack is wrapped, and unwrapped, changes to the unwrapped stack won't be present on the original stack.
> 
> ```kotlin
> // A stack created this way doesn't have an internal Minecraft stack
> val spigotStack = org.bukkit.ItemStack(Material.STONE)
> val wrapped = spigotStack.wrap()
> 
> // Modifying the unwrapped stack
> val unwrapped = wrapped.unwrapSpigot()
> unwrapped.amount = 2
>
> // same = true, because unwrapped is not linked to spigotStack
> val same = spigotStack.amount != unwrapped.amount
> ```
> 
> When consistent behaviour is required, `snapshot()` should be used instead, because those are immutable.
> 


