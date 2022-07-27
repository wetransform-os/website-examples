// The context for this function is a simple groovy script.
// Test if the geometry of the current object is not disjunct with the target shape
def candidates = _.geom.spatialIndexQuery(geometry: _source.p.geom.first())

// find a specific FeatureType from the canidates (here: Binnenwateren)
def binnenwateren = candidates.find {
  it.definition.getDisplayName() == 'Binnenwateren'
}

// emit an object if it was found
if (binnenwateren) {
  _target {

  }
}
else {
  // ... or log that no match was found.
  _log.info("Disjunct object: " + _source.p.vrt_naam.value())
}
