package models

case class Scores(punchScore: Double, timePenalty: Int, finalScore: Double) {
  def getFinalScore: String = f"$finalScore%.2f"

  override def toString: String = f"Punched points: ${punchScore.toInt}%d  Penalty points: $timePenalty  Final score: $finalScore%.2f"
}
