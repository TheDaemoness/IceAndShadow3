#!/bin/env perl

use strict;
use warnings;

use lib './tools';
use IaS3Dev;

$#ARGV == 0 or die "Invalid argument count: expected 1";

my $blockname = $ARGV[0];

IaS3Dev::write_template(
    "$IaS3Dev::loot_prefix/blocks/$blockname.json",
    "loot_tables/block_default.json",
    (BLOCK => $blockname)
);
