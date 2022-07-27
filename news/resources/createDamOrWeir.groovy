// Split source geometries into Simple Feature geometries
def geometries = _.geom.toSimpleGeometries(geometries: _source.p.position.first(), collections: false)

// Source object ID
def sourceId = _source.p.id.value()

// Allow assigning the geometry to the top-level "geometry" attribute
_b.strictValueFlags = false

// Create one target object per source geometry
int num = 1
geometries.each { geom ->
	// Build ID with counter
	def _id = "DamOrWeir_${sourceId}_${num}"
	
	// Namespace defined in the project variable "INSPIRE_NAMESPACE"
	def _ns = _project.vars.INSPIRE_NAMESPACE

	// Create the target object with the ID and geometry
	_target {
		id(_id)
		inspireId {
			Identifier {
				localId(_id)
				namespace(_ns)
			}
		}
		identifier(_ns ? (_ns.endsWith('/') ? _ns : _ns + '/') + _id : _id) {
			codeSpace('http://inspire.ec.europa.eu/ids')
		}
		geometry(geom)
	}

	num++
}