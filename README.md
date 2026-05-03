## Scafall
Scafall assists in the creation of multi-platform Minecraft APIs.

The main goal of scafall is to provide a lightweight solution for creating cross-platform APIs.  
Another goal is to provide wrappers for the most used parts of each platform, like Tasks & Scheduling and Adventure Utils for convenience.  

### Proxy Interfaces
Scafall avoids wrapping every niche Platform/Minecraft by providing simple wrapper interfaces,  
that are used as a kind of proxy.  

1. The API is constructed by accepting proxy interface arguments (e.g. `ScafallItemStack`).   
2. The implementation can `unwrap()` the proxy interfaces to a native Minecraft `ItemStack` and work with that object instead.
3. Mods/Plugins on other platforms `wrap()` their platform specific instances (e.g. `org.bukkit.inventory.ItemStack`) and use those in the API.

These interfaces are lightweight and provide access to very few properties of the wrapped object.
The idea is to purely use them as a transfer between Third-Party Mods/Plugins, API, and Implementation:
![Wrapping and Unwrapping: Third-Party Mods/Plugins <--unwrap/wrap--> API <--wrap/unwrap--> Implementation](https://github.com/user-attachments/assets/d752060d-2e59-4d30-a756-a0540923c005)

Scafall uses Minecraft as the ground truth, so proxies always wrap the native Minecraft types.  
When wrapping other platform types, those are first converted into the native MC types.  
(they are usually wrappers themselves, in which case it just uses that wrapped value)

#### Limitations & Memory Concerns
The same limitation applies to wrappers as they do to native types. That means one should never
attempt to store a wrapper in a list or map or anything else, as that would lead to memory leaks.
They are intended to be used only as parameters or return values to/from API calls.

## Work in Progress 
This is still a work-in-progress project, so don't expect anything to work seamlessly.
There is no guarantee of API compatibility yet, but it will be provided eventually.
The current API is still unstable and **likely to change at anytime without notice!**

## Additional Modules/Libraries
The goal of this project is to really just provide the basic scaffolding and assist in the development process.  
Additional Modules/Libraries may use scafall to provide more niche APIs and Utils.  
* [viewportl](https://github.com/WolfyScript/viewportl) - Minecraft UI Framework based on Compose Runtime, for powerful, reactive and efficient UIs.  
  (Uses scafall to make it possible to create cross-platform UIs)
