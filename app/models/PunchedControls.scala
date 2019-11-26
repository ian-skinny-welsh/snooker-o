package models

case class PunchedControls(numberOfControlsPunched: Int, codes: List[ControlCode]) {
  override def toString = {
    if(numberOfControlsPunched == codes.size) {
      s"Number of controls punched: $numberOfControlsPunched"
    } else {
      s"expected $numberOfControlsPunched controls but got ${codes.size}"
    }
  }

  def fullToString = {
    toString + s" codes: ${codes.mkString(",")}"
  }
}

object PunchedControls {

  def getControlsFromData(numControls: Int, data: List[String]):PunchedControls = {
    // Take every 3rd item and trim to expected num of controls
    val ctrls = data.grouped(3).map(_.head).take(numControls).toList
    val ctrls2 =  ctrls.map(str => ControlCode(str.toInt))
    PunchedControls(numControls, ctrls2)
  }

}
