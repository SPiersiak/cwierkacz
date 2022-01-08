package com.example.cwierkaczapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cwierkaczapp.fragments.HomeFragment
import com.example.cwierkaczapp.fragments.MyActivityFragment
import com.example.cwierkaczapp.fragments.SearchFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private var sectionPagerAdapter:SectionPageAdapter?=null
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val myActivityFragment = MyActivityFragment()
    private val userId=FirebaseAuth.getInstance().currentUser?.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sectionPagerAdapter = SectionPageAdapter(supportFragmentManager)

        container.adapter = sectionPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {



            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }
        })
    }

    fun onLogout(v: View){
        firebaseAuth.signOut()
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    override fun onResume() {
            super.onResume()
            if(userId ==null){
                startActivity(LoginActivity.newIntent(this))
                finish()
            }
    }

    inner class SectionPageAdapter(fm: FragmentManager):FragmentPagerAdapter(fm){
        override fun getCount() = 3

        override fun getItem(position: Int): Fragment {
            return when(position){
                0-> homeFragment
                1->searchFragment
                else->myActivityFragment
            }
        }

    }

    companion object {
        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }
}