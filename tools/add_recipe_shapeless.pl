#!/bin/env perl

use strict;
use warnings;

use lib './tools';
use IaS3Dev;

$#ARGV >= 3 or die "Invalid argument count: recipe needs at least 1 method name, 1 output, 1 count, and 1 input";
$#ARGV <= 11 or die "Invalid argument: recipe may have no more than 9 inputs";

my $method = $ARGV[0];
my $output = $ARGV[1];
my $amount = $ARGV[2];
my @inputs = @ARGV[3..$#ARGV];
my $recipename="craft.$output.$method";

my $handle;
sub printitems {
    print $handle "{\"item\":\"$inputs[0]\"}";
    print $handle ",{\"item\":\"$_\"}" for @inputs[1..$#inputs];
}

$handle = IaS3Dev::open_new("$IaS3Dev::data_prefix/recipes/$recipename.json");
print $handle '{"type":"minecraft:crafting_shapeless","group":"iceandshadow3","ingredients":[';
printitems();
print $handle "],\"result\":{\"item\":\"iceandshadow3:$output\"";
print $handle ",\"count\":$amount" if($amount > 1);
print $handle "}}";
close($handle);

$handle = IaS3Dev::open_new("$IaS3Dev::data_prefix/advancements/recipes/$recipename.json");
print $handle '{"parent":"minecraft:recipes/root","rewards":{"recipes":["';
print $handle "iceandshadow3:$recipename";
print $handle '"]},"criteria":{"i":{"trigger": "minecraft:inventory_changed","conditions":{"items":[';
printitems();
print $handle ']}},"r":{"trigger": "minecraft:recipe_unlocked","conditions":{"recipe":"';
print $handle "iceandshadow3:$recipename";
print $handle '"}}},"requirements":[["r","i"]]}';
close($handle);
