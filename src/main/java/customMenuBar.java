import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class customMenuBar extends JMenu {
    // File
    private JMenuItem btn_Save;
    private JMenuItem btn_Exit;

    public customMenuBar(String option, Map<String, String> data) {
        super(option);
        initMenuUI(option, data);
    }

    private void initMenuUI(String option, Map<String, String> data) {
        if (option.equals("File")) {
            btn_Save = new JMenuItem("Save");
            btn_Exit = new JMenuItem("Exit");

            btn_Exit.addActionListener(e-> System.exit(0));
            btn_Save.addActionListener(new SaveHandler(data));
            add(btn_Save);
            add(btn_Exit);

        }
    }


}

class SaveHandler implements ActionListener {
    private final Map<String, String> data;

    public SaveHandler(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save System Info");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
        int result = fileChooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File selectedFile = fileChooser.getSelectedFile();
        if (!selectedFile.getName().endsWith(".txt")) {
            selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
        }
        if (!isValidFileName(selectedFile.getName())) {
            JOptionPane.showMessageDialog(null, "FileName: " + selectedFile.getName() + " is Invalid", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {

            for (Map.Entry<String, String> entry : data.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "File saved successfully! \n" + selectedFile.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save file: " + ex.getMessage());
        }
    }

    private static boolean isValidFileName(String filename) {
        if (filename == null || filename.isBlank()) return false;
        Matcher matcher = Pattern.compile("^[\\w\\s\\-]+\\.txt$").matcher(filename);
        return matcher.matches();
    }
}
