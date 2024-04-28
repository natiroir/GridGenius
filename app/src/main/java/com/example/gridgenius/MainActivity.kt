package com.example.gridgenius


import android.R
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gridgenius.ui.theme.GridGeniusTheme
import com.example.gridgenius.ui.theme.cowboypixelfont
import kotlinx.coroutines.*
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.BorderStroke

import androidx.compose.ui.input.pointer.pointerInput
import android.content.Context
import android.util.DisplayMetrics
fun getScreenWidth(context: Context): Int {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.black)
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )



        setContent {
                    Myapp()
        }
            }
        }

@Composable
fun Myapp(){
    var isIntroScreen by remember { mutableStateOf(true) }

    if (isIntroScreen) {
        ExeIntro(onStartGameClicked = { isIntroScreen = false })
    } else {

        GameScreen()


    }
}
@Composable
fun ExeIntro(onStartGameClicked: () -> Unit) {
    println("Intro screen")


    GridGeniusTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.LightGray
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                {
                    Text(
                        "Grid",
                        color = Color.White,
                        fontSize = 52.sp,
                        fontFamily = cowboypixelfont,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Genius",
                        color = Color.White,
                        fontSize = 52.sp,
                        fontFamily = cowboypixelfont,
                    )
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Yellow),
                        onClick = onStartGameClicked,
                        shape = RoundedCornerShape(10),
                        modifier = Modifier
                            .padding(horizontal = 50.dp)
                            .padding(vertical = 100.dp)
                    ) {
                        Text(
                            text = "Start the game",
                            color = Color.Black,
                            fontSize = 24.sp,
                        )
                    }
            }
        }
    }
}}

var pause = true
var edit = false
@Composable
fun GameScreen() {
    val width = getScreenWidth(LocalContext.current)

    println("GameScreen")

    var grid by remember { mutableStateOf(createGridData()) }
    LaunchedEffect(Unit) {

        while (true) {
            if (pause) {
                delay(200)
                grid = nextGeneration(grid)
                continue
            }


            delay(10)

        }
    }

    Surface(
        color = Color.Black,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { offset ->
                    // Calculate cell coordinates from offset
                    val cellSize = width / 15
                    val col = (offset.x / cellSize).toInt()
                    val row = (offset.y / cellSize).toInt()

                    // Modify the grid at the tapped cell (row, col)
                    if (edit){
                    grid = modifyGridCell(grid, row, col, 1 - grid.data[row][col])}
                })
            }

    ) {

        Box(modifier = Modifier
            .fillMaxSize()) {
            DrawGrid(grid, width, width)
        }
        Bottom20PercentBox(content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Game of Life",
                    color = Color.White,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                    fontFamily = cowboypixelfont,
                )
                Row {
                    Button(
                        onClick = { grid = nextGeneration(grid) },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(10),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Transparent),
                    border = BorderStroke(2.dp, Color.White)
                    ) {

                        Text("Next Generation",color = Color.White)
                    }

                    Button(
                        onClick = { grid = createGridData() },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(10),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Transparent),
                    border = BorderStroke(2.dp, Color.White)
                    ) {
                        Text("Reset", color = Color.White)
                    }
                }
                Row {
                    val pauseText = if (!pause) "Start" else "Stop"
                    val editText = if (!edit) "Edit" else "Stop editing"
                    Button(
                        onClick = { pause = !pause },
                        modifier = Modifier.padding(16.dp),
                        shape = RoundedCornerShape(10),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Transparent),
                        border = BorderStroke(2.dp, Color.White) // Contour blanc
                    ) {
                        Text(text = pauseText,color = Color.White)
                    }
                    Button(onClick = {pause = false; edit = !edit},modifier = Modifier.padding(16.dp),                        shape = RoundedCornerShape(10),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Transparent),
                        border = BorderStroke(2.dp, Color.White) )
                    {
                        Text(text = editText, color = Color.White)

                    }
                }

            }

            }
        )

    }
}


