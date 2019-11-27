package models

import org.specs2.mock._
import org.specs2.mutable._
import RaceTime.fromString

class RaceTimeSpec extends Specification with Mockito {

  "ValidRaceTime" should {
    "Instanciate from two Excel strings 1" in {
      val res: RaceTime = fromString("00:01:00", "00:21:00")
      res.isInstanceOf[ValidRaceTime] === true
      res.timeTaken === TimeValue(0, 20, 0)
    }

    "Fail to instanciate from two Excel strings 2" in {
      val res: RaceTime = fromString("0:01:00", "00:21:00")
      res.isInstanceOf[InvalidRaceTime] === true
    }

    "Fail to instanciate from two Excel strings 3" in {
      val res: RaceTime = fromString("00:01:00", "00:1:00")
      res.isInstanceOf[InvalidRaceTime] === true
    }

    "Fail to instanciate from two Excel strings 4" in {
      val res: RaceTime = fromString("00:0:00", "00:2:00")
      res.isInstanceOf[InvalidRaceTime] === true
    }
  }
}
