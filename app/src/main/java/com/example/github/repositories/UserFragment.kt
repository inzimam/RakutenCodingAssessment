package com.example.github.repositories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.github.repositories.data.OwnerDTO
import com.example.github.repositories.utils.NetworkResult
import com.example.github.repositories.utils.hide
import com.example.github.repositories.utils.show
import com.squareup.picasso.Picasso

class UserFragment(private val user: OwnerDTO) : Fragment() {

    private val viewModel = UserViewModel()

    private var title: TextView? = null
    private var image: ImageView? = null
    private var detail: TextView? = null
    private var url: TextView? = null
    private var list: RecyclerView? = null
    private var progressBar: ProgressBar? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        progressBar = view.findViewById(R.id.progress_bar)
        title = view.findViewById(R.id.title)
        image = view.findViewById(R.id.image)
        detail = view.findViewById(R.id.detail)
        url = view.findViewById(R.id.url)
        list = view.findViewById(R.id.list)

        title!!.text = user.login
        Picasso.get().load(user.avatar_url.toUri()).into(image)

        viewModel.fetchUser(user.login)
        viewModel.user.observeForever { networkResult ->
            when (networkResult) {
                is NetworkResult.Success -> {
                    detail!!.text = "Twitter handle: " + networkResult.data!!.twitter_username
                    viewModel.fetchRepositories(networkResult.data.repos_url!!)
                }
                is NetworkResult.Error -> {
                    progressBar!!.hide()
                }
                is NetworkResult.Loading -> {
                    progressBar!!.show()
                }
            }
        }
        viewModel.repositories.observeForever {
            when (it) {
                is NetworkResult.Success -> {
                    progressBar!!.hide()
                    list!!.adapter = RepositoryAdapter(it.data!!.toMutableList(), requireActivity())
                }
                is NetworkResult.Error -> {
                    progressBar!!.hide()
                }
                is NetworkResult.Loading -> {
                    progressBar!!.show()
                }
            }
        }
        return view
    }
}