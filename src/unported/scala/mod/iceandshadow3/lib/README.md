# .lib

IaS3's internal "libMinecraft".

This package specifically contains classes that are fundamental to the creation of IaS3.
Breakages between Minecraft versions will mainly happen here, and mainly in the `compat` subpackage.

Classes in subpackages are either used in support of the implementation of these, or are more specialized.

Unless otherwise specified, IaS3's type restrictions apply especially here.