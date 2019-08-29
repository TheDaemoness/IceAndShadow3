#!/bin/env perl

use strict;
use warnings;

use lib './tools';
use IaS3Dev;

$#ARGV >= 3 or die "Invalid argument count: recipe needs at least 1 method name, 1 output, 1 count, and 1 input";
$#ARGV <= 11 or die "Invalid argument: recipe may have no more than 9 inputs";

($ARGV[1] =~ /([a-z_0-9]+):(.+)/) or die "Invalid output: no namespace";

my $method = $ARGV[0];
my $namespace = $1;
my $output = $2;
my $amount = $ARGV[2];
my @inputs = @ARGV[3..$#ARGV];
my $recipename="craft.$output.$method";

sub gen_inputstring {
    my ($prefix) = @_;
    my $inputstring = "${prefix}{\"item\": \"$inputs[0]\"}";
    for (@inputs[1..$#inputs]) {$inputstring .= ",\n${prefix}{\"item\": \"$_\"}";};
    return $inputstring;
}

my %remap = (
    RECIPE => $recipename,
    OUTPUT => "$namespace:$output",
    INPUT_RECIPE => gen_inputstring("\t\t"),
    INPUT_ADVANCEMENT => gen_inputstring("\t\t\t\t\t"),
    COUNT => ($amount>1 ? ",\n\t\t\"count\": $amount" : "")
);

IaS3Dev::write_template(
    "$IaS3Dev::data_prefix/recipes/$recipename.json",
    "recipes/shapeless.json",
    %remap
);

IaS3Dev::write_template(
    "$IaS3Dev::data_prefix/advancements/recipes/$recipename.json",
    "advancements/recipes/shapeless.json",
    %remap
);
