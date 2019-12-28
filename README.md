# Snooker-O
This is a Scala/Play framework project inspired by a mix of an Orienteering Score event and the rules of Snooker.  

The aim of the project is to import the basic results file of a Score event from SiTiming's software and process them according to our simplified Snooker rules to calculate each competitors score.

* Orienteering https://en.wikipedia.org/wiki/Orienteering
* Score event  https://en.wikipedia.org/wiki/Orienteering#Score
* Snooker rules https://en.wikipedia.org/wiki/Rules_of_snooker
* SiTiming https://www.sportident.co.uk/

## How to build and run the application
If you want to build this application from the source code on GitHub, you need to either clone it using 'git'
 or download the source code as a zip file (look for the green 'Clone or download' button)  
1. This is a standard Scala Play framework application, more info here:  https://docs.scala-lang.org/getting-started/sbt-track/getting-started-with-scala-and-sbt-on-the-command-line.html  
2. Once you have a JDK installed and SBT installed you can build the application from the Command line or DOS window.  
3. Open the cmd line at the root folder of the project.
4. Start SBT by typing 'sbt' and pressing return
5. Once SBT has initialised, type 'dist' and press return.
6. When the build is complete it will display something like:  
 [info] Your package is ready in C:\snooker-o\target\universal\snooker-o-1.0-SNAPSHOT.zip
7. You can unzip this package on almost any device that is capable of running a Java application and a web browser.  
8. Once you have unzipped package, look in the 'bin' folder and run the 'snooker-o' file.


## How to use the application
1. Run a normal Score event using SiTiming.  There is no need to allocate points to controls, they will be ignored anyway.
2. Export the results from SiTiming.  Use the export processable CSV option.
3. Run the Snooker-O application.  
4. Using the menu on the left, configure the control numbers used for the event.  
5. Import your SiTiming results file.
6. You should be presented with a summary results page, from here you can examine the detail of each competitor by
 clicking on their name at the right hand end of their result.   

## Credit to those who inspired this application
Variations of "Snooker Orienteering" have been taking place since at least 2009 that I can find with a brief search online. Credit to Southdowns Orienteers (https://www.southdowns-orienteers.org.uk/)  
One of the Moravian Orienteers members ('Roo') took part in an even organised by Quantok Orienteers (https://www.quantockorienteers.co.uk/)in 2018 and decided 
to organise something similar himself.  This application is an attempt to provide a more robust results calculator than the Excel spreadsheet
that seems to have been used previously.  

# Need an alternative version?
If your club wants to use this application it's free to use and customise yourself.  If you want me to tailor this application in some way 
then get in touch either via GitHub or the 'Website managers' via the Moravian website (https://www.moravianorienteering.org/) .