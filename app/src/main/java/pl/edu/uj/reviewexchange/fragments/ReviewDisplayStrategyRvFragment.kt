package pl.edu.uj.reviewexchange.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.uj.reviewexchange.adapters.BookReviewRecyclerViewAdapter
import pl.edu.uj.reviewexchange.databinding.FragmentReviewDisplayStrategyRvBinding
import pl.edu.uj.reviewexchange.models.BookReview

class ReviewDisplayStrategyRvFragment(private val reviews : LiveData<MutableList<BookReview>>) : Fragment() {

    private var _binding : FragmentReviewDisplayStrategyRvBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewDisplayStrategyRvBinding.inflate(layoutInflater, container, false)

        val recyclerView = binding.rvFragmentDisplayStrategyRv
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = BookReviewRecyclerViewAdapter()
        recyclerView.adapter = adapter

        reviews.observe(viewLifecycleOwner, {reviewList ->
            adapter.setReviewList(reviewList)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}