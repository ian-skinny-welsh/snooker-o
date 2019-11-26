package helpers

import models.BallColour.{AnyColouredBall, BlackBall, BlueBall, BrownBall, GreenBall, IllegalColour, IllegalRed, PinkBall, RedBall, UnknownBall, YellowBall}
import models.{BallColour, ControlCode, PunchResult, PunchResults, PunchedControls}

object ScoreCalculator {

  def getScoreForThisPunch(punchedNTimes: Int, ctrlBall: BallColour): Double = {
    ctrlBall.getPointsValue * calcScoreMultiplier(punchedNTimes)
  }

  private def calcScoreMultiplier(nthPunch: Int): Double = {
    Math.max(0, 1-((nthPunch - 1) * 0.25))
  }

  def calcScoresForAllPunches(punchedControls: PunchedControls): PunchResults = {

    def processList(previous: List[PunchResult], remaining: List[ControlCode], requiredColour: BallColour ): PunchResults = {
      remaining match {
        case (thisCtrlCode::tail) => {
          val thisBallColour: BallColour = BallControlsInUse.getBallForControl(thisCtrlCode)
          val hasClearedReds:Option[Int] = hasClearedAllReds(previous)

          val validPunchesOfThisCtrl = countHowManyValidPunchesOfThisCtrl(previous, thisCtrlCode)
          val thisScore = getScoreForThisPunch(validPunchesOfThisCtrl, thisBallColour)

          hasClearedReds match {
            case None =>
              thisBallColour match {
                case UnknownBall =>
                  processList(previous :+ PunchResult(thisCtrlCode, UnknownBall, 0, "Unknown ctrl punched, will be ignored"), tail, requiredColour)

                case ballX if requiredColour.isARed && thisBallColour.isARed && validPunchesOfThisCtrl > 1 =>
                  processList(previous :+ PunchResult(thisCtrlCode, IllegalRed, 0, "This 'Red' has been punched before"), tail, requiredColour)

                case ballX if requiredColour.isARed && thisBallColour.isARed =>
                  processList(previous :+ PunchResult(thisCtrlCode, RedBall, thisScore, "Valid 'Red'"), tail, AnyColouredBall)

                case ballX if requiredColour.isAColour && thisBallColour.isAColour =>
                  val nextBallColour = if(numberOfValidRedsPunched(previous) >= BallControlsInUse.getNumberOfReds) {
                    YellowBall
                  } else {
                    RedBall
                  }
                  processList(previous :+ PunchResult(thisCtrlCode, thisBallColour, thisScore, s"Valid '${thisBallColour.toString}' punched"), tail, nextBallColour)

                case ballX if requiredColour.isAColour && thisBallColour.isARed =>
                  processList(previous :+ PunchResult(thisCtrlCode, IllegalRed, 0, s"'${thisBallColour.toString}' punched when colour required, next colour will score zero"), tail, IllegalColour)

                case ballX if requiredColour.isARed && thisBallColour.isAColour =>
                  processList(previous :+ PunchResult(thisCtrlCode, IllegalColour, 0, s"'${thisBallColour.toString}' punched when red required"), tail, requiredColour)

              }

            case Some(redsClearedAt) =>
              thisBallColour match {
                case UnknownBall =>
                  processList(previous :+ PunchResult(thisCtrlCode, UnknownBall, 0, "Unknown ctrl punched, will be ignored"), tail, requiredColour)

                case RedBall =>
                  processList(previous :+ PunchResult(thisCtrlCode, IllegalRed, 0, "This 'Red' was punched while clearing the colours"), tail, requiredColour)

                case YellowBall if requiredColour == YellowBall =>
                  processList(previous :+ PunchResult(thisCtrlCode, thisBallColour, thisBallColour.getPointsValue, s"Valid '${thisBallColour.toString}'"), tail, GreenBall)

                case GreenBall if requiredColour == GreenBall =>
                  processList(previous :+ PunchResult(thisCtrlCode, thisBallColour, thisBallColour.getPointsValue, s"Valid '${thisBallColour.toString}'"), tail, BrownBall)

                case BrownBall if requiredColour == BrownBall =>
                  processList(previous :+ PunchResult(thisCtrlCode, thisBallColour, thisBallColour.getPointsValue, s"Valid '${thisBallColour.toString}'"), tail, BlueBall)

                case BlueBall if requiredColour == BlueBall =>
                  processList(previous :+ PunchResult(thisCtrlCode, thisBallColour, thisBallColour.getPointsValue, s"Valid '${thisBallColour.toString}'"), tail, PinkBall)

                case PinkBall if requiredColour == PinkBall =>
                  processList(previous :+ PunchResult(thisCtrlCode, thisBallColour, thisBallColour.getPointsValue, s"Valid '${thisBallColour.toString}'"), tail, BlackBall)

                case BlackBall if requiredColour == BlackBall =>
                  processList(previous :+ PunchResult(thisCtrlCode, thisBallColour, thisBallColour.getPointsValue, s"Valid '${thisBallColour.toString}'"), tail, UnknownBall)

                case _ =>
                    processList(previous :+ PunchResult(thisCtrlCode, IllegalColour, 0, s"'${thisBallColour.toString}' in wrong sequence when clearing the colours"), tail, UnknownBall)
            }
          }
        }

        case Nil => PunchResults(previous)
      }
    }

    processList(List.empty, punchedControls.codes, RedBall)
  }

  private def numberOfValidRedsPunched(previous: List[PunchResult]): Int = {
    previous.iterator.count(punch => punch.ball.isARed && punch.isValid)
  }

  private def hasClearedAllReds(previousPunches: List[PunchResult]):Option[Int] = {
    val numBallsToClearReds = BallControlsInUse.getNumberOfReds * 2
    if(previousPunches.size < numBallsToClearReds){
      None
    } else {
      val punchedAllReds = hasPunchedAllReds(previousPunches)
      punchedAllReds
    }
  }

  private def hasPunchedAllReds(previousPunches: List[PunchResult]):Option[Int] = {
    val redBalls = BallControlsInUse.getControlsForBall(RedBall)
    val punchedAllReds = redBalls.forall(ball => previousPunches.exists(punched => punched.controlCode == ball && punched.isValid))
    if(punchedAllReds) findLastValidRed(previousPunches) else None
  }

  private def findLastValidRed(previousPunches: List[PunchResult]):Option[Int] = {
    val reds = previousPunches.zipWithIndex.filter(punchResult => punchResult._1.ball == RedBall && punchResult._1.isValid).map(_._2)
    if(reds.nonEmpty) {
      val lastRedIndex = reds.reverse.headOption.get + 1
      // Check if there is a valid colour after the last red
      if(previousPunches.size > lastRedIndex){
        if(previousPunches(lastRedIndex).isValid && previousPunches(lastRedIndex).ball.isAColour){
           return Some(lastRedIndex + 1)
        }
      }
    }

    None
  }

  private[helpers] def countHowManyValidPunchesOfThisCtrl(previousPunches: List[PunchResult], thisCtrlCode: ControlCode):Int = {
    previousPunches.iterator.count(punch => (punch.controlCode == thisCtrlCode) && (punch.ball.islegal)) + 1
  }
}
