package com.example.e_store.utils.data_layer

import com.example.e_store.utils.data_layer.local.room.EStoreLocalDataSource
import com.example.e_store.utils.data_layer.remote.EStoreRemoteDataSource

interface EStoreRepository : EStoreRemoteDataSource, EStoreLocalDataSource