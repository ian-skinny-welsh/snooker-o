package models

import scala.Ordering.Double.TotalOrdering

case class Competitor(siCard: Long,
                      name: String,
                      ageCat: AgeCat,
                      club: String,
                      courseClass: CourseClassType,
                      raceTime: RaceTime,
                      punchedControls: PunchedControls,
                      punchResults: PunchResults,
                      scores: Scores) {

  // TODO:  FIND OUT WHY BOOTSTRAP HAS BROKEN THE TESTS
  val score = scores.finalScore

  override def toString: String = s"\nCompetitor(Name: $name SiCard: $siCard AgeCat: $ageCat Club: $club Class: $courseClass\n" +
    s"$raceTime \t$punchedControls \n$punchResults \n$scores\n\n"

}

object Competitor {
  implicit def orderingByScoreThenName[A <: Competitor]: Ordering[A] =
    Ordering.by(e => (e.score, e.name))
}
