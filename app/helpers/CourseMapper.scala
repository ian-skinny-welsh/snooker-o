package helpers

import models.{CourseClassType, CourseLists, PlacedCompetitor}
import models.CourseClassType.{FullSnooker, NormalScore, SimplifiedSnooker}

object CourseMapper {

  private var courseMappings: Map[CourseClassType, List[String]] = Map.empty

  def getCourseMappings: Map[CourseClassType, List[String]] = courseMappings

  def setCourseMappings(courseLists: CourseLists): Unit = {
    val courseTypes = courseLists.courseClassTypes.map(CourseClassType.fromString(_))
    val coursePairs = courseTypes.zip(courseLists.courseNames)
    courseMappings = coursePairs.groupMap(_._1)(_._2)
    CompetitorProcessor.updateScores
  }

  def fromString(str: String): CourseClassType = {
    val courseType = findCourseType(str)

    courseType match {
      case Some(cc) => cc
      case None => NormalScore
    }
  }

  private def findCourseType(courseName: String): Option[CourseClassType] = {
    val courses = for(
      courseClassName <- courseMappings.keySet;
      course <- courseMappings(courseClassName).filter(p => p == courseName)
    ) yield {
      courseClassName
    }

    courses.headOption
  }

  def getCourseNamesFromRawData: List[String] = {
    CompetitorProcessor.getCurrentDataSet.keySet.toList
  }
}
