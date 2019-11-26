package helpers

import models.AgeCat
import models.AgeCat._

object AgeHandicaps {
  private val handicaps: Map[AgeCat, Double] = getDefaultHandicaps

  def getHandicapForAge(ageCat: AgeCat): Double ={
    handicaps.getOrElse(ageCat, 1D)
  }

  private def getDefaultHandicaps = {
    Map[AgeCat, Double](
      M10 -> 0.7,
      M12 -> 0.74,
      M14 -> 0.8,
      M16 -> 0.84,
      M18 -> 0.88,
      M20 -> 0.93,
      M21 -> 1.0,
      M35 -> 0.93,
      M40 -> 0.89,
      M45 -> 0.84,
      M50 -> 0.79,
      M55 -> 0.74,
      M60 -> 0.68,
      M65 -> 0.6,
      M70 -> 0.53,
      M75 -> 0.46,
      M80 -> 0.37,
      W10 -> 0.6,
      W12 -> 0.62,
      W14 -> 0.65,
      W16 -> 0.67,
      W18 -> 0.7,
      W20 -> 0.71,
      W21 -> 0.8,
      W35 -> 0.71,
      W40 -> 0.67,
      W45 -> 0.62,
      W50 -> 0.57,
      W55 -> 0.53,
      W60 -> 0.48,
      W65 -> 0.44,
      W70 -> 0.39,
      W75 -> 0.35,
      W80 -> 0.3
    )
  }
}
