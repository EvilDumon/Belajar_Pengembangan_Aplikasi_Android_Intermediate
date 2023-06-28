package com.example.myplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.myplayer.databinding.ActivityMainBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.util.Util

class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var player: ExoPlayer?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun initializedPlayer(){
        val mediaItem = MediaItem.fromUri(URL_VIDEO)
        val anotherMediaItem = MediaItem.fromUri(URL_AUDIO)
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.videoView.player = exoPlayer
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.addMediaItem(anotherMediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    private fun releasePlayer(){
        player?.release()
        player = null
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 23) initializedPlayer()
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        if (Util.SDK_INT <= 23 && player == null) initializedPlayer()
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) releasePlayer()
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    companion object{
        const val URL_VIDEO = "https://du.sf-converter.com/go?payload=1*eJzVlFtvozgUx79KVWn8tEnBGAiVrBHkRu4hba4vEQEHPNwcIJB0tN99bdrVdB9nV%2FtQCf1sLGyf%2Fzmc%2F8%2FHIrvmHlnn8ePzY1iWrHh%2Beqrrun3PruX1RNpeljzVbumF3ys8vY3t%2BHTvxtH%2B8Y%2BPnSP%2Ftze6V59m%2F7wxz2Gr1SrS1kXy9DzVtXaQZUFMKuqTrDmqmbHYvZ9cL%2FpObozmBMtaR1Y1VdJ1QCieb%2FraYbYYWvtqKgeTaXWZ7i0TUIYNuS0behsqaltGEqA%2BzlqmPYi6ibXYltt79%2B5vwmBiG%2FS%2BcJJJ6S%2F94s7Ww2S0WC%2BPTgfQ0g2w2PouGn%2BIBDm5XHkgRRHjOylAEmLXA0mCFfkb7EINJCn%2BJIqv8TeXBrGWFj5ICuxe%2BVqW5iCpcMJBMQQsxlABNKWlV6f%2BiRVY6WhQ5XczDxuGp3Y9GOnz4S6vyUiROlNqTq1sWvet2WHnFNJ45xC2q0HF8grLIKEJwU3Kv8FBwhBIC5y%2FXN%2Fc9WXFUHa790aDYWtEznE9AwHNGyFeTFKMDEVHqgb8a44VSW1LXH%2BclFhWDSTrkCcfQRUiA4g1UQkEdQOCc8UzjkBECHNjWpHmwDOvGIZIknSINODhbd8CJV9SkSIjBYIU7%2BjYhGgVea90pjhcrJu7PEPvleZZIpSDMgFfgFdE5LOpB5%2F8qoRYZR6nSAAfRAL4kBYcXB%2BnUMcHrouTKwIFDbC5uEjOuFOvglFojuIXbRltJ4N1eFys5cNxDo1eEZS73mt4DMxWcFtWycw%2Br0dTk%2FbNt8Nc6b5p487iXK71aj9bJ31fZkc0LApDk6rkaBF2mKfDTv1N6YH4b3FJKOJLBERAiQgxaWKuhFomtHz6EfhGEehQ6R5vZr1yeKDjl%2F5%2B%2BUNTzqa9QDFlp82RDoYMDX3HnhwHaLYN1WjeM6cr26SmOtVuPTYwDxrrWpPFKriti9Um2taGVUaI5K1K7l1Y5fCHx8kf3q1N433FblU04IpZwacKT6SsoIZqQ9GMsiaJNkWwodIQNdQF9Y6g8U6DU5FgQ%2FGlYqCGakOtod6w09D4qlbRlO53rQIa%2FFoFfVgFahua9tkqYAfqkqJB6T9aBfxXVvH%2BF%2Fx%2FZjF2ujffPsR9KyrLNL3ZzNrnveuLe9mqB8scmW4oL21tfoo8YRbVbEleb64mnzbD2DEkfvKPKLbSmm1u1jEObXcf%2B852V8vBlzOLkpYx4U7xUhNSPvSyijy0Hmb3B5u4eflgeiEp%2BFfvhSrM8vH5lyf8%2BRdBNbi0*1681543477*3a74e121cc494eb1"
        const val URL_AUDIO = "https://cdn.pixabay.com/download/audio/2022/01/18/audio_d0a13f69d2.mp3?filename=indie-folk-king-around-here-15045.mp3"
    }
}