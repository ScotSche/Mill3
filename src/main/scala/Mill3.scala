import Controller.Controller
import Model.Board
import View.Tui
import scala.io.StdIn.readLine

// Trigger Travis CI

object Mill3 {

  //val color = ("black", "white")

  val controller = new Controller(new Board, Vector())
  val tui = new Tui(controller)

  @main def mill() = {

    var input: String = ""
    println(tui.welcomeScreen())

    while !input.equalsIgnoreCase("q") do {
      input = readLine()
      tui.processInputLine(input)
      if(input == "n"){
        playerInputIteration()
        println(tui.gamePhaseOneBegin())
        controller.create_empty_Board()
        while !input.equalsIgnoreCase("q") do {
          input = readLine()
          tui.processGameInputLine(input)
        } // || Spielende
      }
    }

    println(tui.goodbyeScreen())
  }
  def playerInputIteration(): Unit ={
    print(tui.playerOneName())
    val playerOneName = readLine()
    print(tui.playerTwoName())
    val playerTwoName = readLine()
    controller.create_new_Players(playerOneName, playerTwoName)
  }
}