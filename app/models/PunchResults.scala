package models

import helpers.AgeHandicaps

case class PunchResults(results: List[PunchResult]){

  def getBasicScore: Double = results.foldLeft(0D)((total, punch) => total + punch.score)

  def getFinalScores(raceTime: RaceTime, ageCat: AgeCat): Scores = {
    val eventData = EventData.getEventData
    val punchScore = getBasicScore

    raceTime match {
      case rt: ValidRaceTime =>
        val penalty = rt.getTimePenaltyPoints (eventData)
        Scores (punchScore, penalty, (punchScore - penalty) / AgeHandicaps.getHandicapForAge (ageCat) )

      case rt: InvalidRaceTime =>Scores(punchScore, 0, 0)

      case _ => Scores(punchScore, 0, 0)
    }
  }

  override def toString: String = {
    results.foldLeft("PunchResults[\n")((acc, res) => acc + "\t" + res.toString +",\n") + "]"
  }
}
