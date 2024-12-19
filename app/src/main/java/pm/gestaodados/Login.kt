package pm.gestaodados

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import pm.gestaodados.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // Verifica se o usuário já está logado
        checkAutoLogin()

        // Configura o listener para o botão de login
        binding.btnLogin.setOnClickListener { doLogin(it) }
    }

    private fun checkAutoLogin() {
        val email = sharedPreferences.getString("user_email", null)
        val password = sharedPreferences.getString("user_password", null)

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            // Se o email e a senha estão armazenados, faça o login automaticamente
            binding.txtEmail.setText(email)
            binding.txtPassword.setText(password)
            binding.checkGuardar.isChecked = true
            doLogin(View(this)) // Chama a função de login
        }
    }

    fun doLogin(view: View) {
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        // Verificar campos vazios
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_LONG).show()
            return
        }

        // Instanciar a fila de requisições
        val queue = Volley.newRequestQueue(this)
        val url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g25/api/login.php"

        // Criar um novo objeto JSON e colocar os parâmetros nele
        val jsonParams = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        // Solicitar uma resposta JSON da URL fornecida
        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, url, jsonParams,
            { response ->
                Log.d("Response", response.toString())
                try {
                    val msg = response.optString("message", "No message received")
                    val responseEmail = response.optString("email", "")

                    // Verifica se o login foi bem-sucedido
                    if (responseEmail == email) {
                        // Salvar email e senha no SharedPreferences se o checkbox estiver marcado
                        if (binding.checkGuardar.isChecked) {
                            val editor = sharedPreferences.edit()
                            editor.putString("user_email", email)
                            editor.putString("user_password", password) // Salva a senha
                            editor.apply()
                        } else {
                            // Limpa as credenciais se o checkbox não estiver marcado
                            val editor = sharedPreferences.edit()
                            editor.remove("user_email")
                            editor.remove("user_password")
                            editor.apply()
                        }

                        // Inicia a MainActivity após o login bem-sucedido
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error parsing response", Toast.LENGTH_LONG).show()
                    Log.e("LoginError", e.toString())
                }
            },
            { error ->
                // Tente obter a resposta do erro
                val errorMessage = error.message ?: "Error occurred"
                Toast .makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                Log.d("Error.Response", error.toString())
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                return mapOf("Content-Type" to "application/json")
            }
        }

        // Adiciona a requisição à fila
        queue.add(jsonRequest)
    }
}