package helpers

import helpers.scorers.AdultsScorer
import org.specs2.mock._
import org.specs2.mutable._
import models.BallColour._
import models.ControlCode
import models.PunchedControls

class RealDataSpec extends Specification with Mockito {

  "RealData" should {
    "Gavin Clegg - Score 780 for this data" in {
      val ctrlCodes = List[Int](200, 37, 211, 37, 240, 37, 221, 33, 220, 36, 230, 36, 210, 34, 241, 34, 201, 35, 231, 35, 200, 32, 33, 34, 35, 36, 37)
      val testData = PunchedControls(ctrlCodes.size, ctrlCodes.map(ControlCode(_)))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 780
    }

    "Robyn Carter - Score 780 for this data" in {
      val ctrlCodes = List[Int](200,37,221,37,240,37,211,33,220,36,210,36,241,36,201,35,230,35,34,231,34,32,33,34,35,36,37)
      val testData = PunchedControls(ctrlCodes.size, ctrlCodes.map(ControlCode(_)))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 780
    }

  }
}
