## Spigot Platform Bundle
This module bundles the `loader-api`, `api`, and includes the `spigot` implementation as an innerjar file.  
This is meant to be shaded and relocated with the plugins using it.

### Pros:
* No version conflicts between plugins that use scafall

### Cons:
* Larger jar file size
* All required dependencies must be shaded (or mentioned in the plugin.yml on Spigot), or provided otherwise
* Scafall updates require a rebuild