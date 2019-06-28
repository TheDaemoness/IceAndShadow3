package IaS3Dev;
use strict;
use warnings FATAL => 'all';

sub open_new {
    my ($where) = @_;
    warn "File already exists: $where" and return if(-e $where);
    open(my $handle, ">", $where) or die "Cannot open file: $where";
    return $handle
}

our $data_prefix = "src/main/resources/data/iceandshadow3";
our $loot_prefix = "$data_prefix/loot_tables";
our $assets_prefix = "src/main/resources/assets/iceandshadow3";

sub add_common_block_files {
    my ($blockname) = @_;
    my $handle;

    $handle = open_new("$assets_prefix/blockstates/$blockname.json");
    print $handle "{\"variants\":{\"\":{ \"model\":\"iceandshadow3:block/$blockname\"}}}";
    close($handle);

    $handle = open_new("$assets_prefix/models/item/$blockname.json");
    print $handle "{\"parent\":\"iceandshadow3:block/$blockname\"}";
    close($handle);
}
