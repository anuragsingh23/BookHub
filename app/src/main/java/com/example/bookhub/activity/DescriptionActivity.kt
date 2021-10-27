package com.example.bookhub.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import com.example.bookhub.database.BookDatabase
import com.example.bookhub.database.BookEntity
import com.example.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {


    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var txtBookName: TextView
    lateinit var txtBookAuther: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDesc: TextView
    lateinit var btnAddToFav: Button
    lateinit var toolbar: Toolbar

    var bookId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuther = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        btnAddToFav = findViewById(R.id.btnAddToFav)

        progressBar.visibility = View.VISIBLE
        progressLayout.visibility = View.VISIBLE

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book details"

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(this@DescriptionActivity, "Something went wrong", Toast.LENGTH_SHORT)
                .show()
        }
        if (bookId == "100") {
            finish()
            Toast.makeText(this@DescriptionActivity, "Something went wrong", Toast.LENGTH_SHORT)
                .show()

        }
        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest = object : JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonParams,
                com.android.volley.Response.Listener {
                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE

                            val bookImageUrl = bookJsonObject.getString("image")

                            Picasso.get().load(bookJsonObject.getString("image"))
                                .error(R.drawable.default_book_cover).into(imgBookImage)
                            txtBookName.text = bookJsonObject.getString("name")
                            txtBookAuther.text = bookJsonObject.getString("author")
                            txtBookPrice.text = bookJsonObject.getString("price")
                            txtBookRating.text = bookJsonObject.getString("rating")
                            txtBookDesc.text = bookJsonObject.getString("description")

                            val bookEntity = BookEntity(
                                bookId?.toInt() as Int,
                                txtBookName.toString(),
                                txtBookAuther.toString(),
                                txtBookPrice.toString(),
                                txtBookRating.toString(),
                                txtBookDesc.toString(),
                                bookImageUrl
                            )
                            val checkFav =
                                DBAsyncTask(applicationContext, bookEntity, 1).execute()
                            val isFav = checkFav.get()

                            if (isFav) {
                                btnAddToFav.text = "Remove From Favourites"
                                val favColour = ContextCompat.getColor(
                                    applicationContext,
                                    R.color.colour_favourite
                                )
                                btnAddToFav.setBackgroundColor(favColour)
                            } else {
                                btnAddToFav.text = "Add to Favourites"
                                val noFavColour =
                                    ContextCompat.getColor(applicationContext, R.color.teal_700)
                                btnAddToFav.setBackgroundColor(noFavColour)
                            }
                            btnAddToFav.setOnClickListener {
                                if (!DBAsyncTask(applicationContext, bookEntity, 1).execute()
                                        .get()
                                ) {
                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                    val result = async.get()
                                    if (result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book added to favourites",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        btnAddToFav.text = "Remove From Favourite"
                                        val favColor = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.colour_favourite
                                        )
                                        btnAddToFav.setBackgroundColor(favColor)
                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Some error occured",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }else{
                                    val async=DBAsyncTask(applicationContext,bookEntity,3).execute()
                                    val result=async.get()

                                    if (result){
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book Removed From Favourites",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        btnAddToFav.text="Add To Favourites"
                                        val noFavColour =
                                            ContextCompat.getColor(applicationContext, R.color.teal_700)
                                        btnAddToFav.setBackgroundColor(noFavColour)
                                    }else{
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Some error occured",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                }

                            }

                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some error has occured",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some error has occured",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                },
                com.android.volley.Response.ErrorListener {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Volley Error $it",
                        Toast.LENGTH_SHORT
                    ).show()

                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "a0be80058430d2"
                    return headers
                }

            }
            queue.add(jsonRequest)
        } else {
            val dailog = AlertDialog.Builder(this@DescriptionActivity)
            dailog.setTitle("Error")
            dailog.setMessage("Internet Connection Not Found")
            dailog.setPositiveButton("Open Settings") { text, listner ->
                // Do Nothing
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dailog.setNegativeButton("exit") { text, listner ->
                //Do nothing
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dailog.create()
            dailog.show()
        }


    }

    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        /*
       Mode 1 -> Check DB if the book is favourite or not
       Mode 2 -> Save the book into DB as favourite
       Mode 3 -> Remove the favourite book
       * */

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    // Check DB if the book is favourite or not
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null

                }
                2 -> {
                    // Save the book into DB as favourite
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true

                }
                3 -> {
                    // Remove the favourite book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true

                }
            }

            return false
        }
    }
}