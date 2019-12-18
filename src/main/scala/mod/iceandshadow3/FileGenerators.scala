package mod.iceandshadow3

import java.nio.file.Path
import java.util

import com.google.common.base.Charsets
import mod.iceandshadow3.lib.base.{BLogic, TLogicWithItem}
import mod.iceandshadow3.lib.compat.file.BJsonAssetGen
import mod.iceandshadow3.lib.compat.{BFileGen, Registrar}
import net.minecraft.data.DataGenerator

object FileGenerators {
	private val assets: BFileGen = new BFileGen("ias3_json_assets") {
		def addTo[Logic <: BLogic, AssetGen <: BJsonAssetGen[Logic]](
			map: util.Map[Path, Array[Byte]],
			assetRoot: Path,
			item: Logic,
			assetGen: AssetGen
		): Unit = {
			val name = item.name
			map.put(
				assetRoot.resolve(assetGen.path).resolve(s"$name.json"),
				assetGen.apply(item).getBytes(Charsets.US_ASCII)
			)
		}
		override protected def getData(root: Path) = {
			import scala.jdk.CollectionConverters._
			val assetRoot = root.resolve(s"assets/${IaS3.MODID}")
			val retval = new util.HashMap[Path, Array[Byte]]
			for(
				item <- ContentLists.item.asScala;
				model <- item.getItemModelGen
			) {
				addTo(retval, assetRoot, item, model)
			}
			for(block <- ContentLists.block.asScala) {
				val blockstates = block.getBlockstatesGen
				val model = block.getBlockModelGen
				blockstates.foreach(gen => addTo(retval, assetRoot, block, gen))
				model.foreach(gen => addTo(retval, assetRoot, block, gen))
				block.itemLogic.foreach(blockItem => {
						blockItem.getItemModelGen.foreach(gen => addTo(retval, assetRoot, blockItem, gen))
				})
			}
			retval
		}
	}
	def run(gen: DataGenerator): Unit = {
		Registrar.getFileGen.attachTo(gen)
		assets.attachTo(gen)
	}
}
