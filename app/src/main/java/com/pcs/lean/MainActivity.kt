package com.pcs.lean

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

import com.google.android.material.navigation.NavigationView
import com.pcs.lean.fragment.*
import com.pcs.lean.model.Warning

const val FRAGMENT_WARNING: Int = 1
const val FRAGMENT_OF_SELECTOR: Int = 2
const val FRAGMENT_SETTINGS: Int = 3
const val FRAGMENT_HISTORY: Int = 4
const val FRAGMENT_INC_SELECTOR=5

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    lateinit var cache: ExpirableCache
    var warning: Warning? = null
    var idApp: Int = 0

    private var currentFragment: Int =1

    private lateinit var prefs :Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createID()

        cache = ExpirableCache(5)

        initView()

        navigateToHome()
    }

    private fun createID(){
        prefs = Prefs(this)
        idApp = prefs.idApp
        if(idApp==0){
            idApp =  (1..1000000).shuffled().first()
            prefs.idApp = idApp
        }
        Log.d("ID_APP", idApp.toString())
    }

    private fun initView(){
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        drawerLayout = findViewById(R.id.drawer_layout)

        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        toolbar.setNavigationOnClickListener{
            if(currentFragment== FRAGMENT_OF_SELECTOR || currentFragment == FRAGMENT_INC_SELECTOR)
                navigateToHome()
            else if (!drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if(currentFragment == FRAGMENT_OF_SELECTOR || currentFragment == FRAGMENT_INC_SELECTOR)
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
            fragment = SelectOfFragment(),
            allowStateLoss = false,
            containerViewId = R.id.fragment_container
        )
        title="Selección de OF"
        currentFragment = FRAGMENT_OF_SELECTOR
        changeActionBarButton(2)
    }

    fun navigateToIncSelector(){
        navigateToFragment(
            fragment = SelectIncFragment(),
            allowStateLoss = false,
            containerViewId = R.id.fragment_container
        )
        title="Selección de Incidebncia"
        currentFragment = FRAGMENT_INC_SELECTOR
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
