package com.example.github.repositories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.github.repositories.data.POSITION
import com.example.github.repositories.data.REQUEST_KEY
import com.example.github.repositories.utils.NetworkResult
import com.example.github.repositories.utils.hide
import com.example.github.repositories.utils.show

class MainFragment : Fragment() {

    private val viewModel = MainViewModel()

    private var swipeRefresh: SwipeRefreshLayout? = null
    private var recyclerview: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var adapter: RepositoryAdapter? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        viewModel.fetchItems()

        progressBar = view.findViewById(R.id.progress_bar)
        swipeRefresh = view.findViewById(R.id.swipe_refresh)
        swipeRefresh!!.setOnRefreshListener { viewModel.refresh() }

        recyclerview = view.findViewById(R.id.news_list)
        recyclerview!!.layoutManager = LinearLayoutManager(context)

        viewModel.repositories.observeForever { networkResult ->

            when (networkResult) {
                is NetworkResult.Success -> {
                    progressBar!!.hide()
                    networkResult.data?.let {
                        adapter = RepositoryAdapter(
                            networkResult.data.take(20).toMutableList(),
                            requireActivity()
                        )
                        recyclerview!!.adapter = adapter
                    }
                }
                is NetworkResult.Error -> {
                    progressBar!!.hide()
                    popCurrentFragment()
                    showErrorFragment()
                }
                is NetworkResult.Loading -> {
                    progressBar!!.show()
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // it will update the adapter for particular position which is bookmarked by user at detail page
        requireActivity().supportFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val position = bundle.getInt(POSITION)
            adapter?.notifyItemChanged(position)
        }
    }

    private fun popCurrentFragment() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun showErrorFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(android.R.id.content, ErrorFragment())
            .commit()
    }
}