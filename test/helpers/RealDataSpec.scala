package helpers

import helpers.scorers.AdultsScorer
import org.specs2.mock._
import org.specs2.mutable._
import models.BallColour._
import models.ControlCode
import models.PunchedControls

class RealDataSpec extends Specification with Mockito {

  "RealData" should {
    "Gavin Clegg - Score 820 for this data" in {
      val ctrlCodes = List[Int](101, 171, 104, 171, 109, 171, 106, 131, 105, 161, 107, 161, 103, 141, 110, 141, 102, 151, 108, 151, 101, 121, 131, 141, 151, 161, 171)
      val testData = PunchedControls(ctrlCodes.size, ctrlCodes.map(ControlCode(_)))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 820
    }

    "Robyn Carter - Score 820 for this data" in {
      val ctrlCodes = List[Int](101,171,106,171,109,171,104,131,105,161,103,161,110,161,102,151,107,151,141,108,141,121,131,141,151,161,171)
      val testData = PunchedControls(ctrlCodes.size, ctrlCodes.map(ControlCode(_)))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 820
    }

  }
}
