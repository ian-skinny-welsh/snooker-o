package helpers

import helpers.scorers.{AdultsScorer, OpenOrderScorer, Under16Scorer}
import models.BallColour.{ RedBall, UnknownBall }
import models.DataFileConstants.{CardNumbers, Category, Club, ControlCodesStart, CourseClass, FinishTime, Name, NumSplits, StartTime}
import models.RaceTime.fromString
import models.{AgeCat, BallColour, Competitor, CourseClassType, PlacedCompetitor, PunchResults, PunchedControls}

object CompetitorProcessor {

  private var currentRawData: List[List[String]] = List.empty

  private var currentDataSet: Map[String, List[PlacedCompetitor]] = Map.empty

  def getCsvData: List[List[String]] = currentRawData

  def setImportedCsvData(data: List[List[String]]):BallColour = {
    var result: BallColour = RedBall
    try{
      getCompetitors(data)
    } catch {
      case _: Throwable => result = UnknownBall
    }

    currentRawData = data

    result
  }

  def getCurrentDataSet: Map[String, List[PlacedCompetitor]] = currentDataSet

  def getResultsForClass(courseClassType: String): List[PlacedCompetitor] = {
    currentDataSet.getOrElse(courseClassType, List.empty)
  }

  def updateScores: Unit = {
    getCompetitors(currentRawData)
  }

  protected def getCompetitors(data: List[List[String]]): Unit = {
    val comps = data.map{line =>
      val numControls = line(NumSplits).toInt
      val punchedControls = PunchedControls.getControlsFromData(numControls, line.drop(ControlCodesStart))
      val ageCat = AgeCat.getAgeCat(line(Category))
      val courseClass = line(CourseClass)
      val raceTime = fromString(line(StartTime), line(FinishTime))
      val punchResults = processPunches(CourseMapper.fromString(courseClass), punchedControls)
      val scores = punchResults.getFinalScores(raceTime, ageCat)

      val comp = Competitor(line(CardNumbers).toLong, line(Name),
        ageCat, line(Club),
        courseClass,
        raceTime,
        punchedControls,
        punchResults,
        scores)

      comp
    }.sorted.reverse

    val placedCompetitorsByClass = convertToPlacedCompetitors(comps)
    currentDataSet = placedCompetitorsByClass
  }

  def processPunches(courseClass: CourseClassType, punchedControls: PunchedControls): PunchResults = {
    courseClass match {
      case CourseClassType.FullSnooker => AdultsScorer.calcScoresForAllPunches(punchedControls)

      case CourseClassType.SimplifiedSnooker => Under16Scorer.calcScoresForAllPunches(punchedControls)

      case CourseClassType.NormalScore => OpenOrderScorer.calcScoresForAllPunches(punchedControls)
    }
  }

  def convertToPlacedCompetitors(comps: List[Competitor]): Map[String, List[PlacedCompetitor]] = {
    // convert to Seq courseClass -> List[Competitor]
    val compsByClass = comps.groupBy(_.courseClass).toSeq.sortBy(_._1.toString)

    compsByClass.map{ case (course,comps) =>
      // number the competitors from 1 to NN within each courseClass
      course -> comps.zip(LazyList from 1).map{
        // then convert the competitor to a PlacedCompetitor
        case (comp, place) => PlacedCompetitor(place, comp)
      }
    }.toMap
  }
}
