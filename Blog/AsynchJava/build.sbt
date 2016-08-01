name := """AsynchJava"""

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  // Uncomment to use Akka
  //"com.typesafe.akka" % "akka-actor_2.11" % "2.3.9",
  "junit"             % "junit"           % "4.12"  % "test",
  "com.novocode"      % "junit-interface" % "0.11"  % "test"
)
// https://mvnrepository.com/artifact/org.jsoup/jsoup
libraryDependencies += "org.jsoup" % "jsoup" % "1.9.2"
// https://mvnrepository.com/artifact/edu.stanford.nlp/stanford-corenlp
libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0"
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-stream_2.11
libraryDependencies += "com.typesafe.akka" % "akka-stream_2.11" % "2.4.8"


fork in run := true

fork in run := true

fork in run := true