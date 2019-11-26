package models

case class PunchResult(controlCode: ControlCode, ball: BallColour, score: Double, description: String) {
  def isValid = score > 0D

  override def toString: String = {
    f"PunchResult(Ctrl: $controlCode%s Type: $ball%s Points: ${score.toInt}%d Reason: $description%s)"
  }
}
