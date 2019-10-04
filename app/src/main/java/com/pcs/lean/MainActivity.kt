package com.pcs.lean

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

import com.google.android.material.navigation.NavigationView
import com.pcs.lean.fragment.HistoryFragment
import com.pcs.lean.fragment.SelectorFragment
import com.pcs.lean.fragment.SettingFragment
import com.pcs.lean.fragment.WarningFragment
import com.pcs.lean.model.Warning

const val FRAGMENT_WARNING: Int = 1
const val FRAGMENT_SELECTOR: Int = 2
const val FRAGMENT_SETTINGS: Int = 3
const val FRAGMENT_HISTORY: Int = 4

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    lateinit var cache: ExpirableCache
    var warning: Warning? = null

    var currentFragment: Int =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cache = ExpirableCache(5)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        drawerLayout = findViewById(R.id.drawer_layout)

        var navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        toolbar.setNavigationOnClickListener{ v ->
           if(currentFragment== FRAGMENT_SELECTOR)
               navigateToHome()
           else if (!drawerLayout.isDrawerOpen(GravityCompat.START))
               drawerLayout.openDrawer(GravityCompat.START)
        }

        navigateToHome()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if(currentFragment== FRAGMENT_SELECTOR)
                navigateToHome()
            else
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
                title="Historico"
                currentFragment = FRAGMENT_HISTORY
            }
            R.id.nav_settings -> {
                navigateToFragment(
                    fragment = SettingFragment(),
                    allowStateLoss = false,
                    containerViewId = R.id.fragment_container
                )
                title="Configuración"
                currentFragment = FRAGMENT_SETTINGS
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun navigateToHome(){
        navigateToFragment(
            fragment = WarningFragment(),
            allowStateLoss = false,
            containerViewId = R.id.fragment_container
        )
        title="Incidencias"
        currentFragment = FRAGMENT_WARNING
        changeActionBarButton(1)
    }

    fun navigateToOFsSelector(){
        navigateToFragment(
            fragment = SelectorFragment(),
            allowStateLoss = false,
            containerViewId = R.id.fragment_container
        )
        title="Selección de OF"
        currentFragment = FRAGMENT_SELECTOR
        changeActionBarButton(2)
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

    private fun changeActionBarButton(type: Int){
        when(type){
            1 -> {
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
            }
            2 -> {
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            }
        }

    }

}
