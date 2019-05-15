# .config

Contains most of IaS3's configuration system, with the notable exception of the classes that contain the configuration options themselves.

To add new types that can be used in the fields of subtypes of `ConfigReflective` (i.e. both of IaS3's current config classes one package up), add the relevant parsing functions to the static block of `SConfigValueTranslator`. At present, the only supported types for fields in `ConfigReflective` subtypes are:
* `Boolean`
* `Integer`