package helpers

import org.specs2.mock._
import org.specs2.mutable._
import models.BallColour._
import models.ControlCode

class BallControlsInUseSpec extends Specification with Mockito {

  "BallControlsInUse - default controls" should {
    "Return the default data when no specific data has been set up" in {
      val data = BallControlsInUse.getBallControlsInUse
      data.size === 7
      data.keySet.contains(RedBall)
      data(RedBall) === List(
        ControlCode(101),
        ControlCode(102),
        ControlCode(103),
        ControlCode(104),
        ControlCode(105),
        ControlCode(106),
        ControlCode(107),
        ControlCode(108),
        ControlCode(109),
        ControlCode(110))

      data.keySet.contains(YellowBall)
      data(YellowBall) === List(ControlCode(121))

      data.keySet.contains(GreenBall)
      data(GreenBall) === List(ControlCode(131))

      data.keySet.contains(BrownBall)
      data(BrownBall) === List(ControlCode(141))

      data.keySet.contains(BlueBall)
      data(BlueBall) === List(ControlCode(151))

      data.keySet.contains(PinkBall)
      data(PinkBall) === List(ControlCode(161))

      data.keySet.contains(BlackBall)
      data(BlackBall) === List(ControlCode(171))
    }

    "Return RedBall for codes 101 to 110" in {
      BallControlsInUse.getBallForControl(ControlCode(101)) === RedBall
      BallControlsInUse.getBallForControl(ControlCode(102)) === RedBall
      BallControlsInUse.getBallForControl(ControlCode(103)) === RedBall
      BallControlsInUse.getBallForControl(ControlCode(108)) === RedBall
      BallControlsInUse.getBallForControl(ControlCode(110)) === RedBall
    }

    "Return the correct colour for other codes" in {
      BallControlsInUse.getBallForControl(ControlCode(121)) === YellowBall
      BallControlsInUse.getBallForControl(ControlCode(131)) === GreenBall
      BallControlsInUse.getBallForControl(ControlCode(141)) === BrownBall
      BallControlsInUse.getBallForControl(ControlCode(151)) === BlueBall
      BallControlsInUse.getBallForControl(ControlCode(161)) === PinkBall
      BallControlsInUse.getBallForControl(ControlCode(171)) === BlackBall
    }

    "Return IllegalBall for illegal controls" in {
      BallControlsInUse.getBallForControl(ControlCode(10)) === UnknownBall
      BallControlsInUse.getBallForControl(ControlCode(100)) === UnknownBall
      BallControlsInUse.getBallForControl(ControlCode(120)) === UnknownBall
      BallControlsInUse.getBallForControl(ControlCode(132)) === UnknownBall
      BallControlsInUse.getBallForControl(ControlCode(1234)) === UnknownBall
      BallControlsInUse.getBallForControl(ControlCode(98736)) === UnknownBall
    }

    "Return an empty list for UnknownBall type" in {
      BallControlsInUse.getControlsForBall(UnknownBall) === List.empty
    }
  }

}
