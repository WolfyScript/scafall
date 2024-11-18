## Spigot Loader Plugin 
This module bundles the `loader-api`, `api`, and `spigot` implementation as an innerjar file.  
This is meant to be used as a standalone plugin, that other plugins can use as a dependency without having to shade it.

### Pros:
* Scafall updates can easily be installed
* Smaller jar file size (compared to platform bundle)
* Easy setup, no shading/relocation, no manual initialization

### Cons:
* Only one version can be present at once
* One extra dependency to install
