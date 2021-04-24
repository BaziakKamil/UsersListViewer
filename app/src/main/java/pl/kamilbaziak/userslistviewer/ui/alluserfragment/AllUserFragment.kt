package pl.kamilbaziak.userslistviewer.ui.alluserfragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pl.kamilbaziak.userslistviewer.R
import pl.kamilbaziak.userslistviewer.databinding.FragmentAllUserBinding
import pl.kamilbaziak.userslistviewer.model.UserModel
import pl.kamilbaziak.userslistviewer.utils.Utils

@AndroidEntryPoint
class AllUserFragment : Fragment(R.layout.fragment_all_user), AllUserAdapter.OnItemClickListener {

    private val viewModel: AllUserFragmentViewModel by viewModels()
    private lateinit var cm: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cm = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        //checking if network is unavailable, if yes fech data from restApi
        checkIfNetworkIsAvailableAndFetchDataFromRestApi()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allUserAdapter = AllUserAdapter(this, requireContext())

        val binding = FragmentAllUserBinding.bind(view)

        binding.apply {
            allUserRecyclerView.apply {
                adapter = allUserAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.userList.observe(viewLifecycleOwner, {
            allUserAdapter.submitList(it)
        })

        //receiving event frmom viewModel and acting for it
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.allUserEvent.collect { event ->
                when (event) {
                    is AllUserFragmentViewModel.AllUserListEvent.NavigateToPickedUserFragment -> {
                        val action =
                            AllUserFragmentDirections.actionAllUserFragmentToPickedUserFragment(
                                event.userModel,
                                event.userModel.userName
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    //passing clicked object form adapter to viewModel
    override fun onItemClicked(userModel: UserModel) {
        if (isNetworkAvailable())
            viewModel.navigateToPickedUserFragment(userModel)
        else
            Utils.showNoNetworkAvailableDialog(requireContext())
    }

    //setting menu button for synchro
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sync_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //action fro menu click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.sync_menu_button -> {
                checkIfNetworkIsAvailableAndFetchDataFromRestApi()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //checking if network is avilable
    private fun isNetworkAvailable(): Boolean {
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    //checking if network is available and fetching data from api
    private fun checkIfNetworkIsAvailableAndFetchDataFromRestApi() {
        if (!isNetworkAvailable())
            Utils.showNoNetworkAvailableDialog(requireContext())
        else
            viewModel.getUsersFromRepo()
    }
}