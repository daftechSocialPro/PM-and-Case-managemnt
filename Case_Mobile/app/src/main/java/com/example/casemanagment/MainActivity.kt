package com.example.casemanagment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.casemanagment.Model.TblUser
import com.example.casemanagment.Remote.IMyAPI
import com.example.casemanagment.Remote.RetrofitClient
import com.example.casemanagment.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {


    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private lateinit var layout: View
    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }



    val compositeDisposable = CompositeDisposable()
    lateinit var iMyAPI: IMyAPI



   override fun onStop() {

       compositeDisposable.clear()
        super.onStop()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle("Login")
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        layout = binding.mainLayoutd

        setContentView(view)


//        onClickRequestPermission(view)
//        onClickRequestPermissionInternet(view)
//        onClickRequestPermissionWrite(view)
//




        var btn_login = findViewById<TextView>(R.id.btn_login);
        var edit_username= findViewById<EditText>(R.id.edt_user_name);
        var edit_password = findViewById<EditText>(R.id.edt_password);
        var btn_setting = findViewById<ImageView>(R.id.btn_setting);



        btn_setting.setOnClickListener{

            var SettingBottomSheet = SettingBottomSheetFragment();
            SettingBottomSheet.show(supportFragmentManager, "BottomSheetDialog")
//

        }
        btn_login.setOnClickListener {


            val sharedPreferences= getSharedPreferences("Setting", Context.MODE_PRIVATE);
            val ipAdd= sharedPreferences.getString("IpAddress", null)
            val pub = sharedPreferences.getString("PubName", null)
            val port =sharedPreferences.getString("Port", null)

           //Toast.makeText(this@MainActivity,ipAdd+pub,Toast.LENGTH_SHORT).show()


            if (ipAdd !=null  && port != null) {
                iMyAPI = RetrofitClient.getInstance(ipAdd, pub, port).create(IMyAPI::class.java)

                val dialog = SpotsDialog.Builder().setContext(this@MainActivity).build()
                dialog.show()

                val user = TblUser(edit_username.text.toString(), edit_password.text.toString())

                compositeDisposable.addAll(
                    iMyAPI.Loginuser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { s ->

                                if (s.fullName!=null) {

                                    val intent = Intent(this, HomeScreen::class.java)

                                    intent.putExtra("userViewModel", s);
                                    startActivity(intent)


                                } else {

                                    Toast.makeText(
                                        this@MainActivity,
                                        "UserName or Password don't match",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                dialog.dismiss()


                            },
                            { t: Throwable? ->
                                Toast.makeText(this@MainActivity, t!!.message, Toast.LENGTH_SHORT)
                                    .show()
                                dialog.dismiss()

                            })
                )
            }
            else {
                Toast.makeText(this@MainActivity,"please make sure you have entered the setting correctly",Toast.LENGTH_SHORT)

            }
        }
    }

    fun onClickRequestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_granted),
                    Snackbar.LENGTH_SHORT,
                    null
                ) {}
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok)
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }
    fun onClickRequestPermissionWrite(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_granted),
                    Snackbar.LENGTH_SHORT,
                    null
                ) {}
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok)
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }

    fun onClickRequestPermissionInternet(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) == PackageManager.PERMISSION_GRANTED -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_granted),
                    Snackbar.LENGTH_SHORT,
                    null
                ) {}
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.INTERNET
            ) -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_required_Internet),
                    Snackbar.LENGTH_SHORT,
                    getString(R.string.ok)
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.INTERNET
                    )
                }
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.INTERNET
                )
            }
        }
    }

}
fun View.showSnackbar(
    view: View,
    msg: String,
    length: Int,
    actionMessage: CharSequence?,
    action: (View) -> Unit
) {
    val snackbar = Snackbar.make(view, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action(this)
        }.show()
    } else {
        snackbar.show()
    }
}
  