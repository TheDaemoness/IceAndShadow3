#!/bin/env perl

use strict;
use warnings;

use lib './tools';
use IaS3Dev;

$#ARGV == 0 or die "Invalid argument count: expected 1";

my $naem = $ARGV[0];
my $assets_prefix = $IaS3Dev::assets_prefix;
IaS3Dev::add_common_block_files($naem);
IaS3Dev::write_template("$assets_prefix/models/block/$naem.json","model_block/deco.json", (BLOCK => $naem));
