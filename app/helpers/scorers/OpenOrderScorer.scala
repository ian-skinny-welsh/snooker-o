package helpers.scorers

import helpers.BallControlsInUse
import models.BallColour._
import models._

object OpenOrderScorer extends Scorer {

  def calcScoresForAllPunches(punchedControls: PunchedControls): PunchResults = {

    def processList(previous: List[PunchResult], remaining: List[ControlCode]): PunchResults = {
      remaining match {
        case (thisCtrlCode::tail) => {
          val thisBallColour: BallColour = BallControlsInUse.getBallForControl(thisCtrlCode)

          val validPunchesOfThisCtrl = countHowManyPunchesOfThisCtrl(previous, thisCtrlCode)

            thisBallColour match {
              case UnknownBall =>
                processList(previous :+ PunchResult(thisCtrlCode, UnknownBall, 0, "Unknown ctrl punched, will be ignored"), tail)

              case _ if validPunchesOfThisCtrl > 1 =>
                processList(previous :+ PunchResult(thisCtrlCode, IllegalColour, 0, s"$thisCtrlCode '${thisBallColour.toString}' has been punched before"), tail)

              case _ =>
                processList(previous :+ PunchResult(thisCtrlCode, thisBallColour, thisBallColour.getPointsValue, s"Valid '${thisBallColour.toString}'"), tail)
            }
          }

        case Nil => PunchResults(previous)
      }
    }

    processList(List.empty, punchedControls.codes)
  }
}
