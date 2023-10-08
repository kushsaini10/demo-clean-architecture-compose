package com.example.testapplication.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.testapplication.R
import com.example.testapplication.data.models.entities.Item
import com.example.testapplication.navigation.Routes
import com.example.testapplication.ui.state.Error
import com.example.testapplication.ui.state.Loaded
import com.example.testapplication.ui.state.Loading
import com.example.testapplication.ui.state.UiState

@Composable
fun ListScreen(
    homeUiState: UiState,
    navController: NavHostController
) {

    Surface {

        when (homeUiState) {
            is Error -> {
                ErrorUi(homeUiState)
            }

            is Loaded -> {
                List(homeUiState, navController)
            }

            Loading -> {
                LoadingUi()
            }
        }
    }
}

@Composable
private fun ErrorUi(homeUiState: UiState) {
    Text(
        text = (homeUiState as Error).message
            ?: stringResource(id = R.string.common_error)
    )
}

@Composable
private fun List(
    homeUiState: UiState,
    navController: NavHostController
) {

    val configuration = LocalConfiguration.current
    val placeholder = painterResource(id = R.drawable.img_placeholder)

    val list = (homeUiState as Loaded).data
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
                items(list.size) { index ->
                    ItemUi(list[index], placeholder, navController)
                }
            })
        }
        else -> {
            LazyColumn(content = {
                items(list) { item ->
                    ItemUi(item, placeholder, navController)
                }
            })

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemUi(item: Item, placeholder: Painter, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            navController.navigate("${Routes.DetailScreen}/${item.id}")
        }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                if (item.image.isNullOrBlank()) {
                    Icon(
                        modifier = Modifier.width(56.dp),
                        painter = placeholder,
                        contentDescription = stringResource(R.string.item_image),
                    )
                } else {
                    AsyncImage(
                        model = item.image,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .width(56.dp),
                        contentDescription = stringResource(R.string.item_image),
                        placeholder = placeholder,
                        fallback = placeholder,
                        error = placeholder
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = item.title, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = item.date)
                }
            }
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = stringResource(R.string.open_detail_desc)
            )
        }
    }
}

@Composable
private fun LoadingUi() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

