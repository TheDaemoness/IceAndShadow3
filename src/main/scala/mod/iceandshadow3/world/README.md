# .world

Contains elements of the world(s) of Ice and Shadow III.
Items, blocks, mobs, and more all go into subpackages here.

Domains, however, go here specifically. Remember to add new domains to `iceandshadow3.Domains`!

## Type Name Prefixes

The following rules apply to subpackages. 

* `Mat` - Scala object that extends BMateria or one of its children.
* `LB` - Scala class that extends BLogicBlock.
* `LM` - Scala class that extends BLogicMob.
* `LI` - Scala class that extends BLogicItem.
* `S*` - BStateData paired with the applicable logic.