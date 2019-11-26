package models

import org.specs2.mock._
import org.specs2.mutable._
import TimeValue.fromString

class TimeValueSpec extends Specification with Mockito {

  "TimeValue" should {
    "Instanciate from a valid Excel string 1" in {
      val res: Option[TimeValue] = ("01:01:01")
      res.isDefined === true
      res.get.isInstanceOf[TimeValue] === true
      res.get.asSeconds === 3661
    }

    "Instanciate from a valid Excel string 2" in {
      val res: Option[TimeValue] = ("90:59:00")
      res.isDefined === true
      res.get.isInstanceOf[TimeValue] === true
      res.get.asSeconds === 327540
    }

    "Instanciate from a valid Excel string 3" in {
      val res: Option[TimeValue] = ("00:00:01")
      res.isDefined === true
      res.get.isInstanceOf[TimeValue] === true
      res.get.asSeconds === 1
    }

    "Not instanciate from an invalid Excel string" in {
      val res: Option[TimeValue] = ("0:01:01")
      res === None
    }

    "Finish should be greater than start" in {
      val start = TimeValue(1, 0, 0)
      val finish = TimeValue(1, 0, 1)

      finish > start
    }

    "Finish minus start should be the difference in minutes" in {
      val start = TimeValue(1, 0, 0)
      val finish = TimeValue(2, 10, 1)

      finish - start === TimeValue(1,10,1)
    }

  }
}
