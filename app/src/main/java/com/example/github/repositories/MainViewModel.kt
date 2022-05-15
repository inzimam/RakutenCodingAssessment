package com.example.github.repositories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.repositories.data.*
import com.example.github.repositories.utils.NetworkResult
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(GITHUB_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service: GitHubEndpoints = retrofit.create(GitHubEndpoints::class.java)

    val repositories = MutableLiveData<NetworkResult<List<RepositoryDTO>>>()

    fun fetchItems() {
        EspressoIdlingResource.increment()
        viewModelScope.launch(Dispatchers.Main) {
            repositories.value = NetworkResult.Loading()
            delay(1_000) // This is to simulate network latency, please don't remove!
            var response: Response?
            withContext(Dispatchers.IO) {
                response = service.searchRepositories(QUERY, SORT, ORDER).execute().body()
            }
            repositories.value = NetworkResult.Success(response?.items!!)
            EspressoIdlingResource.decrement()
        }
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.Main) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            var response: Response?
            withContext(Dispatchers.IO) {
                response = service.searchRepositories(QUERY, SORT, ORDER).execute().body()
            }
            repositories.value = NetworkResult.Success(response?.items!!)
        }
    }
}