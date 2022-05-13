package com.example.github.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment


class ErrorFragment : Fragment(), View.OnClickListener {


    private var retry: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retry = view.findViewById(R.id.retry)
        retry!!.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.retry) {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(android.R.id.content, MainFragment())
                .commit()
        }
    }
}