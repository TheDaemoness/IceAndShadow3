# Developer Scripts

Quick, dirty, and fragile Perl (i.e. Perl 5) scripts to speed up certain tasks associated with creating IaS3.

These scripts are intended to be run from the project root directory
(one up from the one this README is in).

All of these scripts require one argument: the un-namespaced name of the thing to affect
(iceandshadow3 namespace is assumed).

## add_model_item_default.pl
Generates a default item model file.

## add_model_block_*.pl
Generates default `blockstate`, `models/block`, and `models/item` files, as follows:

* **default**: A full cube.
* **deco**: Two intersecting panes, like grass or sugar cane.

## add_loot_block.*.pl
Generates a loot table for the specified block, as follows:

* **default**: The block drops itself.
* **fragment**: The block drops fragments of itself, like glowstone
Takes 3 additional arguments: the namespaced name of the item to drop, the minimum count, and the maxiumum count.