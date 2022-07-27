def id = 'WatercourseLink_' + fid

withTransformationContext{
  def c = _.context.collector(it)
  // collect id
  c.linkIDs << id
}

return id;
