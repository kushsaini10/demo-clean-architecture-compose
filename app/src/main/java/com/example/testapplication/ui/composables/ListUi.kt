package com.example.testapplication.ui.composables
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.testapplication.R
import com.example.testapplication.data.models.entities.Item
import com.example.testapplication.ui.activities.MainActivity
import com.example.testapplication.ui.state.Error
import com.example.testapplication.ui.state.Loaded
import com.example.testapplication.ui.state.Loading
import com.example.testapplication.ui.theme.TestApplicationTheme
import com.example.testapplication.viewmodels.HomeViewModel

@Preview
@Composable
fun MainActivity.ListUi(homeViewModel: HomeViewModel = viewModel()) {
    val homeUiState by homeViewModel.uiState.collectAsState()

    TestApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            when (homeUiState) {
                is Error -> {
                    Text(
                        text = (homeUiState as Error).message
                            ?: stringResource(id = R.string.common_error)
                    )
                }
                is Loaded -> {
                    LazyColumn(content = {
                        items((homeUiState as Loaded).data) { item ->
                            ItemUi(item)
                        }
                    })
                }
                Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun ItemUi(item: Item) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row {
            if (item.image.isNullOrBlank()) {
                Icon(Icons.Filled.Face, contentDescription = stringResource(R.string.item_image))
            } else {
                AsyncImage(
                    model = item.image,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .width(56.dp),
                    contentDescription = stringResource(R.string.item_image),
                    placeholder = rememberVectorPainter(Icons.Default.Face),
                    fallback = rememberVectorPainter(Icons.Default.Face),
                    error = rememberVectorPainter(Icons.Default.Face),
                )
            }
            Column {
                Text(text = item.title)
                Text(text = item.date)
                Text(text = item.desc)
            }
        }
    }
}
