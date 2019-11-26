package models

import org.specs2.mock._
import org.specs2.mutable._
import BallColour._

class BallColourSpec extends Specification with Mockito {

  "BallColour" should {
    "Score 10 points for the Red ball" in {
      RedBall.getPointsValue === 10
    }

    "Score 50 points for the Blue ball" in {
      BlueBall.getPointsValue === 50
    }

    "Score 70 points for the Black ball" in {
      BlackBall.getPointsValue === 70
    }
  }

}
