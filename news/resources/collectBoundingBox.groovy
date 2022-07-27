// define the variable to access the shared collectors
def c = _.context.collector()

if (!c.extent.value()) {
	// if the collector "extent" is still empty, initialize it with the
	// bounding box of the current geometry...
	c.extent = _.geom.bbox(Shape)
}
else {
	// ...otherwise calculate the new extent from the bounding box stored
	// in the collector and the current geometry.
	def ext = _.geom.bbox([c.extent.value(), Shape])
	c.extent = ext
}

// Return the source geometry as is
Shape