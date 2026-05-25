`v1.0-alpha.0.5`

### General
* move some packages into a separate standalone modules.  
  Making it possible to selectively shade them into your project when needed.
  * identifiers
  * wrappers
  * scheduler

### Core
* refactor: rename ModWrapper to ModIdentifier
* build: no longer shade core deps & shade in platform modules instead

### Wrappers
* feat: add ItemStackRef.toTemplate for compatibility with ItemStackTemplates
* feat: add ItemStackTemplate wrapper
* feat: add separate util functions to convert to Minecraft types (without wrapper)
* remove entity wrapper functions and properties
* refactor: rename ItemStackSnapshot create function

## Scheduler
* fix: missing logger
* feat: TaskOwner for scafall independent task owner support