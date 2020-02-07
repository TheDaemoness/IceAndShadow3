package mod.iceandshadow3.lib.compat.file

abstract class JsonGenModelItem(name: String) extends JsonGen(name) {
	final override def basePath = "models/item"
}