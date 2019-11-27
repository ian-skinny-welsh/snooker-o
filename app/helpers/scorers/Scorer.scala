package helpers.scorers

import helpers.BallControlsInUse
import models.BallColour.{IllegalColour, IllegalRed, RedBall, UnknownBall}
import models._

trait Scorer {
  // Each class scorer must implement it's own logic for this method
  def calcScoresForAllPunches(punchedControls: PunchedControls): PunchResults

  private[helpers] def getScoreForThisPunch(punchedNTimes: Int, ctrlBall: BallColour): Double = {
    ctrlBall.getPointsValue * calcScoreMultiplier(punchedNTimes)
  }

  protected def calcScoreMultiplier(nthPunch: Int): Double = {
    Math.max(0, 1-((nthPunch - 1) * 0.25))
  }

  protected def numberOfValidRedsPunched(previous: List[PunchResult]): Int = {
    previous.iterator.count(punch => punch.ball.isARed && punch.isValid)
  }

  // Punched all reds but not red and colour pairs
  protected def hasPunchedAllReds(previousPunches: List[PunchResult]): Boolean = {
    val redBalls = BallControlsInUse.getControlsForBall(RedBall)
    val validRedControls = previousPunches.filter(ctrl => ctrl.isValid && ctrl.ball.isARed).map(_.controlCode)
    // true if all redBalls are in list validRedControls
    redBalls.forall(validRedControls.contains)
  }

  // Has punched all the red colour pairs
  protected def hasClearedAllReds(previousPunches: List[PunchResult]): Boolean = {
    val numBallsToClearReds = BallControlsInUse.getNumberOfReds * 2
    if(previousPunches.filter(_.isValid).size < numBallsToClearReds){
      false
    } else {
      hasPunchedAllReds(previousPunches)
    }
  }

  private[helpers] def countHowManyPunchesOfThisCtrl(previousPunches: List[PunchResult], thisCtrlCode: ControlCode):Int = {
    previousPunches.iterator.count(punch => punch.controlCode == thisCtrlCode) + 1
  }

  private[helpers] def countHowManyValidPunchesOfThisCtrl(previousPunches: List[PunchResult], thisCtrlCode: ControlCode):Int = {
    previousPunches.iterator.count(punch => (punch.controlCode == thisCtrlCode) && (punch.ball.islegal)) + 1
  }

  protected def clearTheColours(previous: List[PunchResult], remaining: List[ControlCode], requiredColour: BallColour,
                                processList: (List[PunchResult], List[ControlCode], BallColour) => PunchResults) = {

    val thisCtrlCode = remaining.head

    val thisBallColour: BallColour = BallControlsInUse.getBallForControl(thisCtrlCode)

    thisBallColour match {
      case RedBall =>
        processList(previous :+ PunchResult(thisCtrlCode, IllegalRed, 0, "This 'Red' was punched while clearing the colours"), remaining.tail, requiredColour)

      case ballX if requiredColour == ballX =>
        processList(previous :+ PunchResult(thisCtrlCode, thisBallColour, thisBallColour.getPointsValue, s"Valid '${thisBallColour.toString}'"), remaining.tail, BallColour.getNextColour(ballX))

      case _ =>
        processList(previous :+ PunchResult(thisCtrlCode, IllegalColour, 0, s"'${thisBallColour.toString}' in wrong sequence when clearing the colours"), remaining.tail, UnknownBall)
    }
  }
}
