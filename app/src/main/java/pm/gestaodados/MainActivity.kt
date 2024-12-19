package pm.gestaodados

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pm.gestaodados.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            binding.placeholder.removeAllViews()
            when(item.itemId) {
                R.id.navigation_Scan -> layoutInflater.inflate(
                        R.layout.scan,
                        binding.placeholder,
                        true)
                R.id.navigation_Patinhas -> layoutInflater.inflate(
                    R.layout.patinhas,
                    binding.placeholder,
                    true)
                R.id.navigation_Adocao -> layoutInflater.inflate(
                    R.layout.adocao,
                    binding.placeholder,
                    true)
                R.id.navigation_Profile -> layoutInflater.inflate(
                    R.layout.logout,
                    binding.placeholder,
                        true)
            }
            true
        }
         binding.bottomNavigationView.selectedItemId = R.id.navigation_Scan


    }

}