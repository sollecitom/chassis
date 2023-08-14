package org.sollecitom.chassis.example.service.endpoint.write.starter

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class SudokuExampleTests {

    @Disabled
    @Test
    fun `hardest Sudoku ever`() {

        val sudoku = sudoku(
                topLeft = Sudoku.Square(topLeft = 8, midRight = 3, bottomCenter = 7), topCenter = Sudoku.Square(midLeft = 6, bottomCenter = 9), topRight = Sudoku.Square(bottomLeft = 2),
                midLeft = Sudoku.Square(topCenter = 5), midCenter = Sudoku.Square(bottomRight = 1, midCenter = 4, midRight = 5, topRight = 7), midRight = Sudoku.Square(midLeft = 7, bottomCenter = 3),
                bottomLeft = Sudoku.Square(bottomCenter = 9, midRight = 8, topRight = 1), bottomCenter = Sudoku.Square(midLeft = 5), bottomRight = Sudoku.Square(bottomLeft = 4, midCenter = 1, topCenter = 6, topRight = 8)
        )
    }

    private fun sudoku(
            topLeft: Sudoku.Square = Sudoku.Square(), topCenter: Sudoku.Square = Sudoku.Square(), topRight: Sudoku.Square = Sudoku.Square(),
            midLeft: Sudoku.Square = Sudoku.Square(), midCenter: Sudoku.Square = Sudoku.Square(), midRight: Sudoku.Square = Sudoku.Square(),
            bottomLeft: Sudoku.Square = Sudoku.Square(), bottomCenter: Sudoku.Square = Sudoku.Square(), bottomRight: Sudoku.Square = Sudoku.Square()
    ): Sudoku.Board {
        TODO("boom")
    }
}

@JvmInline
value class SudokuValue(val value: Int) {

    init {
        require(value in 1..9)
    }
}

interface Sudoku {

    data class Square private constructor(
            val topLeft: SudokuValue? = null, val topCenter: SudokuValue? = null, val topRight: SudokuValue? = null,
            val midLeft: SudokuValue? = null, val midCenter: SudokuValue? = null, val midRight: SudokuValue? = null,
            val bottomLeft: SudokuValue? = null, val bottomCenter: SudokuValue? = null, val bottomRight: SudokuValue? = null
    ) {

        constructor(topLeft: Int? = null, topCenter: Int? = null, topRight: Int? = null,
                    midLeft: Int? = null, midCenter: Int? = null, midRight: Int? = null,
                    bottomLeft: Int? = null, bottomCenter: Int? = null, bottomRight: Int? = null) : this(
                topLeft = topLeft?.let(::SudokuValue), topCenter = topCenter?.let(::SudokuValue), topRight = topRight?.let(::SudokuValue),
                midLeft = midLeft?.let(::SudokuValue), midCenter = midCenter?.let(::SudokuValue), midRight = midRight?.let(::SudokuValue),
                bottomLeft = bottomLeft?.let(::SudokuValue), bottomCenter = bottomCenter?.let(::SudokuValue), bottomRight = bottomRight?.let(::SudokuValue)
        )

        // TODO add checks
    }

    interface Board {

    }

    interface Builder {

    }
}
