package com.example.casemanagment

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.casemanagment.TabLayout.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class caseScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_screen)



        val actionbar = supportActionBar
        //set actionbar title

        //set back button
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayHomeAsUpEnabled(true)

    //TabLayout
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout);
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager2);
        val affairId= intent.getStringExtra("affairId") as String;
        val documents= intent.getStringArrayExtra("documents") as Array<String>
     //   val dd:Array<String>  = arrayOf("hello","sdfsdfdf")

        val adapter = ViewPagerAdapter(supportFragmentManager,lifecycle,affairId,documents)



        viewPager2.adapter=adapter
        TabLayoutMediator(tabLayout,viewPager2){ tab,position->
            when (position){

                0-> {
                    tab.text ="Attachments"
                }
                1->{
                    tab.text="Histories"
                }

            }

        }.attach()
    //string
        val pageTitle:String = intent.getStringExtra("title") as String;


        setTitle(pageTitle)




    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}