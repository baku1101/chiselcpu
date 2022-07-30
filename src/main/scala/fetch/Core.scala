package fetch

import chisel3._
import chisel3.util._
import common.Consts._

class Core extends Module {
  val io = IO(new Bundle {
    // ImemPortIoをインスタンス化したものをFlippedで反転
    // つまり、出力ポートaddr、および入力ポートinstを生成
    val imem = Flipped(new ImemPortIo())

    // 出力ポートexitは、プログラム処理が終わった際にtrue.Bとなる。
    val exit = Output(Bool())
  })

  // 32bit幅×32本のレジスタを生成
  // WORD_LEN =32 (Consts.scalaで定義)
  val regfile = Mem(32, UInt(WORD_LEN.W))

  // Instruction Fetch (IF) Stage

  // 初期値を0とするPCレジスタを生成
  // サイクルごとに4ずつカウントアップ
  // START_ADDR = 0 （Consts.scalaで定義）

  val pc_reg = RegInit(START_ADDR)
  pc_reg := pc_reg + 4.U(WORD_LEN.W)

  // 出力ポートaddrにpc_regを接続し、入力ポートinstをinstで受けます。
  io.imem.addr := pc_reg
  val inst = io.imem.inst

  // exit信号はinstが"3433231"(読み込みプログラムの最終行)の場合にtrue.Bとします
  io.exit := (inst === 0x34333231.U(WORD_LEN.W))

  // テスト用print出力
  printf(p"pc_reg : 0x${Hexadecimal(pc_reg)}\n")
  printf(p"inst   : 0x${Hexadecimal(inst)}\n")
  printf("---------------------\n")
}
