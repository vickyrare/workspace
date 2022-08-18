class Solution:
    converted_board = []

    # convert board to have int values
    def convert_board(self, board: list[list[str]]):
        self.converted_board.clear()

        for i in range(9):
            boardRow = []
            for j in range(9):
                boardRow.append(0)
            self.converted_board.append(boardRow)

        for i in range(len(board)):
            boardRow = board[i]
            for j in range(len(boardRow)):
                value = boardRow[j]
                if (value.isdigit()):
                    self.converted_board[i][j] = int(value)
                else:
                    self.converted_board[i][j] = 0

    def is_valid_sudoku(self, board: list[list[str]]) -> bool:
        self.convert_board(board)

        if not self.verify_rows():
           return False

        if not self.verify_cols():
            return False

        for i in range(len(self.converted_board)):
            if not self.check_valid(self.get_block(i)):
                return False

        return True

    def get_block(self, index):
        block_values = []
        row = 0
        if index >= 0 and index < 3:
            row = 0
        elif index > 2 and index < 6:
            row = 3
        elif index > 5 and index < 9:
            row = 6

        if index > 2 and index <= 5:
            index = index - 3

        if index > 5 and index <= 8:
            index = index - 6

        for i in range(row, row + 3):
            for j in range(index * 3, index * 3 + 3):
                block_values.append(self.converted_board[i][j])

        return block_values

    def get_row(self, index):
        return self.converted_board[index]

    def get_col(self, index):
        col = []
        for i in range(len(self.converted_board[index])):
            col.append(self.converted_board[i][index])
        return col

    def verify_row(self, board_row):
        if not self.check_valid(board_row):
            return False

        return True

    def verify_rows(self):
        for i in range(len(self.converted_board)):
            if not self.verify_row(self.get_row(i)):
                return False

        return True

    def verify_cols(self):
        for i in range(len(self.converted_board[0])):
            board_col = self.get_col(i)
            if not self.check_valid(board_col):
                return False

        return True

    # check that
    # the values are between 1 and 9
    # there is no repetition in the row
    # the sum is <=45
    def check_valid(self, items):
        values = []
        for i in range(len(items)):
            value = items[i]
            if value < 0 or value > 9:
                return False

            if value != 0 and value not in values:
                values.append(items[i])
            elif value == 0:
                values.append(value)

        if len(values) != 9:
            return False

        if sum(values) > 45:
            return False

        return True

if __name__ == '__main__':
    board = [
                ["5", "3", "1", ".", "7", ".", ".", ".", "."],
                ["6", ".", ".", "1", "9", "5", ".", ".", "."],
                [".", "9", "8", ".", ".", ".", ".", "6", "."],
                ["8", ".", ".", ".", "6", ".", ".", ".", "3"],
                ["4", ".", ".", "8", ".", "3", ".", ".", "1"],
                ["7", ".", ".", ".", "2", ".", ".", ".", "6"],
                [".", "6", ".", ".", ".", ".", "2", "8", "."],
                [".", ".", ".", "4", "1", "9", ".", ".", "5"],
                [".", ".", ".", ".", "8", ".", ".", "7", "9"]
            ]

    s = Solution()
    print(s.is_valid_sudoku(board))
    print(s.is_valid_sudoku(board))