package models

import scala.language.implicitConversions

trait RaceTime {
  val startTime: TimeValue
  val finishTime: TimeValue
  val timeTaken: TimeValue = finishTime - startTime

  def getTimePenaltyPoints(eventData: EventData): Int = {
    if (timeTaken > eventData.timeAllowed) {
      (timeTaken - eventData.timeAllowed).asMinutes * eventData.penaltyPerMinute
    } else {
      0
    }
  }

  override def toString: String = s"Start: $startTime Finish: $finishTime Time: $timeTaken"
}


object RaceTime {

  case class ValidRaceTime(startTime: TimeValue, finishTime: TimeValue) extends RaceTime

  case class InvalidRaceTime(startTime: TimeValue, finishTime: TimeValue, st2: String, ft2: String) extends RaceTime {
    override val timeTaken = TimeValue(0, 0, 0)

    override def toString = s"**** Invalid times read from file **** Start: $st2, Finish: $ft2"
  }

  implicit def fromString(start: String, finish: String): RaceTime = {
    val startTime: Option[TimeValue] = start
    val finishTime: Option[TimeValue] = finish

    (startTime, finishTime) match {
      case (Some(st), Some(fn)) if fn > st => ValidRaceTime(st, fn)

      case _ => InvalidRaceTime(TimeValue(0,0,0), TimeValue(0,0,0), start, finish)
    }
  }
}