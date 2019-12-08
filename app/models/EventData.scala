package models

case class EventData(timeAllowed: TimeValue, penaltyPerMinute: Int)

object EventData {
  private val defaultTimeAllowed: TimeValue = TimeValue(1, 0, 0)

  private val defaultPenaltyPerMinute = 20

  private var inUseValues:EventData = EventData(defaultTimeAllowed, defaultPenaltyPerMinute)

  def getEventData = inUseValues

  def setEventData(newTimeAllowed: TimeValue = defaultTimeAllowed, newPenaltyPerMin: Int = defaultPenaltyPerMinute) = {
    inUseValues = EventData(newTimeAllowed, newPenaltyPerMin)
  }
}
