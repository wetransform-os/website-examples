// This function creates a WaterwayNode with references (Node -> Links) for
// each collected coordinate object, and stores information to create
// inverse references (Link -> Node)

// get collector
def c = _.context.collector()
def pointsIdsMap = c.nodes
def linksIdsMap = c.links

pointsIdsMap.eachCollector{
  key, child ->
    // define ID
    def thisInspireId = "WaterwayNode." + UUID.nameUUIDFromBytes(key.toString().getBytes())
    def pointGeometry = _.geom.with(geometry: "POINT(" + key.x + " " + key.y + ")", crs: "EPSG:28992")

    _target {
      geometry {
        Point (pointGeometry)
      }
      child.start.each{
        thisId ->
          // Store Link ID with mapping to Node ID
          linksIdsMap[thisId].start << thisInspireId

          // assign the spokeStart property
          spokeStart {
            href("#WaterwayLink." + thisId)
          }
      }
      child.stop.each{
        thisId ->
          // Store Link ID with mapping to Node ID
          linksIdsMap[thisId].stop << thisInspireId

          // assign the spokeEnd property
          spokeEnd {
            href("#WaterwayLink." + thisId)
          }
      }
      id (thisInspireId)
      inspireId {
        Identifier {
          localId( thisInspireId )
          namespace ( _project.vars.INSPIRE_DATASET_NAMESPACE )
        }
      }
    }
}
