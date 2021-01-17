import Controller.Controller
import Model.Board
import View.Tui
import scala.io.StdIn.readLine

object Mill3 {

  val controller: Controller = new Controller(new Board, Vector())
  val tui: Tui = new Tui(controller)

  @main def mill() = {

    var input: String = ""
    println(tui.welcomeScreen())

    while !input.equalsIgnoreCase("q") do {
      input = readLine()
      tui.processInputLine(input)
      if input == "n" then
        playerInputIteration()
        println(tui.gamePhaseOneBegin())
        controller.create_empty_Board()
        while !input.equalsIgnoreCase("q") do {
          input = readLine()
          tui.processGameInputLine(input)
        } // || Spielende
      end if
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