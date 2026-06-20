`v1.0-alpha.1.0`

### General
* update to Minecraft 26.2
* move adventure utilities into a separate standalone module.  
  Making it possible to selectively shade them into your project when needed.

### Identifiers
* add `Key.parse` function with default namespace

### Core
* Removed Adventure Audiences abstraction

### Spigot
Due to PaperMC discontinuing maintenance and support for Adventure on the Spigot platform.

> We recommend that users of these libraries update to modern platforms that natively support Adventure
> 
> *See [adventure-platform GitHub](https://github.com/PaperMC/adventure-platform#adventure-platform)*

This update removes Adventure support, and the Adventure utils are now in a separate module
but no longer shaded into the scafall spigot build.

**Important:** Because of those changes, I am considering dropping support for Spigot/Bukkit in the near future.  
I'm still evaluating if there are any other ways, but I'm unable to maintain a custom adventure-platform implementation. 
CustomCrafting and especially viewportl make heavy use of Adventure, so yeah... not looking good for spigot so far. 
