package com.example.influenz2

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

// -----------------------
// SplashActivity
// -----------------------
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Tampilkan splash selama 5 detik, kemudian pindah ke OnboardingActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
        }, 5000)
    }
}

// -----------------------
// Model data untuk Onboarding
// -----------------------
data class OnBoardingData(val title: String, val description: String, val imageRes: Int)

// -----------------------
// OnboardingActivity menggunakan ViewPager2
// -----------------------
class OnboardingActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var btnSkip: Button
    private lateinit var btnNext: Button

    private lateinit var indicator1: View
    private lateinit var indicator2: View
    private lateinit var indicator3: View

    private val pages = listOf(
        OnBoardingData(
            "Temukan Produk yang Tepat",
            "Tidak lagi bingung mencari produk yang kamu inginkan.",
            R.drawable.onboard_image
        ),
        OnBoardingData(
            "Belajar & Berkembang Bersama",
            "Dapatkan berbagai modul pembelajaran dan edukasi eksklusif.",
            R.drawable.onboard_image2
        ),
        OnBoardingData(
            "Gabung & Mulai Perjalananmu!",
            "Gabung komunitas dan mulai perjalanan kesuksesanmu!",
            R.drawable.onboard_image3
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        btnSkip = findViewById(R.id.btnSkip)
        btnNext = findViewById(R.id.btnNext)
        indicator1 = findViewById(R.id.indicator1)
        indicator2 = findViewById(R.id.indicator2)
        indicator3 = findViewById(R.id.indicator3)

        viewPager.adapter = OnboardingAdapter(pages)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
            }
        })

        btnSkip.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnNext.setOnClickListener {
            if (viewPager.currentItem < pages.size - 1) {
                viewPager.currentItem = viewPager.currentItem + 1
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun updateIndicators(position: Int) {
        indicator1.setBackgroundResource(if (position == 0) R.drawable.indicator_active else R.drawable.indicator_gray)
        indicator2.setBackgroundResource(if (position == 1) R.drawable.indicator_active else R.drawable.indicator_gray)
        indicator3.setBackgroundResource(if (position == 2) R.drawable.indicator_active else R.drawable.indicator_gray)
    }
}

// -----------------------
// Adapter untuk tampilan Onboarding (menggunakan ViewPager2)
// -----------------------
class OnboardingAdapter(private val items: List<OnBoardingData>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgOnboard: ImageView = itemView.findViewById(R.id.imgOnboard)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtDescription: TextView = itemView.findViewById(R.id.txtDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val data = items[position]
        holder.imgOnboard.setImageResource(data.imageRes)
        holder.txtTitle.text = data.title
        holder.txtDescription.text = data.description
    }

    override fun getItemCount(): Int = items.size
}

// -----------------------
// LoginActivity
// -----------------------
class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoogle: Button
    private lateinit var btnFacebook: Button
    private lateinit var tvForgotPassword: TextView
    private lateinit var tvSignUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoogle = findViewById(R.id.btnGoogle)
        btnFacebook = findViewById(R.id.btnFacebook)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        tvSignUp = findViewById(R.id.tvSignUp)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nama dan password belum diisi", Toast.LENGTH_SHORT).show()
            } else {
                // Proses login (tambahkan validasi dan logika autentikasi)
                Toast.makeText(this, "Proses masuk", Toast.LENGTH_SHORT).show()

                // Menghubungkan ke HomeActivity setelah login berhasil
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        btnGoogle.setOnClickListener {
            launchURL("https://accounts.google.com/Login")
        }

        btnFacebook.setOnClickListener {
            launchURL("https://www.facebook.com/login.php/")
            // Logika login dengan Facebook
            Toast.makeText(this, "Login dengan Facebook", Toast.LENGTH_SHORT).show()
        }

        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun launchURL(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }
}

