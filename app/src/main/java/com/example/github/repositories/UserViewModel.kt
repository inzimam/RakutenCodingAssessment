package com.example.github.repositories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.repositories.data.GITHUB_URL
import com.example.github.repositories.data.GitHubEndpoints
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.UserDTO
import com.example.github.repositories.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(GITHUB_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service: GitHubEndpoints = retrofit.create(GitHubEndpoints::class.java)

    val user = MutableLiveData<NetworkResult<UserDTO>>()
    val repositories = MutableLiveData<NetworkResult<List<RepositoryDTO>>>()

    fun fetchUser(username: String) {
        // FIXME Use the proper scope

        viewModelScope.launch(Dispatchers.Main) {
            repositories.value = NetworkResult.Loading()
            delay(1_000) // This is to simulate network latency, please don't remove!
            var response: UserDTO?
            withContext(Dispatchers.IO) {
                response = service.getUser(username).execute().body()
            }
            if (response != null)
                user.value = NetworkResult.Success(response!!)
            else user.value = NetworkResult.Error("Something went wrong")
        }
    }

    fun fetchRepositories(reposUrl: String) {

        viewModelScope.launch(Dispatchers.Main) {
            repositories.value = NetworkResult.Loading()
            delay(1_000) // This is to simulate network latency, please don't remove!
            var response: List<RepositoryDTO>?
            withContext(Dispatchers.IO) {
                response = service.getUserRepositories(reposUrl).execute().body()
            }
            repositories.value = NetworkResult.Success(response!!)
        }
    }
}