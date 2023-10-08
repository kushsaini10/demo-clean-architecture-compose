package com.example.testapplication.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.testapplication.R
import com.example.testapplication.data.models.entities.Item
import com.example.testapplication.viewmodels.AppViewModel

@Composable
fun DetailScreen(
    itemId: Int,
    appViewModel: AppViewModel = viewModel(),
    navController: NavHostController
) {

    val itemState by appViewModel.itemDetailFlow.collectAsState()

    if (itemState?.id == null || itemState?.id != itemId) {
        LaunchedEffect(key1 = itemId, block = {
            appViewModel.getItem(itemId)
        })
    }

    Surface {
        itemState?.let { item ->
            DetailScreenUi(item, navController)
        } ?: kotlin.run {
            LoadingUi()
        }
    }
}

@Composable
fun DetailScreenUi(
    item: Item,
    navController: NavHostController
) {

    val placeholder = painterResource(id = R.drawable.img_placeholder)

    val scrollState = rememberScrollState(0)

    Box {
        Column(modifier = Modifier
            .verticalScroll(scrollState)
            .padding(8.dp)) {

            ItemImage(item, placeholder)

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = item.title, style = MaterialTheme.typography.displaySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = item.date, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${item.desc} ${item.desc} ${item.desc} ${item.desc}${item.desc}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        BackButton(navController)
    }
}

@Composable
private fun ItemImage(
    item: Item,
    placeholder: Painter
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item.image?.let {
            AsyncImage(
                model = item.image,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(),
                contentDescription = stringResource(R.string.item_image),
                placeholder = placeholder,
                fallback = placeholder,
                error = placeholder
            )
        } ?: kotlin.run {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    modifier = Modifier.width(56.dp),
                    painter = placeholder,
                    contentDescription = stringResource(R.string.item_image),
                )
            }
        }
    }
}

@Composable
private fun BackButton(navController: NavHostController) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.size(48.dp)
        ) {
        }
        Icon(
            Icons.Rounded.KeyboardArrowLeft,
            contentDescription = stringResource(R.string.go_back),
            tint = MaterialTheme.colorScheme.onPrimary,
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
