package models

import scala.Ordering.Double.TotalOrdering

case class Competitor(siCard: Long,
                      name: String,
                      ageCat: AgeCat,
                      club: String,
                      courseClass: String,
                      raceTime: RaceTime,
                      punchedControls: PunchedControls,
                      punchResults: PunchResults,
                      scores: Scores) {

  override def toString: String = s"\nCompetitor(Name: $name SiCard: $siCard AgeCat: $ageCat Club: $club Class: $courseClass\n" +
    s"$raceTime \t$punchedControls \n$punchResults \n$scores\n\n"
}

object Competitor {
  implicit def orderingByScoreThenName[A <: Competitor]: Ordering[A] =
    // Invert the time so that longer time is more negative i.e. smaller
    Ordering.by(e => (e.scores.finalScore, - e.raceTime.timeTaken.asSeconds))
}
