package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.block.HarvestMethod
import mod.iceandshadow3.lib.util.MathUtils
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

// TODO: Allow construction of Materials here, along with setting the color.

case class Materia private(
	private[compat] val secrets: Materia.Secrets,
	name: String,
	hardness: Float,
	resistance: Float,
	harvestLevel: Int,
	luma: Int,
	opacity: Int,
	slipperiness: Float,
	solid: Boolean,
	transparent: Boolean,
	private val effectiveMethods: Set[HarvestMethod],
	private var parent: Option[Materia]
) {
	def isTypeOf(materia: Materia): Boolean = parent.fold(false)(_.isTypeOf(materia))
	def isEffective(what: HarvestMethod): Boolean = effectiveMethods.contains(what)
}

object Materia {
	//TODO: Some of these are probably wrong (especially their blastFactor values).
	import HarvestMethod._
	val air = new Builder(Material.AIR, SoundType.SNOW, 0f, HAND).hardness(0).opacity(0)("air")
	val stone = new Builder(Material.ROCK, SoundType.STONE, 2f, PICKAXE).resistFactor(3f)("stone")
	val plasma = new Builder(Material.LAVA, SoundType.SLIME, 0f, PICKAXE).opacity(1)("plasma")
	val glass = new Builder(Material.GLASS, SoundType.GLASS, 0.3f).opacity(0)("glass")
	val metal = new Builder(Material.IRON, SoundType.METAL, 5f, PICKAXE).resistFactor(2f)("metal")
	val ice = new Builder(Material.ICE, SoundType.GLASS, 0.3f, PICKAXE).slipperiness(0.98f).opacity(2)("ice")
	val ice_packed = builder(ice).secrets(Secrets(Material.PACKED_ICE, SoundType.GLASS))("ice_packed")
	val ice_blue = builder(ice_packed).slipperiness(0.989f).hardness(2.8f)("ice_blue")
	val sand = new Builder(Material.SAND, SoundType.SAND, 0.5f, SHOVEL)("sand")
	val gravel = new Builder(Material.SAND, SoundType.GROUND, 0.6f, SHOVEL)("gravel")
	val wood = new Builder(Material.WOOD, SoundType.WOOD, 2f, AXE)("wood")
	val leaves = new Builder(Material.LEAVES, SoundType.PLANT, 1f, BLADE, SHEAR)("leaves")
	val portal = new Builder(Secrets(Material.PORTAL, SoundType.GLASS), None, Set.empty[HarvestMethod], None)("portal")

	private val indestructibleByMining = -1f
	private val indestructibleByBlast = 3600000f

	private[block] case class Secrets(material: Material, soundType: SoundType) {
		def withSound(soundType: SoundType) = Secrets(material, soundType)
	}

	final class Builder private[block](
		private var _secrets: Secrets,
		private var _hardness: Option[Float],
		private var _methods: Set[HarvestMethod],
		private var _parent: Option[Materia]
	) {
		private var _resistFactor: Option[Float] = if(_secrets.material.isLiquid) None else Some(1f)
		private var _harvestLevel: Int = if(_secrets.material.isToolNotRequired) -1 else 0
		private var _luma = 0
		private var _opacity: Int = if(_secrets.material.isOpaque) 0xff else 0
		private var _slipperiness = 0.6f
		private var _solid = _secrets.material.blocksMovement
		private var _transparent = !_secrets.material.isOpaque

		private[block] def this(material: Material, soundType: SoundType, hardness: Float, methods: HarvestMethod*) =
			this(Secrets(material, soundType), Some(hardness), methods.toSet, None)

		def apply(name: String): Materia = {
			Materia(
				_secrets,
				name,
				_hardness.getOrElse(indestructibleByMining),
				_resistFactor.fold(indestructibleByBlast)(a => _hardness.fold(indestructibleByBlast)(b => a*b)),
				_harvestLevel,
				_luma,
				_opacity,
				_slipperiness,
				_solid,
				_transparent,
				_methods,
				_parent
			)
		}
		def hardness(float: Float): this.type = {_hardness = Some(Math.max(0f, float)); this}
		/** Explosion resistance is calculated as a multiple of the hardness. This sets that multiplier. */
		def resistFactor(float: Float): this.type = {_resistFactor = Some(Math.max(0f, float)); this}
		def harvestLevel(int: Int): this.type = {_harvestLevel = int; this}
		def luma(int: Int): this.type = {_luma = MathUtils.bound(0, int, 15); this}
		def opacity(int: Int): this.type = {_opacity = if(int >= 15) 0xff else Math.max(0, int); this}
		def slipperiness(float: Float): this.type = {_slipperiness = float; this}
		def solid(boolean: Boolean): this.type = {_solid = boolean; this}
		def transparent(boolean: Boolean): this.type = {_transparent = boolean; this}
		def indestructible: this.type = {_hardness = None; this}
		def blastproof: this.type = {_resistFactor = None; this}
		def sounds(materia: Materia): this.type = {_secrets = _secrets.withSound(materia.secrets.soundType); this}
		def harvestWith(how: HarvestMethod*): this.type = {_methods = how.toSet; this}
		private[compat] def secrets(secrets: Secrets): this.type = {_secrets = secrets; this}
	}

	def builder(parent: Materia) = {
		val builder = new Builder(
			parent.secrets,
			if(parent.hardness >= 0) Some(parent.hardness) else None,
			parent.effectiveMethods,
			Some(parent)
		).harvestLevel(parent.harvestLevel)
  		.luma(parent.luma)
  		.opacity(parent.opacity)
  		.slipperiness(parent.slipperiness)
  		.solid(parent.solid)
  		.transparent(parent.transparent)
		if(parent.hardness <= 0 || parent.resistance >= indestructibleByBlast) builder.indestructible
		else if(parent.hardness != 0) builder.resistFactor(parent.resistance/parent.hardness)
		// TODO: Iterate backward through the parent tree until we get a parent with nonzero hardness.
		builder
	}
}
