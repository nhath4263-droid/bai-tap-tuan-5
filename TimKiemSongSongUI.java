/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TimKiemSongSong;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class TimKiemSongSongUI extends JFrame {
    private JTextField txtTuKhoa;
    private JTextField txtThuMuc;
    private JCheckBox cbNguyenTu;
    private JCheckBox cbPhanBietHoaThuong;
    private JButton btnTim;
    private JButton btnXuatFile;
    private JTable tableKetQua;
    private DefaultTableModel model;

    public TimKiemSongSongUI() {
        setTitle("Tìm trong File");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Nhập liệu
        JPanel panelTop = new JPanel(new GridLayout(3, 2, 10, 10));
        panelTop.add(new JLabel("Tìm từ khóa:"));
        txtTuKhoa = new JTextField();
        panelTop.add(txtTuKhoa);

        panelTop.add(new JLabel("Thư mục:"));
        txtThuMuc = new JTextField();
        panelTop.add(txtThuMuc);

        cbNguyenTu = new JCheckBox("Tìm từ nguyên vẹn");
        cbPhanBietHoaThuong = new JCheckBox("Phân biệt chữ hoa/thường");
        panelTop.add(cbNguyenTu);
        panelTop.add(cbPhanBietHoaThuong);

        panel.add(panelTop, BorderLayout.NORTH);

        // Bảng kết quả
        model = new DefaultTableModel(new Object[]{"Tên File", "Dòng", "Nội dung"}, 0);
        tableKetQua = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableKetQua);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Nút chức năng
        JPanel panelBottom = new JPanel();
        btnTim = new JButton("Tìm");
        btnXuatFile = new JButton("Xuất ra file");
        panelBottom.add(btnTim);
        panelBottom.add(btnXuatFile);
        panel.add(panelBottom, BorderLayout.SOUTH);

        add(panel);

        // Sự kiện nút Tìm
        btnTim.addActionListener(e -> timKiem());

        // Sự kiện nút Xuất file
        btnXuatFile.addActionListener(e -> xuatKetQua());
    }

    private void timKiem() {
        model.setRowCount(0); // Xóa dữ liệu cũ

        String tuKhoa = txtTuKhoa.getText().trim();
        String thuMuc = txtThuMuc.getText().trim();
        boolean nguyenTu = cbNguyenTu.isSelected();
        boolean phanBiet = cbPhanBietHoaThuong.isSelected();

        File folder = new File(thuMuc);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy file .txt trong thư mục!");
            return;
        }

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
                String line;
                int lineNumber = 0;
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    String text = line;
                    String key = tuKhoa;

                    if (!phanBiet) {
                        text = text.toLowerCase();
                        key = key.toLowerCase();
                    }

                    boolean found = false;
                    if (nguyenTu) {
                        // Tách từ theo ký tự không phải chữ cái/ số
                        String[] words = text.split("\\W+");
                        for (String w : words) {
                            if (w.equals(key)) {
                                found = true;
                                break;
                            }
                        }
                    } else {
                        found = text.contains(key);
                    }

                    if (found) {
                        model.addRow(new Object[]{file.getName(), lineNumber, line});
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả!");
        } else {
            JOptionPane.showMessageDialog(this, "Hoàn tất tìm kiếm!");
        }
    }

    private void xuatKetQua() {
        File fileKetQua = new File("ketqua.txt");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileKetQua), "UTF-8"))) {
            writer.write(String.format("%-25s | %-10s | %s", "Tên File", "Dòng", "Nội dung"));
            writer.newLine();
            writer.write(new String(new char[80]).replace("\0", "-"));
            writer.newLine();

            for (int i = 0; i < model.getRowCount(); i++) {
                writer.write(String.format("%-25s | %-10s | %s",
                        model.getValueAt(i, 0),
                        model.getValueAt(i, 1),
                        model.getValueAt(i, 2)));
                writer.newLine();
            }

            JOptionPane.showMessageDialog(this, "Kết quả đã xuất ra ketqua.txt!");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất file!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TimKiemSongSongUI().setVisible(true));
    }
}