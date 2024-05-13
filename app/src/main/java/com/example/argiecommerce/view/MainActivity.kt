package com.example.argiecommerce.view

import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.ActivityMainBinding
import com.example.argiecommerce.model.LoginApiResponse
import com.example.argiecommerce.model.LoginRequest
import com.example.argiecommerce.model.Token
import com.example.argiecommerce.model.User
import com.example.argiecommerce.network.Api
import com.example.argiecommerce.network.RetrofitClient
import com.example.argiecommerce.utils.Constants
import com.example.argiecommerce.utils.Constants.USER
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.utils.Utils
import com.example.argiecommerce.viewmodel.LoginViewModel
import com.example.argiecommerce.viewmodel.UserViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var pendingIntent: PendingIntent
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(this)
    }
    private val apiService: Api by lazy {
        RetrofitClient.getInstance().getApi()
    }
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private var user: User? = null
    private lateinit var viewModel: UserViewModel
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        checkCertificate()

        val intent = intent
        if (intent != null && intent.hasExtra(USER)) {
            user = intent.getParcelableExtra(USER) as? User
            if (user != null){
                requestNewToken()
                signIn()
                sendRegistrationToServer()
            }
        } else {
            // Lấy dữ liệu từ login
            user = viewModel.user
            signIn()
            lifecycleScope.launch {
                val localToken = Firebase.messaging.token.await()
                Log.d("TEST", "token fcm: ${localToken}")
                updateToken(localToken)
            }
        }

        viewModel.user = user
        setupNavHostFragment()

        val notifyIntent = Intent(this, MainActivity::class.java)
        pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(notifyIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    private fun signIn(){
        auth.signInWithEmailAndPassword(user!!.email, user!!.password)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    Log.d("TEST", "signInWithEmail:success")
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendRegistrationToServer() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful){
                Log.w("TEST", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            // check fcm token
            val savedFcmToken = loginUtils.getFcmToken()
            if (savedFcmToken == null || !savedFcmToken.equals(token)){
                loginUtils.saveFcmToken(token)
                // Update token to server
                lifecycleScope.launch {
                    val userToken = loginUtils.getUserToken()
                    val response = apiService.updateFcmToken(userToken, user!!.id, token)
                    if (response.isSuccessful){
                        if (response.body() != null){
                            Log.d("TEST", "update fcm token successfully")
                        } else {
                            Log.d("TEST", "failed to update fcm token")
                        }
                    }
                }
            }
            Log.d("TEST", "token fcm: ${token}")
            updateToken(token)
        })
//        lifecycleScope.launch {
//            val localToken = Firebase.messaging.token.await()
////            val localToken = FirebaseMessaging.getInstance().token.await()
//            Log.d("TEST", "token fcm: ${localToken}")
//        }
    }

    private fun updateToken(fcmToken: String){
        val firebaseUser = auth.currentUser
        val ref = FirebaseDatabase.getInstance().reference.child(Constants.CHAT_TOKEN)
        val token = Token(fcmToken)
        ref.child(firebaseUser!!.uid).setValue(token)
    }

    private fun checkCertificate() {
        Utils.readRawResource(this, R.raw.server)
    }
    private fun requestNewToken() {
        val loginRequest = LoginRequest(user!!.email, user!!.password)
        loginViewModel.getLoginResponseLiveData(loginRequest)
            .observe(this, { state -> processLoginResponse(state) })
    }

    private fun processLoginResponse(state: ScreenState<LoginApiResponse?>) {
        when (state) {
            is ScreenState.Loading -> {
                val progressDialog = ProgressDialog()
                alertDialog = progressDialog.createAlertDialog(this)
            }

            is ScreenState.Success -> {
                if (state.data != null) {
                    alertDialog.dismiss()
                    Log.d("TEST", "in here")
                    user?.let { loginUtils.saveUserInfo(state.data, it) }
                }
            }

            is ScreenState.Error -> {
                alertDialog.dismiss()
                if (state.message != null) {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupNavHostFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = binding.bottomNavigationView
        setupWithNavController(bottomNavigationView, navController)

        setSupportActionBar(findViewById(R.id.toolbar))
        val config = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, config)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    supportActionBar?.show()
                }

                else -> {
                    supportActionBar?.hide()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_cart)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_cart -> {
                if (viewModel.user == null) {
                    val dialog =
                        ProgressDialog.createMessageDialog(this, getString(R.string.need_to_login))
                    dialog.show()
                } else goToCartFragment()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToCartFragment() {
        navController.navigate(R.id.action_homeFragment_to_cartFragment)
    }

//    override fun onStart() {
//        super.onStart()
//        val currentUser = auth.currentUser
//        if (currentUser != null){
//            reload()
//        }
//    }
//
//    private fun reload(){
//
//    }
}
