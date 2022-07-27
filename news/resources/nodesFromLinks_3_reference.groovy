// get the collector with the references from Links to Nodes

def c = _.context.collector()
def linksIdsMap = c.links
def thisLinkID = vwk_id

linksIdsMap[thisLinkID].eachCollector {
  key, child ->
    if (key == "start") {
      child.each{
        thisId ->
          _target {
            href ("#" + thisId)
          }
      }
    }
}
