// Log geometry state before simplification
_log.info("the_geom type: " + the_geom.getClass().getCanonicalName())
_log.info("number of geoms: " + the_geom.size())
_log.info("toString: " + the_geom.get(0).toString())

// simplify individual sub-geometries
def points = 0
def geoms = []
the_geom.p.values().each { geom ->
  points += geom.getGeometry().getNumPoints()
  geoms << _.geom.toSimpleGeometries(geometries: geom, collections: false)
}

// aggregate to build a single geometry with the segments in the right order
def aggr = _.geom.aggregate(the_geom.p.values())

// again simplify the resulting aggregate
def result = _.geom.toSimpleGeometries(geometries: aggr, collections: false)

// log the state after simplification
_log.info("result list size = " + result.size())
_log.info("total result pts = " + result.getGeometry().getNumPoints())
_log.info("total tgt geoms: " + result.size())
_log.info("result = " + result.toString())

// return the result
result
