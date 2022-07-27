// access source values and store in variables
def edgeIdList = LINKID.p.values()
def edgeSeqList = EDGE_SEQ.p.values()
def linkFromPList = LINK_FROMP.p.values()
def subrouteStartKMList = SUBROUTE_STARTKM.p.values()

// sort LINKIDs by composite key built from attribute values
TreeMap sortedEdgeIDMap = [:]
for (def i = 0; i < edgeIdList.size; i++) {
    
	// build parts of the composite key for sorting
    def seqKey = String.format('%04d', Long.parseLong(edgeSeqList.get(i)))
    def linkFromPKey = String.format('%08d', (Double.parseDouble(linkFromPList.get(i)) * 10000).longValue())
    def subrouteStart = Double.parseDouble(subrouteStartKMList.get(i))
    
    // build composite key, while ignoring subroute start values of -1
    def compositeKey = ""
    if (subrouteStart >= 0.0) {
    	def subrouteStartKey = String.format('%08d', (subrouteStart * 10000).longValue());
    	compositeKey = seqKey + "_" + subrouteStartKey + "_" + linkFromPKey
    }
    else {
    	compositeKey = seqKey + "_" + linkFromPKey
    }
    
    // log any key collisions that occur
	if (sortedEdgeIDMap.get(compositeKey)) {
		_log.warn("Route " + ROUTE_ID.p.value() + " / Subroute " + SUBROUTE_ID.p.value()  + " has Pre-Existing compositeKey: " + compositeKey)
	}
	sortedEdgeIDMap.put(compositeKey, edgeIdList.get(i))
}

def directionSymbol = EDGE_DIR.p.value() == 0 ? "-" : "+"

// iterate over values and write them to the target property
sortedEdgeIDMap.values().each {
	value ->
		_target {
			DirectedLink {
				direction ( directionSymbol )
				link {
					href( '#RoadLink_' + value )
				}
			}
	}
}