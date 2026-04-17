import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppFrame extends JFrame{
    private ScrollablePanel yAxisBox_SI;
    private JButton reScan;
    private JProgressBar progressBar;
    private JPanel headerRow_SI;
    private final Map<String, String> lastScanData = new LinkedHashMap<>();


    public AppFrame() {
        super("SystemInfo");
        setIconImage(new ImageIcon(getClass().getResource("/blue-chip-intelligence-cpu.png")).getImage());


        JMenuBar menuBar = new JMenuBar();
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        menuBar.add(new customMenuBar("File", lastScanData));

        JLabel header_SI = new JLabel("System Info");
        header_SI.setFont(new Font("Arial", Font.BOLD, 22));
        header_SI.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        header_SI.setToolTipText("Displays full PC specs & OS details (RAM, CPU, BIOS, network, hotfixes, and more) directly in Command Prompt.");
        reScan = new JButton("Rescan");
        reScan.addActionListener(e -> scanSiInfo());




        headerRow_SI = new JPanel();
        headerRow_SI.setLayout(new BoxLayout(headerRow_SI, BoxLayout.X_AXIS));
        headerRow_SI.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2, false));
        headerRow_SI.add(header_SI);
        headerRow_SI.add(Box.createHorizontalGlue());
        headerRow_SI.add(progressBar);
        headerRow_SI.add(Box.createHorizontalGlue());
        headerRow_SI.add(reScan);
        yAxisBox_SI = new ScrollablePanel();
        yAxisBox_SI.setLayout(new BoxLayout(yAxisBox_SI, BoxLayout.Y_AXIS));
        yAxisBox_SI.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        yAxisBox_SI.add(headerRow_SI);

        JScrollPane taskScrollBar_MS = new JScrollPane(yAxisBox_SI);
        taskScrollBar_MS.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        taskScrollBar_MS.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel systeminfoPane = new JPanel(new BorderLayout(0, 0));
        systeminfoPane.add(taskScrollBar_MS, BorderLayout.CENTER);

        JPanel pane = new JPanel(new BorderLayout(0, 0));
        pane.add(menuBar, BorderLayout.NORTH);
        pane.add(systeminfoPane, BorderLayout.CENTER);

        add(pane);
        setSize(800, 800);
        setVisible(true);
    }
    private static class ScrollablePanel extends JPanel implements Scrollable {
        @Override
        public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 10; }
        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 100; }
        @Override
        public boolean getScrollableTracksViewportWidth() { return true; }
        @Override
        public boolean getScrollableTracksViewportHeight() { return false; }
    }

    private void scanSiInfo() {
        reScan.setEnabled(false);
        progressBar.setIndeterminate(true);
        progressBar.setVisible(true);
        yAxisBox_SI.removeAll();
        yAxisBox_SI.add(headerRow_SI);
        yAxisBox_SI.revalidate();
        yAxisBox_SI.repaint();

        new SwingWorker<String[][], Void>() {
            @Override
            protected String[][] doInBackground() {
                return getSiInfo();
            }
            @Override
            protected void done() {
                try {
                    lastScanData.clear();
                    for (String[] row : get()) {
                        lastScanData.put(row[0], row[1]);
                    }
                    for (Map.Entry<String, String> entry : lastScanData.entrySet()) {
                        JPanel row = new JPanel();
                        row.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 0));
                        row.setLayout(new GridLayout(1, 2, 2, 2));
                        row.add(new JLabel(entry.getKey()));
                        row.add(new JLabel(entry.getValue()));
                        yAxisBox_SI.add(row);
                    }
                } catch (Exception ignored) {}
                progressBar.setVisible(false);
                progressBar.setIndeterminate(false);
                reScan.setEnabled(true);
                yAxisBox_SI.revalidate();
                yAxisBox_SI.repaint();
            }
        }.execute();
    }

    private String[][] getSiInfo() {
        List<String[]> siResult = new ArrayList<String[]>();

        try {
            Process o = Runtime.getRuntime().exec("systeminfo");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(o.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    siResult.add(new String[]{parts[0].trim(), parts[1].trim()});
                }
            }
        } catch (IOException e) {

        }
        return siResult.toArray(new String[0][]);
    }
}
