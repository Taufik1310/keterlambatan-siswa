import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Siswa {
    int nomorAbsen;
    String nama;
    String nis;
    String kelas;
    int poinKeterlambatan;
    List<String> tanggalKeterlambatan;

    public Siswa(int nomorAbsen, String nama, String nis, String kelas) {
        this.nomorAbsen = nomorAbsen;
        this.nama = nama;
        this.nis = nis;
        this.kelas = kelas;
        this.poinKeterlambatan = 0;
        this.tanggalKeterlambatan = new ArrayList<>();
    }
}

public class PengelolaanKeterlambatan {
    static Scanner scanner = new Scanner(System.in);
    static Map<String, List<String>> jurusanKelas = new TreeMap<>();
    static Map<String, Map<String, List<Siswa>>> dataSiswa = new HashMap<>();

    public static void main(String[] args) {
        initializeData();

        while (true) {
            tampilkanMenuUtama();
            System.out.print("Pilih opsi: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1 -> lihatData();
                case 2 -> inputDataKeterlambatan();
                case 3 -> {
                    System.out.println("Program selesai.");
                    return;
                }
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    static void initializeData() {
        // Daftar jurusan dan kelas yang sudah disebutkan
        jurusanKelas.put("PPLG", Arrays.asList("X PPLG 1", "X PPLG 2", "X PPLG 3",
                "XI PPLG 1", "XI PPLG 2", "XI PPLG 3",
                "XII PPLG 1", "XII PPLG 2", "XII PPLG 3"));
        jurusanKelas.put("TJKT", Arrays.asList("X TJKT 1", "X TJKT 2",
                "XI TJKT 1", "XI TJKT 2", "XII TJKT 1", "XII TJKT 2"));
        jurusanKelas.put("DKV", Arrays.asList("X DKV 1", "X DKV 2",
                "XI DKV 1", "XI DKV 2", "XII DKV 1", "XII DKV 2"));

        // Inisialisasi Map untuk setiap jurusan
        for (String jurusan : jurusanKelas.keySet()) {
            dataSiswa.put(jurusan, new HashMap<>());
            for (String kelas : jurusanKelas.get(jurusan)) {
                loadSiswaFromCSV(jurusan, kelas); // Memuat siswa untuk setiap kelas
            }
        }
    }

    static void loadSiswaFromCSV(String jurusan, String kelas) {
        List<Siswa> siswaList = new ArrayList<>();
        String fileName = "./data/" + kelas.toLowerCase().replace(" ", "_") + ".csv"; // Path ke file CSV

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 3) {
                    System.out.println("Format data tidak sesuai di file: " + fileName);
                    continue; // Jika data tidak sesuai, lanjutkan ke baris berikutnya
                }
                int nomorAbsen = Integer.parseInt(data[0]);
                String nis = data[1];
                String nama = data[2];
                siswaList.add(new Siswa(nomorAbsen, nama, nis, kelas));
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file " + fileName);
            e.printStackTrace(); // Menampilkan stack trace untuk debugging lebih lanjut
        }

        dataSiswa.get(jurusan).put(kelas, siswaList); // Menyimpan siswa ke dalam Map
    }

    static void tampilkanMenuUtama() {
        System.out.println("\n=============================================");
        System.out.println("|         APLIKASI KETERLAMBATAN SISWA       |");
        System.out.println("=============================================");
        System.out.println("|       Selamat datang di aplikasi ini!      |");
        System.out.println("|  Silakan pilih opsi yang ingin Anda lakukan|");
        System.out.println("=============================================");
        System.out.println("| 1. Lihat Data                              |");
        System.out.println("| 2. Input Data Keterlambatan                |");
        System.out.println("| 3. Keluar                                  |");
        System.out.println("=============================================");
    }

    static void lihatData() {
        System.out.println("\n===== LIHAT DATA =====");
        String jurusan = pilihJurusan();
        String kelas = pilihKelas(jurusan);
        System.out.println("\nMenampilkan data siswa untuk: " + kelas + " (" + jurusan + ")");
        List<Siswa> siswaList = dataSiswa.get(jurusan).get(kelas);
        tampilkanSiswa(siswaList);

        System.out.println();
        System.out.println("1. Cari berdasarkan tanggal keterlambatan");
        System.out.println("2. Cari berdasarkan nama/absen");
        System.out.println("3. Kembali ke menu utama");
        System.out.print("Pilih opsi: ");
        int opsi = scanner.nextInt();
        scanner.nextLine();

        switch (opsi) {
            case 1 -> cariBerdasarkanTanggal(siswaList);
            case 2 -> cariBerdasarkanNamaAbsen(siswaList);
            case 3 -> {
            }
            default -> System.out.println("Pilihan tidak valid.");
        }
    }

    static void inputDataKeterlambatan() {
        System.out.println("\n===== INPUT DATA KETERLAMBATAN =====");
        String jurusan = pilihJurusan();
        String kelas = pilihKelas(jurusan);
        System.out.println("\nInput data keterlambatan untuk: " + kelas + " (" + jurusan + ")");
        List<Siswa> siswaList = dataSiswa.get(jurusan).get(kelas);

        System.out.println("\nDaftar Siswa:");
        for (Siswa siswa : siswaList) {
            System.out.println(siswa.nomorAbsen + ". " + siswa.nama);
        }

        System.out.print("Masukkan nomor absen siswa: ");
        int nomorAbsen = scanner.nextInt();
        scanner.nextLine();

        Siswa siswa = siswaList.stream()
                .filter(s -> s.nomorAbsen == nomorAbsen)
                .findFirst()
                .orElse(null);

        if (siswa == null) {
            System.out.println("Siswa dengan nomor absen tersebut tidak ditemukan.");
        } else {
            String tanggal = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            siswa.poinKeterlambatan += 5;
            siswa.tanggalKeterlambatan.add(tanggal);
            System.out.println("\nData siswa diperbarui:");
            System.out.println("Nama: " + siswa.nama);
            System.out.println("NIS: " + siswa.nis);
            System.out.println("Kelas: " + siswa.kelas);
            System.out.println("Nomor Absen: " + siswa.nomorAbsen);
            System.out.println("Poin Keterlambatan: " + siswa.poinKeterlambatan);
            System.out.println("Tanggal Keterlambatan Terbaru: " + tanggal);
            System.out.println();
        }
    }

    static String pilihJurusan() {
        System.out.println("\nPilih Jurusan:");
        List<String> jurusanList = new ArrayList<>(jurusanKelas.keySet());
        for (int i = 0; i < jurusanList.size(); i++) {
            System.out.println((i + 1) + ". " + jurusanList.get(i));
        }
        System.out.print("Masukkan pilihan: ");
        int pilihan = scanner.nextInt();
        scanner.nextLine();

        if (pilihan < 1 || pilihan > jurusanList.size()) {
            System.out.println("Pilihan tidak valid. Coba lagi.");
            return pilihJurusan();
        }
        return jurusanList.get(pilihan - 1);
    }

    static String pilihKelas(String jurusan) {
        System.out.println("\nPilih Kelas untuk Jurusan " + jurusan + ":");
        List<String> kelasList = jurusanKelas.get(jurusan);
        for (int i = 0; i < kelasList.size(); i++) {
            System.out.println((i + 1) + ". " + kelasList.get(i));
        }
        System.out.print("Masukkan pilihan: ");
        int pilihan = scanner.nextInt();
        scanner.nextLine();

        if (pilihan < 1 || pilihan > kelasList.size()) {
            System.out.println("Pilihan tidak valid. Coba lagi.");
            return pilihKelas(jurusan);
        }
        return kelasList.get(pilihan - 1);
    }

    static void tampilkanSiswa(List<Siswa> siswaList) {
        int maxNama = siswaList.stream().mapToInt(s -> s.nama.length()).max().orElse(20);
        int maxNis = siswaList.stream().mapToInt(s -> s.nis.length()).max().orElse(6);
        int maxKelas = siswaList.stream().mapToInt(s -> s.kelas.length()).max().orElse(20);

        String format = "| %-11s | %-" + maxNama + "s | %-" + maxNis + "s | %-" + maxKelas + "s | %-20s |\n";

        System.out.println("\nDaftar Siswa:");
        System.out.printf(
                "+-------------+-%s+-%s+-%s+----------------------+%n",
                "-".repeat(maxNama + 2), "-".repeat(maxNis + 2), "-".repeat(maxKelas + 2));
        System.out.printf(format, "Nomor Absen", "Nama", "NIS", "Kelas", "Poin Keterlambatan");
        System.out.printf(
                "+-------------+-%s+-%s+-%s+----------------------+%n",
                "-".repeat(maxNama + 2), "-".repeat(maxNis + 2), "-".repeat(maxKelas + 2));

        for (Siswa siswa : siswaList) {
            System.out.printf(format,
                    siswa.nomorAbsen, siswa.nama, siswa.nis, siswa.kelas, siswa.poinKeterlambatan);
        }

        System.out.printf(
                "+-------------+-%s+-%s+-%s+----------------------+%n",
                "-".repeat(maxNama + 2), "-".repeat(maxNis + 2), "-".repeat(maxKelas + 2));
    }

    static void cariBerdasarkanTanggal(List<Siswa> siswaList) {
        System.out.print("Masukkan tanggal (dd-MM-yyyy): ");
        String tanggal = scanner.nextLine();

        List<Siswa> hasil = new ArrayList<>();
        for (Siswa siswa : siswaList) {
            if (siswa.tanggalKeterlambatan.stream().anyMatch(t -> t.startsWith(tanggal))) {
                hasil.add(siswa);
            }
        }

        if (hasil.isEmpty()) {
            System.out.println("Tidak ada data siswa yang terlambat pada tanggal tersebut.");
            tampilkanSiswa(siswaList);
        } else {
            System.out.println("\nSiswa yang terlambat pada " + tanggal + ":");
            tampilkanSiswa(hasil);
        }
    }

    static void cariBerdasarkanNamaAbsen(List<Siswa> siswaList) {
        System.out.print("Masukkan nomor absen siswa: ");
        int nomorAbsen = scanner.nextInt();
        scanner.nextLine();

        Siswa siswa = siswaList.stream()
                .filter(s -> s.nomorAbsen == nomorAbsen)
                .findFirst()
                .orElse(null);

        if (siswa == null) {
            System.out.println("Siswa dengan nomor absen tersebut tidak ditemukan.");
            tampilkanSiswa(siswaList);
        } else {
            System.out.println("\nData Siswa:");
            System.out.println("Nama: " + siswa.nama);
            System.out.println("NIS: " + siswa.nis);
            System.out.println("Kelas: " + siswa.kelas);
            System.out.println("Nomor Absen: " + siswa.nomorAbsen);
            System.out.println("Poin Keterlambatan: " + siswa.poinKeterlambatan);

            System.out.println("Tanggal Keterlambatan:");
            for (int i = 0; i < siswa.tanggalKeterlambatan.size(); i++) {
                String tanggal = siswa.tanggalKeterlambatan.get(i);
                System.out.printf("  %d. %s", i + 1, tanggal);

                // Menambahkan status "Dipulangkan" untuk kelipatan 7
                if ((i + 1) % 7 == 0) {
                    System.out.print(" - Status: Dipulangkan");
                }
                System.out.println();
            }

            if (siswa.tanggalKeterlambatan.size() == 0) {
                System.out.println("  Belum ada catatan keterlambatan.");
            }
            System.out.println();
        }
    }
}
