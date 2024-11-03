package com.kadir.dersuygulama2

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var scoreText: TextView
    private lateinit var ball: ImageView
    private lateinit var gameLayout: RelativeLayout
    private var score = 0
    private val handler = Handler()
    private val delay: Long = 1000 // Topun çıkış aralığı (ms cinsinden)
    private val gameDuration: Long = 30000 // 30 saniye

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ana oyun düzenini oluştur
        gameLayout = RelativeLayout(this).apply {
            background = ContextCompat.getDrawable(this@MainActivity, R.drawable.stad)
        }
        setContentView(gameLayout)

        // Skor metnini kare bir kutuda oluştur ve daha belirgin hale getir
        scoreText = TextView(this).apply {
            text = "Score: 0"
            textSize = 28f // Skor metnini daha büyük yap
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            // Skor metni için kare bir çerçeve oluştur
            val border = GradientDrawable()
            border.shape = GradientDrawable.RECTANGLE
            border.setColor(Color.parseColor("#88000000")) // Hafif siyah arka plan
            border.setStroke(4, Color.WHITE) // Beyaz çerçeve
            background = border
            setPadding(40, 20, 40, 20) // Kutunun iç boşluğunu ayarla
        }
        val scoreParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.ALIGN_PARENT_TOP)
            addRule(RelativeLayout.CENTER_HORIZONTAL)
            setMargins(0, 50, 0, 0)
        }
        gameLayout.addView(scoreText, scoreParams)

        // Futbol topu görselini oluştur
        ball = ImageView(this).apply {
            setImageResource(R.drawable.futbol_topu) // Futbol topu görseli
            visibility = View.INVISIBLE
        }
        val ballParams = RelativeLayout.LayoutParams(250, 250) // Topun boyutunu artırdık
        gameLayout.addView(ball, ballParams)

        ball.setOnClickListener {
            score++
            scoreText.text = "Score: $score"
            ball.visibility = View.INVISIBLE // Tıkladığında top kaybolur
            spawnBall() // Yeni top konumlandırılır
        }

        spawnBall() // Oyunun başlaması için ilk top konumlandırılır

        // Oyun süresini 30 saniyeye ayarla
        handler.postDelayed({
            endGame()
        }, gameDuration)
    }

    private fun spawnBall() {
        handler.postDelayed({
            // Ekranda rastgele konum oluştur
            val randomX = Random.nextInt(0, gameLayout.width - ball.width)
            val randomY = Random.nextInt(0, gameLayout.height - ball.height)

            ball.x = randomX.toFloat()
            ball.y = randomY.toFloat()
            ball.visibility = View.VISIBLE // Top görünür hale gelir

            // Eğer topa tıklanmazsa belli bir süre sonra tekrar kaybolur ve başka bir yerde çıkar
            handler.postDelayed({
                if (ball.visibility == View.VISIBLE) {
                    ball.visibility = View.INVISIBLE
                    spawnBall()
                }
            }, delay)
        }, delay)
    }

    private fun endGame() {
        // Top görünmez hale gelir ve oyun sona erer
        ball.visibility = View.INVISIBLE
        handler.removeCallbacksAndMessages(null) // Tüm geri çağrıları durdur

        // Skoru ekranda ortada oyun içi ile aynı tarzda göster
        scoreText.apply {
            text = "Game Over\nScore: $score"
            textSize = 28f // Oyun içi ile aynı boyutta
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER

            // Oyun içi çerçeveyle aynı stili kullan
            val border = GradientDrawable()
            border.shape = GradientDrawable.RECTANGLE
            border.setColor(Color.parseColor("#88000000")) // Hafif siyah arka plan
            border.setStroke(4, Color.WHITE) // Beyaz çerçeve
            background = border
            setPadding(40, 20, 40, 20) // Aynı padding
        }
        val scoreEndParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.CENTER_IN_PARENT)
        }
        scoreText.layoutParams = scoreEndParams
    }
}
