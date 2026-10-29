package com.coffeetrack.app

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var score = 120
    private var coins = 50

    private lateinit var scoreText: TextView
    private lateinit var coinsText: TextView
    private lateinit var statusText: TextView

    private val brown = Color.rgb(76, 45, 30)
    private val cream = Color.rgb(255, 247, 232)
    private val orange = Color.rgb(207, 111, 52)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun makeText(
        text: String,
        size: Float,
        color: Int,
        bold: Boolean = false
    ): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = size
            setTextColor(color)
            gravity = Gravity.CENTER
            if (bold) setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(12, 12, 12, 12)
        }
    }

    private fun makeButton(text: String, action: () -> Unit): Button {
        return Button(this).apply {
            this.text = text
            textSize = 15f
            isAllCaps = false
            setTextColor(Color.WHITE)
            backgroundTintList =
                android.content.res.ColorStateList.valueOf(orange)
            gravity = Gravity.CENTER
            setPadding(12, 8, 12, 8)
            setOnClickListener { action() }
        }
    }

    private fun baseLayout(): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(20, 24, 20, 24)
            setBackgroundColor(cream)
        }
    }

    private fun addSpace(layout: LinearLayout, height: Int = 8) {
        layout.addView(TextView(this), LinearLayout.LayoutParams(
            1, height
        ))
    }

    private fun showHome() {
        val root = baseLayout()

        root.addView(makeText("☕ COFFEE TRACK", 28f, brown, true))
        root.addView(makeText("YOUR COFFEE SHOP ADVENTURE", 13f, orange, true))
        addSpace(root, 24)

        root.addView(makeText("🏠  WELCOME TO YOUR CAFE", 20f, brown, true))
        root.addView(makeText("Serve customers. Earn points. Grow your cafe!", 15f, brown))
        addSpace(root, 20)

        scoreText = makeText("⭐ Score: $score", 20f, brown, true)
        coinsText = makeText("🪙 Coins: $coins", 18f, brown, true)
        root.addView(scoreText)
        root.addView(coinsText)
        addSpace(root, 24)

        root.addView(makeButton("▶  START SHIFT") { showGame() },
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 64
            ))
        addSpace(root, 8)

        root.addView(makeButton("☕  CAFE MENU") { showMenu() },
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 60
            ))
        addSpace(root, 8)

        root.addView(makeButton("🛠  SHOP UPGRADES") { showUpgrades() },
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 60
            ))

        setContentView(root)
    }

    private fun showGame() {
        val root = baseLayout()
        var selected = ""

        root.addView(makeText("☕ YOUR COFFEE SHOP", 25f, brown, true))
        root.addView(makeText("⭐ $score points     🪙 $coins coins", 17f, orange, true))
        addSpace(root, 12)

        root.addView(makeText("👩  CUSTOMER AT THE COUNTER", 20f, brown, true))
        root.addView(makeText("\"Hello! I'd like a Latte, please.\"", 17f, brown))
        addSpace(root, 12)

        root.addView(makeText("CHOOSE THE ORDER", 16f, orange, true))


        val options = listOf(
            "☕ Latte",
            "🥤 Iced Coffee",
            "🥐 Croissant",
            "🍰 Cheesecake"
        )

        val selectionText = makeText("Select one item", 15f, brown)
        root.addView(selectionText)

        options.forEach { option ->
            root.addView(makeButton(option) {
                selected = option
                selectionText.text = "Selected: $option"
            }, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 64
            ))
        }

        addSpace(root, 12)

        statusText = makeText("", 16f, brown, true)
        root.addView(statusText)

        root.addView(makeButton("SERVE ORDER") {
            if (selected.isEmpty()) {
                statusText.text = "Please select an item first!"
            } else if (selected == "☕ Latte") {
                score += 20
                coins += 10
                statusText.text = "✅ Correct order! +20 points, +10 coins"
                selectionText.text = "Order served: Latte"
                updateScore()
            } else {
                score = (score - 10).coerceAtLeast(0)
                statusText.text = "❌ Wrong order! -10 points"
                updateScore()
            }
        }, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 60
        ))

        addSpace(root, 8)
        root.addView(makeButton("← BACK TO CAFE") { showHome() },
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 64
            ))

        setContentView(root)
    }

    private fun showMenu() {
        val root = baseLayout()
        root.addView(makeText("☕ CAFE MENU", 28f, brown, true))
        addSpace(root, 12)

        val items = listOf(
            "☕ Espresso", "🥤 Cappuccino", "☕ Latte",
            "🧋 Iced Coffee", "🥐 Croissant", "🍰 Cheesecake",
            "🍪 Chocolate Cookie", "🥪 Sandwich"
        )

        items.forEach { root.addView(makeText(it, 18f, brown)) }

        addSpace(root, 12)
        root.addView(makeButton("← BACK") { showHome() })
        setContentView(root)
    }

    private fun showUpgrades() {
        val root = baseLayout()
        root.addView(makeText("🛠 CAFE UPGRADES", 26f, brown, true))
        root.addView(makeText("Coins: $coins", 19f, orange, true))
        addSpace(root, 16)

        root.addView(makeText("🪑 New Table — 30 coins", 17f, brown))
        root.addView(makeButton("BUY NEW TABLE") {
            if (coins >= 30) {
                coins -= 30
                statusText = makeText("New table unlocked!", 16f, brown)
                showUpgrades()
            } else {
                showUpgradeMessage(root, "Not enough coins!")
            }
        })

        addSpace(root, 12)
        root.addView(makeText("☕ Better Coffee Machine — 50 coins", 17f, brown))
        root.addView(makeButton("BUY COFFEE MACHINE") {
            if (coins >= 50) {
                coins -= 50
                score += 25
                showUpgrades()
            } else {
                showUpgradeMessage(root, "Not enough coins!")
            }
        })

        addSpace(root, 16)
        root.addView(makeButton("← BACK TO CAFE") { showHome() })
        setContentView(root)
    }

    private fun showUpgradeMessage(root: LinearLayout, message: String) {
        root.addView(makeText(message, 15f, orange, true))
    }

    private fun updateScore() {
        scoreText.text = "⭐ Score: $score"
        coinsText.text = "🪙 Coins: $coins"
    }
}