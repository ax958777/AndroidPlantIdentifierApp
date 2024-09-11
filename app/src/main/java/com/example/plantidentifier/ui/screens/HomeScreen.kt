package com.example.plantidentifier.ui.screens
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.plantidentifier.viewmodels.HomeViewModel
import com.example.plantidentifier.viewmodels.HomeViewModel.UiState

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val uiState by viewModel.uiState.collectAsState()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            viewModel.setSelectedImage(uri)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Plant Identifier",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        when (val state = uiState) {
            is UiState.Initial -> {
                // Show initial state
                if (selectedImageUri == null) {
                    Text(
                        "Select a plant image to identify",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
            is UiState.Success -> {
                LaunchedEffect(state) {
                    navController.navigate("result/${state.plantInfo}")
                }
            }
            is UiState.Error -> {
                Text(
                    "Error: ${state.message}",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        if (selectedImageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(selectedImageUri),
                contentDescription = "Selected plant image",
                modifier = Modifier
                    .size(200.dp)
                    .padding(vertical = 16.dp),
                contentScale = ContentScale.Crop
            )
        }

        Button(
            onClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            enabled = uiState !is UiState.Loading
        ) {
            Text(if (selectedImageUri == null) "Select Plant Image" else "Change Image")
        }

        if (selectedImageUri != null) {
            Button(
                onClick = {
                    //Version 5
                    viewModel.identifyPlant()
                    /*viewModel.identifyPlant() { text ->
                        navController.navigate("result/$text")
                    }*/
                          },
                enabled = uiState !is UiState.Loading
            ) {
                Text("Identify Plant")
            }
        }
    }
}

