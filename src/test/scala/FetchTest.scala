package fetch

import chisel3._

// ChiselTestを利用するために必要なpackage
import org.scalatest.flatspec._
import chiseltest._

class HexTest extends AnyFlatSpec with ChiselScalatestTester {
  // "テスト対象名" should "正しい振る舞い" in ...
  "mycpu" should "work through hex" in {
    test(new Top) { c =>
      // このブロックでテストを記述
      while (!c.io.exit.peek().litToBoolean) {
        c.clock.step()
      }
    }
  }
}