// -----------------------
// SignUpActivity
// -----------------------
class SignUpActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etWhatsApp: EditText
    private lateinit var btnSignUp: Button
    private lateinit var tvBackToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        etUsername = findViewById(R.id.etUsername)
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        etWhatsApp = findViewById(R.id.etWhatsApp)
        btnSignUp = findViewById(R.id.btnSignUp)
        tvBackToLogin = findViewById(R.id.tvBackToLogin)

        setupTogglePassword(etPassword)
        setupTogglePassword(etConfirmPassword)

        btnSignUp.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            val whatsApp = etWhatsApp.text.toString().trim()

            // Validasi apakah ada field yang kosong.
            if (username.isEmpty() ||
                fullName.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                confirmPassword.isEmpty() ||
                whatsApp.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Username, Full Name, Email, Password, Konfirmasi Password, dan WhatsApp belum diisi",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Validasi apakah password dan confirm password sama.
            if (password != confirmPassword) {
                Toast.makeText(
                    this,
                    "Kata sandi tidak cocok",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Jika validasi sukses, lanjutkan proses sign up
            Toast.makeText(
                this,
                "Proses Sign Up ...",
                Toast.LENGTH_SHORT
            ).show()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }
        // Membuat teks "Login" pada tvBackToLogin clickable dan warnai biru.
        val fullText = tvBackToLogin.text.toString()  // misal "Sudah punya akun? Login"
        val loginKeyword = "Login"
        val startIndex = fullText.indexOf(loginKeyword)
        if (startIndex != -1) {
            val endIndex = startIndex + loginKeyword.length
            val spannableString = SpannableString(fullText)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                    finish()
                }
                override fun updateDrawState(ds: android.text.TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = ContextCompat.getColor(this@SignUpActivity, R.color.blue)
                    ds.isUnderlineText = false
                }
            }
            spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            tvBackToLogin.text = spannableString
            tvBackToLogin.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun setupTogglePassword(editText: EditText) {
        editText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                // Pastikan drawable di sebelah kanan ada
                val drawableEnd = 2
                editText.compoundDrawables[drawableEnd]?.let { drawable ->
                    if (event.rawX >= (editText.right - drawable.bounds.width())) {
                        // Jika password disembunyikan, tampilkan dan ganti icon menjadi ic_eye,
                        // dan sebaliknya.
                        if (editText.transformationMethod is PasswordTransformationMethod) {
                            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0)
                        } else {
                            editText.transformationMethod = PasswordTransformationMethod.getInstance()
                            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_off, 0)
                        }
                        // Pastikan kursor tetap di akhir teks
                        editText.setSelection(editText.text.length)
                        view.performClick()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }
}

// -----------------------
// ForgotPasswordActivity
// -----------------------
class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var btnSend: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etEmail = findViewById(R.id.etEmail)
        btnSend = findViewById(R.id.btnSend)
        btnBack = findViewById(R.id.btnBack)

        btnSend.setOnClickListener {
            startActivity(Intent(this, EmailSentActivity::class.java))
        }
        btnBack.setOnClickListener {
            finish()
        }
    }
}

// -----------------------
// EmailSentActivity
// -----------------------
class EmailSentActivity : AppCompatActivity() {
    private lateinit var btnContinue: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_sent)

        btnContinue = findViewById(R.id.btnContinue)
        btnBack = findViewById(R.id.btnBack)

        btnContinue.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        btnBack.setOnClickListener {
            finish()
        }
    }
}

// -----------------------
// ChangePasswordActivity
// -----------------------
class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var etCode: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmNewPassword: EditText
    private lateinit var btnChangePassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        etCode = findViewById(R.id.etCode)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword)
        btnChangePassword = findViewById(R.id.btnChangePassword)

        btnChangePassword.setOnClickListener {
            if (etNewPassword.text.toString() == etConfirmNewPassword.text.toString()){
                startActivity(Intent(this, PasswordChangedActivity::class.java))
            } else {
                Toast.makeText(this, "Password tidak cocok!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

// -----------------------
// PasswordChangedActivity
// -----------------------
class PasswordChangedActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_changed)

        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }
}

