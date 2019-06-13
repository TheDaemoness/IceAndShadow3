#!/bin/env perl

use strict;
use warnings;

use lib './tools';
use IaS3Dev;

$#ARGV == 0 or die "Invalid argument count: expected 1";

my $blockname = $ARGV[0];

my $assets_prefix = $IaS3Dev::assets_prefix;
my $handle;

IaS3Dev::add_common_block_files($blockname);

$handle = IaS3Dev::open_new("$assets_prefix/models/block/$blockname.json");
print $handle "{\"parent\": \"block/tinted_cross\", \"textures\": {\"cross\": \"iceandshadow3:block/$blockname\"}}";
close($handle);
