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
                    processList(previous :+ PunchResult(thisCtrlCode, IllegalRed, 0, "This 'Red' has been punched before"), tail, requiredColour)

                  case ballX if requiredColour.isARed && thisBallColour.isARed =>
                    processList(previous :+ PunchResult(thisCtrlCode, RedBall, thisScore, "Valid 'Red'"), tail, AnyColouredBall)

                  case ballX if requiredColour.isAColour && thisBallColour.isAColour =>
                    val nextBallColour = if (numberOfValidRedsPunched(previous) >= BallControlsInUse.getNumberOfReds) {
                      YellowBall
                    } else {
                      RedBall
                    }
                    processList(previous :+ PunchResult(thisCtrlCode, thisBallColour, thisScore, s"Valid '${thisBallColour.toString}' punched"), tail, nextBallColour)

                  case ballX if requiredColour.isAColour && thisBallColour.isARed =>
                    processList(previous :+ PunchResult(thisCtrlCode, IllegalRed, 0, s"'${thisBallColour.toString}' punched when colour required, next colour will score zero"), tail, IllegalColour)

                  case ballX if requiredColour.isARed && thisBallColour.isAColour =>
                    processList(previous :+ PunchResult(thisCtrlCode, IllegalColour, 0, s"'${thisBallColour.toString}' punched when Red required"), tail, requiredColour)

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
