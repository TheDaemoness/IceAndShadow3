package IaS3Dev;
use strict;
use warnings FATAL => 'all';
use CGI::FastTemplate;
use File::Basename;

sub open_new {
    my ($where) = @_;
    warn "File already exists: $where" and return if(-e $where);
    open(my $handle, ">", $where) or die "Cannot open file: $where";
    return $handle
}

sub write_template {
    my ($path, $template, %map) = @_;
    my $handle = open_new($path);
    my $dirname = dirname($template);
    my $filename = basename($template);
    my $engine = CGI::FastTemplate->new("templates/$dirname");
    $engine->define(
        main => $filename
    );
    $engine->assign(\%map);
    $engine->parse(MAIN => "main");
    print $handle ${$engine->fetch("MAIN")};
    close($handle);
}

our $data_prefix = "src/main/resources/data/iceandshadow3";
our $loot_prefix = "$data_prefix/loot_tables";
our $assets_prefix = "src/main/resources/assets/iceandshadow3";

sub add_common_block_files {
    my ($blockname) = @_;
    my $handle;

    my %map = (BLOCK => $blockname);

    write_template("$assets_prefix/blockstates/$blockname.json", "blockstates/default.json", %map);
    write_template("$assets_prefix/models/item/$blockname.json", "model_item/itemblock.json", %map);
}
