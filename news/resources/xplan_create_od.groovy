def extRefs = _source.p.externeReferenz.list()

extRefs.each {
  value ->
    def uuidForReference = "OfficialPlan_"
    + UUID.nameUUIDFromBytes(value.XP_SpezExterneReferenz.referenzURL.value())
    _target {
      id(uuidForReference)
    }
}
