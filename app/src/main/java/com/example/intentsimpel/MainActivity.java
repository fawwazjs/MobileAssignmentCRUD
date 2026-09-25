package com.example.intentsimpel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Deklarasi variabel komponen UI
    private EditText edtNama;
    private EditText edtNrp;
    private EditText edtTempatLahir;
    private EditText edtTanggalLahir;
    private EditText edtAsal;
    private EditText edtHobi;
    private EditText edtMbti;
    private EditText edtUmur;
    private Button btnNext;
    private Button btnCrud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi komponen berdasarkan ID dari XML
        edtNama = findViewById(R.id.edtNama);
        edtNrp = findViewById(R.id.edtNrp);
        edtTempatLahir = findViewById(R.id.edtTempatLahir);
        edtTanggalLahir = findViewById(R.id.edtTanggalLahir);
        edtAsal = findViewById(R.id.edtAsal);
        edtHobi = findViewById(R.id.edtHobi);
        edtMbti = findViewById(R.id.edtMbti);
        edtUmur = findViewById(R.id.edtUmur);
        btnNext = findViewById(R.id.btnNext);
        btnCrud = findViewById(R.id.btnCrud);

        // Listener untuk tombol Kelola Biodata (CRUD SQLite)
        btnCrud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CrudActivity.class);
                startActivity(intent);
            }
        });

        // Menambahkan OnClickListener pada input tanggal lahir untuk memunculkan DatePickerDialog (Kalender)
        edtTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkanKalenderDialog();
            }
        });

        // Menambahkan OnClickListener pada tombol Next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengambil nilai teks yang diinputkan pengguna
                String nama = edtNama.getText().toString().trim();
                String nrp = edtNrp.getText().toString().trim();
                String tempatLahir = edtTempatLahir.getText().toString().trim();
                String tanggalLahir = edtTanggalLahir.getText().toString().trim();
                String asal = edtAsal.getText().toString().trim();
                String hobi = edtHobi.getText().toString().trim();
                String mbti = edtMbti.getText().toString().trim();
                String umur = edtUmur.getText().toString().trim();

                // Validasi sederhana: pastikan tidak ada field yang kosong
                if (nama.isEmpty() || nrp.isEmpty() || tempatLahir.isEmpty() ||
                        tanggalLahir.isEmpty() || asal.isEmpty() ||
                        hobi.isEmpty() || mbti.isEmpty() || umur.isEmpty()) {
                    Toast.makeText(MainActivity.this, 
                            getString(R.string.validation_empty_fields), 
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Membuat Intent eksplisit untuk berpindah dari MainActivity ke SecondActivity
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);

                // Memasukkan data ke dalam Intent melalui putExtra
                intent.putExtra("EXTRA_NAMA", nama);
                intent.putExtra("EXTRA_NRP", nrp);
                intent.putExtra("EXTRA_TEMPAT_LAHIR", tempatLahir);
                intent.putExtra("EXTRA_TANGGAL_LAHIR", tanggalLahir);
                intent.putExtra("EXTRA_ASAL", asal);
                intent.putExtra("EXTRA_HOBI", hobi);
                intent.putExtra("EXTRA_MBTI", mbti);
                intent.putExtra("EXTRA_UMUR", umur);

                // Menjalankan activity berikutnya
                startActivity(intent);
            }
        });
    }

    /**
     * Membuka dialog pemilih tanggal (Kalender)
     */
    private void tampilkanKalenderDialog() {
        // Mendapatkan tanggal saat ini sebagai nilai default awal
        Calendar kalender = Calendar.getInstance();
        int tahunDefault = kalender.get(Calendar.YEAR);
        int bulanDefault = kalender.get(Calendar.MONTH);
        int tanggalDefault = kalender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Nama-nama bulan dalam Bahasa Indonesia
                        String[] namaBulan = {
                                "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                                "Juli", "Agustus", "September", "Oktober", "November", "Desember"
                        };

                        // Format teks tanggal yang dipilih
                        String hasilTanggal = dayOfMonth + " " + namaBulan[month] + " " + year;
                        edtTanggalLahir.setText(hasilTanggal);
                    }
                },
                tahunDefault,
                bulanDefault,
                tanggalDefault
        );

        datePickerDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LIFECYCLE", "MainActivity: onPause (Page 1 dipause)");
        Toast.makeText(this, "MainActivity: onPause (Page 1 dipause)", Toast.LENGTH_SHORT).show();
    }
}
