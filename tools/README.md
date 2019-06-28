# Developer Scripts

Quick, dirty, and fragile Perl (i.e. Perl 5) scripts to speed up certain tasks associated with creating IaS3.

These scripts are intended to be run from the project root directory
(one up from the one this README is in).

All of these scripts require one argument: the un-namespaced name of the thing to affect
(iceandshadow3 namespace is assumed).

## External Dependencies
* CGI::FastTemplate

## Scripts

### add_model_item_default.pl
Generates a default item model file.

### add_model_block_*.pl
Generates default `blockstate`, `models/block`, and `models/item` files, as follows:

* **default**: A full cube.
* **deco**: Two intersecting panes, like grass or sugar cane.

### add_loot_block.*.pl
Generates a loot table for the specified block, as follows:

* **default**: The block drops itself.
* **fragment**: The block drops fragments of itself, like glowstone
Takes 3 additional arguments: the namespaced name of the item to drop, the minimum count, and the maxiumum count.

### add_recipe_shapeless.pl
Generates a shapeless crafting recipe and unlock advancement for it.

Takes the following arguments:
* A recipe name to disambiguate among recipes with the same output.
* The output item name (WITHOUT namepsace).
* The output item count.
* One to nine namespaced item names for inputs.

### add_recipe_twosize.pl
Generates two crafting recipes for converting 9 of a smaller item to 1 larger item, and vice versa.
Also generates an advancement that unlocks both recipes at the same time.

Takes the following arguments:
* The un-namespaced name of the larger item.
* (Optional) The un-namespaced name of the smaller item. If unspecified, concats `_small` onto the larger item name.