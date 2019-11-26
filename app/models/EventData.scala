package models

case class EventData(timeAllowed: TimeValue, penaltyPerMinute: Int)

object EventData {
  private val defaultTimeAllowed: TimeValue = TimeValue(1, 0, 0)

  private val defaultPenaltyPerMinute = 20

  var inUSeValues:EventData = EventData(defaultTimeAllowed, defaultPenaltyPerMinute)

  def getEventData = inUSeValues

  def setEventData(newTimeAllowed: TimeValue = defaultTimeAllowed, newPenaltyPerMin: Int = defaultPenaltyPerMinute) = {
    inUSeValues = EventData(newTimeAllowed, newPenaltyPerMin)
  }
}
