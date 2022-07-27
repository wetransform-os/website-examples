boolean hatFahrbahnachse = !!_source.links.AX_Fahrbahnachse.first()
boolean hatStrassenachse = !!_source.links.AX_Strassenachse.first()

def strasseId = _source.p.id.value()
if (!hatFahrbahnachse && !hatStrassenachse) {
	_log.error("ERoad: Straße $strasseId hat weder Fahrbahnachsen noch Straßenachsen")
	throw new NoResultException('Kann kein ERoad-Objekt ohne RoadLinks erstellen')
}