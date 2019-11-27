package helpers

import helpers.scorers.AdultsScorer
import org.specs2.mock._
import org.specs2.mutable._
import models.BallColour._
import models.{ControlCode, PunchResult, PunchedControls}

class ScoreCalculatorSpec extends Specification with Mockito {

  private val defaultBalls = BallControlsInUse.getBallControlsInUse
  private val RED_1 = defaultBalls(RedBall).head
  private val RED_2 = defaultBalls(RedBall).drop(1).head
  private val RED_3 = defaultBalls(RedBall).drop(2).head
  private val RED_4 = defaultBalls(RedBall).drop(3).head
  private val RED_5 = defaultBalls(RedBall).drop(4).head
  private val RED_6 = defaultBalls(RedBall).drop(5).head
  private val RED_7 = defaultBalls(RedBall).drop(6).head
  private val RED_8 = defaultBalls(RedBall).drop(7).head
  private val RED_9 = defaultBalls(RedBall).drop(8).head
  private val RED_10 = defaultBalls(RedBall).drop(9).head
  private val YELLOW = defaultBalls(YellowBall).head
  private val GREEN = defaultBalls(GreenBall).head
  private val BROWN = defaultBalls(BrownBall).head
  private val BLUE = defaultBalls(BlueBall).head
  private val PINK = defaultBalls(PinkBall).head
  private val BLACK = defaultBalls(BlackBall).head
  private val ILLEGALRED = IllegalRed

  "Scorer - basic scores" should {
    "Score full points the first time" in {
      AdultsScorer.getScoreForThisPunch(1, RedBall) === RedBall.getPointsValue
    }

    "Score 3/4 points the second time" in {
      AdultsScorer.getScoreForThisPunch(2, BlueBall) === (BlueBall.getPointsValue * 0.75)
    }

    "Score 1/2 points the third time" in {
      AdultsScorer.getScoreForThisPunch(3, PinkBall) === (PinkBall.getPointsValue * 0.5)
    }

    "Score 1/4 points the forth time" in {
      AdultsScorer.getScoreForThisPunch(4, RedBall) === (RedBall.getPointsValue * 0.25)
    }

    "Score no points the fith time" in {
      AdultsScorer.getScoreForThisPunch(5, RedBall) === 0
    }

    "Score no points the 20th time" in {
      AdultsScorer.getScoreForThisPunch(20, RedBall) === 0
    }
  }

  "Scorer - Red then another ball" should {
    "Score 10 for Red - another Red" in {
      val testData = PunchedControls(2, List[ControlCode](RED_1, RED_2))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 10
    }

    "Score 30 for Red - Yellow" in {
      val testData = PunchedControls(2, List[ControlCode](RED_1, YELLOW))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 30
    }

    "Score 40 for Red - Green" in {
      val testData = PunchedControls(2, List[ControlCode](RED_1, GREEN))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 40
    }

    "Score 80 for Red - Black" in {
      val testData = PunchedControls(2, List[ControlCode](RED_1, BLACK))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 80
    }
  }

  "Scorer - Red, colour, red, different colour" should {
    "Score 70 for Red, Yellow, Red, Green" in {
      val testData = PunchedControls(4, List[ControlCode](RED_1, YELLOW, RED_2, GREEN))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 70
    }

    "Score 120 for Red, Black, Red, Green" in {
      val testData = PunchedControls(4, List[ControlCode](RED_1, BLACK, RED_2, GREEN))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 120
    }
  }

  "Scorer - Red, colour, red, same colour" should {
    "Score 25 for Red, Yellow, Red, Yellow" in {
      val testData = PunchedControls(4, List[ControlCode](RED_1, YELLOW, RED_2, YELLOW))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 55
    }

    "Score 125 for Red, Pink, Red, Pink" in {
      val testData = PunchedControls(4, List[ControlCode](RED_1, PINK, RED_2, PINK))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 125
    }
  }

