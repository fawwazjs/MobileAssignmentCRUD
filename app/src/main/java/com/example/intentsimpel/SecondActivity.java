package com.example.intentsimpel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    // Deklarasi variabel komponen UI
    private TextView tvHasilNama;
    private TextView tvHasilNrp;
    private TextView tvHasilTempatLahir;
    private TextView tvHasilTanggalLahir;
    private TextView tvHasilAsal;
    private TextView tvHasilHobi;
    private TextView tvHasilMbti;
    private TextView tvHasilUmur;
    private Button btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Inisialisasi komponen berdasarkan ID dari XML
        tvHasilNama = findViewById(R.id.tvHasilNama);
        tvHasilNrp = findViewById(R.id.tvHasilNrp);
        tvHasilTempatLahir = findViewById(R.id.tvHasilTempatLahir);
        tvHasilTanggalLahir = findViewById(R.id.tvHasilTanggalLahir);
        tvHasilAsal = findViewById(R.id.tvHasilAsal);
        tvHasilHobi = findViewById(R.id.tvHasilHobi);
        tvHasilMbti = findViewById(R.id.tvHasilMbti);
        tvHasilUmur = findViewById(R.id.tvHasilUmur);
        btnKembali = findViewById(R.id.btnKembali);

        // Mengambil Intent yang dikirim dari MainActivity
        Intent intent = getIntent();

        if (intent != null) {
            String nama = intent.getStringExtra("EXTRA_NAMA");
            String nrp = intent.getStringExtra("EXTRA_NRP");
            String tempatLahir = intent.getStringExtra("EXTRA_TEMPAT_LAHIR");
            String tanggalLahir = intent.getStringExtra("EXTRA_TANGGAL_LAHIR");
            String asal = intent.getStringExtra("EXTRA_ASAL");
            String hobi = intent.getStringExtra("EXTRA_HOBI");
            String mbti = intent.getStringExtra("EXTRA_MBTI");
            String umur = intent.getStringExtra("EXTRA_UMUR");

            // Menampilkan data ke masing-masing TextView
            if (nama != null) tvHasilNama.setText(nama);
            if (nrp != null) tvHasilNrp.setText("NRP: " + nrp);
            if (tempatLahir != null) tvHasilTempatLahir.setText(tempatLahir);
            if (tanggalLahir != null) tvHasilTanggalLahir.setText(tanggalLahir);
            if (asal != null) tvHasilAsal.setText(asal);
            if (hobi != null) tvHasilHobi.setText(hobi);
            if (mbti != null) tvHasilMbti.setText(mbti);
            if (umur != null) tvHasilUmur.setText(umur + " Tahun");
        }

        // Menambahkan OnClickListener pada tombol Kembali / Tutup
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menutup SecondActivity dan kembali ke halaman awal (MainActivity)
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LIFECYCLE", "SecondActivity: onDestroy (Page 2 didestroy)");
        Toast.makeText(getApplicationContext(), "SecondActivity: onDestroy (Page 2 didestroy)", Toast.LENGTH_SHORT).show();
    }
}
