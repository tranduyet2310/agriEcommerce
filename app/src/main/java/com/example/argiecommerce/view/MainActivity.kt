package com.example.argiecommerce.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.argiecommerce.R
import com.example.argiecommerce.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.argiecommerce.model.User
import com.example.argiecommerce.utils.Constants.USER
import com.example.argiecommerce.utils.ProgressDialog
import com.example.argiecommerce.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var isHomeFragment: Boolean = false
    private var user: User? = null
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val intent = intent
        if(intent != null && intent.hasExtra(USER)){
            user = intent.getParcelableExtra(USER) as? User
        } else {
            // Lấy dữ liệu từ login
            user = viewModel.user
        }
        // Truyền dữ liệu tới các fragment
        viewModel.user = user

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
                    isHomeFragment = true; supportActionBar?.show()
                }

                R.id.specialtyFragment,
                R.id.standardFragment,
                R.id.categoryFragment,
                R.id.profileFragment, R.id.seeAllFragment, R.id.loginFragment -> {
                    isHomeFragment = false; supportActionBar?.hide()
                }

                else -> {
                    isHomeFragment = false; supportActionBar?.show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_cart)?.isVisible = isHomeFragment
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_cart -> {
                if (user == null) {
                    val dialog = ProgressDialog.createMessageDialog(this, getString(R.string.need_to_login))
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
