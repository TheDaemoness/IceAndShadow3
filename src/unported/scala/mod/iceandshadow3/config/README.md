# .config

Contains most of IaS3's configuration system,
with the notable exception of the classes that contain the configuration options themselves.

The fields of subtypes of `ConfigReflective` must implement `data.ITextLineRW`.

IaS3 has this particular bit of NIHiness for historical reasons (and a distrust of Forge's config system).