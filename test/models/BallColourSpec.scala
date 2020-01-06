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

    "Score -40 points for Red through Brown" in {
      RedBall.getPenaltyValue === -40
      YellowBall.getPenaltyValue === -40
      GreenBall.getPenaltyValue === -40
      BrownBall.getPenaltyValue === -40
    }

    "Score -50, -60, -70 for Blue, Pink and Black" in {
      BlueBall.getPenaltyValue === -50
      PinkBall.getPenaltyValue === -60
      BlackBall.getPenaltyValue === -70
    }

    "Return the correct BallColour from the string name" in {
      BallColour.getBallFromName("Red") === RedBall
      BallColour.getBallFromName("Pink") === PinkBall
    }

    "Return the correct next colour" in {
      BallColour.getNextColour(RedBall) === YellowBall
      BallColour.getNextColour(YellowBall) === GreenBall
      BallColour.getNextColour(GreenBall) === BrownBall
      BallColour.getNextColour(BrownBall) === BlueBall
      BallColour.getNextColour(BlueBall) === PinkBall
      BallColour.getNextColour(PinkBall) === BlackBall
      BallColour.getNextColour(BlackBall) === IllegalColour
      BallColour.getNextColour(IllegalColour) === IllegalColour
    }

    "Red checks" in {
      RedBall.isAColour === false
      RedBall.isARed === true
      RedBall.islegal === true
    }

    "Yellow checks" in {
      YellowBall.isAColour === true
      YellowBall.isARed === false
      YellowBall.islegal === true
    }
  }

}