data class Grid(val rows: Int, val cols: Int, val data: List<List<Int>>)

fun createGridData(): Grid {
    val gridData = MutableList(200) {
        MutableList(200) { 0 }
    }

    return Grid(rows = gridData.size, cols = gridData[0].size, data = gridData.map { it.toList() })
}

fun nextGeneration(grid: Grid): Grid {
    val newGridData = MutableList(grid.rows) { MutableList(grid.cols) { 0 } }

    for (i in 0 until grid.rows) {
        for (j in 0 until grid.cols) {
            val aliveNeighbors = countAliveNeighbors(grid, i, j)
            if (grid.data[i][j] == 1) {
                // Cellule vivante avec moins de 2 voisins vivants meurt de solitude
                // Cellule vivante avec plus de 3 voisins vivants meurt de surpopulation
                newGridData[i][j] = if (aliveNeighbors < 2 || aliveNeighbors > 3) 0 else 1
            } else {
                // Cellule morte avec exactement 3 voisins vivants devient vivante
                if (aliveNeighbors == 3) {
                    newGridData[i][j] = 1
                }
            }
        }
    }

    return Grid(rows = grid.rows, cols = grid.cols, data = newGridData)
}

fun countAliveNeighbors(grid: Grid, row: Int, col: Int): Int {
    val rows = grid.rows
    val cols = grid.cols
    val rowIndices = listOf(row - 1, row, row + 1).filter { it in 0 until rows }
    val colIndices = listOf(col - 1, col, col + 1).filter { it in 0 until cols }

    var count = 0
    for (i in rowIndices) {
        for (j in colIndices) {
            if (i == row && j == col) continue
            count += grid.data[i][j]
        }
    }

    return count
}

@Composable
fun DrawGrid(grid: Grid, screenWidth: Int, screenHeight: Int) {
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()){
            val numColumns = 15
            val numRows = 25
            val cellWidth = screenWidth / numColumns
            val cellHeight = screenHeight / numColumns
            for (row in 0 until numRows) {
                for (col in 0 until numColumns) {
                    val cellValue = grid.data[row][col]
                    val color = if (cellValue == 1) Color.White else Color.Black
                    val x = (col * cellWidth).toFloat()
                    val y = (row * cellHeight).toFloat()
                    drawRect(
                        color = color,
                        topLeft = Offset(x, y),
                        size = Size(cellWidth.toFloat(), cellHeight.toFloat())
                    )
                }
            }
        }
    }
}




@Composable
fun Bottom20PercentBox(content: @Composable () -> Unit) {
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels.toFloat()
    val bottomBoxHeight = with(LocalDensity.current) { (screenHeight * 0.2f).dp }

    Surface(
        color = Color.Transparent, // Couleur de fond de la boîte
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(bottomBoxHeight),
            contentAlignment = Alignment.BottomCenter
        ) {
            content()
        }
    }
}



@Composable
fun TouchEventExample(onTouch: (x: Float, y: Float) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()

            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onTouch(offset.x, offset.y) // Récupérer les coordonnées du clic
                }
            }
    )
}

fun modifyGridCell(
    grid: Grid,
    rowIndex: Int,
    colIndex: Int,
    newValue: Int
): Grid {
    val newData = grid.data.toMutableList() // Copie mutable des données de la grille
    if (rowIndex in 0 until grid.rows && colIndex in 0 until grid.cols) {
        // Récupérer la ligne (List<Int>) correspondante à rowIndex
        val rowToUpdate = newData[rowIndex].toMutableList()

        // Modifier la valeur à l'index colIndex dans la ligne
        rowToUpdate[colIndex] = newValue

        // Mettre à jour la ligne dans newData
        newData[rowIndex] = rowToUpdate
    }
    // Retourner une nouvelle instance de Grid avec les données mises à jour
    return Grid(grid.rows, grid.cols, newData)
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GridGeniusTheme {
        MainActivity()
    }
}

