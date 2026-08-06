package com.pairlix.dating

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.pairlix.dating.viewModel.SocketViewModel

class AppLifecycleObserver(
    private val socketViewModel: SocketViewModel
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // App came to foreground
        socketViewModel.sendOnline()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        // App went to background
       // socketViewModel.sendOffline()
    }
}