package pm.gestaodados

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Logout : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logout) // Certifique-se de que o layout está correto

        // Inicializa o SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // Inicializa o botão de logout
        btnLogout = findViewById(R.id.btnLogout)

        // Configura o listener para o botão de logout
        btnLogout.setOnClickListener {
            doLogout()
        }
    }

    private fun doLogout() {
        // Limpa os dados do SharedPreferences
        val editor = sharedPreferences.edit()
        editor.clear() // Limpa todos os dados
        editor.apply()

        // Exibe uma mensagem de confirmação
        Toast.makeText(this, "Logout realizado com sucesso", Toast.LENGTH_LONG).show()

        // Redireciona para a LoginActivity
        startActivity(Intent(this, Login::class.java))
        finish() // Fecha a atividade atual
    }
}