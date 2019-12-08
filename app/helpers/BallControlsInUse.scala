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
        ControlCode(101),
        ControlCode(102),
        ControlCode(103),
        ControlCode(104),
        ControlCode(105),
        ControlCode(106),
        ControlCode(107),
        ControlCode(108),
        ControlCode(109),
        ControlCode(110)),
      YellowBall -> List(ControlCode(121)),
      GreenBall -> List(ControlCode(131)),
      BrownBall -> List(ControlCode(141)),
      BlueBall -> List(ControlCode(151)),
      PinkBall -> List(ControlCode(161)),
      BlackBall -> List(ControlCode(171)),

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
