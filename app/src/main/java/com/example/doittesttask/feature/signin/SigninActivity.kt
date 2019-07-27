package com.example.doittesttask.feature.signin

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.doittesttask.R
import com.example.doittesttask.architecture.base.BaseActivity
import com.example.doittesttask.architecture.base.ResultError
import com.example.doittesttask.architecture.base.ResultLoading
import com.example.doittesttask.architecture.base.ResultSuccess
import com.example.doittesttask.feature.tasklist.TaskListActivity
import com.example.doittesttask.util.afterTextChanged
import kotlinx.android.synthetic.main.activity_signin.*
import org.koin.android.viewmodel.ext.android.viewModel

class SigninActivity : BaseActivity() {

    private val viewModel by viewModel<SigninViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        setupUi()
        setupObserve()
    }

    private fun setupUi() {
        editTextEmail.afterTextChanged {
            if (Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                editTextEmail.error = null
            } else {
                editTextEmail.error = getString(R.string.signinErrorInvalidEmailAddress)
            }
        }
        switchMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeSigninMode(if (isChecked) SigninViewModel.Mode.REGISTER else SigninViewModel.Mode.LOG_IN)
        }
        buttonSignIn.setOnClickListener {
            if (editTextEmail.text.isBlank()) {
                editTextEmail.error = getString(R.string.signinErrorEmptyEmail)
                return@setOnClickListener
            }

            if (editTextPassword.text.isBlank()) {
                editTextPassword.error = getString(R.string.signinErrorEmptyPassword)
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.text.toString()).matches()) {
                editTextEmail.error = getString(R.string.signinErrorInvalidEmailAddress)
                return@setOnClickListener
            }

            viewModel.signinUser(editTextEmail.text.toString(), editTextPassword.text.toString())
        }
    }

    private fun setupObserve() {
        viewModel.signinModeLiveData.observe(this, Observer { mode ->
            mode ?: return@Observer

            when(mode) {
                SigninViewModel.Mode.LOG_IN -> buttonSignIn.text = getString(R.string.signinLogIn)
                SigninViewModel.Mode.REGISTER -> buttonSignIn.text = getString(R.string.signinRegister)
            }
        })

        viewModel.signinResultLiveData.observe(this, Observer { result ->
            result ?: return@Observer

            when (result) {
                is ResultSuccess -> {
                    progressBar.visibility = View.GONE
                    val intent = Intent(this, TaskListActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                is ResultError -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, result.exception.message, Toast.LENGTH_SHORT).show()
                }
                is ResultLoading -> {
                    progressBar.visibility = View.VISIBLE

                }
            }
        })
    }
}
