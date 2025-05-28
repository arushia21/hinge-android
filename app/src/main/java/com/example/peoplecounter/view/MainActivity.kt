package com.example.peoplecounter.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.peoplecounter.R
import com.example.peoplecounter.ui.theme.PeopleCounterTheme
import com.example.peoplecounter.viewmodel.PeopleCounterViewModel

/**
 * MainActivity - The main screen of our People Counter app
 * 
 * What's happening here?
 * 1. We create a ViewModel to hold our data and logic
 * 2. We use Compose to build the UI declaratively
 * 3. We observe the ViewModel's data and automatically update the UI when it changes
 * 4. We call ViewModel functions when buttons are pressed
 */
class MainActivity : ComponentActivity() {
    
    // Create the ViewModel (this survives screen rotations)
    private val viewModel: PeopleCounterViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set the content to our Compose UI
        setContent {
            PeopleCounterTheme {
                // Main UI composable
                PeopleCounterScreen(viewModel = viewModel)
            }
        }
    }
}

/**
 * The main UI screen built with Jetpack Compose
 * 
 * What is @Composable?
 * - A function that describes what the UI should look like
 * - Gets called automatically when data changes
 * - No need to manually update views like in traditional Android
 */
@Composable
fun PeopleCounterScreen(viewModel: PeopleCounterViewModel) {
    
    // Observe the data from ViewModel
    // These will automatically trigger UI updates when the data changes
    val currentCount by viewModel.currentCount.observeAsState(0)
    val totalCount by viewModel.totalCount.observeAsState(0)
    
    // Main container - fills the entire screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        
        // Top section: Total count and Reset button
        TopSection(
            totalCount = totalCount,
            onResetClick = { viewModel.resetCounts() }
        )
        
        // Center section: Current people count
        CenterSection(
            currentCount = currentCount,
            isOverCapacity = viewModel.isOverCapacity()
        )
        
        // Bottom section: Plus and Minus buttons
        BottomSection(
            showMinusButton = viewModel.shouldShowMinusButton(),
            onPlusClick = { viewModel.incrementCount() },
            onMinusClick = { viewModel.decrementCount() }
        )
    }
}

/**
 * Top section with total count and reset button
 */
@Composable
fun TopSection(
    totalCount: Int,
    onResetClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Total count text
        Text(
            text = stringResource(R.string.total_label, totalCount),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF6200EE) // Purple color
        )
        
        // Reset button
        Button(
            onClick = onResetClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.reset),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Center section with current people count
 * Changes color to red if over capacity (15 people)
 */
@Composable
fun CenterSection(
    currentCount: Int,
    isOverCapacity: Boolean
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.people_count, currentCount),
            fontSize = 40.sp,
            fontWeight = FontWeight.Medium,
            color = if (isOverCapacity) Color.Red else Color(0xFF6200EE), // Red if over capacity, purple otherwise
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Bottom section with plus and minus buttons
 * Minus button is hidden when count is 0
 */
@Composable
fun BottomSection(
    showMinusButton: Boolean,
    onPlusClick: () -> Unit,
    onMinusClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp, vertical = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Minus button (only shown if count > 0)
        if (showMinusButton) {
            Button(
                onClick = onMinusClick,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE), // Purple
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.minus),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            // Empty space when minus button is hidden
            //takes up the same amnt of space, so plus button in same position
            Spacer(modifier = Modifier.weight(1f))
        }
        
        // Plus button (always shown)
        Button(
            onClick = onPlusClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE), // Purple
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.plus),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}