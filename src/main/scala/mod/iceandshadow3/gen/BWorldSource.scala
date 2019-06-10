package mod.iceandshadow3.gen

import mod.iceandshadow3.compat.block.`type`.BBlockType

/** The actual world generator for IaS3 dimensions.
	*
	* CONCURRENCY WARNING:
	* BWorldSource and all of its children need to be thread-safe.
	* That applies to all of their methods and fields.
	*/
abstract class BWorldSource {
	def getTerrainChunk(xFrom: Int, zFrom: Int, xLast: Int, zLast: Int): BChunkSource
}
