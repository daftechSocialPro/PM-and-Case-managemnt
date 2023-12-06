package com.example.casemanagment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.casemanagment.Model.Affairs
import com.example.casemanagment.Model.NotificationCount
import com.example.casemanagment.Model.TblUser
import com.example.casemanagment.Remote.*
import com.example.casemanagment.fragments.AppointmentFragment
import com.example.casemanagment.fragments.CaseFragment
import com.example.casemanagment.fragments.WaitingListFragment
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class HomeScreen:AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle //toggle of nav view


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Cases");
        setContentView(R.layout.home_screen)




        val caseFragment = CaseFragment()
        val appointmentFragment=AppointmentFragment()

        makeCurrentFragment(caseFragment)

//get ipaddress and  publish name

        val sharedPreferences= getSharedPreferences("Setting", Context.MODE_PRIVATE);
        val ipAdd= sharedPreferences.getString("IpAddress", null)
        val pub = sharedPreferences.getString("PubName", null)


//get user value

        val user = intent.getSerializableExtra("userViewModel") as? TblUser

        val editor = sharedPreferences.edit();

        editor.apply{

            putString("empId", user!!.employeeID);


        }.apply();



//navigation view and bottom navigation

        val drawerLayout:DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView =findViewById(R.id.nav_view)
        val bottomView: BottomNavigationView = findViewById(R.id.bottom)

        val header:View =navView.getHeaderView(0)
        val fullName:TextView =header.findViewById(R.id.fullname);
        val structureName:TextView = header.findViewById(R.id.Structure);
        val imageView: CircleImageView = header.findViewById(R.id.user_image)
        val executor= Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())




        val ipAddd= sharedPreferences.getString("IpAddress", null)
        val pubb = sharedPreferences.getString("PubName", null)
        val port = sharedPreferences.getString("Port", null)
        val empId = sharedPreferences.getString("empId",null)
        val compositeDisposable = CompositeDisposable()


        var iMyAPI: IMyAPI = RetrofitClient.getInstance(ipAddd, pubb,port).create(IMyAPI::class.java)


        val userd = TblUser(empId)
        compositeDisposable.addAll(
            iMyAPI.getNotification(userd!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {

                            s -> if (s!=null){
                                if (s.affairCount!!>=1)
                                badgeSetup(R.id.nav_affair, s.affairCount!!, bottomView)
                        if (s.waitingListCount!!>=1)
                            badgeSetup(R.id.nav_WaitingList, s.waitingListCount!!, bottomView)

                        if (s.appointmentCount!!>=1)
                        badgeSetup(R.id.nav_Appointment, s.appointmentCount!!, bottomView)




                    }
                   },
                    { t: Throwable? ->
                        Toast.makeText(this, t!!.message, Toast.LENGTH_SHORT)
                            .show()


                    }


                ))



        var image:Bitmap?= null
        executor.execute{


            var imageURL = "";
            if (pub!=""){


                imageURL ="http://$ipAdd:$port/$pub/" + user?.imagePath;
            }
            else {
                imageURL =  "http://$ipAdd:$port/" + user?.imagePath;
            }



            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    imageView.setImageBitmap(image)
                }
            }


            catch (e: Exception) {
                e.printStackTrace()
            }
        }
        fullName.text= user?.fullName
        structureName.text=user?.structureName +" | "+ user?.userRole



        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            val intent= Intent(this,MainActivity::class.java)
            when(it.itemId){

                R.id.nav_Appointment ->{

                        drawerLayout.closeDrawer(GravityCompat.START)
                    makeCurrentFragment(appointmentFragment)
                }
                R.id.nav_affair ->{
                    drawerLayout.closeDrawer(GravityCompat.START)
                 makeCurrentFragment(caseFragment)
                }
                R.id.nav_WaitingList ->{
                drawerLayout.closeDrawer(GravityCompat.START)
                makeCurrentFragment(WaitingListFragment())}
                R.id.nav_signout -> startActivity(intent)



            }
            true
        }
        bottomView.setOnNavigationItemSelectedListener {


            when(it.itemId){
                R.id.nav_Appointment -> {


                        drawerLayout.closeDrawer(GravityCompat.START)

                    makeCurrentFragment(appointmentFragment)



                }
                R.id.nav_WaitingList-> {

                        drawerLayout.closeDrawer(GravityCompat.START)
                    makeCurrentFragment(WaitingListFragment())
                }
                R.id.nav_affair ->{

                        drawerLayout.closeDrawer(GravityCompat.START)
                    makeCurrentFragment(caseFragment)
                }
                            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper,fragment)
            commit()
        }

    private fun badgeSetup (id:Int,alerts:Int,bottomView:BottomNavigationView){

        val badge: BadgeDrawable = bottomView.getOrCreateBadge(id)
        badge.isVisible = true
        badge.number = alerts
    }


    private fun badgeClear (id:Int,bottomView:BottomNavigationView){

        val badgeDrawable: BadgeDrawable? = bottomView.getBadge(id)
        if (badgeDrawable!=null){

            badgeDrawable.isVisible = false
            badgeDrawable.clearNumber()
        }


    }


}