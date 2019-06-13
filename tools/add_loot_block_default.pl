#!/bin/env perl

use strict;
use warnings;

use lib './tools';
use IaS3Dev;

$#ARGV == 0 or die "Invalid argument count: expected 1";

my $blockname = $ARGV[0];

my $handle = IaS3Dev::open_new("$IaS3Dev::loot_prefix/blocks/$blockname.json");
print $handle "{\"type\":\"minecraft:block\",\"pools\":[{\"name\":\"standard\",\"rolls\":1,\"entries\":[{\"type\":\"minecraft:item\",\"name\":\"iceandshadow3:$blockname\"}],\"conditions\":[{\"condition\":\"minecraft:survives_explosion\"}]}]}";
close($handle);
