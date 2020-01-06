package helpers

import models.BallColour._
import models.{BallColour, ControlCode}

object BallControlsInUse {
  private var balls: Map[BallColour, List[ControlCode]] = getDefaultData

  def getBallControlsInUse: Map[BallColour, List[ControlCode]] = balls

  def setBallControlsInUse(newData: Map[BallColour, List[ControlCode]]): Unit = {
    newData.map{case (colour, list) => balls = balls + (colour ->list) }
  }

  def getNumberOfReds: Int = {
    getControlsForBall(RedBall).size
  }

  def getControlsForBall(ball: BallColour): List[ControlCode] = {
    balls.getOrElse(ball, List.empty)
  }

  def getBallForControl(findCtrl: ControlCode): BallColour = {
    val res = for(ballColour <- balls.keySet if balls(ballColour).contains(findCtrl))
      yield ballColour

    if(res.nonEmpty) res.head else UnknownBall
  }

  private def getDefaultData(): Map[BallColour, List[ControlCode]] = {
    Map(
      RedBall -> List(
        ControlCode(200),
        ControlCode(201),
        ControlCode(210),
        ControlCode(211),
        ControlCode(220),
        ControlCode(221),
        ControlCode(230),
        ControlCode(231),
        ControlCode(240),
        ControlCode(241)),
      YellowBall -> List(ControlCode(32)),
      GreenBall -> List(ControlCode(33)),
      BrownBall -> List(ControlCode(34)),
      BlueBall -> List(ControlCode(35)),
      PinkBall -> List(ControlCode(36)),
      BlackBall -> List(ControlCode(37)),
    )
  }

  def getControlsFromFileData(data: List[List[String]]): Map[BallColour, List[ControlCode]] = {
    val controls = data.map{ line =>
      val ballColour = BallColour.getBallFromName(line(0))

      val controls = line.drop(1).map(ctrlNum => ControlCode(ctrlNum.trim.toInt))
      (ballColour, controls)
    }

    val newControls = controls.toMap
    setBallControlsInUse((newControls))
    newControls
  }
}
