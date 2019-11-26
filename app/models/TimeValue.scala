package models

import scala.language.implicitConversions
import models.DataFileConstants.validInput

case class TimeValue(hours: Int, mins: Int, secs: Int) {
  val asMinutes = hours * 60 + mins + (if(secs > 0) 1 else 0)

  val asSeconds = (hours * 60 * 60) + (mins * 60) + secs

  def secondsToTimeValue(seconds: Int): TimeValue = {
    val hours: Int = seconds / 3600
    val mins: Int = (seconds - (hours * 3600)) / 60
    val secs: Int = seconds - (hours * 3600) - (mins * 60)
    TimeValue(hours, mins, secs)
  }

  def >(that: TimeValue):Boolean = this.asSeconds > that.asSeconds

  def <(that: TimeValue):Boolean = !(this > that)

  def -(that: TimeValue):TimeValue = secondsToTimeValue(this.asSeconds - that.asSeconds)

  override def toString = f"$hours%d:$mins%02d:$secs%02d"
}

object TimeValue {
  implicit def fromString(str: String):Option[TimeValue] = {
    str match {
      case validInput() => {
        val parts = str.split(":").map(_.toInt)
        Some(TimeValue(parts(0), parts(1), parts(2)))
      }

      case _ => None
    }
  }
}
