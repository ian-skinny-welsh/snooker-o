package models

import helpers.AgeHandicaps

case class Competitor(siCard: Long,
                      name: String,
                      ageCat: AgeCat,
                      club: String,
                      courseClass: CourseClassType,
                      raceTime: RaceTime,
                      punchedControls: PunchedControls,
                      punchResults: PunchResults,
                      scores: Scores) {

  // TODO:  Deal with time penalties
  // TODO:  Add tests for AgeHandicap helper
  // TODO:  FIND OUT WHY BOOTSTRAP HAS BROKEN THE TESTS
  val score = (punchResults.getBasicScore) / AgeHandicaps.getHandicapForAge(ageCat)

  override def toString: String = s"\nCompetitor(Name: $name SiCard: $siCard AgeCat: $ageCat Club: $club Class: $courseClass\n" +
    s"$raceTime \t$punchedControls \n$punchResults \n$scores\n\n"

}
