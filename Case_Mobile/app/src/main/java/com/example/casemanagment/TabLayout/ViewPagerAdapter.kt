package com.example.casemanagment.TabLayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.casemanagment.fragments.CaseAttachmentsFragment
import com.example.casemanagment.fragments.CaseHistoriesFragment

class ViewPagerAdapter(fragmentManager:FragmentManager,lifecycle:Lifecycle, val affairId:String? ,val documents:Array<String>?) : FragmentStateAdapter(
    fragmentManager, lifecycle
){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

       return  when (position){

            0-> {CaseAttachmentsFragment(documents)}
            1-> {

                CaseHistoriesFragment(affairId)}
            else -> {Fragment()}

        }
    }
}