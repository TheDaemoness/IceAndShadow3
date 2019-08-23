#!/bin/env perl

use strict;
use warnings;

use lib './tools';
use IaS3Dev;

$#ARGV >= 1 or die "Invalid argument count: recipe needs 1 output, 1 namespaceless input, optionally 1 xp yield, optionally 1 cooking type, optionally 1 cooking time in ticks";
$#ARGV < 5 or die "Invalid argument: expected no more than 5 arguments";

my $output = $ARGV[0];
my $input = $ARGV[1];
my $xp = 0.0;
my $type = "smelting";
my $time = "";
$xp = $ARGV[2]*1.0 if($#ARGV >= 2);
if($#ARGV >= 3) {
    # No campfires on Nyx.
    $ARGV[3] =~ /smelting|blasting|smoking/ or die "Unsupported cooking type";
    $type = $ARGV[3];
}
$time = ",\n\t\"cookingtime\": $ARGV[3]" if($#ARGV >= 4);

my $recipename="$type.$input";

my $xpresult = "";
$xpresult = ",\n\t\"experience\": $xp" if ($xp > 0);

my %remap = (
    RECIPE => $recipename,
    TYPE => $type,
    OUTPUT => $output,
    INPUT => "$input",
    EXTRA => "$xpresult$time",
);

IaS3Dev::write_template(
    "$IaS3Dev::data_prefix/recipes/$recipename.json",
    "recipes/cooking.json",
    %remap
);

IaS3Dev::write_template(
    "$IaS3Dev::data_prefix/advancements/recipes/$recipename.json",
    "advancements/recipes/single.json",
    %remap
);