// -----------------------------
// Kelas Beranda (Home) Activity
// -----------------------------
class HomeActivity : AppCompatActivity() {
    private lateinit var tvPoints: TextView
    private lateinit var tvProductShowcase: TextView
    private lateinit var tvModule: TextView
    private lateinit var btnSpecialOffer: Button
    private lateinit var btnContinueModule: Button
    private lateinit var recyclerViewFeaturedProducts: RecyclerView
    private lateinit var recyclerViewProducts: RecyclerView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tvPoints = findViewById(R.id.tvPoints)
        tvProductShowcase = findViewById(R.id.tvProductShowcase)
        tvModule = findViewById(R.id.tvModule)
        btnSpecialOffer = findViewById(R.id.btnSpecialOffer)
        btnContinueModule = findViewById(R.id.btnContinueModule)
        recyclerViewFeaturedProducts = findViewById(R.id.recyclerViewFeaturedProducts)
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts)

        // Update UI with user data (this could be fetched from a ViewModel or API)
        tvPoints.text = "1000 Points"
        tvProductShowcase.text = "100 Products"
        tvModule.text = "100 Modules"

        btnSpecialOffer.setOnClickListener {
            // Handle special offer button click
            Toast.makeText(this, "Special Offer Clicked!", Toast.LENGTH_SHORT).show()
        }

        btnContinueModule.setOnClickListener {
            // Handle continue module button click
            Toast.makeText(this, "Continue Module Clicked!", Toast.LENGTH_SHORT).show()
        }

        // Populate RecyclerView with featured products
        recyclerViewFeaturedProducts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFeaturedProducts.adapter = ProductAdapter(getFeaturedProducts())

        // Populate RecyclerView with all products
        recyclerViewProducts.layoutManager = GridLayoutManager(this, 2)
        recyclerViewProducts.adapter = ProductAdapter(getAllProducts())
    }

    private fun getFeaturedProducts(): List<Product> {
        // Example data, this should be fetched from a database or API
        return listOf(
            Product("Baju Koko Lebaran Cowo", 100000, 5000),
            Product("Kaos Polos Pria", 150000, 7000)
        )
    }

    private fun getAllProducts(): List<Product> {
        // Example data, this should be fetched from a database or API
        return listOf(
            Product("Baju Koko Lebaran Cowo", 100000, 5000),
            Product("Kaos Polos Pria", 150000, 7000),
            Product("Sepatu Sport Pria", 200000, 8000),
            Product("Tas Punggung Wanita", 120000, 4000)
        )
    }
}

// ------------------------
// Kelas (Classes) Activity
// ------------------------
class ClassesActivity : AppCompatActivity() {
    private lateinit var recyclerViewClasses: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classes)

        recyclerViewClasses = findViewById(R.id.recyclerViewClasses)
        recyclerViewClasses.layoutManager = LinearLayoutManager(this)
        recyclerViewClasses.adapter = ClassAdapter(getClasses())
    }

    private fun getClasses(): List<ClassItem> {
        // Example data, this should be fetched from a database or API
        return listOf(
            ClassItem("Fundamental Affiliate Marketing", 60),
            ClassItem("Social Media Mastery", 30),
            ClassItem("Advanced Web Development", 100)
        )
    }
}

// ------------------------
// Kelas (Product) Activity
// ------------------------
class ProductsActivity : AppCompatActivity() {
    private lateinit var recyclerViewAllProducts: RecyclerView
    private lateinit var recyclerViewMyProducts: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        recyclerViewAllProducts = findViewById(R.id.recyclerViewAllProducts)
        recyclerViewMyProducts = findViewById(R.id.recyclerViewMyProducts)

