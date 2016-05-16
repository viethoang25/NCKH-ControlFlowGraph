import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JLabel;

import file.FileManager;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Gui extends JFrame {

	private Application application;
	private File file;

	private JPanel contentPane;
	private JTextField txtFile;
	private JButton btnOpen, btnResult;
	private JTextPane txtContent, txtResult;
	private JTextField txtDepth;
	private JComboBox<String> cbxCoverage;
	private int resultCover;

	public Gui() {
		initGui();
		handleAction();
	}

	private void initGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 425);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtFile = new JTextField();
		txtFile.setBounds(10, 11, 227, 20);
		contentPane.add(txtFile);
		txtFile.setColumns(10);

		btnOpen = new JButton("Open");
		btnOpen.setBounds(245, 10, 89, 23);
		contentPane.add(btnOpen);

		txtContent = new JTextPane();
		JScrollPane scrollContent = new JScrollPane(txtContent);
		scrollContent.setBounds(10, 42, 324, 309);
		scrollContent.setViewportView(txtContent);
		contentPane.add(scrollContent);

		txtResult = new JTextPane();
		JScrollPane scrollResult = new JScrollPane(txtResult);
		scrollResult.setBounds(359, 42, 265, 309);
		scrollResult.setViewportView(txtResult);
		contentPane.add(scrollResult);

		btnResult = new JButton("Result");
		btnResult.setBounds(535, 10, 89, 23);
		contentPane.add(btnResult);

		txtDepth = new JTextField();
		txtDepth.setText("0");
		txtDepth.setBounds(479, 11, 46, 20);
		contentPane.add(txtDepth);
		txtDepth.setColumns(10);

		JLabel lblDepth = new JLabel("Depth :");
		lblDepth.setBounds(423, 14, 46, 14);
		contentPane.add(lblDepth);

		String[] item = { "edge", "vertex" };
		cbxCoverage = new JComboBox(item);
		cbxCoverage.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (cbxCoverage.getSelectedItem() == "edge") {
					resultCover = Constants.COVERAGE_EDGE;
				} else
					resultCover = Constants.COVERAGE_VERTEX;
			}
		});
		cbxCoverage.setBounds(359, 11, 54, 20);
		contentPane.add(cbxCoverage);
	}

	private void handleAction() {
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser openFile = new JFileChooser("./example/");
				openFile.showOpenDialog(null);
				file = openFile.getSelectedFile();
				txtFile.setText(file.toString());

				FileManager fm = FileManager.getInstance();
				fm.readFile(file);
				txtContent.setText(fm.getData());
			}
		});

		btnResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (file != null && !txtDepth.getText().isEmpty()) {
					application = new Application(file, Integer
							.parseInt(txtDepth.getText()), resultCover);
					txtResult.setText(application.getResult());
				}
			}
		});
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
