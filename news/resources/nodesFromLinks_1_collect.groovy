// This function collects the first and last points of each
// link and stores them in a map, where the points serve as
// keys, while the IDs of the links serve as values.

// get collector
def c = _.context.collector()
def pointsIdsMap = c.nodes

// get geometry and extract first and last point
def thisGeometry = _.geom.find(_source.p.geom.first())
def thisCoordinates = thisGeometry.geometry.getCoordinates()
def first = thisCoordinates[0]
def last = thisCoordinates[thisCoordinates.length - 1]

// add ID of this waarwegvakken (Link) to map value list
def thisId =  _source.p.vwk_id.value()

pointsIdsMap[first].start << thisId
pointsIdsMap[last].stop << thisId
