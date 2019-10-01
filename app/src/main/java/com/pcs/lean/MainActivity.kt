package com.pcs.lean

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.google.android.material.navigation.NavigationView
import com.pcs.lean.fragment.HistoryFragment
import com.pcs.lean.fragment.SettingFragment
import com.pcs.lean.fragment.WarningFragment

import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       toolbar = findViewById(R.id.toolbar)
       setSupportActionBar(toolbar)

       drawerLayout = findViewById(R.id.drawer_layout)
       val toggle = ActionBarDrawerToggle(
           this, drawerLayout, toolbar, 0, 0
       )
       drawerLayout.addDrawerListener(toggle)
       toggle.syncState()

       navView = findViewById(R.id.nav_view)
       navView.setNavigationItemSelectedListener(this)

       navigateToHome()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_warning -> {
                navigateToHome()
            }
            R.id.nav_history -> {
                navigateToFragment(
                    fragment = HistoryFragment(),
                    allowStateLoss = false,
                    containerViewId = R.id.fragment_container
                )
                setTitle("Historico")
            }
            R.id.nav_settings -> {
                navigateToFragment(
                    fragment = SettingFragment(),
                    allowStateLoss = false,
                    containerViewId = R.id.fragment_container
                )
                setTitle("Configuración")
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun navigateToHome(){
        navigateToFragment(
            fragment = WarningFragment(),
            allowStateLoss = false,
            containerViewId = R.id.fragment_container
        )
        setTitle("Incidencias")
    }

    private fun navigateToFragment(fragment: Fragment, allowStateLoss: Boolean = false, @IdRes containerViewId: Int){
        val ft = supportFragmentManager
                    .beginTransaction()
                    .replace(containerViewId, fragment)
        if (!supportFragmentManager.isStateSaved ){
            ft.commit()
        }
        else if (allowStateLoss){
            ft.commitAllowingStateLoss()
        }
    }

}
