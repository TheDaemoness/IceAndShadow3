package mod.iceandshadow3.compat

import mod.iceandshadow3.util.Vec3

trait ISpatial {
	def position: Vec3
	//TODO: Methods for bounding boxes, maybe a subtype with facing methods.
}