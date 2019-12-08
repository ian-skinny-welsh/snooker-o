package models

import models.AgeCat._
import org.specs2.mock._
import org.specs2.mutable._

class AgeCatSpec extends Specification with Mockito {

  "AgeCat" should {
    "Return correct value for a string" in {
      AgeCat.getAgeCat("M10") === AgeCat.M10
      AgeCat.getAgeCat("W10") === AgeCat.W10
      AgeCat.getAgeCat("m35") === AgeCat.M35
      AgeCat.getAgeCat("w40") === AgeCat.W40
      AgeCat.getAgeCat("M80") === AgeCat.M80
      AgeCat.getAgeCat("W80") === AgeCat.W80
      AgeCat.getAgeCat("M7") === AgeCat.M21
      AgeCat.getAgeCat("W90") === AgeCat.M21
    }

    "Return the correct handicap value for each AgeCat" in {
      AgeCat.M10.getHandicapValue === 0.7D
      AgeCat.W10.getHandicapValue === 0.6D
      AgeCat.M21.getHandicapValue === 1.0D
      AgeCat.W40.getHandicapValue === 0.67D
      AgeCat.M80.getHandicapValue === 0.37D
      AgeCat.W80.getHandicapValue === 0.3D
    }

  }

}
