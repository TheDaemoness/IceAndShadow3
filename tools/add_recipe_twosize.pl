#!/bin/env perl

use strict;
use warnings;

use lib './tools';
use IaS3Dev;

$#ARGV <= 1 or die "Script needs the un-namespaced names of the larger item and optionally the smaller";

my $larger = $ARGV[0];
my $smaller = $#ARGV == 1 ? $ARGV[1] : "${larger}_small";
my $recipenamea="craft.$larger.from_small";
my $recipenameb="craft.$larger.to_small";

my %remap = (
    RECIPE_A => $recipenamea,
    RECIPE_B => $recipenameb,
    SMALLER  => "iceandshadow3:$smaller",
    LARGER   => "iceandshadow3:$larger"
);

IaS3Dev::write_template(
    "$IaS3Dev::data_prefix/advancements/recipes/$larger.twosize.json",
    "advancements/recipes/twosize.json",
    %remap
);

IaS3Dev::write_template(
    "$IaS3Dev::data_prefix/recipes/$recipenamea.json",
    "recipes/twosize.from_small.json",
    %remap
);

IaS3Dev::write_template(
    "$IaS3Dev::data_prefix/recipes/$recipenameb.json",
    "recipes/twosize.to_small.json",
    %remap
);
