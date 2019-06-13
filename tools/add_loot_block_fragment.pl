#!/bin/env perl

use strict;
use warnings;

use lib './tools';
use IaS3Dev;

$#ARGV == 3 or die "Invalid argument count: expected 4";

(my $blockname, my $itemnamespaced, my $min, my $max) = @ARGV;

my $handle = IaS3Dev::open_new("$IaS3Dev::loot_prefix/blocks/$blockname.json");
print $handle "{\"type\":\"minecraft:block\",\"pools\":[{\"name\":\"standard\",\"rolls\":1,\"entries\":[{\"type\":\"minecraft:alternatives\",\"children\":[{\"type\":\"minecraft:item\",\"conditions\":[{\"condition\":\"minecraft:match_tool\",\"predicate\":{\"enchantments\":[{\"enchantment\":\"minecraft:silk_touch\",\"levels\":{\"min\":1}}]}}],\"name\":\"iceandshadow3:$blockname\"},{\"type\":\"minecraft:item\",\"functions\":[{\"function\":\"minecraft:set_count\",\"count\":{\"min\":$min,\"max\":$max,\"type\":\"minecraft:uniform\"}},{\"function\":\"minecraft:apply_bonus\",\"enchantment\":\"minecraft:fortune\",\"formula\":\"minecraft:uniform_bonus_count\",\"parameters\":{\"bonusMultiplier\":1}},{\"function\":\"minecraft:limit_count\",\"limit\":{\"min\":1,\"max\":$max}},{\"function\":\"minecraft:explosion_decay\"}],\"name\":\"$itemnamespaced\"}]}]}]}";
close($handle);
