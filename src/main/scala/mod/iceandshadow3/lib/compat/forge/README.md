# .lib.forge

Package for the parts of IaS3 that deal more with Forge than vanilla Minecraft or the rest of IaS3
(e.g. capabilities, event handling).

Files in this package (including subpackages EXCEPT `fish`) may use any otherwise restricted type,
though there is a ban on inheriting from anything in `net.minecraft` and `com.mojang`.
To clarify, `mod.iceandshadow3.lib.compat.forge.fish` still falls under the full restrictions.

## Fishing

Most uses of Forge's events are going to be by `BLogic` subtypes.

`BLogic` subtypes return `IEventFish` implementations from a `getEventFish` method,
which contain the actual logic for how an event should be handled.
Why fish? Because we're putting something on Forge's event hooks, and it might as well be fish.

The fish objects returned are logic objects themselves. The default implementation of `getEventFish`
allows `BLogic`s to return themselves in response to requests for certain fish types.

The acquiring and calling of the fish objects is done by specialized Forge event handlers,
specifically subtypes of `BEventBait` with their type parameters filled in.
Why bait? Because bait gets put on a hook to get a fish on the hook instead.

BEventBait instances are actually instantiated and registered with the event bus by `EventFisherman`.
Why fisherman? Because a fisherman is what actually puts bait on the hooks.

Why has this metaphor been taken this far? If you're hoping for any reasonable reason, you'll be disappointed.

## Other Event Handling

Fish-unrelated event handlers should still be instantiated by `EventFisherman`.

