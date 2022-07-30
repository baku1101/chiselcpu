package fetch

import chisel3._
import chisel3.util._

import common.Consts._
import chisel3.util.experimental.loadMemoryFromFile

/// Bundleを継承する形で、addrとinstの2信号をまとめている。
/// addr：メモリアドレス用の入力ポート
/// inst：命令データ用の出力ポート
/// ともに32bit幅
class ImemPortIo extends Bundle {
  val addr = Input(UInt(WORD_LEN.W))
  val inst = Output(UInt(WORD_LEN.W))
}

class Memory extends Module {
  val io = IO(new Bundle {
    val imem = new ImemPortIo()
  })

  // メモリの実体：8bit×16384本=16KBのレジスタ
  // 8bit幅は、PCのカウントアップ幅を4にするため
  // 1アドレスに8bit、つまり4アドレスで32bit格納する
  val mem = Mem(16384, UInt(8.W))

  // メモリデータをロード(hexファイルにデータを入れておく)
  loadMemoryFromFile(mem, "src/hex/fetch.hex")

  // 各アドレスに格納された8bitデータを4つつなげて32bit幅に
  io.imem.inst := Cat(
    mem(io.imem.addr + 3.U(WORD_LEN.W)),
    mem(io.imem.addr + 2.U(WORD_LEN.W)),
    mem(io.imem.addr + 1.U(WORD_LEN.W)),
    mem(io.imem.addr)
  )
}
