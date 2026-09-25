package com.example.intentsimpel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CrudActivity extends AppCompatActivity {

    // Member variables sesuai pola materi dosen
    private SQLiteDatabase dbku;
    private SQLiteOpenHelper openDb;

    // EditText variables
    private EditText etNrp;
    private EditText etNama;
    private EditText etTempatLahir;
    private EditText etTanggalLahir;
    private EditText etAsalDaerah;
    private EditText etHobi;
    private EditText etMbti;
    private EditText etUmur;

    // Button variables
    private Button btnSimpan;
    private Button btnCari;
    private Button btnUpdate;
    private Button btnHapus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        // Inisialisasi semua EditText
        etNrp = findViewById(R.id.etNrp);
        etNama = findViewById(R.id.etNama);
        etTempatLahir = findViewById(R.id.etTempatLahir);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        etAsalDaerah = findViewById(R.id.etAsalDaerah);
        etHobi = findViewById(R.id.etHobi);
        etMbti = findViewById(R.id.etMbti);
        etUmur = findViewById(R.id.etUmur);

        // Inisialisasi semua Button
        btnSimpan = findViewById(R.id.btnSimpan);
        btnCari = findViewById(R.id.btnCari);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnHapus = findViewById(R.id.btnHapus);

        // Inisialisasi database SQLite sesuai pola dosen
        openDb = new DatabaseHelper(this);
        dbku = openDb.getWritableDatabase();

        // Pasang listener tombol dengan dialog konfirmasi keamanan
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konfirmasiDanJalankan("Konfirmasi Simpan", new Runnable() {
                    @Override
                    public void run() {
                        simpan();
                    }
                });
            }
        });

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cari();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konfirmasiDanJalankan("Konfirmasi Update", new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konfirmasiDanJalankan("Konfirmasi Hapus", new Runnable() {
                    @Override
                    public void run() {
                        hapus();
                    }
                });
            }
        });

        // Pasang listener pemilih tanggal (Kalender)
        etTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkanKalenderDialog();
            }
        });

        // Ambil LAST_NRP dari SharedPreferences PREF_MHS
        SharedPreferences prefs = getSharedPreferences("PREF_MHS", MODE_PRIVATE);
        String lastNrp = prefs.getString("LAST_NRP", "");
        if (!lastNrp.isEmpty()) {
            etNrp.setText(lastNrp);
        }
    }

    /**
     * Dialog Konfirmasi Keamanan sebelum melakukan Simpan, Update, atau Hapus
     */
    private void konfirmasiDanJalankan(String judul, final Runnable aksi) {
        AlertDialog dialog = new AlertDialog.Builder(CrudActivity.this)
                .setTitle(judul)
                .setMessage("Apakah anda yakin melakukan perubahan ini")
                .setPositiveButton("ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        aksi.run();
                    }
                })
                .setNegativeButton("tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .create();

        dialog.show();

        // Ubah warna tombol jadi lebih cerah sesuai permintaan
        Button btnYa = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (btnYa != null) {
            btnYa.setTextColor(android.graphics.Color.parseColor("#fff6e8ff")); // Warna putih
        }

        Button btnTidak = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        if (btnTidak != null) {
            btnTidak.setTextColor(android.graphics.Color.parseColor("#ffeae9ff")); // Warna putih
        }
    }

    /**
     * Method Simpan Data ke SQLite Database
     */
    private void simpan() {
        String nrp = etNrp.getText().toString().trim();
        String nama = etNama.getText().toString().trim();
        String tempatLahir = etTempatLahir.getText().toString().trim();
        String tanggalLahir = etTanggalLahir.getText().toString().trim();
        String asalDaerah = etAsalDaerah.getText().toString().trim();
        String hobi = etHobi.getText().toString().trim();
        String mbti = etMbti.getText().toString().trim();
        String umurStr = etUmur.getText().toString().trim();

        if (nrp.isEmpty()) {
            Toast.makeText(this, "Isikan NRP Mahasiswa!", Toast.LENGTH_SHORT).show();
            return;
        }

        int umur = 0;
        try {
            if (!umurStr.isEmpty()) {
                umur = Integer.parseInt(umurStr);
            }
        } catch (NumberFormatException ignored) {}

        ContentValues data = new ContentValues();
        data.put("nrp", nrp);
        data.put("nama", nama);
        data.put("tempat_lahir", tempatLahir);
        data.put("tanggal_lahir", tanggalLahir);
        data.put("asal_daerah", asalDaerah);
        data.put("hobi", hobi);
        data.put("mbti", mbti);
        data.put("umur", umur);

        dbku.insert("mhs", null, data);
        Toast.makeText(this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method Cari Data berdasarkan NRP dari SQLite Database
     */
    private void cari() {
        String nrp = etNrp.getText().toString().trim();
        if (nrp.isEmpty()) {
            Toast.makeText(this, "Isikan NRP untuk mencari!", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cur = dbku.rawQuery(
                "SELECT * FROM mhs WHERE nrp='" + nrp + "'",
                null
        );

        if (cur.moveToFirst()) {
            etNama.setText(cur.getString(cur.getColumnIndexOrThrow("nama")));
            etTempatLahir.setText(cur.getString(cur.getColumnIndexOrThrow("tempat_lahir")));
            etTanggalLahir.setText(cur.getString(cur.getColumnIndexOrThrow("tanggal_lahir")));
            etAsalDaerah.setText(cur.getString(cur.getColumnIndexOrThrow("asal_daerah")));
            etHobi.setText(cur.getString(cur.getColumnIndexOrThrow("hobi")));
            etMbti.setText(cur.getString(cur.getColumnIndexOrThrow("mbti")));
            etUmur.setText(String.valueOf(cur.getInt(cur.getColumnIndexOrThrow("umur"))));

            Toast.makeText(this, "Data Ditemukan", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
        }
        cur.close();

        // Simpan LAST_NRP yang dicari ke SharedPreferences
        SharedPreferences prefs = getSharedPreferences("PREF_MHS", MODE_PRIVATE);
        prefs.edit().putString("LAST_NRP", nrp).apply();
    }

    /**
     * Method Update Data pada SQLite Database berdasarkan NRP
     */
    private void update() {
        String nrp = etNrp.getText().toString().trim();
        String nama = etNama.getText().toString().trim();
        String tempatLahir = etTempatLahir.getText().toString().trim();
        String tanggalLahir = etTanggalLahir.getText().toString().trim();
        String asalDaerah = etAsalDaerah.getText().toString().trim();
        String hobi = etHobi.getText().toString().trim();
        String mbti = etMbti.getText().toString().trim();
        String umurStr = etUmur.getText().toString().trim();

        if (nrp.isEmpty()) {
            Toast.makeText(this, "Isikan NRP untuk diupdate!", Toast.LENGTH_SHORT).show();
            return;
        }

        int umur = 0;
        try {
            if (!umurStr.isEmpty()) {
                umur = Integer.parseInt(umurStr);
            }
        } catch (NumberFormatException ignored) {}

        ContentValues data = new ContentValues();
        data.put("nama", nama);
        data.put("tempat_lahir", tempatLahir);
        data.put("tanggal_lahir", tanggalLahir);
        data.put("asal_daerah", asalDaerah);
        data.put("hobi", hobi);
        data.put("mbti", mbti);
        data.put("umur", umur);

        dbku.update(
                "mhs",
                data,
                "nrp='" + nrp + "'",
                null
        );

        Toast.makeText(this, "Data Terupdate", Toast.LENGTH_SHORT).show();
    }

    /**
     * Method Hapus Data dari SQLite Database berdasarkan NRP
     */
    private void hapus() {
        String nrp = etNrp.getText().toString().trim();
        if (nrp.isEmpty()) {
            Toast.makeText(this, "Isikan NRP untuk dihapus!", Toast.LENGTH_SHORT).show();
            return;
        }

        dbku.delete(
                "mhs",
                "nrp='" + nrp + "'",
                null
        );

        // Kosongkan seluruh EditText setelah berhasil menghapus
        etNrp.setText("");
        etNama.setText("");
        etTempatLahir.setText("");
        etTanggalLahir.setText("");
        etAsalDaerah.setText("");
        etHobi.setText("");
        etMbti.setText("");
        etUmur.setText("");

        Toast.makeText(this, "Data Terhapus", Toast.LENGTH_SHORT).show();
    }

    /**
     * Tampilkan DatePickerDialog untuk pemilih tanggal
     */
    private void tampilkanKalenderDialog() {
        Calendar kalender = Calendar.getInstance();
        int tahunDefault = kalender.get(Calendar.YEAR);
        int bulanDefault = kalender.get(Calendar.MONTH);
        int tanggalDefault = kalender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CrudActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String[] namaBulan = {
                                "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                                "Juli", "Agustus", "September", "Oktober", "November", "Desember"
                        };
                        String hasilTanggal = dayOfMonth + " " + namaBulan[month] + " " + year;
                        etTanggalLahir.setText(hasilTanggal);
                    }
                },
                tahunDefault,
                bulanDefault,
                tanggalDefault
        );
        datePickerDialog.show();
    }

    @Override
    protected void onStop() {
        if (dbku != null && dbku.isOpen()) {
            dbku.close();
        }
        super.onStop();
    }
}
