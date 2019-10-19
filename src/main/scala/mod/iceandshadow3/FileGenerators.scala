package mod.iceandshadow3

import java.nio.file.Path
import java.util

import com.google.common.base.Charsets
import mod.iceandshadow3.lib.base.BLogicWithItem
import mod.iceandshadow3.lib.compat.file.BJsonAssetGen
import mod.iceandshadow3.lib.compat.{BFileGen, Registrar}
import net.minecraft.data.DataGenerator

object FileGenerators {
	private val assets: BFileGen = new BFileGen("ias3_json_assets") {
		def addTo[Logic <: BLogicWithItem, AssetGen <: BJsonAssetGen[Logic]](
			map: util.Map[Path, Array[Byte]],
			assetRoot: Path,
			item: Logic,
			variant: Int,
			assetGen: AssetGen
		): Unit = {
			val name = item.getName(variant)
			map.put(
				assetRoot.resolve(assetGen.path).resolve(s"$name.json"),
				assetGen.apply(item, variant).getBytes(Charsets.US_ASCII)
			)
		}
		override protected def getData(root: Path) = {
			import scala.jdk.CollectionConverters._
			val assetRoot = root.resolve(s"assets/${IaS3.MODID}")
			val retval = new util.HashMap[Path, Array[Byte]]
			for(item <- ContentLists.item.asScala;
				variant <- 0 until item.countVariants;
				model <- item.getItemModelGen(variant)
			) {
				addTo(retval, assetRoot, item, variant, model)
			}
			for(block <- ContentLists.block.asScala; variant <- 0 until block.countVariants) {
				val blockstates = block.getBlockstatesGen(variant)
				val model = block.getBlockModelGen(variant)
				blockstates.foreach(gen => addTo(retval, assetRoot, block, variant, gen))
				model.foreach(gen => addTo(retval, assetRoot, block, variant, gen))
				if(!block.isTechnical) {
					val modelItem = block.getItemModelGen(variant)
					modelItem.foreach(gen => addTo(retval, assetRoot, block, variant, gen))
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
