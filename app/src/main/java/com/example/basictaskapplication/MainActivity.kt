package com.example.basictaskapplication

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.example.basictaskapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val tasks = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks)
        val taskListView = findViewById<ListView>(R.id.taskListView)
        taskListView.adapter = adapter

        binding.fab.setOnClickListener { view ->
            var titulo = ""
            var descricao = ""
            var status = ""

            val popupTitulo = createInputDialog("Título") { text ->
                titulo = text
            }

            val popupDescricao = createInputDialog("Descrição") { text ->
                descricao = text
            }

            val popupStatus = createStatusSelector { selectedStatus ->
                status = selectedStatus
            }

            popupTitulo.setOnDismissListener {
                if (titulo.isNotEmpty()) {
                    popupDescricao.show()
                }
            }

            popupDescricao.setOnDismissListener {
                if (descricao.isNotEmpty()) {
                    popupStatus.show()
                }
            }

            popupStatus.setOnDismissListener {
                if (status.isNotEmpty()) {
                    val task = "$titulo - $descricao - $status"
                    tasks.add(task)
                    adapter.notifyDataSetChanged()
                    Snackbar.make(view, "Tarefa criada: $task", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }
            }

            popupTitulo.show()
        }
    }

    private fun createInputDialog(title: String, onTextEntered: (String) -> Unit): AlertDialog {
        val editText = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setView(editText)
            .setPositiveButton("Ok") { dialog, _ ->
                val text = editText.text.toString()
                onTextEntered(text)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
            .create()

        return dialog
    }

    private fun createStatusSelector(onStatusSelected: (String) -> Unit): AlertDialog {
        val spinner = Spinner(this)
        val statusOptions = arrayOf("Pendente", "Concluída")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusOptions)
        spinner.adapter = adapter

        val dialog = AlertDialog.Builder(this)
            .setTitle("Status")
            .setView(spinner)
            .setPositiveButton("Ok") { dialog, _ ->
                val selectedStatus = spinner.selectedItem.toString()
                onStatusSelected(selectedStatus)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
            .create()

        return dialog
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
