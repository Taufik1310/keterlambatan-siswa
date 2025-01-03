import java.util.ArrayList;
import java.util.Scanner;

class Siswa {
    String nama;
    String kelas;
    String nis;
    String tanggal;
    int jumlahKeterlambatan;

    public Siswa(String nama, String kelas, String nis, String tanggal) {
        this.nama = nama;
        this.kelas = kelas;
        this.nis = nis;
        this.tanggal = tanggal;
        this.jumlahKeterlambatan = 1; // Keterlambatan awal dihitung 1
    }

    public String getAsterisks() {
        return "*".repeat(jumlahKeterlambatan);
    }
}

public class KeterlambatanSiswa {
    static ArrayList<Siswa> dataSiswa = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            tampilkanMenu();
        }
    }

    static void tampilkanMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Lihat Data");
        System.out.println("2. Input Data Keterlambatan Siswa");
        System.out.println("3. Keluar");
        System.out.print("Pilih opsi: ");
        int pilihan = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (pilihan) {
            case 1:
                lihatData();
                break;
            case 2:
                inputData();
                break;
            case 3:
                System.out.println("Program selesai. Terima kasih!");
                System.exit(0);
                break;
            default:
                System.out.println("Pilihan tidak valid. Silakan coba lagi.");
        }
    }

    static void lihatData() {
        if (dataSiswa.isEmpty()) {
            System.out.println("Belum ada data siswa.");
        } else {
            int maxNama = "Nama".length();
            int maxKelas = "Kelas".length();
            int maxNis = "NIS".length();
            int maxTanggal = "Tanggal".length();
            int maxAsterisks = "Jumlah Keterlambatan".length();

            // Hitung lebar maksimum setiap kolom
            for (Siswa siswa : dataSiswa) {
                maxNama = Math.max(maxNama, siswa.nama.length());
                maxKelas = Math.max(maxKelas, siswa.kelas.length());
                maxNis = Math.max(maxNis, siswa.nis.length());
                maxTanggal = Math.max(maxTanggal, siswa.tanggal.length());
                maxAsterisks = Math.max(maxAsterisks, siswa.getAsterisks().length());
            }

            // Format header
            String format = "| %-" + maxNama + "s | %-" + maxKelas + "s | %-" + maxNis + "s | %-" + maxTanggal
                    + "s | %-" + maxAsterisks + "s |%n";
            System.out.printf(format, "Nama", "Kelas", "NIS", "Tanggal", "Jumlah Keterlambatan");
            System.out.println("-".repeat(maxNama + maxKelas + maxNis + maxTanggal + maxAsterisks + 17)); // Garis
                                                                                                          // pemisah

            // Format isi data
            for (Siswa siswa : dataSiswa) {
                System.out.printf(format, siswa.nama, siswa.kelas, siswa.nis, siswa.tanggal, siswa.getAsterisks());
            }
        }
    }

    static void inputData() {
        System.out.print("Masukkan Nama: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan Kelas: ");
        String kelas = scanner.nextLine();
        System.out.print("Masukkan NIS: ");
        String nis = scanner.nextLine();
        System.out.print("Masukkan Hari/Tanggal: ");
        String tanggal = scanner.nextLine();

        Siswa siswa = cariSiswa(nis);
        if (siswa == null) {
            siswa = new Siswa(nama, kelas, nis, tanggal);
            dataSiswa.add(siswa);
            System.out.println("Data Berhasil Dilaporkan");
        } else {
            siswa.jumlahKeterlambatan++;
            siswa.tanggal = tanggal;
            System.out.println("Data Berhasil Dilaporkan (Keterlambatan Bertambah)");
        }

        berikanSanksi(siswa.jumlahKeterlambatan);
    }

    static Siswa cariSiswa(String nis) {
        for (Siswa siswa : dataSiswa) {
            if (siswa.nis.equals(nis)) {
                return siswa;
            }
        }
        return null;
    }

    static void berikanSanksi(int jumlahKeterlambatan) {
        if (jumlahKeterlambatan <= 5) {
            System.out.println("Sanksi: Sanksi Ringan");
        } else if (jumlahKeterlambatan > 5 && jumlahKeterlambatan <= 8) {
            System.out.println("Sanksi: Peringatan, siswa ini sudah terlambat lima kali");
        } else if (jumlahKeterlambatan > 8) {
            System.out.println("Sanksi: Panggilan orang tua bersangkutan (SP)");
            System.out.println("Jika SP diterima sebanyak 3 kali, siswa akan dikeluarkan.");
        }
    }
}
