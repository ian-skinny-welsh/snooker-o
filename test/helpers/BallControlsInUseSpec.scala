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
        ControlCode(200),
        ControlCode(201),
        ControlCode(210),
        ControlCode(211),
        ControlCode(220),
        ControlCode(221),
        ControlCode(230),
        ControlCode(231),
        ControlCode(240),
        ControlCode(241))

      data.keySet.contains(YellowBall)
      data(YellowBall) === List(ControlCode(32))

      data.keySet.contains(GreenBall)
      data(GreenBall) === List(ControlCode(33))

      data.keySet.contains(BrownBall)
      data(BrownBall) === List(ControlCode(34))

      data.keySet.contains(BlueBall)
      data(BlueBall) === List(ControlCode(35))

      data.keySet.contains(PinkBall)
      data(PinkBall) === List(ControlCode(36))

      data.keySet.contains(BlackBall)
      data(BlackBall) === List(ControlCode(37))
    }

    "Return RedBall for codes 200 to 241" in {
      BallControlsInUse.getBallForControl(ControlCode(200)) === RedBall
      BallControlsInUse.getBallForControl(ControlCode(201)) === RedBall
      BallControlsInUse.getBallForControl(ControlCode(210)) === RedBall
      BallControlsInUse.getBallForControl(ControlCode(221)) === RedBall
      BallControlsInUse.getBallForControl(ControlCode(241)) === RedBall
    }

    "Return the correct colour for other codes" in {
      BallControlsInUse.getBallForControl(ControlCode(32)) === YellowBall
      BallControlsInUse.getBallForControl(ControlCode(33)) === GreenBall
      BallControlsInUse.getBallForControl(ControlCode(34)) === BrownBall
      BallControlsInUse.getBallForControl(ControlCode(35)) === BlueBall
      BallControlsInUse.getBallForControl(ControlCode(36)) === PinkBall
      BallControlsInUse.getBallForControl(ControlCode(37)) === BlackBall
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
