package browser

import java.nio.file.{Path, Files => JFiles}

import models.DataFileConstants
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import org.scalatestplus.play.{HtmlUnitFactory, OneBrowserPerSuite, PlaySpec}

class BrowserSpec extends PlaySpec with GuiceOneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory {

  val dataRow1 = ",2012945,,Pete West,M50,QO,,adults,11:09:50,11:54:00,,N,1,,,,,,,,,,,,,,,,,,,17,101,,,171,,,131,,,151,,,104,,,106,,,105,,,103,,,110,,,102,,,141,,,161,,,121,,,171,,,108,,,107,,,109,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n"
  val dataRow2 = ",2012928,,Gavin Clegg,M60,QO,,adults,11:07:53,11:56:25,,N,1,,,,,,,,,,,,,,,,,,,27,102,,,171,,,104,,,171,,,109,,,171,,,106,,,131,,,105,,,161,,,107,,,161,,,103,,,141,,,110,,,141,,,102,,,151,,,108,,,151,,,101,,,121,,,131,,,141,,,151,,,161,,,171,,,,,,,,,,,,,,,,,,,,,,,\n"

  "Browser" must {
    "upload file" in {
      val tmpPath = JFiles.createTempFile(null, ".csv")
      val content = DataFileConstants.SiHeaders + "\n" + dataRow1 + dataRow2
      writeFile(tmpPath, content)

      // http://doc.scalatest.org/3.0.0/index.html#org.scalatest.selenium.WebBrowser
      go to s"http://localhost:$port/"
      click on name("results_upload")
      pressKeys(tmpPath.toAbsolutePath.toString)
      submit()

      eventually { pageSource startsWith "File load result = 2 competitors loaded:" }
    }
  }

  def writeFile(path: Path, content: String): Path = {
    JFiles.write(path, content.getBytes)
  }

}