        recyclerViewAllProducts.layoutManager = GridLayoutManager(this, 2)
        recyclerViewAllProducts.adapter = ProductAdapter(getAllProducts())

        recyclerViewMyProducts.layoutManager = LinearLayoutManager(this)
        recyclerViewMyProducts.adapter = ProductAdapter(getMyProducts())
    }

    private fun getAllProducts(): List<Product> {
        // Example data, this should be fetched from a database or API
        return listOf(
            Product("Baju Koko Lebaran Cowo", 100000, 5000),
            Product("Kaos Polos Pria", 150000, 7000),
            Product("Sepatu Sport Pria", 200000, 8000),
            Product("Tas Punggung Wanita", 120000, 4000)
        )
    }
}
    private fun getMyProducts(): List<Product> {
        // Example data, this should be fetched from a database or API
        return listOf(
            Product("Baju Koko Lebaran Cowo", 100000, 5000),
            Product("Kaos Polos Pria", 150000, 7000)
        )
    }

// ---------------------
// Kelas (Chat) Activity
// ---------------------
class ChatActivity : AppCompatActivity() {
    private lateinit var recyclerViewChats: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerViewChats = findViewById(R.id.recyclerViewChats)
        recyclerViewChats.layoutManager = LinearLayoutManager(this)
        recyclerViewChats.adapter = ChatAdapter(getChats())
    }

    private fun getChats(): List<Chat> {
        // Example data, this should be fetched from a database or API
        return listOf(
            Chat("Adam Himawan", "Loren ipsum dolor sit amet...", "Today", 10),
            Chat("Sirah For Kids", "Resulullah...", "Yesterday", 20)
        )
    }
}

// ------------------------
// Kelas (Profile) Activity
// ------------------------
class ProfileActivity : AppCompatActivity() {
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnNotification: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)
        btnLogout = findViewById(R.id.btnLogout)
        btnNotification = findViewById(R.id.btnNotification)

        // Update profile data
        tvName.text = "Riyan Fauzi"
        tvEmail.text = "ryanx@gmail.com"

        btnLogout.setOnClickListener {
            // Handle logout logic
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnNotification.setOnClickListener {
            // Open notification settings or details
            Toast.makeText(this, "Opening notifications...", Toast.LENGTH_SHORT).show()
        }
    }
}

// --------------------------------
// Product Adapter for RecyclerView
// --------------------------------
class ProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val productEarn: TextView = itemView.findViewById(R.id.productEarn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.productName.text = product.name
        holder.productPrice.text = "Rp ${product.price}"
        holder.productEarn.text = "Earn Rp ${product.earn}"
    }

    override fun getItemCount(): Int = products.size
}

// ------------------------------
// Class Adapter for RecyclerView
// ------------------------------
class ClassAdapter(private val classes: List<ClassItem>) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {
    inner class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val className: TextView = itemView.findViewById(R.id.className)
        val classProgress: TextView = itemView.findViewById(R.id.classProgress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
        return ClassViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classItem = classes[position]
        holder.className.text = classItem.name
        holder.classProgress.text = "${classItem.progress}% completed"
    }

    override fun getItemCount(): Int = classes.size
}

// -----------------------------
// Chat Adapter for RecyclerView
// -----------------------------
class ChatAdapter(private val chats: List<Chat>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatName: TextView = itemView.findViewById(R.id.chatName)
        val chatMessage: TextView = itemView.findViewById(R.id.chatMessage)
        val chatTime: TextView = itemView.findViewById(R.id.chatTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]
        holder.chatName.text = chat.name
        holder.chatMessage.text = chat.message
        holder.chatTime.text = chat.time
    }

    override fun getItemCount(): Int = chats.size
}

// -----------------------
// Data Models
// -----------------------
data class Product(val name: String, val price: Int, val earn: Int)
data class ClassItem(val name: String, val progress: Int)
data class Chat(val name: String, val message: String, val time: String, val unreadCount: Int)

