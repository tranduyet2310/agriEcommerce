package com.example.argiecommerce.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.ActivityMainBinding
import com.example.argiecommerce.model.LoginApiResponse
import com.example.argiecommerce.model.LoginRequest
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants.USER
import com.example.argiecommerce.utils.LoginUtils
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.utils.ScreenState
import com.example.argiecommerce.utils.Utils
import com.example.argiecommerce.viewmodel.LoginViewModel
import com.example.argiecommerce.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }
    private val loginUtils: LoginUtils by lazy {
        LoginUtils(this)
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
            }
        } else {
            // Lấy dữ liệu từ login
            user = viewModel.user
        }

        viewModel.user = user
        setupNavHostFragment()
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
}
