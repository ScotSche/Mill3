package View

import Controller.{Controller, GameStatus}
import Model.{Board, Player, Stone}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class TuiSpec extends AnyWordSpec with Matchers{

  "A Mill Tui" should{
    val controller = new Controller(new Board, Vector())
    controller.create_new_Players("PlayerOne", "PlayerTwo")
    val tui = new Tui(controller)
    "create a new board on input 'n'" in{
      tui.processInputLine("n")
      controller.board should be(new Board)
    }
    "should do nothing and leave loop on input 'q'" in {
      tui.processInputLine("q")
    }
    "should print the help board on input 'h'" in {
      tui.processInputLine("h")
    }
    "should provide any other input" in {
      tui.processInputLine("*")
    }

    "should do nothing and leave loop in input 'q' in game mode" in {
      tui.processGameInputLine("q")
    }
    "should print the help board on input 'h' in game mode" in {
      tui.processGameInputLine("h")
    }
    "should start a new game on input 'n' in game mode" in {
      tui.processGameInputLine("n")
      controller.players(0).MAX_STONE should be(9)
      controller.players(1).MAX_STONE should be(9)
      tui.currentPlayer should be(controller.players(1))
      controller.board should be(new Board)
    }

    "should handle a valid input in GPONE" in {
      controller.gameStatus = GameStatus.GPONE
      controller.create_empty_Board()

      // PlayerOne
      tui.processGameInputLine("11")
      controller.board.stone(0, 0) should be(Stone(1))

      // PlayerTwo
      tui.processGameInputLine("12")
      controller.board.stone(0, 1) should be(Stone(2))
    }
    "should handle an invalid input in GPONE" in {
      // PlayerOne
      tui.processGameInputLine("Invalid")
    }
    "should handle an invalid and a valid mill input in GPONE" in {
      // PlayerOne
      tui.processGameInputLine("18")
      // PlayerTwo
      tui.processGameInputLine("13")
      // PlayerOne
      tui.processGameInputLine("17")
      tui.newMill should be(true)
      // PlayerOne Invalid
      tui.processGameInputLine("00")
      // PlayerOne Valid
      tui.processGameInputLine("12")
      controller.board.check_stone_Set(0, 1) should be(false)
    }
    "should change from GPONE to GPTWO" in {
      // PlayerTwo
      tui.processGameInputLine("12")
      // PlayerOne
      tui.processGameInputLine("14")
      // PlayerTwo
      tui.processGameInputLine("15")
      // PlayerOne
      tui.processGameInputLine("16")
      // PlayerTwo
      tui.processGameInputLine("21")
      // PlayerOne
      tui.processGameInputLine("22")
      // PlayerTwo
      tui.processGameInputLine("23")
      // PlayerOne
      tui.processGameInputLine("24")
      // PlayerTwo
      tui.processGameInputLine("25")
      // PlayerOne
      tui.processGameInputLine("26")
      // PlayerTwo
      tui.processGameInputLine("27")
      // PlayerOne
      tui.processGameInputLine("28")
      // PlayerTwo
      tui.processGameInputLine("31")
      controller.gameStatus should be(GameStatus.GPTWO)
    }

    "should handle a valid input in GPTWO" in {
      // PlayerOne
      tui.processGameInputLine("28")
      tui.processGameInputLine("38")

      controller.board.stone(1, 7) should be(Stone(0))
      controller.board.stone(2, 7) should be(Stone(1))
    }
    "should handle an invalid input in GPTWO" in {
      tui.gpTwoSeparator = false
      // PlayerTwo
      tui.processGameInputLine("Invalid")

      tui.gpTwoSeparator = true
      // PlayerTwo
      tui.processGameInputLine("Invalid")
      tui.gpTwoSeparator = false
    }
    "should change from GPTWO to GPTHREE" in {
      controller.create_empty_Board()
      controller.gameStatus = GameStatus.GPTWO
      controller.players(0).MAX_STONE = 3;
      controller.notifyPlayerObserver
      controller.gameStatus should be(GameStatus.GPTHREE)

      controller.players(0).MAX_STONE = 9;
      controller.players(1).MAX_STONE = 3;
      controller.notifyPlayerObserver
      controller.gameStatus should be(GameStatus.GPTHREE)
    }

    "should change from GPTHREE to END" in {
      controller.players(0).MAX_STONE = 2
      controller.notifyPlayerObserver
      controller.gameStatus should be(GameStatus.END)

      controller.players(0).MAX_STONE = 9
      controller.players(1).MAX_STONE = 2
      controller.gameStatus = GameStatus.GPTHREE
      controller.notifyPlayerObserver
      controller.gameStatus should be(GameStatus.END)

      controller.gameStatus = GameStatus.GPTHREE
      controller.board.update_board(0, 0, 1)
      controller.board.update_board(0, 1, 2)
      controller.board.update_board(0, 7, 2)
      controller.notifyPlayerObserver
      controller.gameStatus should be(GameStatus.END)
    }

    "should provide a welcome screen" in {
      val welcomeScreen = """
        :**********************************************************************************************
        :*                                       WELCOME TO                                           *
        :*  __________   __     __   ________      _____      ______    __    __           __         *
        :* |___    ___| |  |   |  | |   _____|    |   _  \   /  _   |  |  |  |  |         |  |        *
        :*     |  |     |  |___|  | |  |_____     |  | \  \_/  / |  |  |  |  |  |         |  |        *
        :*     |  |     |   ___   | |  ______|    |  |  \_____/  |  |  |  |  |  |         |  |        *
        :*     |  |     |  |   |  | |  |_____     |  |           |  |  |  |  |  |______   |  |______  *
        :*     |__|     |__|   |__| |________|    |__|           |__|  |__|  |_________|  |_________| *
        :*                                        IN SCALA                                            *
        :**********************************************************************************************
        :Press 'n' for new Game
        :Press 'h' for help
        :Press 'q' to quit """.stripMargin(':')
      tui.welcomeScreen() should be(welcomeScreen)
    }
    /*"should provide an endscreen" in {
      val endScreen =
        "                         ***************************************\n" +
        "                            Congratulations PlayerOne!\n" +
        "                            you won the game\n" +
        "                            Press q to quit or n for new Game\n" +
        "                         ***************************************"
        tui.endGameScreen(controller.players(0)) should be(endScreen)
    }*/
    "should provide a goodbye screen" in {
      val goodbyeScreen = """
        :**********************************************************************************************
        :*                                  THANK YOU FOR PLAYING                                     *
        :*  __________   __     __   ________      _____      ______    __    __           __         *
        :* |___    ___| |  |   |  | |   _____|    |   _  \   /  _   |  |  |  |  |         |  |        *
        :*     |  |     |  |___|  | |  |_____     |  | \  \_/  / |  |  |  |  |  |         |  |        *
        :*     |  |     |   ___   | |  ______|    |  |  \_____/  |  |  |  |  |  |         |  |        *
        :*     |  |     |  |   |  | |  |_____     |  |           |  |  |  |  |  |______   |  |______  *
        :*     |__|     |__|   |__| |________|    |__|           |__|  |__|  |_________|  |_________| *
        :*                                        IN SCALA                                            *
        :********************************************************************************************** """.stripMargin(':')
      tui.goodbyeScreen() should be(goodbyeScreen)
    }
    "should provide a help board" in {
      val helpBoard = 
        "To access the Nodes see the following coordinates:\n" +
          "               O----------------------------O----------------------------O\n" +
          "               | (11)                       | (12)                  (13) |\n" +
          "               |                            |                            |\n" +
          "               |          O-----------------O-----------------O          |\n" +
          "               |          | (21)            | (22)       (23) |          |\n" +
          "               |          |            (32) |                 |          |\n" +
          "               |          |         O-------O-------O         |          |\n" +
          "               |          |         | (31)     (33) |         |          |\n" +
          "               |          |         |               |         |          |\n" +
          "               O----------O---------O (38)     (34) O---------O----------O\n" +
          "               | (18)     | (28)    |               |    (24) |     (14) |\n" +
          "               |          |         | (37)     (35) |         |          |\n" +
          "               |          |         O-------O-------O         |          |\n" +
          "               |          |            (36) |                 |          |\n" +
          "               |          | (27)            | (26)       (25) |          |\n" +
          "               |          O-----------------O-----------------O          |\n" +
          "               |                            |                            |\n" +
          "               | (17)                       | (16)                  (15) |\n" +
          "               O----------------------------O----------------------------O\n"
      tui.helpBoard() should be(helpBoard)
    }
    "should provide a player one name input" in {
      val playerOneInput =
        """
          |Please enter name of player one: """.stripMargin
      tui.playerOneName() should be(playerOneInput)
    }
    "should provide a player two name input" in {
      val playerTwoInput = """
        |Please enter name of player two: """.stripMargin
      tui.playerTwoName() should be(playerTwoInput)
    }
    "should provide a Game-Phase-One phrase" in {
      val gpOne = "Let the game begin.\nGame Phase One: Please place your stones on a free field."
      tui.gamePhaseOneBegin() should be(gpOne)
    }
    "should provide a Game-Phase-Two phrase" in {
      val gpTwo = "Game Phase Two: Move your Stones strategically and get the victory."
      tui.gamePhaseTwoBegin() should be(gpTwo)
    }
    "should provide a Game-Phase-Three phrase" in {
      val gpThree = "Game Phase Three: Be aware! One Player is able to jump."
      tui.gamePhaseThreeBegin() should be(gpThree)
    }
    "should provide a message to show which player is playing with the amount of stones played" in {
      controller.create_empty_Board()
      tui.currentPlayer = Player("Your Name", 1, 9)
      val playerMsg = "\nYour Name it is your turn Place one stone on a specific coordinate (1 of 9):"
      tui.playerGamePhaseOneTurns() should be(playerMsg)
    }
    "should provide a dialog in GPTWO for player interaction" in {
      tui.currentPlayer = controller.players(0)
      tui.gpTwoSeparator = false
      tui.mainGamePhaseTurns() should be("PlayerOne choose the stone you want to move:")
      tui.gpTwoSeparator = true
      tui.mainGamePhaseTurns() should be("PlayerOne where do you want to place it:")
    }
    "should provide the current gamescore (amount of stones)" in {
      controller.players(0).MAX_STONE = 9
      controller.players(1).MAX_STONE = 9
      tui.currentGameScore() should be("                                         9 vs. 9\n")
    }
    "should update the board with new stones" in {
      val emptyBoard = new Board
      val filledPlayerOneBoard = emptyBoard.update_board(0, 0, 1)
      val filledPlayerTwoBoard = emptyBoard.update_board(0, 0, 2)

      val emptyBoardString =
        "               O----------------------------O----------------------------O\n" +
          "               |                            |                            |\n" +
          "               |                            |                            |\n" +
          "               |          O-----------------O-----------------O          |\n" +
          "               |          |                 |                 |          |\n" +
          "               |          |                 |                 |          |\n" +
          "               |          |         O-------O-------O         |          |\n" +
          "               |          |         |               |         |          |\n" +
          "               |          |         |               |         |          |\n" +
          "               O----------O---------O               O---------O----------O\n" +
          "               |          |         |               |         |          |\n" +
          "               |          |         |               |         |          |\n" +
          "               |          |         O-------O-------O         |          |\n" +
          "               |          |                 |                 |          |\n" +
          "               |          |                 |                 |          |\n" +
          "               |          O-----------------O-----------------O          |\n" +
          "               |                            |                            |\n" +
          "               |                            |                            |\n" +
          "               O----------------------------O----------------------------O\n"
      val filledPlayerOneBoardString =
        "               W----------------------------O----------------------------O\n" +
          "               |                            |                            |\n" +
          "               |                            |                            |\n" +
          "               |          O-----------------O-----------------O          |\n" +
          "               |          |                 |                 |          |\n" +
          "               |          |                 |                 |          |\n" +
          "               |          |         O-------O-------O         |          |\n" +
          "               |          |         |               |         |          |\n" +
          "               |          |         |               |         |          |\n" +
          "               O----------O---------O               O---------O----------O\n" +
          "               |          |         |               |         |          |\n" +
          "               |          |         |               |         |          |\n" +
          "               |          |         O-------O-------O         |          |\n" +
          "               |          |                 |                 |          |\n" +
          "               |          |                 |                 |          |\n" +
          "               |          O-----------------O-----------------O          |\n" +
          "               |                            |                            |\n" +
          "               |                            |                            |\n" +
          "               O----------------------------O----------------------------O\n"
      val filledPlayerTwoBoardString =
        "               B----------------------------O----------------------------O\n" +
          "               |                            |                            |\n" +
          "               |                            |                            |\n" +
          "               |          O-----------------O-----------------O          |\n" +
          "               |          |                 |                 |          |\n" +
          "               |          |                 |                 |          |\n" +
          "               |          |         O-------O-------O         |          |\n" +
          "               |          |         |               |         |          |\n" +
          "               |          |         |               |         |          |\n" +
          "               O----------O---------O               O---------O----------O\n" +
          "               |          |         |               |         |          |\n" +
          "               |          |         |               |         |          |\n" +
          "               |          |         O-------O-------O         |          |\n" +
          "               |          |                 |                 |          |\n" +
          "               |          |                 |                 |          |\n" +
          "               |          O-----------------O-----------------O          |\n" +
          "               |                            |                            |\n" +
          "               |                            |                            |\n" +
          "               O----------------------------O----------------------------O\n"
      tui.updateBoard(emptyBoard) should be(emptyBoardString)
      tui.updateBoard(filledPlayerOneBoard) should be(filledPlayerOneBoardString)
      tui.updateBoard(filledPlayerTwoBoard) should be(filledPlayerTwoBoardString)
    }
  }
}
