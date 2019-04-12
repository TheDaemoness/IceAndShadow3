package mod.iceandshadow3.compat

import mod.iceandshadow3.Config
import mod.iceandshadow3.IceAndShadow3

import net.minecraft.world.DimensionType
import net.minecraft.world.WorldProvider
import net.minecraftforge.common.DimensionManager

object CDimension {
	private var dims: java.util.HashMap[Integer, CDimension] =
		new java.util.HashMap()

	var OVERWORLD: CDimension = new CDimension(0)
	var NETHER: CDimension = new CDimension(-1)
	var END: CDimension = new CDimension(1)
	var NYX: CDimension = new CDimension() //TODO: Move this.

	def get(id: Int): CDimension = {
		var retval: CDimension = dims.get(id)
		if (retval == null && DimensionManager.isDimensionRegistered(id))
			retval = new CDimension(id)
		retval
	}
}

class CDimension () {
	private var registered: Boolean = false

	/**
	 * Constructor for already-existing/vanilla dimensions.
	 */
	private def this(id: Int) = {
		this()
		CDimension.dims.put(id, this)
		registered = true
	}

	def register(cfg: Config.IDimensionConfig, name: String, suffix: String): Unit = {
		if (registered) return
		if (DimensionManager.isDimensionRegistered(cfg.getDimId)) {
			val oldid: Int = cfg.getDimId
			cfg.setDimId(DimensionManager.getNextFreeDimId)
			IceAndShadow3.logger().error(
				"Dimension id " + oldid +
				" for dimension \"" + name +
				"\" is taken. Changing to next available id: " + cfg.getDimId
			)
		}
		//TODO: The following really shouldn't pass WorldProvider.class
		DimensionManager.registerDimension(
			cfg.getDimId,
			DimensionType.register(name, suffix, cfg.getDimId, classOf[WorldProvider], false))
		CDimension.dims.put(cfg.getDimId(), this)
		registered = true
	}
}