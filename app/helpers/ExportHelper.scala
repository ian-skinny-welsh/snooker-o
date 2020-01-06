package helpers

import java.io.{BufferedWriter, File, FileWriter}

import com.github.tototoshi.csv.CSVWriter
import com.typesafe.config.ConfigFactory
import play.api.mvc.{AnyContent, MessagesRequest}
import scala.collection.mutable.ListBuffer

import helpers.ResourceManager.using
import models.DataFileConstants.{CardNumbers, DETAILED_RESULTS_PATH, FinalScore, Position, SUMMARY_RESULTS_TITLE, SiHeaders}
import models.{CourseClassType, PlacedCompetitor}
import models.DataFileConstants._

object ExportHelper {

  def exportResults()(implicit request: MessagesRequest[AnyContent]): Boolean = {
    val conf = ConfigFactory.load
    val basePath = conf.getString("export.basePath")
    createDirectories(basePath)

    val res = try{
      exportHtmlResults(basePath)(request)
      exportCsvResults(basePath)
      true
    } catch {
      case _: Throwable => false
    }

    res
  }

  private def exportHtmlResults(basePath: String)(implicit request: MessagesRequest[AnyContent]): Unit = {
    val summaryResultsPath = basePath + SUMMARY_RESULTS_NAME
    val summaryResultsPage = views.html.results.core.results_wrapper(SUMMARY_RESULTS_TITLE,
      views.html.results.core.summary_results_core(CompetitorProcessor.getCurrentDataSet, true))
    outputToFile(summaryResultsPath, summaryResultsPage.toString)

    val processedData = CompetitorProcessor.getCurrentDataSet

    for(
      courseClassType <- processedData.keySet;
      numEntries = processedData(courseClassType).size;
      placedCompetitor  <- processedData(courseClassType)
    ) yield {
      val detailedResultsPage = views.html.results.core.results_wrapper("Detailed results",
        views.html.results.core.detailed_results_core(courseClassType, placedCompetitor, true, numEntries))
      val path = basePath + s"${DETAILED_RESULTS_PATH}\\${courseClassType.replaceAll(" ", "").toLowerCase}-${placedCompetitor.place}.html"
      outputToFile(path, detailedResultsPage.toString)
    }
  }

  private def exportCsvResults(basePath: String): Unit = {
    val processedData = CompetitorProcessor.getCurrentDataSet
    val rawData = CompetitorProcessor.getCsvData
    val csvResultsPath = basePath + CSV_RESULTS_NAME
    val headers = SiHeaders.split(",").toList

    val exportData: ListBuffer[List[String]] = ListBuffer.empty += headers

    for(
      entry <- rawData
    ) yield {
      val compOption = findCompetitor(entry(CardNumbers), processedData)
      compOption match {
        case Some(comp) => {
          exportData += replaceValues(entry, comp)
        }

        case _ =>
      }
    }

    outputToCsvFile(csvResultsPath, exportData)
  }

  private def findCompetitor(siCard: String, competitors: Map[String, List[PlacedCompetitor]]): Option[PlacedCompetitor] = {
    val comps = for(
      courseClassType <- competitors.keySet;
      comp <- competitors(courseClassType).filter(p => p.competitor.siCard.toString == siCard)
    ) yield {
      comp
    }

    comps.headOption
  }

  private def replaceValues(rawEntry: List[String], placedComp: PlacedCompetitor): List[String] = {
    rawEntry.take(Position) ::: s"${placedComp.place}" ::
      rawEntry.drop(Position + 1).take(FinalScore - Position -1) :::
      f"${placedComp.competitor.scores.finalScore.toInt}%d" ::
      rawEntry.drop(FinalScore + 1)
  }

  private def outputToFile(path: String, content: String) = {
    using(new BufferedWriter(new FileWriter(new File(path), false))) { outputFile =>
      outputFile.write(content)
    }
  }

  private def outputToCsvFile(path: String, content: ListBuffer[List[String]]) = {
    val writer = CSVWriter.open(new File(path), false)
    for(
      line <- content
    ) yield {
      writer.writeRow(line)
    }

    writer.close()
  }

  private def createDirectories(basePath: String) = {
    val baseDir = new File(basePath)
    if(!baseDir.exists()) baseDir.mkdir()

    val subDir = new File(basePath, DETAILED_RESULTS_PATH)
    if(!subDir.exists())
      subDir.mkdir()
  }

}
