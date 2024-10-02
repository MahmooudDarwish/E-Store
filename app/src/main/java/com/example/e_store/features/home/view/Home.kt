package com.example.e_store.features.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_store.features.home.view_model.HomeViewModel
import com.example.e_store.features.home.view_model.HomeViewModelFactory
import com.example.e_store.utils.data_layer.EStoreRepositoryImpl
import com.example.e_store.utils.data_layer.local.room.EStoreLocalDataSourceImpl
import com.example.e_store.utils.data_layer.remote.EStoreRemoteDataSourceImpl

@Composable
fun HomeScreen() {
     val repo by lazy {
        EStoreRepositoryImpl.getInstance(
            EStoreRemoteDataSourceImpl.getInstance(),
            EStoreLocalDataSourceImpl()
        )
    }

    val homeViewModelFactory by lazy {
       HomeViewModelFactory(repo)
    }


    val viewModel: HomeViewModel = viewModel(factory = homeViewModelFactory)

    val uiState by viewModel.brands.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getBrands()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Text(
            text = "HomeScreen",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}