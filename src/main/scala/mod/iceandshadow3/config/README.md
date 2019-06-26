# .config

Contains most of IaS3's configuration system,
with the notable exception of the classes that contain the configuration options themselves.

The fields of subtypes of `ConfigReflective` must implement `data.ITextLineRW`.
For convenience, concrete subtypes
of `data.BDataTreeLeaf` are required to.
