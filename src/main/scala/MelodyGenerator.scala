// Henry Cooney <hacoo36@gmail.com>
// Chord Predictor -- https://github.com/bburdette/ChordPredictor/
//
// Melody Generator
//
// Melody generation module. Learns 'melody' from underlying chords and
// generates new melodies. Chords may be overlayed onto this.
package MelodyGenerator

import musicProcessing._

object MelodyGenerator_Test extends MelodyGenerator ("/home/henry/courses/ppaml/chordpredictor/ChordPredictor/ClassicalMidiCSV/"){
    def main(args: Array[String]) {
      println("Hello World!")
      println("Loaded " + dataSet.songList.length + " songs.")
      printTransitionMatrix
      println()
      println()
      val matrix = getTransitionMatrix

      for(row <- matrix){
        for(col <- row)
          print(col)
        println()
      }
    }
}

class MelodyGenerator (filepath: String) {

  // Melody generator class.
  // Loads a csv file, then 'learns' melody patterns.
  // Outputs a new csv file containing a generated melody.

  val dataSet = new musicDataSet(filepath)
  val songs = dataSet.songList
  val transitions = Array.fill(12, 12)(0)
  var totaltransistions = 0



  learnSongs(songs)

  val normalizedtransitions = transitions.map(_.map((x: Int) => x.toDouble/totaltransistions))
  for(row <- normalizedtransitions) {
    for(col <- row)
      print(col + ",")
    println()
  }

  def getTransitionMatrix = {normalizedtransitions}

  def getTotalTransistion = {totaltransistions}

  def printTransitionMatrix = {
    println("RAW TRANSITION MATRIX: ")

    for(row <- transitions) {
    for(col <- row)
      print(col + ",")
    println()
    }

    println()
    println("NORMALIZED TRANSISTION MATRIX: ")
    for(row <- normalizedtransitions) {
    for(col <- row)
      print(col + ",")
    println()
    }

    println()
    println("TOTAL TRANSISTIONS: " + totaltransistions)
  }
  
  def learnSongs(songlist: List[Song]) {
    for (song <- songlist) {
      learnChords(song.getChordList)
    }
  }

  def learnChords(s: List[Chord]) {
    // Populate the melody transition matrix with each chord transition found.
    if(s.length > 1) {
      val first = s.head
      val second = s.tail.head
      transitions(first.getRootFreq) (second.getRootFreq) += 1
      //println("First: " + first.getRootFreq)
      //println("Second: " + second.getRootFreq)
      totaltransistions += 1
      learnChords(s.tail)
    }
  }
}