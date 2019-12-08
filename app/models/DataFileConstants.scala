package models

object DataFileConstants {
  // Indexes for the position of fields in the SiTiming CSV file
  val RaceNumber = 0
  val CardNumbers = 1
  val MembershipNumbers = 2
  val Name = 3
  val Category = 4
  val Club = 5
  val Country = 6
  val CourseClass = 7
  val StartTime = 8
  val FinishTime = 9
  val RaceTime = 10
  val NonCompetitive = 11
  val Position = 12
  val Status = 13
  val Handicap = 14
  val PenaltyScore = 15
  val ManualScoreAdjust = 16
  val FinalScore = 17
  val HandicapTime = 18
  val HandicapScore = 19
  val AwardLevel = 20
  val SiEntriesIDs = 21
  val Eligibility = 22
  val JourneyTime = 23
  val ExcludedExcess = 24
  val BehindTime = 25
  val GenderDOB = 26
  val NumSplits =31
  val ControlCodesStart = 32

  val validInput = """^\d\d:\d\d:\d\d$""".r

  // Headers for a SI export file
  val SiHeaders: String = "RaceNumber,CardNumbers,MembershipNumbers,Name,Category,Club,Country,CourseClass,StartTime,FinishTime,RaceTime,NonCompetitive,Position,Status,Handicap,PenaltyScore,ManualScoreAdjust,FinalScore,HandicapTime,HandicapScore,AwardLevel,SiEntriesIDs,Eligibility,JourneyTime,ExcludedExcess,BehindTime,GenderDOB,NotUsed,NotUsed,NotUsed,NotUsed,NumSplits,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points,ControlCode,Split,Points"

  // Constants for result files export
  val SUMMARY_RESULTS_NAME = "summary-results.html"
  val SUMMARY_RESULTS_TITLE = "Summary of Results by Class"
  val DETAILED_RESULTS_PATH = "details"

}
