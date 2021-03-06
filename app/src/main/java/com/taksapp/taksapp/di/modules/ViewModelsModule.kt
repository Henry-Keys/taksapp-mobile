package com.taksapp.taksapp.di.modules

import com.taksapp.taksapp.domain.TaxiRequest
import com.taksapp.taksapp.application.auth.viewmodels.LoginViewModel
import com.taksapp.taksapp.application.auth.viewmodels.RiderSignUpOtpConfirmationViewModel
import com.taksapp.taksapp.application.auth.viewmodels.RiderSignUpViewModel
import com.taksapp.taksapp.application.drivers.taxirequests.viewmodels.ArrivedViewModel
import com.taksapp.taksapp.application.drivers.taxirequests.viewmodels.ArrivingViewModel
import com.taksapp.taksapp.application.drivers.taxirequests.viewmodels.DriverMainViewModel
import com.taksapp.taksapp.application.drivers.taxirequests.viewmodels.IncomingTaxiRequestViewModel
import com.taksapp.taksapp.application.drivers.trips.viewmodels.TripFinishedViewModel
import com.taksapp.taksapp.application.drivers.trips.viewmodels.TripInProgressViewModel
import com.taksapp.taksapp.application.launch.viewmodels.LaunchViewModel
import com.taksapp.taksapp.application.riders.taxirequests.viewmodels.AutocompletePlaceChooserViewModel
import com.taksapp.taksapp.application.riders.taxirequests.viewmodels.FareEstimationViewModel
import com.taksapp.taksapp.application.riders.taxirequests.viewmodels.TaxiRequestViewModel
import com.taksapp.taksapp.application.shared.presentationmodels.TaxiRequestPresentationModel
import com.taksapp.taksapp.application.shared.presentationmodels.TripPresentationModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
val viewModelsModule = module {
    viewModel { LaunchViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RiderSignUpViewModel(get(), get()) }
    viewModel { RiderSignUpOtpConfirmationViewModel(get(), get()) }
    viewModel {
        AutocompletePlaceChooserViewModel(
            get()
        )
    }
    viewModel {
        FareEstimationViewModel(
            get(),
            get(),
            androidContext()
        )
    }
    viewModel { (taxiRequest: TaxiRequest) ->
        TaxiRequestViewModel(
            taxiRequest,
            get(),
            get(),
            androidContext()
        )
    }

    viewModel { DriverMainViewModel(get(), get(), get(), get(), get(), androidContext())}

    viewModel { (taxiRequest: TaxiRequestPresentationModel) ->
        IncomingTaxiRequestViewModel(
            taxiRequest,
            get(),
            get(),
            androidContext()
        )
    }

    viewModel { (taxiRequest: TaxiRequestPresentationModel) ->
        ArrivingViewModel(
            taxiRequest,
            get(),
            get(),
            get(),
            androidContext()
        )
    }

    viewModel { (taxiRequest: TaxiRequestPresentationModel) ->
        ArrivedViewModel(
            taxiRequest,
            get(),
            get(),
            get(),
            androidContext()
        )
    }

    viewModel { (trip: TripPresentationModel) ->
        TripInProgressViewModel(
            trip,
            get(),
            get(),
            androidContext()
        )
    }

    viewModel { (trip: TripPresentationModel) -> TripFinishedViewModel(trip) }
}