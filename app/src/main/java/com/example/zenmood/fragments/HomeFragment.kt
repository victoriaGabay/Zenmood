package com.example.zenmood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.zenmood.R
import com.example.zenmood.classes.SharedPreferences
import com.example.zenmood.classes.User
import com.example.zenmood.classes.firebase
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var v : View
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var btn : Button
    private lateinit var user : User
    private val parentJob = Job()
    private val fbScope = CoroutineScope(Dispatchers.Main + parentJob)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)
        viewPager = v.findViewById(R.id.view_pager)
        tabLayout = v.findViewById(R.id.tab_layout)


        return v
    }
    override fun onStart() {
        super.onStart()
        val parentJob = Job()
        val fbScope = CoroutineScope(Dispatchers.Main + parentJob)

        if(!firebase.checkLogIn()){
            val action = HomeFragmentDirections.actionHomeFragmentToLogInFragment()
            v.findNavController().navigate(action)
        }else{
            fbScope.launch {
                user = firebase.getUser()
                if(!user.preferences){
                    val action = HomeFragmentDirections.actionHomeFragmentToPreferencesFrg(user)
                    v.findNavController().navigate(action)
                }
            }
        }



        viewPager.adapter = createCardAdapter()
        TabLayoutMediator(tabLayout, viewPager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> tab.text = "Actividades"
                1 -> tab.text = "Información"
                else -> tab.text = "undefined"
            }
        }).attach()

    }

    private fun createCardAdapter(): ViewPagerAdapter? {
        return ViewPagerAdapter(requireActivity())
    }

    class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {

            return when(position){
                0 -> MeditationFragment()
                1 -> InfoFragment()

                else -> HomeFragment()
            }
        }

        override fun getItemCount(): Int {
            return TAB_COUNT
        }

        companion object {
            private const val TAB_COUNT = 2
        }
    }
         fun pepe() : User{
            fbScope.launch {
                user = firebase.getUser()
            }
            return user
        }
}