# SudokuSama
If you need a precompiled apk file, apk-debug should work.

Badly designed Sudoku app for Android (badly as in behind the scenes, but it should run fine on most modern phones, it does on my Galaxy S7)
Feel free to reuse or rewrite this code according to the GPL

Changes:
- Added game resume feature
- Enabled offline scoring
- Fixed positioning of numbers in GameView
- Colorful number selection
- Removed online stubs (it's a useless feature)

Remaining bugs:
- Both AsyncTasks (CounterTask and GenerateGame) are never destroyed
--As a result, timer keeps going while not playing, and you can't generate more than one game in a session
- Offline scores don't show the date
