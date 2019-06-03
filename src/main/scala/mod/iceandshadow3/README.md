# (root)

Contains `IceAndShadow3`.

Otherwise, contains types and pure-static classes/singletons that are immediately relevant to the overall initialization/configuration of IaS3.

The `S` prefix can be freely omitted here.

Classes in this package (but NOT subpackages) may freely use (but not inherit from) otherwise restricted types.

## Altering the Configuration Files

`ConfigClient` and `ConfigServer` are classes that are instantiated each time the relevant configuration file is loaded.
For each entry that needs to be in the file,
there should be one public field in the relevant class annotated with `@Entry`
and whose type implements `mod.iceandshadow3.data.ITextLineRW`, initialized to its default value.

Upon increasing the number of fields, the minor version should be increased by 1.
Upon changing any of the existing fields, the major version should be increased by 1 and the minor version reset to 0.
