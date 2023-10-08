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
import androidx.compose.material3.Button
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
import com.example.testapplication.viewmodels.AppViewModel

@Composable
fun ListScreen(
    homeUiState: UiState,
    appViewModel: AppViewModel,
    navController: NavHostController
) {

    Surface {

        when (homeUiState) {
            is Error -> {
                ErrorUi(homeUiState, appViewModel)
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
private fun ErrorUi(homeUiState: UiState, appViewModel: AppViewModel) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = (homeUiState as Error).message
                    ?: stringResource(id = R.string.common_error)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { appViewModel.getItems() }) {
                Text(text = stringResource(R.string.btn_retry))
            }
        }
    }
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
            ListLandScapeUi(list, placeholder, navController)
        }

        else -> {
            ListPortraitUi(list, placeholder, navController)

        }
    }
}

@Composable
private fun ListLandScapeUi(
    list: List<Item>,
    placeholder: Painter,
    navController: NavHostController
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
        items(list.size) { index ->
            ItemUi(list[index], placeholder, navController)
        }
    })
}

@Composable
private fun ListPortraitUi(
    list: List<Item>,
    placeholder: Painter,
    navController: NavHostController
) {
    LazyColumn(content = {
        items(list) { item ->
            ItemUi(item, placeholder, navController)
        }
    })
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

        ItemCardContentUi(item, placeholder)
    }
}

@Composable
private fun ItemCardContentUi(
    item: Item,
    placeholder: Painter
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            ItemImage(item, placeholder)
            Spacer(modifier = Modifier.width(8.dp))
            ItemInfo(item)
        }
        Arrow()
    }
}

@Composable
private fun Arrow() {
    Icon(
        imageVector = Icons.Outlined.KeyboardArrowRight,
        contentDescription = stringResource(R.string.open_detail_desc)
    )
}

@Composable
private fun ItemInfo(item: Item) {
    Column {
        Text(
            text = item.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = item.date)
    }
}

@Composable
private fun ItemImage(
    item: Item,
    placeholder: Painter
) {
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

