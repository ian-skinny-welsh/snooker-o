package helpers

import helpers.scorers.{AdultsScorer, OpenOrderScorer, Under16Scorer}
import models.DataFileConstants.{CardNumbers, Category, Club, ControlCodesStart, CourseClass, FinishTime, Name, NumSplits, StartTime}
import models.RaceTime.fromString
import models.{AgeCat, Competitor, CourseClassType, PunchResults, PunchedControls}

object CompetitorProcessor {

  def getCompetitors(data: List[List[String]]): List[Competitor] = {
    data.map{line =>
      val numControls = line(NumSplits).toInt

      val punchedControls = PunchedControls.getControlsFromData(numControls, line.drop(ControlCodesStart))

      val ageCat = AgeCat.getAgeCat(line(Category))

      val courseClass = CourseClassType.getCourseClass(line(CourseClass))

      val raceTime = fromString(line(StartTime), line(FinishTime))

      val punchResults = processPunches(courseClass, punchedControls)

      val scores = punchResults.getFinalScores(raceTime, ageCat)

      val comp = Competitor(line(CardNumbers).toLong, line(Name),
        ageCat, line(Club),
        courseClass,
        raceTime,
        punchedControls,
        punchResults,
        scores)

      comp
    }
  }

  def processPunches(courseClass: CourseClassType, punchedControls: PunchedControls): PunchResults = {
    courseClass match {
      case CourseClassType.Adults => AdultsScorer.calcScoresForAllPunches(punchedControls)

      case CourseClassType.Under16 => Under16Scorer.calcScoresForAllPunches(punchedControls)

      case CourseClassType.OpenOrder => OpenOrderScorer.calcScoresForAllPunches(punchedControls)
    }
  }
}
