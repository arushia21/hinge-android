package com.example.peoplecounter.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.peoplecounter.R
import com.example.peoplecounter.ui.theme.PeopleCounterTheme
import com.example.peoplecounter.viewmodel.PeopleCounterViewModel

/**
 * MainActivity, main screen of people counter
 * the view of the mvvm architecture
 *
 * using jetpack compose here
 * automatically updates with viewmodel data
 *
 */
class MainActivity : ComponentActivity() {

    // using viewmodel class - people counter viewmodel
    private val viewModel: PeopleCounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //compose UI
        setContent {
            PeopleCounterTheme {
                PeopleCounterScreen(viewModel = viewModel)
            }
        }
    }
}

/**
 * UI built with Jetpack Compose
 * describes what UI should look like, compared to traditional view system where have to explicitly define structure 
 */
@Composable
fun PeopleCounterScreen(viewModel: PeopleCounterViewModel) {

    // two data values, current and total count of people
    val currCount by viewModel.currentCount.observeAsState(0)
    val totalCount by viewModel.totalCount.observeAsState(0)

    // main container, fills the entire screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween // adds even spacing between all items
    ) {

        // top: total count, reset
        TopSection(
            totalCount = totalCount,
            //calls view model (as to mvvm architecture) to reset
            onResetClick = { viewModel.resetCounts() }
        )

        // center: curr people count
        CenterSection(
            currentCount = currCount,
            //function that checks if people is greater than 15
            overCap = viewModel.isOverCapacity()
        )

        // bottom: +/- buttons
        BottomSection(
            showMinusButton = viewModel.shouldShowMinusButton(),
            //functionality of the buttons
            plusClick = { viewModel.incrementCount() },
            minusClick = { viewModel.decrementCount() }
        )
    }
}

/**
 * top: total count, reset
 * used row layout with horizontal alignment
 */
@Composable
fun TopSection(
    totalCount: Int,
    onResetClick: () -> Unit //no return value
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp), //changed spacing to match to the UI in the spec as best as possible
        //ensured padding on left and right sides of screen, visual readability
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // total count text
        // using string variable from strings.xml
        Text(
            text = stringResource(R.string.total_label, totalCount),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF6200EE) // purple
        )

        // reset button
        Button(
            onClick = onResetClick, //reset function passed in
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(6.dp) //added rounded corners to match UI in spec
        ) {
            Text(
                text = stringResource(R.string.reset), //used string variable from strings.xml
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * center: text with curr ppl count
 * change color to red if over 15 ppl
 * using box as container to center content
 */
@Composable
fun CenterSection(
    currentCount: Int,
    overCap: Boolean
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center //centering content
    ) {
        Text(
            text = stringResource(R.string.people_count, currentCount),
            fontSize = 40.sp,
            fontWeight = FontWeight.Medium,
            //using if statement to check if over 15 ppl, change to red
            //otherwise the text should stay purple
            color = if (overCap) Color.Red else Color(0xFF6200EE),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * bottom: +/- buttons
 * recent commit to ui change: making button spacing responsive to screen width
 * therefore it is proportional and is closer in porttrait, further in landscape
 *
 * minus button hides when count = 0
 */
@Composable
fun BottomSection(
    showMinusButton: Boolean,
    plusClick: () -> Unit,
    minusClick: () -> Unit
) {
    // using configuration to get screen width
    // this allows for responsive spacing based on screen size below
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // responsive spacing - 0.2f (eyeballed from spec)
    val buttonSpacing = screenWidth * 0.2f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 24.dp), //modified spacing to match UI in spec
        horizontalArrangement = Arrangement.spacedBy(buttonSpacing, Alignment.CenterHorizontally)
    ) {
        // LEFT SIDE: - button
        //only shown if count > 0
        if (showMinusButton) {
            Button(
                onClick = minusClick,
                modifier = Modifier
                    .width(120.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE), // purple
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(6.dp) //made buttons slightly rounded, as in UI spec
            ) {
                Text(
                    text = stringResource(R.string.minus), //using string from strings.xml
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            // still adds spacing to keep + button in same position
            // so when - is hidden, + button doesnt move
            Spacer(modifier = Modifier.width(120.dp))
        }

        // RIGHT SIDE + button
        Button(
            onClick = plusClick,
            modifier = Modifier
                .width(120.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE), // purple
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(6.dp) ///made buttons slightly rounded, as in UI spec
        ) {
            Text(
                text = stringResource(R.string.plus),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}