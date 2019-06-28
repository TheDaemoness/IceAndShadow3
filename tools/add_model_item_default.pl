#!/bin/env perl

use strict;
use warnings;

use lib './tools';
use IaS3Dev;

$#ARGV == 0 or die "Invalid argument count: expected 1";

my $naem = $ARGV[0];
IaS3Dev::write_template("$IaS3Dev::assets_prefix/models/item/$naem.json", "model_item/default.json", (ITEM => $naem));


