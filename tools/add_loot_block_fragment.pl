#!/bin/env perl

use strict;
use warnings;

use lib './tools';
use IaS3Dev;

$#ARGV == 3 or die "Invalid argument count: expected 4";

(my $blockname, my $itemnamespaced, my $min, my $max) = @ARGV;

IaS3Dev::write_template(
    "$IaS3Dev::loot_prefix/blocks/$blockname.json",
    "loot_tables/block_fragment.json",
    (
        BLOCK => $blockname,
        ITEM  => $itemnamespaced,
        MIN   => $min,
        MAX   => $max
    )
);