  "Scorer - All ctrls punched" should {
    "Score 810 for all the reds and colours in best order" in {
      val testData = PunchedControls(4, List[ControlCode](
        RED_1, YELLOW, //30
        RED_2, GREEN,  //40
        RED_3, BROWN,  //50
        RED_4, BLUE,   //60
        RED_5, PINK,   //70
        RED_6, BLACK,  //80
        RED_7, BLACK,  //62.5
        RED_9, PINK,   //55
        RED_10, BLUE,  //47.5
        RED_8, BLACK,  //45
        YELLOW, GREEN, BROWN, BLUE, PINK, BLACK)) //270 = 810
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 810
    }

    "Score 540 for all the reds but all colours in the wrong order" in {
      val testData = PunchedControls(4, List[ControlCode](
        RED_1, YELLOW, //30
        RED_2, GREEN,  //40
        RED_3, BROWN,  //50
        RED_4, BLUE,   //60
        RED_5, PINK,   //70
        RED_6, BLACK,  //80
        RED_7, BLACK,  //62.5
        RED_9, PINK,   //55
        RED_10, BLUE,  //47.5
        RED_8, BLACK,  //45
        GREEN, YELLOW, BROWN, BLUE, PINK, BLACK)) //0 = 540
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 540
    }

    "Score 560 for all the reds but only yellow in the right order" in {
      val testData = PunchedControls(4, List[ControlCode](
        RED_1, YELLOW, //30
        RED_2, GREEN,  //40
        RED_3, BROWN,  //50
        RED_4, BLUE,   //60
        RED_5, PINK,   //70
        RED_6, BLACK,  //80
        RED_7, BLACK,  //62.5
        RED_9, PINK,   //55
        RED_10, BLUE,  //47.5
        RED_8, BLACK,  //45
        YELLOW, BROWN, GREEN, BROWN, BLUE, PINK, BLACK)) //0 = 560
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 560
    }
  }

  "Scorer - Red, colour, same red, any colour" should {
    "Score 30 for Red, Yellow, Same Red, Pink" in {
      val testData = PunchedControls(4, List[ControlCode](RED_1, YELLOW, RED_1, PINK))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 30
    }

    "Score 40 for Red, Yellow, Same Red, Pink, Red" in {
      val testData = PunchedControls(5, List[ControlCode](RED_1, YELLOW, RED_1, PINK, RED_2))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 40
    }

    "Score 100 for Red, Yellow, Same Red, Pink, Red, Pink" in {
      val testData = PunchedControls(6, List[ControlCode](RED_1, YELLOW, RED_1, PINK, RED_2, PINK))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 100
    }

    "Score 100 for Red, Yellow, Same Red, Pink, Red, Pink, repeated Red, Black" in {
      val testData = PunchedControls(6, List[ControlCode](RED_1, YELLOW, RED_1, PINK, RED_2, PINK, RED_2, BLACK))
      val res = AdultsScorer.calcScoresForAllPunches(testData)
      res.getBasicScore === 100
    }
  }


  "Scorer - countHowManyValidPunchesOfThisCtrl" should {
    "Return 1 if control not in the list" in {
      val testData = List[PunchResult](PunchResult(RED_1, RedBall, 10, "Valid"),PunchResult(RED_2, RedBall, 10, "Valid"))
      val res = AdultsScorer.countHowManyValidPunchesOfThisCtrl(testData, RED_3)
      res === 1
    }

    "Return 2 if control is in the list once" in {
      val testData = List[PunchResult](PunchResult(RED_1, RedBall, 10, "Valid"),PunchResult(RED_2, RedBall, 10, "Valid"),PunchResult(RED_3, RedBall, 10, "Valid"))
      val res = AdultsScorer.countHowManyValidPunchesOfThisCtrl(testData, RED_2)
      res === 2
    }

    "Return 2 if control is valid once in the list" in {
      val testData = List[PunchResult](PunchResult(RED_1, RedBall, 10, "Valid"),PunchResult(RED_2, RedBall, 10, "Valid"),PunchResult(RED_2, ILLEGALRED, 0, "Invalid as zero points"))
      val res = AdultsScorer.countHowManyValidPunchesOfThisCtrl(testData, RED_2)
      res === 2
    }

  }

}
