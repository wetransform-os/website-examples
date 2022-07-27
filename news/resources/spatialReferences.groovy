// Get the LineString geometry of the Boundary object
def geom = _source.p.the_geom.value()

// create a target object
_target {
  // get all candidates from the global spatial index that have any spatial relationship with the  the Boundary geometry
  def cand = _.geom.spatialIndexQuery(spatialIndex: _spatialIndex, geometry: geom)
  def ids = []
  // for each candidate, verify boundaryCovers spatial test and construct references according to the identified AdminUnit
  // (different IDs necessary depending on the level of the AU in this case)
  cand.each { inst ->
    if (inst != null && !ids.contains(inst.p.UUID.value())) {
      ids << inst.p.UUID.value()
      def surface = inst.p.the_geom.value()
      def icc = inst.p.ICC.value()
      if (_.geom.boundaryCovers(line: geom, geometry: surface) && (icc == null || icc.equals('CH') || icc.equals('LI'))) {
        if (inst.p.BFS_NUMMER.value() != null) {
          admUnit {
            href('#AdministrativeUnit_Gemeinde_' + inst.p.BFS_NUMMER.value()) // 5th Level AU
          }
        }
        else if (inst.p.BEZIRKSNUM.value() != null) {
          admUnit {
            href('#AdministrativeUnit_Bezirk_' + inst.p.BEZIRKSNUM.value()) // 4th Level AU
          }
        }
        else if (inst.p.KANTONSNUM.value() != null) {
          admUnit {
            href('#AdministrativeUnit_Kanton_' + inst.p.KANTONSNUM.value()) // 2nd Level AU
          }
        }
        else if (inst.p.ICC.value() != null && !inst.p.ICC.value().equals('DE') && !inst.p.ICC.value().equals('IT')) {
          admUnit {
            href('#AdministrativeUnit_Land_' + inst.p.ICC.value())  // 1st Level AU border
          }
        }
      }
    }
    else {
      _log.error("Unable to resolve instance");
    }
  }
}
