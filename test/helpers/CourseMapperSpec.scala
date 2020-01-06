package helpers

import models.BallColour._
import models.CourseClassType.{FullSnooker, NormalScore, SimplifiedSnooker}
import models.{ControlCode, CourseLists}
import org.specs2.mock._
import org.specs2.mutable._

class CourseMapperSpec extends Specification with Mockito {

  "CourseMapper" should {
    "Convert a CourseLists class to a CourseMapping" in {
      val courseNames = List("Adults", "Under16", "Open Order")
      val courseClassTypes = List(FullSnooker.entryName, SimplifiedSnooker.entryName, NormalScore.entryName)
      CourseMapper.setCourseMappings(CourseLists(courseNames, courseClassTypes))
      val data = CourseMapper.getCourseMappings

      data.keySet.size === 3

      data.keySet.contains(FullSnooker)
      data(FullSnooker) === List("Adults")

      data.keySet.contains(SimplifiedSnooker)
      data(SimplifiedSnooker) === List("Under16")

      data.keySet.contains(NormalScore)
      data(NormalScore) === List("Open Order")
    }
  }

}
