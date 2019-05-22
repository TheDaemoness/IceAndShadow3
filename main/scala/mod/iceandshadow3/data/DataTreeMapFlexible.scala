package mod.iceandshadow3.data

/** A map that will generate objects to be read into during fromNBT operations.
  */
class DataTreeMapFlexible(defaultGen: String => Option[IDataTreeRW[_ <: BDataTree[_]]]) extends DataTreeMap {
  override protected def getForRead(key: String) = {
    var wouldget: Option[IDataTreeRW[_ <: BDataTree[_]]] = super.getForRead(key)
    if(wouldget.isEmpty) {
      wouldget = defaultGen(key)
      wouldget.foreach{add(key, _)}
    }
    wouldget
  }
}
