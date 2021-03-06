package pl.edu.uj.reviewexchange.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import pl.edu.uj.reviewexchange.R
import pl.edu.uj.reviewexchange.databinding.FragmentBookReviewsBinding
import pl.edu.uj.reviewexchange.models.BookReviewViewModel

class BookReviewsFragment : Fragment() {
    private var _binding : FragmentBookReviewsBinding? = null
    private val binding get() = _binding!!

    private val bookReviewVM by viewModels<BookReviewViewModel>()

    private lateinit var bookId : String
    private lateinit var bookName : String
    private lateinit var bookAuthor : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookReviewsBinding.inflate(layoutInflater, container, false)

        bookId = arguments?.getString("book_id").toString()
        bookName = arguments?.getString("book_name").toString()
        bookAuthor =  arguments?.getString("book_author").toString()

        bookReviewVM.bookHasReviews(bookId).observe(viewLifecycleOwner, { hasReviews ->
            if(hasReviews == false)
                displayMessageAlertDialog(getString(R.string.no_reviews_found))
        })

        // set default style of display to recycler view
        setRecyclerViewDisplayMode()
        binding.rgFragmentBookReviews.check(binding.rbFragmentBookReviewsRecyclerViewMode.id)

        binding.tvBookReviewAuthor.text = bookAuthor
        binding.tvBookReviewTitle.text = bookName

        binding.cvFragmentBookReviewsWrite.setOnClickListener {
            findNavController().navigate(R.id.action_bookReviewsFragment_to_writeReviewFragment,
                bundleOf("book_id" to bookId, "book_name" to bookName, "book_author" to bookAuthor))
        }

        // setting display mode
        binding.rgFragmentBookReviews.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                binding.rbFragmentBookReviewsViewPagerMode.id -> setViewPagerDisplayMode()

                binding.rbFragmentBookReviewsRecyclerViewMode.id -> setRecyclerViewDisplayMode()
            }

        }

        return binding.root
    }

    private fun setRecyclerViewDisplayMode() {
        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainerFragmentBookReviews,
                ReviewDisplayStrategyRvFragment(bookReviewVM.getReviews(bookId)))
        }
    }

    private fun setViewPagerDisplayMode() {
        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainerFragmentBookReviews,
                ReviewDisplayStrategyVpFragment(bookReviewVM.getReviews(bookId)))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}