package mod.iceandshadow3

import java.nio.file.Path
import java.util

import com.google.common.base.Charsets
import mod.iceandshadow3.lib.compat.file.BJsonGen
import mod.iceandshadow3.lib.compat.{BFileGen, Registrar}
import net.minecraft.data.DataGenerator

object FileGenerators {
	private val logicFilesGen: BFileGen = new BFileGen("ias3_logic_json") {
		def addTo(map: util.Map[Path, Array[Byte]], root: Path, assetGen: BJsonGen): Unit = map.put(
			root.resolve(assetGen.basePath).resolve(s"${assetGen.name}.json"),
			assetGen.apply.getBytes(Charsets.US_ASCII)
		)
		override protected def getData(root: Path) = {
			import scala.jdk.CollectionConverters._
			val retval = new util.HashMap[Path, Array[Byte]]
			val assetRoot = BFileGen.getAssetsPath(root)
			for(
				item <- ContentLists.item.asScala;
				model <- item.getItemModelGen
			) addTo(retval, assetRoot, model)
			for(block <- ContentLists.block.asScala) {
				for(gen <- block.getGenAssetsBlock) {
					addTo(retval, assetRoot, gen)
					gen.models.foreach(addTo(retval, assetRoot, _))
				}
				for(blockItem <- block.itemLogic; gen <- blockItem.getGenModelItem) {
					addTo(retval, assetRoot, gen)
				}
			}
			retval
		}
	}
	def run(gen: DataGenerator): Unit = {
		Registrar.getFileGen.attachTo(gen)
		logicFilesGen.attachTo(gen)
	}
}
