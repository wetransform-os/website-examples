// This function builds a polygon from a set of (unsorted) linestrings.
// If the linestrings contain multiple polygons, it also groups linestrings
// per polygon, so that a correct MultiPolygon geometry can be emitted.
​
// get source geometry attribute
def allSourceLinestrings = the_geom.p.values()
​
// determine CRS of the source objects
def sourceCRS = _.geom.find(the_geom).getCRSDefinition()
​
// aggregate Linestrings to order them correctly
def aggregateGeometry = _.geom.aggregate(allSourceLinestrings)
​
// Split MultilineString into Linestrings
def numberGeometries = aggregateGeometry.geometry.getNumGeometries()
def listLinestrings = []
for (int i = 0; i < numberGeometries; i++) {
  listLinestrings.add(aggregateGeometry.geometry.getGeometryN(i))
}
​
// split according to first/last coordinate match
def individualPolygons = []
def currentPolygonFirstPoint = null
def previousLastPoint = null
def currentPolygon = []
listLinestrings.each {
  value ->
    def coordinateList = value.getCoordinates()
    def first = coordinateList[0]
    def last = coordinateList[coordinateList.length - 1]

    // one-linestring polygon
    if (first.equals(last)) {
      currentPolygon.addAll(coordinateList as List)
      individualPolygons.add(currentPolygon)
      currentPolygon = []
    }
    // multi-linestring polygon
    else {
      if (!currentPolygonFirstPoint) {
        // first linestring of a new polygon
        currentPolygonFirstPoint = first
      }
      previousLastPoint = last
      currentPolygon.addAll(coordinateList as List)

      if (currentPolygonFirstPoint.equals(last)) {
        // found a closed polygon, add to list,
        // then reset loop variables
        individualPolygons.add(currentPolygon)
        currentPolygonFirstPoint = null
        currentPolygon = []
      }
    }
}
​
// build polygon from each Linestring
def polygonWKTs = []
def counter = 0
individualPolygons.each {
  value ->
    def wktDefinition = "(("
    value.each {
      thisCoordinate ->
        wktDefinition += thisCoordinate.x + " " + thisCoordinate.y + ","
    }
    wktDefinition = wktDefinition.replaceAll("[,]\$", "))");
    polygonWKTs[counter] = wktDefinition
    counter++
}
​
// build final MultiPolygon WKT
def wktMultiPolygon = "MULTIPOLYGON("
polygonWKTs.each {
  value ->
    wktMultiPolygon += value + ","
}
// replace last comma with closing parentheses
wktMultiPolygon = wktMultiPolygon.replaceAll("[,]\$", ")");
​
// write out geometry
def resultGeom = null
try {
  resultGeom = _.geom.with(geometry: wktMultiPolygon, crs: sourceCRS)
}
catch (e) {
  // log debug information and errors in encoding if there are any
  _log.warn("Error in encoding geometry: " + e)
  _log.info("wktMultiPolygon: " + wktMultiPolygon)
  _log.info("aggregateGeometry: " + aggregateGeometry)
  _log.info("allSourceLinestrings: " + allSourceLinestrings)
}

return resultGeom
