package com.example.ucp2roomdatabase.ui.view.matakuliah

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2roomdatabase.R
import com.example.ucp2roomdatabase.data.entity.MataKuliah
import com.example.ucp2roomdatabase.ui.customewidget.TopAppBar
import com.example.ucp2roomdatabase.ui.viewmodel.matakuliah.DetailMKViewModel
import com.example.ucp2roomdatabase.ui.viewmodel.matakuliah.DetailUiState
import com.example.ucp2roomdatabase.ui.viewmodel.matakuliah.PenyediaMKViewModel
import com.example.ucp2roomdatabase.ui.viewmodel.matakuliah.toMataKuliahEntity

@Composable
fun DetailMKView (
    modifier: Modifier = Modifier,
    viewModel: DetailMKViewModel = viewModel (factory = PenyediaMKViewModel.Factory),
    onBack: () -> Unit = { },
    onEditClick: (String) -> Unit = { },
    onDeleteClick: () -> Unit = { }
){
    Scaffold (
        modifier= Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.purple_500))
            .padding(16.dp)
            .padding(top = 18.dp),
        topBar = {
            TopAppBar (
                judul = "Detail MataKuliah",
                showBackButton = true,
                onBack = onBack,
                modifier = modifier
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEditClick(viewModel.detailUiState.value.detailUiEvent.kode)
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit MataKuliah",
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.purple_500))
                .padding(innerPadding)
        )
        val detailUiState by viewModel.detailUiState.collectAsState()

        BodyDetailMK(
            modifier = Modifier.padding(innerPadding),
            detailUiState = detailUiState,
            onDeleteClick = {
                viewModel.deleteMatKul()
                onDeleteClick()
            }
        )
    }
}

@Composable
fun BodyDetailMK (
    modifier: Modifier = Modifier,
    detailUiState: DetailUiState = DetailUiState(),
    onDeleteClick: () -> Unit = { }
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    when {
        detailUiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator() // Tampilkan loading
            }
        }

        detailUiState.isUiEventNotEmpty -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ItemDetailMK(
                    mataKuliah = detailUiState.detailUiEvent.toMataKuliahEntity(),
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Button(

                    onClick = {
                        deleteConfirmationRequired = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF276AA3)
                    )

                ) {
                    Text(text = "Delete", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                if (deleteConfirmationRequired) {
                    DeleteConfirmationDialog(
                        onDeleteConfirm = {
                            deleteConfirmationRequired = false
                            onDeleteClick()
                        },
                        onDeleteCancel = { deleteConfirmationRequired = false },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        detailUiState.isUiEventEmpty -> {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Data tidak ditemukan",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}


@Composable
fun ItemDetailMK (
    modifier: Modifier = Modifier,
    mataKuliah: MataKuliah
){
    Card (
        modifier = modifier
            .fillMaxWidth (),
        colors = CardDefaults.cardColors (
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ){
        Column (
            modifier = Modifier.padding(16.dp)
        ){
            ComponentDetailMK (judul = "Kode", isinya = mataKuliah.kode)
            Spacer (modifier = Modifier.padding(4.dp))

            ComponentDetailMK (judul = "Nama", isinya = mataKuliah. nama)
            Spacer (modifier = Modifier.padding(4.dp))
            ComponentDetailMK (judul = "SKS", isinya = mataKuliah.sks)
            Spacer (modifier = Modifier.padding(4.dp))
            ComponentDetailMK (judul = "Semester", isinya = mataKuliah. semester)
            Spacer (modifier = Modifier.padding(4.dp))
            ComponentDetailMK (judul = "Jenis", isinya = mataKuliah.jenis)
            Spacer (modifier = Modifier.padding(4.dp))
            ComponentDetailMK (judul = "Dosen Pengampu", isinya = mataKuliah.dosenPengampu)
        }
    }
}

@Composable
fun ComponentDetailMK (
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$judul : ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray

        )
        Text(
            text = isinya, fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun DeleteConfirmationDialog (
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier =
        Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text("Delete Data") },
        text = { Text("Apakah anda yakin ingin menghapus data?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes")
            }
        }
    )
}
