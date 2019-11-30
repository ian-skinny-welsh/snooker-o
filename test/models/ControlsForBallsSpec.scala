package models

import org.specs2.mock._
import org.specs2.mutable._
import ControlsForBalls._

class ControlsForBallsSpec extends Specification with Mockito {

  val testData = ControlsForBalls(
    "101, 102, 199, 208", //Red
    "121", //Yellow
    "131", //Green
    "142", //Brown
    "153", //Blue
    "164", //Pink
    "178" //Black
   )

  "ControlsForBalls" should {
    "Convert '121' to a ControlCode" in {
      testData.convertStringToControls(testData.yellow) === List(ControlCode(121))
    }

    "Convert '178' to a ControlCode" in {
      testData.convertStringToControls(testData.black) === List(ControlCode(178))
    }

    "Convert '101, 102, 199, 208' to a ControlCode" in {
      testData.convertStringToControls(testData.red) === List(ControlCode(101), ControlCode(102), ControlCode(199), ControlCode(208))
    }

  }

}
