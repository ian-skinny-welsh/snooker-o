package helpers.scorers

import helpers.BallControlsInUse
import models.BallColour._
import models._

object AdultsScorer extends Scorer {

  def calcScoresForAllPunches(punchedControls: PunchedControls): PunchResults = {

    def processList(previous: List[PunchResult], remaining: List[ControlCode], requiredColour: BallColour ): PunchResults = {
      remaining match {
        case (thisCtrlCode::tail) => {
          val thisBallColour: BallColour = BallControlsInUse.getBallForControl(thisCtrlCode)

          thisBallColour match {
            case UnknownBall =>
              processList(previous :+ PunchResult(thisCtrlCode, UnknownBall, 0, "Unknown ctrl punched, will be ignored"), tail, requiredColour)

            case _ =>
              val hasClearedReds = hasClearedAllReds(previous)

              if (hasClearedReds) {
                clearTheColours(previous, remaining, requiredColour, processList)
              } else {
                val validPunchesOfThisCtrl = countHowManyValidPunchesOfThisCtrl(previous, thisCtrlCode)
                val thisScore = getScoreForThisPunch(validPunchesOfThisCtrl, thisBallColour)

                thisBallColour match {
                  case ballX if requiredColour.isARed && thisBallColour.isARed && validPunchesOfThisCtrl > 1 =>
                    processList(previous :+ PunchResult(thisCtrlCode, IllegalRed, ballX.getPenaltyValue, s"This 'Red' has been punched before"), tail, requiredColour)

                  case ballX if requiredColour.isARed && thisBallColour.isARed =>
                    processList(previous :+ PunchResult(thisCtrlCode, RedBall, thisScore, "Valid 'Red'"), tail, AnyColouredBall)

                  case ballX if requiredColour.isAColour && thisBallColour.isAColour =>
                    val nextBallColour = if (numberOfValidRedsPunched(previous) >= BallControlsInUse.getNumberOfReds) {
                      YellowBall
                    } else {
                      RedBall
                    }
                    val msgText = getNthText(validPunchesOfThisCtrl)
                    processList(previous :+ PunchResult(thisCtrlCode, thisBallColour, thisScore, s"Valid '${thisBallColour.toString}' - $msgText"), tail, nextBallColour)

                  case ballX if requiredColour.isAColour && thisBallColour.isARed =>
                    processList(previous :+ PunchResult(thisCtrlCode, IllegalRed, ballX.getPenaltyValue, s"'Red' punched when colour required"), tail, IllegalColour)

                  case ballX if requiredColour.isARed && thisBallColour.isAColour =>
                    processList(previous :+ PunchResult(thisCtrlCode, IllegalColour, ballX.getPenaltyValue, s"'${thisBallColour.toString}' punched when Red required"), tail, requiredColour)

                }
              }
          }
        }

        case Nil => PunchResults(previous)
      }
    }

    processList(List.empty, punchedControls.codes, RedBall)
  }
}
