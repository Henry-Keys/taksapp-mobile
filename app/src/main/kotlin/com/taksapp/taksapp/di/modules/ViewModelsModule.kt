package com.taksapp.taksapp.di.modules

import com.taksapp.taksapp.ui.auth.viewmodels.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { LoginViewModel(get(), get()) }
}