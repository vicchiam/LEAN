package com.pcs.lean

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

import com.google.android.material.navigation.NavigationView
import com.pcs.lean.fragment.HistoryFragment
import com.pcs.lean.fragment.SettingFragment
import com.pcs.lean.fragment.WarningFragment
import com.pcs.lean.model.Warning

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    var cache: ExpirableCache = ExpirableCache(60)
    var warning: Warning? = null

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
