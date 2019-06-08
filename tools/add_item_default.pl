#!/bin/env perl

use strict;
use warnings;

use lib './tools';
use IaS3Dev;

$#ARGV == 0 or die "Invalid argument count: expected 1";

my $itemname = $ARGV[0];

my $assets_prefix = $IaS3Dev::assets_prefix;
my $handle;

$handle = IaS3Dev::open_new("$assets_prefix/models/item/$itemname.json");
print $handle "{\"parent\": \"item/generated\", \"textures\": {\"layer0\": \"iceandshadow3:item/$itemname\"}}";
close($handle);

