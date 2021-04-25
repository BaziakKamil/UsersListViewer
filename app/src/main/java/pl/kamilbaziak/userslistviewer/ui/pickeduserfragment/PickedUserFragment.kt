package pl.kamilbaziak.userslistviewer.ui.pickeduserfragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pl.kamilbaziak.userslistviewer.R
import pl.kamilbaziak.userslistviewer.databinding.FragmentPickedUserBinding


@AndroidEntryPoint
class PickedUserFragment : Fragment(R.layout.fragment_picked_user) {

    private val viewModel: PickedUserFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getUserByUsername()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPickedUserBinding.bind(view)

        binding.apply {
            textViewUsername.text = viewModel.pickedUserUsername
            viewModel.loadImageWithUri(imageViewUserAvatar)
        }

        viewModel.progressVisible.observe(viewLifecycleOwner, { shoudBeVisible ->
            if(shoudBeVisible)
                binding.progressBar.visibility = View.VISIBLE
            else
                binding.progressBar.visibility = View.GONE
        })

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.pickedUserEvent.collect { event ->
                when (event) {
                    PickedUserFragmentViewModel.PickedUserListEvent.BindDataToViews -> {
                        binding.apply {
                            ifTextEmptyHideViewElseSetText(
                                textViewName,
                                viewModel.pickedUserWithMoreDetails.value?.name,
                            ""
                            )

                            ifTextEmptyHideViewElseSetText(
                                textViewFollowers,
                                viewModel.pickedUserWithMoreDetails.value?.followers.toString(),
                                getString(R.string.followersText)
                            )

                            ifTextEmptyHideViewElseSetText(
                                textViewLocation,
                                viewModel.pickedUserWithMoreDetails.value?.location,
                                getString(R.string.locationText)
                            )

                            //hiding layout if blog url empty, if not giving possibility to open in browser given link
                            if (ifEmptyBlogSIteHideLayout(
                                    textViewBlogUrl,
                                    blogSiteRelativeLayout,
                                    viewModel.pickedUserWithMoreDetails.value?.blog
                                )
                            ) {
                                imageViewGoToWebsite.setOnClickListener {
                                    val browserIntent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(viewModel.pickedUserWithMoreDetails.value?.blog)
                                    )
                                    startActivity(browserIntent)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //checking if text is empty and text or hide textview
    fun ifTextEmptyHideViewElseSetText(view: TextView, text: String?, textToInsertBeforeValue: String) {
        val textToInsert = "$textToInsertBeforeValue $text"
        if (text.isNullOrEmpty()) {
            view.visibility = View.GONE
        } else {
            view.text = textToInsert
            view.visibility = View.VISIBLE
        }
    }

    //checking if text is empty and text or hide textview
    fun ifEmptyBlogSIteHideLayout(view: TextView, layoutToHide: ViewGroup, text: String?): Boolean {
        return if (text.isNullOrEmpty()) {
            layoutToHide.visibility = View.GONE
            false
        } else {
            view.text = text
            layoutToHide.visibility = View.VISIBLE
            true
        }
    }
}