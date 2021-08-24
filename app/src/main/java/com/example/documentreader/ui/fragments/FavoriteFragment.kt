package com.example.documentreader.ui.fragments

import android.Manifest
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MediatorLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.documentreader.adapters.DocListAdapter
import com.example.documentreader.databinding.FragmentAllDocsBinding
import com.example.documentreader.db.FavDocs
import com.example.documentreader.db.FavDocsDataBase
import com.example.documentreader.other.AppSettingsContracts
import com.example.documentreader.other.PermissionUtil
import com.example.documentreader.other.StoragePermissionContract
import com.example.documentreader.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {


    private lateinit var launcher: ActivityResultLauncher<String>
    private lateinit var externalLauncher: ActivityResultLauncher<String>
    private val docViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentAllDocsBinding
    private var readActivityLauncher = registerForActivityResult(AppSettingsContracts.GoToReadActivityContract()){}
    @Inject
    lateinit var dataBase: FavDocsDataBase




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllDocsBinding.inflate(inflater, container, false)
        val view = binding.root

        val favList = dataBase.getDao().getAllDocsList()
        val favDocsLiveData = dataBase.getDao().getAllDocs()
        // Setup recycler view




        val docAdapter = DocListAdapter(
            docViewModel,
            Callbacks.likePressed(viewLifecycleOwner, docViewModel),
            Callbacks.goToReadActivity(readActivityLauncher)
        )

        binding.run {
            allDocsRecycler.layoutManager = LinearLayoutManager(requireContext())
            allDocsRecycler.adapter = docAdapter

            root.setOnRefreshListener {
                Log.e("refresh", "resresh")
                val action = AllDocsFragmentDirections.actionAllDocsFragmentSelf()
                findNavController().navigate(action)
            }
        }
        //



        val mediatorLiveData = object: MediatorLiveData<Pair<List<FavDocs>, List<DocumentFile>>>(){
            var favDocs: List<FavDocs>? = null
            var allDocs: List<DocumentFile>? = null


            init {
                addSource(favDocsLiveData){  favDocs ->
                    this.favDocs = favDocs
                    allDocs?.let {  allDocs ->
                        postValue(favDocs to allDocs)
                    }
                }
                addSource(docViewModel.fileList){  allDocs->
                    this.allDocs = allDocs
                    favDocs?.let {  favDocs ->
                        postValue(favDocs to allDocs)
                    }
                }

            }
        }



        mediatorLiveData.observe(viewLifecycleOwner,SetupFavoriteRecyclerViewObserver(docAdapter))

        //load all docs from memory
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            executeWithPerm {
                docViewModel.getAllDocs()
            }
        }
        //
        return view
    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun <T> executeWithPerm(code: () -> T) {
        if (!PermissionUtil.checkExternalStoragePermission(requireContext())) {
            PermissionUtil.sdk29andMore {
                requestManageExternalStoragePermission(code)
            } ?: requestReadExternalStoragePermission(code)
        } else code()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun <T> requestManageExternalStoragePermission(permissionGranted: () -> T) {
        val alertBox = AlertDialog.Builder(requireContext())
            .setTitle("External storage permission")
            .setMessage("Manage External storage permission is needed for this app work, please enable it in device settings")
            .setPositiveButton("Continue") { _, _ ->
                launcher.launch(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            }
        launcher = registerForActivityResult(StoragePermissionContract()) {
            when (it) {
                true -> {
                    permissionGranted()
                }
                false -> alertBox.show()
            }
        }

        alertBox.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun <T> requestReadExternalStoragePermission(permissionGranted: () -> T) {


        val settingsDialog = AlertDialog.Builder(requireContext())
            .setTitle("External storage permission")
            .setMessage("Read External storage permission is needed for this app work, please enable it in device settings")
            .setPositiveButton("Continue") { _, _ ->
                launcher.launch(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            }


        launcher = registerForActivityResult(AppSettingsContracts()) {
            when (it) {
                true -> {
                    permissionGranted()
                }
                false -> settingsDialog.show()
            }
        }

        val permissionDeniedDialog = AlertDialog.Builder(requireContext())
            .setTitle("External storage permission")
            .setMessage("Read External storage permission is needed for this app work")
            .setPositiveButton("Continue") { _, _ ->
                externalLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

        externalLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            when {
                it -> permissionGranted()
                !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    Log.e("write permission", "denied forever")
                    settingsDialog.show()
                }
                else -> {
                    Log.e("write permission", "denied")
                    permissionDeniedDialog.show()
                }

            }
        }
        externalLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    }




}

