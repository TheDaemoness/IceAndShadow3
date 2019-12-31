package mod.iceandshadow3

import java.nio.file.Path
import java.util

import com.google.common.base.Charsets
import mod.iceandshadow3.lib.compat.file.BJsonGenAsset
import mod.iceandshadow3.lib.compat.{BFileGen, Registrar}
import net.minecraft.data.DataGenerator

object FileGenerators {
	private val assets: BFileGen = new BFileGen("ias3_json_assets") {
		def addTo(map: util.Map[Path, Array[Byte]], assetRoot: Path, assetGen: BJsonGenAsset): Unit = map.put(
			assetRoot.resolve(assetGen.basePath).resolve(s"${assetGen.name}.json"),
			assetGen.apply.getBytes(Charsets.US_ASCII)
		)
		override protected def getData(root: Path) = {
			import scala.jdk.CollectionConverters._
			val assetRoot = root.resolve(s"assets/${IaS3.MODID}")
			val retval = new util.HashMap[Path, Array[Byte]]
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
		assets.attachTo(gen)
	}
}
