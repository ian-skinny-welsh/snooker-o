package helpers.scorers

import helpers.BallControlsInUse
import models.BallColour._
import models._

object Under16Scorer extends Scorer {

  private val POINTS_FOR_RED = RedBall.getPointsValue

  def calcScoresForAllPunches(punchedControls: PunchedControls): PunchResults = {

    def processList(previous: List[PunchResult], remaining: List[ControlCode], requiredColour: BallColour ): PunchResults = {

      remaining match {
        case (thisCtrlCode::tail) => {
          val thisBallColour: BallColour = BallControlsInUse.getBallForControl(thisCtrlCode)

          thisBallColour match {
            case UnknownBall =>
              processList(previous :+ PunchResult(thisCtrlCode, UnknownBall, 0, "Unknown ctrl punched, will be ignored"), tail, requiredColour)

            case _ =>
              val hasPunchedAllTheReds = hasPunchedAllReds(previous)

              if (hasPunchedAllTheReds) {
                clearTheColours(previous, remaining, requiredColour, processList)
              } else {
                val validPunchesOfThisCtrl = countHowManyValidPunchesOfThisCtrl(previous, thisCtrlCode)

                thisBallColour match {
                  case ballX if requiredColour.isARed && thisBallColour.isARed && validPunchesOfThisCtrl > 1 =>
                    processList(previous :+ PunchResult(thisCtrlCode, IllegalRed, ballX.getPenaltyValue, "This 'Red' has been punched before"), tail, requiredColour)

                  case _ if requiredColour.isARed && thisBallColour.isARed =>
                    val nextBallColour = if (numberOfValidRedsPunched(previous) + 1 >= BallControlsInUse.getNumberOfReds) {
                      YellowBall
                    } else {
                      RedBall
                    }
                    processList(previous :+ PunchResult(thisCtrlCode, thisBallColour, POINTS_FOR_RED, "Valid 'Red'"), tail, nextBallColour)

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
